import java.util.*;

public class retrieveDatabaseData {
    private String cardNumber;
    private int pin;
    private int customerID;
    private ArrayList<Integer> accounts;
    private ArrayList<Double> amounts;
    private ArrayList<String> accountType;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String cardholderName;

    // construction method
    public retrieveDatabaseData(String cardNumber, int pin, ArrayList<Integer> accounts, ArrayList<Double> amounts,
            ArrayList<String> accountType, String firstName, String lastName, String dateOfBirthday,
            String cardholderName) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.accounts = accounts;
        this.amounts = amounts;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirthday;
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public int getCustomerID() {
        return customerID;
    }

    public ArrayList<Integer> getAccounts() {
        return accounts;
    }

    public ArrayList<Double> getAmounts() {
        return amounts;
    }

    public ArrayList<String> getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirthay() {
        return dateOfBirth;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    // method to check the account type
    public String determineAccountType(int accountNumber) {
        if (accountNumber / 100000 == 72) {
            return "saving";
        } else {
            return "checking";
        }
    }
}
