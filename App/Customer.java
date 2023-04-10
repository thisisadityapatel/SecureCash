package App;
public class Customer {
    private int customerID;
    private SavingsAccount savingsAccount;
    private ChequeingsAccount chequeingsAccount;
    private Card card;

    public Customer(int customerID, SavingsAccount savingsAccount, ChequeingsAccount chequeingsAccount) {
        this.customerID = customerID;
        this.savingsAccount = savingsAccount;
        this.chequeingsAccount = chequeingsAccount;
    }

    public Card getCard() {
        return card;
    }

    public int getCustomerID() {
        return customerID;
    }

    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }

    public ChequeingsAccount getChequeingsAccount() {
        return chequeingsAccount;
    }

    public void setSavingsAccount(SavingsAccount s){
        this.savingsAccount = s;
    }
    
    public void setChequingAccount(ChequeingsAccount ch){
        this.chequeingsAccount = ch;
    }

    public boolean login(Card card, int pin) {
        if (card.getPin() == pin) {
            return true;
        } else {
            return false;
        }
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
