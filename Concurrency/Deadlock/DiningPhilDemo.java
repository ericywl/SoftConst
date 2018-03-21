import java.util.Random;

public class DiningPhilDemo {
    private static int numOfPhilosophers = 5;

    public static void main(String[] args) throws Exception {
        Philosopher[] philosophers = new Philosopher[numOfPhilosophers];
        Fork[] forks = new Fork[numOfPhilosophers];

        for (int i = 0; i < numOfPhilosophers; i++) {
            forks[i] = new Fork(i);
        }

        for (int i = 0; i < numOfPhilosophers; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + numOfPhilosophers - 1) %
                    numOfPhilosophers]);
            philosophers[i].start();
        }
    }
}

class Philosopher extends Thread {
    private final int index;
    private final Fork left;
    private final Fork right;

    public Philosopher(int index, Fork left, Fork right) {
        this.index = index;
        this.left = left;
        this.right = right;
    }

    public void run() {
        Random randomGenerator = new Random();
        try {
            while (true) {
                Thread.sleep(randomGenerator.nextInt(100)); // not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");

                System.out.println("Phil " + index + " trying to pick up left fork" + left.getIndex() + ".");
                left.pickUp();
                System.out.println("Phil " + index + " picks up left fork " + left.getIndex() + ".");

                System.out.println("Phil " + index + " starts thinking again.");
                Thread.sleep(randomGenerator.nextInt(1000)); // not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");

                System.out.println("Phil " + index + " trying to pick up right fork " + right.getIndex() +
                        ".");
                right.pickUp();
                System.out.println("Phil " + index + " picks up right fork" + right.getIndex() + ".");

                Thread.sleep(randomGenerator.nextInt(1000)); // eating
                System.out.println("Phil " + index + " finishes eating.");
                left.putDown();
                System.out.println("Phil " + index + " puts down left fork " + left.getIndex() + ".");
                right.putDown();
                System.out.println("Phil " + index + " puts down right fork" + right.getIndex() + ".");
            }
        } catch (InterruptedException e) {
            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
        }
    }
}

class Fork {
    private final int index;
    private boolean isAvailable = true;

    public Fork(int index) {
        this.index = index;
    }

    public synchronized void pickUp() throws InterruptedException {
        while (!isAvailable) {
            System.out.println(this.toString());
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