import java.io.File;
import java.io.IOException;
import java.util.*;

public class Database {
    private int databaseID;

    // access databaseID
    public int getDatabaseID(int accessCode) {
        return databaseID;
    }

    // verify user
    public boolean verifyUser(Card card, int verifyPin) {
        try {
            File file = new File("bankDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                String cardNumber = in.nextLine().trim();
                int pin = Integer.parseInt(in.nextLine());

                // verification
                if (pin == verifyPin && card.getCardNumber().equals(cardNumber)) {
                    in.close(); // terminating file stream
                    return true;
                }
                for (int i = 0; i < 8; i++) {
                    in.nextLine();
                }
            }
            in.close(); // closing the stream
        } catch (IOException io) {
            System.out.println(io.getMessage()); // printing error, if any
        }
        return false;
    }

    // retrieve account details
    public List<Integer> retrieve(Customer customer) {
        List<String> tempAcc = new ArrayList<String>();
        try {
            File file = new File("bankDatabase.txt");
            Scanner in = new Scanner(file);

            while (in.hasNextLine()) {
                for (int i = 0; i < 2; i++) {
                    in.nextLine();
                }
                int customerId = Integer.parseInt(in.nextLine());

                // id verification
                if (customer.getCustomerID() == customerId) {
                    tempAcc = Arrays.asList(in.nextLine().split(","));
                    List<Integer> userAccounts = new ArrayList<Integer>();
                    for (int i = 0; i < tempAcc.size(); i++) {
                        userAccounts.add(Integer.parseInt(tempAcc.get(i)));
                    }
                    in.close();
                    return userAccounts;
                }
                for (int i = 0; i < 7; i++) {
                    in.nextLine();
                }
            }
            in.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
        return new ArrayList<Integer>();
    }

    public static void main(String[] args) {
        System.out.println("Hey there!!");
        Database testing = new Database();

        Card temp = new Card("1234123412341234", "Aditya", "");
        System.out.println(testing.verifyUser(temp, 1234));

        Customer tempCust = new Customer(1235);
        System.out.println(testing.retrieve(tempCust));
    }
}