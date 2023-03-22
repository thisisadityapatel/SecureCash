public class Customer {
    private int customerID;
    private Account savingsAccount;
    private Account checkingAccount;
    private Account creditAccount;

    public Customer(int customerID) {
        this.customerID = customerID;
        this.savingsAccount = new Account();
        this.checkingAccount = new Account();
        this.creditAccount = new Account();
    }

    public int getCustomerID() {
        return customerID;
    }
    
    public Account getSavingsAccount() {
        return savingsAccount;
    }
    
    public Account getCheckingAccount() {
        return checkingAccount;
    }
    
    public Account getCreditAccount() {
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
