package App;
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
        String output = "<html>";
        output += "<br>";
        output += "-----------------------------<br>";
        output += "Date: " + t.getDate() + "<br>";
        output += "Account: " + t.getAccount().getAccountNumber() + "<br>";
        output += "Amount: " + amount + "<br>";
        output += "-----------------------------<br>";
        output += "<html>";
        return output;
    }

    public String getReceipt(Transaction t1, Transaction t2) {
        String output = "<html>";
        output += "<br>";
        output += "-----------------------------<br>";
        output += "Date: " + date + "<br>";
        output += "Transfer From Account: " + t1.getAccount().getAccountNumber() + "<br>";
        output += "Transfer To Account: " + t2.getAccount().getAccountNumber() + "<br>";
        output += "Amount: " + amount + "<br>";
        output += "-----------------------------<br>";
        output += "<html>";
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