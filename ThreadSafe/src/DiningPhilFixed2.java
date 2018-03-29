import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilFixed2 {
    private static int numOfPhils = 5;

    public static void main(String[] args) {
        PhilosopherTryLock[] phils = new PhilosopherTryLock[numOfPhils];
        ForkTryLock[] forks = new ForkTryLock[numOfPhils];

        for (int i = 0; i < numOfPhils; i++) {
            forks[i] = new ForkTryLock(i);
        }

        for (int i = 0; i < numOfPhils; i++) {
            phils[i] = new PhilosopherTryLock(i, forks[i], forks[(i + numOfPhils - 1) % numOfPhils]);
            phils[i].start();
        }
    }
}

class PhilosopherTryLock extends Thread {
    private final int index;
    private final ForkTryLock left;
    private final ForkTryLock right;

    public PhilosopherTryLock(int index, ForkTryLock left, ForkTryLock right) {
        this.index = index;
        this.left = left;
        this.right = right;
    }

    public void run() {
        Random randomGenerator = new Random();
        ReentrantLock leftLock = left.getLock();
        ReentrantLock rightLock = right.getLock();

        try {
            while (true) {
                Thread.sleep(randomGenerator.nextInt(1000)); // not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");

                boolean leftFlag = leftLock.tryLock(1000, TimeUnit.MILLISECONDS);
                boolean rightFlag = rightLock.tryLock(1000, TimeUnit.MILLISECONDS);
                if (leftFlag && rightFlag) {
                    try {
                        left.pickUp();
                        System.out.println("Phil " + index + " picks up left fork.");
                        right.pickUp();
                        System.out.println("Phil " + index + " picks up right fork.");

                        Thread.sleep(randomGenerator.nextInt(1000));
                        System.out.println("Phil " + index + " finishes eating.");

                        left.putDown();
                        System.out.println("Phil " + index + " puts down left fork.");
                        right.putDown();
                        System.out.println("Phil " + index + " puts down right fork.");

                    } finally {
                        leftLock.unlock();
                        rightLock.unlock();
                    }
                } else {
                    leftLock.unlock();
                    rightLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
        }
    }
}

class ForkTryLock {
    private final int index;
    private boolean isAvailable = true;
    private ReentrantLock lock;

    public ForkTryLock(int index) {
        this.index = index;
        this.lock = new ReentrantLock();
    }

    public void pickUp() {
        isAvailable = false;
    }

    public void putDown() {
        isAvailable = true;
    }

    public int getIndex() {
        return index;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public String toString() {
        if (isAvailable) {
            return "Fork " + index + " is available.";
        } else {
            return "Fork " + index + " is NOT available.";
        }
    }
}

