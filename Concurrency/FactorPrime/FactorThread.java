import java.math.BigInteger;

public class FactorThread {
    public static final int numberOfThreads = 4;

    public static void main(String[] args) throws Exception {
        BigInteger n = new BigInteger("1127451830576035879");
        System.out.println("Got it: " + factorMultiThread(n));
    }

    // Pre-condition: n is a semi-prime number.
    // Post-condition: the returned value is a prime factor of n;
    private static BigInteger factorMultiThread(BigInteger input) throws InterruptedException {
        FactorInterrupt[] factors = new FactorInterrupt[numberOfThreads];
        BigInteger result = null;

        for (int i = 0; i < numberOfThreads; i++) {
            factors[i] = new FactorInterrupt(input, i + 2, numberOfThreads);
            factors[i].start();
        }

        while (result == null) {
            for (FactorInterrupt ft : factors) {
                if (ft.getResult() != null) {
                    result = ft.getResult();
                    for (int i = 0; i < numberOfThreads; i++) {
                        factors[i].interrupt();
                    }
                }
            }
        }

        for (FactorInterrupt ft : factors) {
            ft.join();
        }

        return result;
    }
}

class FactorInterrupt extends Thread {
    private BigInteger n;
    private BigInteger init;
    private BigInteger stepSize;
    private BigInteger result = null;

    FactorInterrupt(BigInteger n, int init, int stepSize) {
        this.n = n;
        this.init = BigInteger.valueOf(init);
        this.stepSize = BigInteger.valueOf(stepSize);
    }

    public void run() {
        BigInteger zero = new BigInteger("0");

        while (init.compareTo(n) < 0) {
            if (this.isInterrupted()) {
                return;
            }

            if (n.remainder(init).compareTo(zero) == 0) {
                result = init;
                break;
            }

            init = init.add(stepSize);
        }
    }

    public BigInteger getResult() {
        return result;
    }
}