public class Transaction {
    private String date;
    private Double amount;
    private Customer customer;
    private Account account;

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
