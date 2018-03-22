public class ThirdError {
    public static void main(String args[]) {
        int numOfThreads = 1000;
        BankAccount myAccount = new BankAccount(10000, 1000);
        ATM[] atms = new ATM[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            atms[i] = new ATM(myAccount);
            atms[i].start();
        }

        try {
            for (int i = 0; i < numOfThreads; i++) {
                atms[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println("some thread is not finished");
        }

        System.out.println(myAccount);
    }
}

class ATM extends Thread {
    private final BankAccount account;

    public ATM(BankAccount account) {
        this.account = account;
    }

    public void run() {
        account.invest(10);
    }
}

class BankAccount {
    // @GuardedBy("this")
    private int saving; // this variable must not be negative;
    // @GuardedBy("this")
    private int investment; // this variable must not be negative;
    // invariant: saving + investment remains constant

    public BankAccount(int saving, int investment) {
        this.saving = saving;
        this.investment = investment;
    }

    public void deposit(int n) {
        synchronized (this) {
            saving += n;
        }
    }

    public void invest(int n) {
        synchronized (this) {
            saving -= n;
            investment += n;
        }
    }

    public String toString() {
        synchronized (this) {
            return "Saving: SGD " + saving + "; Investment: SGD " + investment;
        }
    }
}
