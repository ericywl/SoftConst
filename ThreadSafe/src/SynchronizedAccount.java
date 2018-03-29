import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedAccount {
    private final ReentrantLock lock = new ReentrantLock();
    private AtomicInteger balance;

    SynchronizedAccount() {
        this.balance = new AtomicInteger(0);
    }

    SynchronizedAccount(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    public void deposit(Integer amount) {
        synchronized (lock) {
            balance.getAndAdd(amount);
            System.out.println("Deposited: " + amount);
        }
    }

    public boolean withdraw(Integer amount) {
        synchronized (lock) {
            if (balance.get() < amount) {
                System.out.println("Insufficient balance.");
                return false;
            }

            balance.getAndAdd(-amount);
            System.out.println("Withdrew: " + amount);
        }

        return true;
    }

    public Integer checkBalance() {
        Integer temp = balance.get();
        System.out.println("Current balance: " + temp);
        return temp;
    }
}

class TestAccount {
    public static void main(String[] args) throws InterruptedException {
        SynchronizedAccount account = new SynchronizedAccount();
        Random random = new Random();
        int numOfThreads = 5;

        Thread[] depositThreads = new Thread[numOfThreads];
        Thread[] withdrawThreads = new Thread[numOfThreads];
        Thread[] checkBalanceThreads = new Thread[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            depositThreads[i] = new Thread(() -> account.deposit(100));
            withdrawThreads[i] = new Thread(() -> account.withdraw(100));
            checkBalanceThreads[i] = new Thread(account::checkBalance);
        }

        for (int i = 0; i < numOfThreads; i++) {
            depositThreads[i].start();
            withdrawThreads[i].start();
            checkBalanceThreads[i].start();
        }

        for (int i = 0; i < numOfThreads; i++) {
            depositThreads[i].join();
            withdrawThreads[i].join();
            checkBalanceThreads[i].join();
        }

        System.out.println();
        Thread.sleep(100);
        account.checkBalance();
    }
}
