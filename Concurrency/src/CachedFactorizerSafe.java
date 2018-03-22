import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class FactorizerUser {
    public static void main(String[] args) {
        CachedFactorizerSafe factorizer = new CachedFactorizerSafe();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new MyRunnable(factorizer));
            threads[i].start();
        }

        try {
            for (Thread tr : threads) {
                tr.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\nCache hit ratio: " + factorizer.getCacheHitRatio());
    }

}

class MyRunnable implements Runnable {
    private CachedFactorizerSafe factorizer;

    public MyRunnable(CachedFactorizerSafe factorizer) {
        this.factorizer = factorizer;
    }

    public void run() {
        Random random = new Random();
        int num = random.nextInt(100);
        System.out.println(num + ": " + factorizer.service(num));
    }
}

public class CachedFactorizerSafe {
    private int lastNumber;
    private List<Integer> lastFactors;
    private long hits;
    private long cacheHits;

    public long getHits() {
        synchronized (this) {
            return hits;
        }
    }

    public double getCacheHitRatio() {
        synchronized (this) {
            return (double) cacheHits / (double) hits;
        }
    }

    public List<Integer> service(int input) {
        synchronized (this) {
            List<Integer> factors = null;
            ++hits;

            if (input == lastNumber) {
                ++cacheHits;
                factors = new ArrayList<>(lastFactors);
            }

            if (factors == null) {
                factors = factor(input);
                lastNumber = input;
                lastFactors = new ArrayList<>(factors);
            }

            return factors;
        }
    }

    public List<Integer> factor(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        return factors;
    }
}