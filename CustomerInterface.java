public class CustomerInterface {

    ATM atm;

    // constructor
    public CustomerInterface(ATM atm) {
        this.atm = atm;
    }

    // deposit amount
    public boolean deposit(Customer customer, double amount, String accountType) {
        return this.atm.depositAmount(customer, amount, accountType);
    }

    // withrdaw amount
    public boolean withdraw(Customer customer, double amount, String accountType) {
        return this.atm.withdrawAmount(customer, amount, accountType);
    }

    // transfer amount
    public boolean transfer(Customer sender, Customer receiver, double amount, String senderAccType,
            String recipientAccType) {
        return this.atm.transferAmount(sender, receiver, amount, senderAccType, recipientAccType);
    }

    // check balance of account
    public double checkBalance(Customer customer, String accType) {
        return this.atm.getCustomerBalance(customer, accType);
    }

    // change pin of account
    public boolean changePin(Customer customer, String newPin) {
        return this.atm.changeAccountPin(customer, newPin);
    }

    // pay bill
    public boolean payBill(Customer customer, double amount, String accType) {
        return this.atm.withdrawAmount(customer, amount, accType);
    }

    // print account statement
    public boolean printStatement() {
        // database not complete
        return false;
    }

    // request to display help
    public void accessHelp() {
        // no idea how this works
    }
}
