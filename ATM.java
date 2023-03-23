public class ATM {

    final Double LIMIT = Double.MAX_VALUE; // ATM balance limit
    private double cashTotal;
    Database db;

    // constructor
    public ATM() {
        db = new Database();
        this.cashTotal = LIMIT;
    }

    // deposit to customer
    public boolean depositAmount(Customer customer, double amount, String accountType) {
        if (accountType.equals("savings")) {
            customer.getSavingsAccount().setBalance(customer.getSavingsAccount().getBalance() + amount);
            // missing code for updating database
            return true;
        } else {
            customer.getChequeingsAccount().setBalance(customer.getChequeingsAccount().getBalance() + amount);
            // missing code for updating database
            return true;
        }
    }

    // withdraw from customer
    public boolean withdrawAmount(Customer customer, double amount, String accountType) {
        if (accountType.equals("savings")) {
            double newBalance = customer.getSavingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return false;
            } else {
                customer.getSavingsAccount().setBalance(newBalance);
            }
            // missing code for updating database
            return true;
        } else {
            double newBalance = customer.getChequeingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return false;
            } else {
                customer.getChequeingsAccount().setBalance(newBalance);
            }
            // missing code for updating database
            return true;
        }
    }

    // transfer from sender to receiver
    public boolean transferAmount(Customer sender, Customer receiver, double amount, String senderAccType,
            String recipientAccType) {
        if (withdrawAmount(sender, amount, senderAccType) == true) {
            depositAmount(receiver, amount, recipientAccType);
            return true;
        }
        return false;
    }

    // get customer balance
    public double getCustomerBalance(Customer customer, String accType) {
        if (accType.equals("savings")) {
            return customer.getSavingsAccount().getBalance();
        } else {
            return customer.getChequeingsAccount().getBalance();
        }
    }

    // change customer pin
    public boolean changeAccountPin(Customer customer, int newPin) {
        return customer.getCard().setPin(newPin);

    }

    // set ATM Balance
    public double setCashTotal(double amount) {
        double newTotal = this.getCashTotal() + amount;
        if (newTotal <= LIMIT) {
            this.cashTotal = newTotal;
            return 0.0;
        } else {
            double extraAmount = newTotal - LIMIT;
            this.cashTotal = LIMIT;
            return extraAmount;
        }
    }

    public double getCashTotal() {
        return this.cashTotal;
    }

}
