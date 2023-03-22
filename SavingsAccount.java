public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(int accountNumber, int clientID, double balance, Transaction transactions,
            double interestRate) {
        super(accountNumber, clientID, balance, transactions);
        this.interestRate = interestRate;
    }

    public double calculateInterest(double interestRate) {
        return this.interestRate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }
}