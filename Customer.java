public class Customer {
    private int customerID;

    public Customer(int customerID) {
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public boolean login(Card card, int pin) {
        if (card.getPin() == pin) {
            return true;
        } else {
            return false;
        }
    }
}