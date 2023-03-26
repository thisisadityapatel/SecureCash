public class CustomerInterface {

    ATM atm;

    // constructor
    public CustomerInterface(ATM atm) {
        this.atm = atm;
    }

    // deposit amount
    public String deposit(Customer customer, double amount, String accountType) {
        return this.atm.depositAmount(customer, amount, accountType);
    }

    // withrdaw amount
    public String withdraw(Customer customer, double amount, String accountType) {
        return this.atm.withdrawAmount(customer, amount, accountType);
    }

    // transfer amount
    public String transfer(Customer sender, Customer receiver, double amount, String senderAccType,
            String recipientAccType) {
        return this.atm.transferAmount(sender, receiver, amount, senderAccType, recipientAccType);
    }

    // check balance of account
    public double checkBalance(Customer customer, String accType) {
        return this.atm.getCustomerBalance(customer, accType);
    }

    // change pin of account
    public boolean changePin(Customer customer, int newPin) {
        return this.atm.changeAccountPin(customer, newPin);
    }

    // pay bill
    public String payBill(Customer customer, double amount, String accType) {
        return this.atm.BillPayment(customer, amount, accType);
    }

    // print account statement
    public String printStatement(Customer customer, String accountNum) {
        return this.atm.getLog(customer, accountNum);
    }

    // request to display help
    public void accessHelp() {
        // no idea how this works
    }
}
