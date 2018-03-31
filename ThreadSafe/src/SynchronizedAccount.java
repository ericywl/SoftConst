import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SynchronizedAccount {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    // guarded by the above lock
    private AtomicInteger balance;

    SynchronizedAccount() {
        this.balance = new AtomicInteger(0);
    }

    SynchronizedAccount(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    // guarded by lock.writeLock()
    // pre-condition: no one else is reading & writing
    // post-condition: add amount to balance
    public void deposit(Integer amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }

        lock.writeLock().lock();
        try {
            balance.getAndAdd(amount);
            System.out.println("Successfully deposited $" + amount + ".");

        } finally {
            lock.writeLock().unlock();
        }
    }

    // guarded by lock.writeLock()
    // pre-condition: no one else is reading & writing
    // post-condition: minus amount from balance if sufficient balance
    public boolean withdraw(Integer amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }

        boolean success;
        lock.writeLock().lock();
        try {
            if (balance.get() < amount) {
                System.out.println("Failed to withdraw $" + amount + ", insufficient balance.");
                success = false;
            } else {
                balance.getAndAdd(-amount);
                System.out.println("Successfully withdrawn $" + amount + ".");
                success = true;
            }

        } finally {
            lock.writeLock().unlock();
        }

        return success;
    }

    // guarded by lock.readLock()
    // pre-condition: no one is writing
    // post-condition: balance unchanged, return balance
    public Integer checkBalance() {
        Integer result;
        lock.readLock().lock();
        try {
            result = balance.get();
            System.out.println("Current balance: $" + result);

        } finally {
            lock.readLock().unlock();
        }

        return result;
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
            depositThreads[i] = new Thread(() -> account.deposit(random.nextInt(100)));
            withdrawThreads[i] = new Thread(() -> account.withdraw(random.nextInt(100)));
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


        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        lock.writeLock().lock();
        System.out.println("Does not reach here.");
    }
}
