import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimedTestSyncMap {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected BarrierTimer timer = new BarrierTimer();
    protected CyclicBarrier barrier;
    protected Map<Integer, Integer> map;
    protected final int numOfTrials, numOfThreads;

    public TimedTestSyncMap(int trials, int numOfThreads) {
        this.map = Collections.synchronizedMap(new HashMap<>());
        this.numOfTrials = trials;
        this.numOfThreads = numOfThreads;
        this.barrier = new CyclicBarrier(numOfThreads + 1, timer);
    }

    public static void main(String[] args) throws InterruptedException {
        int repeats = 3;
        int numOfThreads = 1000;
        int maxNumOfTrials = 10000;
        runTest(repeats, numOfThreads, maxNumOfTrials);
    }

    static void runTest(int repeats, int numOfThreads, int maxNumOfTrials) throws InterruptedException {
        for (int trials = 10; trials <= maxNumOfTrials; trials *= 10) {
            TimedTestSyncMap testMap = new TimedTestSyncMap(trials, numOfThreads);
            long[] timeArr = new long[repeats];
            System.out.println("Number of items: " + trials * numOfThreads);
            // test put
            System.out.print(pad("Time to put: ", true));
            for (int j = 0; j < repeats; j++) {
                timeArr[j] = testMap.testPut();
                System.out.print("\t");
                Thread.sleep(500);
            }
            System.out.print("  |  Average: " + pad(getAverage(timeArr) + "ns", false));

            // test get
            System.out.println();
            System.out.print(pad("Time to get: ", true));
            for (int j = 0; j < repeats; j++) {
                timeArr[j] = testMap.testGet();
                System.out.print("\t");
                Thread.sleep(500);
            }
            System.out.print("  |  Average: " + pad(getAverage(timeArr) + "ns", false));

            // test remove
            System.out.println();
            System.out.print(pad("Time to remove: ", true));
            for (int j = 0; j < repeats; j++) {
                timeArr[j] = testMap.testRemove();
                System.out.print("\t");
                Thread.sleep(500);
            }
            System.out.print("  |  Average: " + pad(getAverage(timeArr) + "ns", false));

            System.out.println("\n");
            Thread.sleep(500);

            assert testMap.mapIsEmpty();
        }

        TimedTestSyncMap.pool.shutdown();
    }

    long testPut() {
        try {
            timer.clear();
            for (int i = 0; i < numOfThreads; i++)
                pool.execute(new Putter(i));

            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.print(pad(time + "ns", false));

            return time;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        return 0;
    }

    long testGet() {
        try {
            timer.clear();
            for (int i = 0; i < numOfThreads; i++)
                pool.execute(new Getter(i));

            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.print(pad(time + "ns", false));

            return time;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        return 0;
    }


    long testRemove() {
        try {
            timer.clear();
            for (int i = 0; i < numOfThreads; i++)
                pool.execute(new Remover(i));

            barrier.await();
            barrier.await();
            long time = timer.getTime();
            System.out.print(pad(time + "ns", false));

            return time;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        return 0;
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
                int num = this.hashCode() ^ (int) System.nanoTime();
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
        private final int index;
        private final int start;

        Getter(int index) {
            this.index = index;
            this.start = index * numOfTrials;
        }

        @Override
        public void run() {
            try {
                barrier.await();
                for (int i = 0; i < numOfTrials; i++)
                    map.get(i + start);

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Remover implements Runnable {
        private final int index;
        private final int start;

        Remover(int index) {
            this.index = index;
            this.start = index * numOfTrials;
        }

        @Override
        public void run() {
            try {
                barrier.await();
                for (int i = 0; i < numOfTrials; i++)
                    map.remove(i + start);

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    boolean mapIsEmpty() {
        return map.isEmpty();
    }

    static long getAverage(long[] array) {
        long sum = 0;
        for (long num : array) {
            sum+= num;
        }

        return sum / array.length;
    }

    static String pad(String input, boolean right) {
        String minus = right ? "-" : "";

        return String.format("%1$" + minus + "17s", input);
    }
}
