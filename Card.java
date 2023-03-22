public class Card {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private int pin;

    public Card(String cardNumber, String cardHolderName, String expiryDate, int pin) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.pin = pin;
    }

    public int getPin() {
        return this.pin;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }
    
    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }
}
