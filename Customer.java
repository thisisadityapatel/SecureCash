public class Customer {
    private int customerID;
    private SavingsAccount savingsAccount;
    private ChequeingsAccount chequeingsAccount;
    private CreditAccount creditAccount;

    public Customer(int customerID, SavingsAccount savingsAccount, ChequeingsAccount chequeingsAccount, CreditAccount creditAccount) {
        this.customerID = customerID;
        this.savingsAccount = savingsAccount;
        this.chequeingsAccount = chequeingsAccount;
        this.creditAccount = creditAccount;
    }

    public int getCustomerID() {
        return customerID;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public ChequeingsAccount getChequeingsAccount() {
        return chequeingsAccount;
    }

    public CreditAccount getCreditAccount() {
        return creditAccount;
    }

    public boolean login(Card card, int pin) {
        if (card.getPin() == pin) {
            return true;
        } else {
            return false;
        }
    }
}
