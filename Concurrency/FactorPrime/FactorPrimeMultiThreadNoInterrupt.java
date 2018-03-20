import java.math.BigInteger;

public class FactorPrimeMultiThreadNoInterrupt {
    public static final int numberOfThreads = 4;

    public static void main(String[] args) throws Exception {
        BigInteger n = new BigInteger("1127451830576035879");
        FactorNoInterrupt[] factors = new FactorNoInterrupt[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            factors[i] = new FactorNoInterrupt(n, i + 2, numberOfThreads);
            factors[i].start();
        }

        for (FactorNoInterrupt ft : factors) {
            ft.join();
        }

    }
}

class FactorNoInterrupt extends Thread {
    private BigInteger n;
    private BigInteger init;
    private BigInteger stepSize;
    private BigInteger result = null;

    FactorNoInterrupt(BigInteger n, int init, int stepSize) {
        this.n = n;
        this.init = BigInteger.valueOf(init);
        this.stepSize = BigInteger.valueOf(stepSize);
    }

    public void run() {
        BigInteger zero = new BigInteger("0");

        while (init.compareTo(n) < 0) {
            if (n.remainder(init).compareTo(zero) == 0) {
                result = init;
                System.out.println("Got it: " + result);
                break;
            }

            init = init.add(stepSize);
        }
    }

    public BigInteger getResult() {
        return result;
    }
}
