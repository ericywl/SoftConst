package Account;

public class Account {
    private int balance = 0;

    public Account() {
        // empty constructor
    }

    public Account(int balance) {
        this.balance = balance;
    }

    // Tripling the balance and then plus 10
    int calAmount() {
        return balance * 3 + 10;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void setBalance(int amount) {
        balance = amount;
    }

    public int getBalance() {
        return balance;
    }

    public int compare(Account yourAccount) {
        return balance / yourAccount.balance;
    }
}
