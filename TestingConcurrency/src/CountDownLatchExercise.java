import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExercise {
    private static final int numOfThreads = 4;

    public static void main(String[] args) {
        String[] array = new String[]{"F", "F", "F", "F", "A", "F", "F", "F",
                "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"};

        checkFail(array);
    }

    private static void checkFail(String[] array) {
        if (array.length < numOfThreads) {
            System.out.println("Number of threads cannot be larger than array length.");
            return;
        }

        final CountDownLatch latch = new CountDownLatch(7);
        Thread[] threads = new Thread[numOfThreads];
        int splitLen = (int) Math.ceil(array.length / numOfThreads);

        for (int i = 0; i < numOfThreads; i++) {
            String[] newArray;
            if (i != numOfThreads - 1) {
                newArray = new String[splitLen];
                System.arraycopy(array, i * splitLen, newArray, 0, splitLen);
            } else {
                int remainLen = array.length - ((numOfThreads - 1) * splitLen);
                newArray = new String[remainLen];
                System.arraycopy(array, i * splitLen, newArray, 0, remainLen);
            }

            threads[i] = new Thread(new Task(newArray, latch));
        }

        for (Thread t : threads)
            t.start();

        try {
            latch.await();
            System.out.println("7 Fails Found!");
            for (Thread t : threads) {
                t.interrupt();
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Task implements Runnable {
        private final String[] array;
        private final CountDownLatch latch;

        public Task(String[] array, CountDownLatch latch) {
            this.array = Arrays.copyOf(array, array.length);
            this.latch = latch;
        }

        @Override
        public void run() {
            for (String grade : array) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                if (grade.equalsIgnoreCase("f"))
                    latch.countDown();
            }
        }
    }
}
