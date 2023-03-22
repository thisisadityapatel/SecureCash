public class Card {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private int pin;

    public Card(String cardNumber, String cardHolderName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return this.pin;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }
}