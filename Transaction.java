public class Transaction {
    private String date;
    private Double amount;
    private Customer customer;
    private Account account;

    public Transaction(String date, Double amount, Customer customer, Account account) {
        this.date = date;
        this.amount = amount;
        this.customer = customer;
        this.account = account;
    }

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

    public String getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Account getAccount() {
        return account;
    }
}
