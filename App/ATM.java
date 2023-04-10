package App;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.standard.RequestingUserName;



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
        this.cashTotal = db.getLastAtmBalance();
    }

    // rounding function
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // deposit to customer
    public String depositAmount(Customer customer, double amount, String accountType) {
        if (accountType.equalsIgnoreCase("savings")) {
            double dbBalance = amount + cashTotal;
            if (dbBalance > LIMIT) {
                return "ATM Cash Limit reached.";
            }
            cashTotal = dbBalance;
            db.updateLastAtmBalance(round(cashTotal,2));
            customer.getSavingsAccount().setBalance(round(customer.getSavingsAccount().getBalance() + amount, 2));
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), round(customer.getSavingsAccount().getBalance(), 2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(amount, 2), customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            // return Transaction
            return tr.getReceipt(tr);
        } else if (accountType.equalsIgnoreCase("checking")) {
            double dbBalance = amount + cashTotal;
            if (dbBalance > LIMIT) {
                return "ATM Cash Limit reached.";
            }
            cashTotal = dbBalance;
            db.updateLastAtmBalance(round(cashTotal,2));
            customer.getChequeingsAccount().setBalance(round(customer.getChequeingsAccount().getBalance() + amount,2));
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(),
                    round(customer.getChequeingsAccount().getBalance(),2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(amount,2), customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            // return Transaction
            return tr.getReceipt(tr);
        } else {
            return "";
        }
    }

    // withdraw from customer
    public String withdrawAmount(Customer customer, double amount, String accountType) {
        if (accountType.equalsIgnoreCase("savings")) {
            double newBalance = customer.getSavingsAccount().getBalance() - amount;
            double newDBbalance = cashTotal - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings.";
            }
            if (newDBbalance < 0) {
                return "ATM out of Balance.";
            }
            cashTotal = newDBbalance;
            db.updateLastAtmBalance(round(cashTotal,2));
            customer.getSavingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(-1 * amount,2), customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            return tr.getReceipt(tr);
        } else if (accountType.equalsIgnoreCase("checking")) {
            double newBalance = customer.getChequeingsAccount().getBalance() - amount;
            double newDBbalance = cashTotal - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Checking.";
            }
            if (newDBbalance < 0) {
                return "ATM out of Balance.";
            }
            cashTotal = newDBbalance;
            db.updateLastAtmBalance(round(cashTotal,2));
            customer.getChequeingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(-1 * amount,2), customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            return tr.getReceipt(tr);
        } else {
            return "";
        }
    }

    // transfer from sender to receiver
    public String transferAmount(Customer sender, Customer receiver, double amount, String senderAccType, String recipientAccType) {
        if (senderAccType.equalsIgnoreCase("savings")) {
            double newBalance = sender.getSavingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings.";
            }
            // sender transaction
            sender.getSavingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(sender.getSavingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction trSender = new Transaction(finalDate, round(-1 * amount,2), sender, sender.getSavingsAccount());
            db.addAccountTransaction(trSender);

            if (recipientAccType.equalsIgnoreCase("savings")) {
                // receiver transaction
                receiver.getSavingsAccount().setBalance(round(receiver.getSavingsAccount().getBalance() + amount,2));
                // updating database
                db.updateAccountAmount(receiver.getSavingsAccount(),
                        round(receiver.getSavingsAccount().getBalance(),2));
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, round(amount,2), receiver, receiver.getSavingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return trSender.getReceipt(trSender, trReceiver);
            } else if (recipientAccType.equalsIgnoreCase("checking")) {
                // receiver transaction
                receiver.getChequeingsAccount().setBalance(round(receiver.getChequeingsAccount().getBalance() + amount,2));
                // updating database
                db.updateAccountAmount(receiver.getChequeingsAccount(),
                        round(receiver.getChequeingsAccount().getBalance(),2));
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, round(amount,2), receiver, receiver.getChequeingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return trSender.getReceipt(trSender, trReceiver);
            }
        } else {
            double newBalance = sender.getChequeingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Chequeing.";
            }
            // sender transaction
            sender.getChequeingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(sender.getChequeingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction trSender = new Transaction(finalDate, round(-1 * amount,2), sender, sender.getChequeingsAccount());
            db.addAccountTransaction(trSender);

            if (recipientAccType.equalsIgnoreCase("savings")) {
                // receiver transaction
                receiver.getSavingsAccount().setBalance(round(receiver.getSavingsAccount().getBalance() + amount,2));
                // updating database
                db.updateAccountAmount(receiver.getSavingsAccount(),
                        round(receiver.getSavingsAccount().getBalance(),2));
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, round(amount,2), receiver, receiver.getSavingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return trSender.getReceipt(trSender, trReceiver);
            } else {
                // receiver transaction
                receiver.getChequeingsAccount().setBalance(round(receiver.getChequeingsAccount().getBalance() + amount,2));
                // updating database
                db.updateAccountAmount(receiver.getChequeingsAccount(),
                        round(receiver.getChequeingsAccount().getBalance(),2));
                // adding transaction
                Transaction trReceiver = new Transaction(finalDate, round(amount,2), receiver, receiver.getChequeingsAccount());
                db.addAccountTransaction(trReceiver);
                // return transaction here
                return trSender.getReceipt(trSender, trReceiver);
            }
        }

        return "";
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
        if ((customer.getCard().getPin() == newPin) || (Integer.toString(newPin).length() != 4)) {
            return false;
        }
        if (customer.getCard().setPin(newPin) == true) {
            db.updatePin(customer.getCard(), newPin);
        }
        return true;
    }

    public String BillPayment(Customer customer, double amount, String accType) {
        if (accType.equalsIgnoreCase("savings")) {
            double newBalance = customer.getSavingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Savings.";
            }
            customer.getSavingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(customer.getSavingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(-1 * amount,2), customer, customer.getSavingsAccount());
            db.addAccountTransaction(tr);
            return tr.getReceipt(tr);
        } else {
            double newBalance = customer.getChequeingsAccount().getBalance() - amount;
            if (newBalance < 0) {
                return "Not enough Balance in Chequeing.";
            }
            customer.getChequeingsAccount().setBalance(round(newBalance,2));
            // updating database
            db.updateAccountAmount(customer.getChequeingsAccount(), round(newBalance,2));
            // adding transaction
            Transaction tr = new Transaction(finalDate, round(-1 * amount,2), customer, customer.getChequeingsAccount());
            db.addAccountTransaction(tr);
            return tr.getReceipt(tr);
        }
    }

    public String getLog(Customer customer, String accType) {
        if (accType.equalsIgnoreCase("savings")) {
            ArrayList<Transaction> tr = db.getStatement(customer.getSavingsAccount().getAccountNumber());
            String temp = "";
            for (Transaction t : tr) {
                Transaction newT = new Transaction(t.getDate(), t.getAmount(), customer, customer.getSavingsAccount());
                temp = temp + newT.getReceipt(newT) + "\n";
            }
            return temp;
        } else {
            ArrayList<Transaction> tr = db.getStatement(customer.getChequeingsAccount().getAccountNumber());
            String temp = "";
            for (Transaction t : tr) {
                Transaction newT = new Transaction(t.getDate(), t.getAmount(), customer,
                        customer.getChequeingsAccount());
                temp = temp + newT.getReceipt(newT) + "\n";
            }
            return temp;
        }
    }

    // set ATM Balance
    public double setCashTotal(double amount) {
        double newTotal = db.getLastAtmBalance() + amount;
        if (newTotal <= LIMIT) {
            this.cashTotal = newTotal;
            db.updateLastAtmBalance(round(cashTotal,2));
            return 0.0;
        } else {
            double extraAmount = newTotal - LIMIT;
            this.cashTotal = LIMIT;
            db.updateLastAtmBalance(round(cashTotal,2));
            return extraAmount;
        }
    }

    public double employeeWithdraw(double amount){
        double newDBbalance = db.getLastAtmBalance() - amount;
        if (amount > LIMIT){
            return -1.0;
        }
        if (newDBbalance < 0){
            return -1.0;
        }
        db.updateLastAtmBalance(round(newDBbalance,2));
        this.cashTotal = db.getLastAtmBalance();
        return amount;
    }

    public double getCashTotal() {
        return this.cashTotal;
    }

}
