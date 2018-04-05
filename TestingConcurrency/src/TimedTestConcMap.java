import java.util.concurrent.ConcurrentHashMap;

public class TimedTestConcMap extends TimedTestSyncMap {
    public TimedTestConcMap(int trials, int numOfThreads) {
        super(trials, numOfThreads);
        this.map = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) throws InterruptedException {
        int repeats = 3;
        int numOfThreads = 1000;
        int maxNumOfTrials = 10000;
        runTest(repeats, numOfThreads, maxNumOfTrials);
    }
}
