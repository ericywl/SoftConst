import java.util.concurrent.atomic.AtomicInteger;

public class LockStaticVariablesFixed {
    public static final AtomicInteger saving = new AtomicInteger(5000);
    public static final AtomicInteger cash = new AtomicInteger(0);
    public static final Object lock = new Object();

    public static void main(String args[]){
        int numOfThreads = 10000;
        WD[] threads = new WD[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            threads[i] = new WD();
            threads[i].start();
        }

        try {
            for (int i = 0; i < numOfThreads; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println("some thread is not finished");
        }

        System.out.print("The result is: " + LockStaticVariablesFixed.cash);
    }
}

class WD extends Thread {
    public void run () {
        synchronized (LockStaticVariablesFixed.lock) {
            if (LockStaticVariablesFixed.saving.get() >= 1000) {
                System.out.println("I am doing something.");
                LockStaticVariablesFixed.saving.getAndAdd(-1000);
                LockStaticVariablesFixed.cash.getAndAdd(1000);
            }
        }
    }
}

