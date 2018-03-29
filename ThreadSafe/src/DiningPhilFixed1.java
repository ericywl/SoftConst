import java.util.Random;

public class DiningPhilFixed1 {
    private static int numOfPhils = 5;

    public static void main(String[] args) {
        PhilosopherOrdered[] phils = new PhilosopherOrdered[numOfPhils];
        ForkOrdered[] forks = new ForkOrdered[numOfPhils];

        for (int i = 0; i < numOfPhils; i++) {
            forks[i] = new ForkOrdered(i);
        }

        for (int i = 0; i < numOfPhils; i++) {
            phils[i] = new PhilosopherOrdered(i, forks[i], forks[(i + numOfPhils - 1) % numOfPhils]);
            phils[i].start();
        }
    }
}

class PhilosopherOrdered extends Thread {
    private final int index;
    private final ForkOrdered left;
    private final ForkOrdered right;

    public PhilosopherOrdered(int index, ForkOrdered left, ForkOrdered right) {
        this.index = index;
        this.left = left;
        this.right = right;
    }

    public void run() {
        // impose an order, take only the lower index fork
        boolean orderCheck = left.getIndex() < right.getIndex();
        ForkOrdered first = orderCheck ? left : right;
        String firstStr = orderCheck ? "left" : "right";
        ForkOrdered second = orderCheck ? right : left;
        String secondStr = orderCheck ? "right" : "left";
        Random randomGenerator = new Random();
        try {
            while (true) {
                // Thread.sleep(randomGenerator.nextInt(1000)); // not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");
                first.pickUp();
                System.out.println("Phil " + index + " picks up " + firstStr + " fork.");
                Thread.sleep(1000);
                second.pickUp();
                System.out.println("Phil " + index + " picks up " + secondStr + " fork.");

                Thread.sleep(randomGenerator.nextInt(1000)); // eating
                System.out.println("Phil " + index + " finishes eating.");
                first.putDown();
                System.out.println("Phil " + index + " picks up " + firstStr + " fork.");
                second.putDown();
                System.out.println("Phil " + index + " picks up " + secondStr + " fork.");

            }
        } catch (InterruptedException e) {
            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
        }
    }
}

class ForkOrdered {
    private final int index;
    private boolean isAvailable = true;

    public ForkOrdered(int index) {
        this.index = index;
    }

    public synchronized void pickUp() throws InterruptedException {
        while (!isAvailable) {
            wait();
        }

        isAvailable = false;
        notifyAll();
    }

    public synchronized void putDown() throws InterruptedException {
        while (isAvailable) {
            wait();
        }

        isAvailable = true;
        notifyAll();
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        if (isAvailable) {
            return "Fork " + index + " is available.";
        } else {
            return "Fork " + index + " is NOT available.";
        }
    }
}