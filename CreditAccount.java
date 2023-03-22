public class CreditAccount extends Account {

    private double interestRate;
    private double limit;

    public CreditAccount(int accountNumber, int clientID, double balance, Transaction transactions, double interestRate,
            double limit) {
        super(accountNumber, clientID, balance, transactions);
        this.limit = limit;
        this.interestRate = interestRate;
    }

    public double calculateInterest(double interestRate) {
        return this.interestRate;
    }

    public double getInterestRate() {
        return this.interestRate;
    }
}