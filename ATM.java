import java.text.SimpleDateFormat;
import java.util.Date;

public class ATM {

    final Double LIMIT = 200000.0; // ATM balance limit
    private double cashTotal;
    Database db;
    // get date
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String finalDate = dateFormat.format(date);

    // constructor
    public ATM(Database db) {
        this.db = db;
        this.cashTotal = LIMIT / 2;
    }

    // deposit to customer
    public String depositAmount(Customer customer, double amount, String accountType) {
        if (accountType.equalsIgnoreCase("savings")) {
            double dbBalance = amount + cashTotal;
            if (dbBalance > LIMIT) {
                return "Cannot Deposit: ATM Cash Limit reached.";
            }
            cashTotal = dbBalance;
            customer.getSavingsAccount().setBalance(customer.getSavingsAccount().getBalance() + amount);
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), customer.getSavingsAccount().getBalance() + amount);
            // adding transaction
            Transaction tr = new Transaction(finalDate, amount, customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            // return Transaction
            return "Deposit Successful";
        } else {
            double dbBalance = amount + cashTotal;
            if (dbBalance > LIMIT) {
                return "Cannot Deposit: ATM Cash Limit reached.";
            }
            cashTotal = dbBalance;
            customer.getChequeingsAccount().setBalance(customer.getChequeingsAccount().getBalance() + amount);
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(),
                    customer.getChequeingsAccount().getBalance() + amount);
            // adding transaction
            Transaction tr = new Transaction(finalDate, amount, customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            // return Transaction
            return "Deposit Successful";
        }
    }

    // withdraw from customer
    public String withdrawAmount(Customer customer, double amount, String accountType) {
        if (accountType.equalsIgnoreCase("savings")) {
            double newBalance = customer.getSavingsAccount().getBalance() - amount;
            double newDBbalance = cashTotal - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings Account.";
            }
            if (newDBbalance < 0) {
                return "ATM out of Balance.";
            }
            cashTotal = newDBbalance;
            customer.getSavingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), newBalance);
            // adding transaction
            Transaction tr = new Transaction(finalDate, -1 * amount, customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            return "Withdrawal Successful";
        } else {
            double newBalance = customer.getChequeingsAccount().getBalance() - amount;
            double newDBbalance = cashTotal - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Chequeing Account.";
            }
            if (newDBbalance < 0) {
                return "ATM out of Balance.";
            }
            cashTotal = newDBbalance;
            customer.getChequeingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(), newBalance);
            // adding transaction
            Transaction tr = new Transaction(finalDate, -1 * amount, customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            return "Withdrawal Successful";
        }
    }

    // transfer from sender to receiver
    public String transferAmount(Customer sender, Customer receiver, double amount, String senderAccType,
            String recipientAccType) {
        if (senderAccType.equalsIgnoreCase("savings")) {
            double newBalance = sender.getSavingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings Account.";
            }
            // sender transaction
            sender.getSavingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(sender.getSavingsAccount(), newBalance);
            // adding transaction
            Transaction trSender = new Transaction(finalDate, -1 * amount, sender, sender.getSavingsAccount());
            db.addAccountTransaction(trSender);

            if (recipientAccType.equalsIgnoreCase("savings")) {
                // receiver transaction
                receiver.getSavingsAccount().setBalance(receiver.getSavingsAccount().getBalance() + amount);
                // updating database
                db.updateAccountAmount(receiver.getSavingsAccount(),
                        receiver.getSavingsAccount().getBalance() + amount);
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, amount, receiver, receiver.getSavingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return "Transfer Successful";
            } else {
                // receiver transaction
                receiver.getChequeingsAccount().setBalance(receiver.getChequeingsAccount().getBalance() + amount);
                // updating database
                db.updateAccountAmount(receiver.getChequeingsAccount(),
                        receiver.getChequeingsAccount().getBalance() + amount);
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, amount, receiver, receiver.getChequeingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return "Transfer Successful";
            }
        } else {
            double newBalance = sender.getChequeingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Chequeing Account.";
            }
            // sender transaction
            sender.getChequeingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(sender.getChequeingsAccount(), newBalance);
            // adding transaction
            Transaction trSender = new Transaction(finalDate, -1 * amount, sender, sender.getChequeingsAccount());
            db.addAccountTransaction(trSender);

            if (recipientAccType.equalsIgnoreCase("savings")) {
                // receiver transaction
                receiver.getSavingsAccount().setBalance(receiver.getSavingsAccount().getBalance() + amount);
                // updating database
                db.updateAccountAmount(receiver.getSavingsAccount(),
                        receiver.getSavingsAccount().getBalance() + amount);
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, amount, receiver, receiver.getSavingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return "Transfer Successful";
            } else {
                // receiver transaction
                receiver.getChequeingsAccount().setBalance(receiver.getChequeingsAccount().getBalance() + amount);
                // updating database
                db.updateAccountAmount(receiver.getChequeingsAccount(),
                        receiver.getChequeingsAccount().getBalance() + amount);
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, amount, receiver, receiver.getChequeingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return "Transfer Successful";
            }
        }
    }

    // get customer balance
    public double getCustomerBalance(Customer customer, String accType) {
        if (accType.equalsIgnoreCase("savings")) {
            return customer.getSavingsAccount().getBalance();
        } else {
            return customer.getChequeingsAccount().getBalance();
        }
    }

    // change customer pin
    public boolean changeAccountPin(Customer customer, int newPin) {
        return customer.getCard().setPin(newPin);

    }

    public String BillPayment(Customer customer, double amount, String accType) {
        if (accType.equalsIgnoreCase("savings")) {
            double newBalance = customer.getSavingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings Account.";
            }
            customer.getSavingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), newBalance);
            // adding transaction
            Transaction tr = new Transaction(finalDate, -1 * amount, customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            return "Payment Successful";
        } else {
            double newBalance = customer.getChequeingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Chequeing Account.";
            }
            customer.getChequeingsAccount().setBalance(newBalance);
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(), newBalance);
            // adding transaction
            Transaction tr = new Transaction(finalDate, -1 * amount, customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            return "Payment Successful";
        }
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
