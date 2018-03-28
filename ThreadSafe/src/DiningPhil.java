//import java.util.Random;
//
//public class DiningPhil {
//    private static int numOfPhils = 5;
//
//    public static void main(String[] args) throws Exception {
//        Philosopher[] phils = new Philosopher[numOfPhils];
//        Fork[] forks = new Fork[numOfPhils];
//
//        for (int i = 0; i < numOfPhils; i++) {
//            forks[i] = new Fork(i);
//        }
//
//        for (int i = 0; i < numOfPhils; i++) {
//            phils[i] = new Philosopher(i, forks[i], forks[(i + numOfPhils - 1) % numOfPhils]);
//            phils[i].start();
//        }
//    }
//}
//
//class Philosopher extends Thread {
//    private final int index;
//    private final Fork left;
//    private final Fork right;
//
//    public Philosopher(int index, Fork left, Fork right) {
//        this.index = index;
//        this.left = left;
//        this.right = right;
//    }
//
//    public void run() {
//        Random randomGenerator = new Random();
//        try {
//            while (true) {
//                Thread.sleep(randomGenerator.nextInt(100)); // not sleeping but thinking
//                System.out.println("Phil " + index + " finishes thinking.");
//                left.pickUp();
//                System.out.println("Phil " + index + " picks up left fork.");
//                right.pickUp();
//                System.out.println("Phil " + index + " picks up right fork.");
//                Thread.sleep(randomGenerator.nextInt(100)); // eating
//                System.out.println("Phil " + index + " finishes eating.");
//                left.putDown();
//                System.out.println("Phil " + index + " puts down left fork.");
//                right.putDown();
//                System.out.println("Phil " + index + " puts down right fork.");
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
//        }
//    }
//}
//
//class Fork {
//    private final int index;
//    private boolean isAvailable = true;
//
//    public Fork(int index) {
//        this.index = index;
//    }
//
//    public synchronized void pickUp() throws InterruptedException {
//        while (!isAvailable) {
//            wait();
//        }
//
//        isAvailable = false;
//        notifyAll();
//    }
//
//    public synchronized void putDown() throws InterruptedException {
//        while (isAvailable) {
//            wait();
//        }
//
//        isAvailable = true;
//        notifyAll();
//    }
//
//    public String toString() {
//        if (isAvailable) {
//            return "Fork " + index + " is available.";
//        } else {
//            return "Fork " + index + " is NOT available.";
//        }
//    }
//}