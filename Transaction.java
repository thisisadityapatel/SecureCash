public class Transaction {
    private String date;
    private Double amount;
    private Customer customer;
    private Account account;

    // constructor method
    public Transaction(String date, Double amount, Customer customer, Account account) {
        this.date = date;
        this.amount = amount;
        this.customer = customer;
        this.account = account;
    }

    // printing the receipt of an account transaction
    public String getReceipt(Transaction t) {
        String output = "";
        output += "\n";
        output += "-----------------------------\n";
        output += "Date: " + t.getDate() + "\n";
        output += "Account: " + t.getAccount().getAccountNumber() + "\n";
        output += "Amount: " + amount + "\n";
        output += "-----------------------------\n";
        return output;
    }

    // printing the receipt of an transfer transaction between two accounts
    public String getReceipt(Transaction t1, Transaction t2) {
        String output = "";
        output += "\n";
        output += "-----------------------------\n";
        output += "Date: " + date + "\n";
        output += "Transfer From Account: " + t1.getAccount().getAccountNumber() + "\n";
        output += "Transfer To Account: " + t2.getAccount().getAccountNumber() + "\n";
        output += "Amount: " + amount + "\n";
        output += "-----------------------------\n";
        return output;
    }

    // method to get transaction date
    public String getDate() {
        return date;
    }

    // method to get the transaction amount
    public Double getAmount() {
        return amount;
    }

    // method to get the customer of the transaction
    public Customer getCustomer() {
        return customer;
    }

    // method to get the account involved in the transaction
    public Account getAccount() {
        return account;
    }
}
