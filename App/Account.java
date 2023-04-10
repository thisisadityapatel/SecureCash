package App;
public class Account {

    private int accountNumber, clientID;
    private double balance;
    private Transaction transactions;

    public Account(int accountNumber, int clientID, double balance, Transaction transactions) {
        this.accountNumber = accountNumber;
        this.clientID = clientID;
        this.balance = balance;
        this.transactions = transactions;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getClientID() {
        return this.clientID;
    }
}