import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimedTestSyncMap {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    private BarrierTimer timer = new BarrierTimer();
    private final Map<Integer, Integer> map;
    private final int numOfTrials, numOfThreads;

    public TimedTestSyncMap(int trials, int numOfThreads) {
        this.map = Collections.synchronizedMap(new HashMap<>());
        this.numOfTrials = trials;
        this.numOfThreads = numOfThreads;
        this.barrier = new CyclicBarrier(numOfThreads + 1, timer);
    }

    public static void main(String[] args) throws InterruptedException {
        int num = 1000;
        int maxNumTrials = 10000;

        for (int trials = 10; trials <= maxNumTrials; trials *= 10) {
            TimedTestSyncMap testMap = new TimedTestSyncMap(trials, num);
            System.out.println("Number of items: " + trials * num);
            // test put
            System.out.print("Time to put: ");
            testMap.testPut();
            Thread.sleep(500);
            System.out.print("    ");
            testMap.testPut();

            // test get
            Thread.sleep(500);
            System.out.println();
            System.out.print("Time to get: ");
            testMap.testGet();
            Thread.sleep(500);
            System.out.print("    ");
            testMap.testGet();

            System.out.println("\n");
            Thread.sleep(500);
        }

        TimedTestSyncMap.pool.shutdown();
    }

    void testPut() {
        try {
            timer.clear();
            for (int i = 0; i < numOfThreads; i++)
                pool.execute(new Putter(i));

            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.print("    " + time + "ns");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    void testGet() {
        try {
            timer.clear();
            for (int i = 0; i < numOfThreads; i++)
                pool.execute(new Getter());

            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.print("    " + time + "ns");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    class Putter implements Runnable {
        private final int index;
        private final int start;

        Putter(int index) {
            this.index = index;
            this.start = index * numOfTrials;
        }

        @Override
        public void run() {
            try {
                int num = Math.abs(this.hashCode() ^ (int) System.nanoTime());
                barrier.await();
                for (int i = 0; i < numOfTrials; i++)
                    map.put(i + start, num);

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Getter implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
                for (int i = 0; i < numOfTrials; i++)
                    map.get(i);

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Remover implements Runnable {
        @Override
        public void run() {
            try {
                barrier.await();
                for (int i = 0; i < numOfTrials; i++)
                    map.remove(i);

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
