import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.*;

public class Database {
    private int databaseID;

    // access databaseID
    public int getDatabaseID(int accessCode) {
        return databaseID;
    }

    // verify user from database
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

    // retrieve account details from database
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

    // updating the user account amount
    public Boolean updateAccountAmount(Account account, double newAmount) {
        String oldfilename = "bankDatabase.txt";
        String tempname = "tempDatabase.txt";

        File oldFile = new File(oldfilename);
        File newFile = new File(tempname);

        Scanner s = null;

        try {
            FileWriter fw = new FileWriter(tempname, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            s = new Scanner(new File(oldfilename));
            while (s.hasNextLine()) {
                String cardNumber = s.nextLine();
                pw.println(cardNumber);
                String PIN = s.nextLine();
                pw.println(PIN);
                String custID = s.nextLine();
                pw.println(custID);

                int flag = 0;
                String allAccounts = s.nextLine();
                List<String> tempAcc = Arrays.asList(allAccounts.split(","));
                for (int i = 0; i < tempAcc.size(); i++) {
                    if (Integer.parseInt(tempAcc.get(i)) == account.getAccountNumber()) {
                        flag = 1;
                        break;
                    }
                }
                pw.println(allAccounts);

                String oldAccountAmount = s.nextLine();
                if (flag == 1) {
                    pw.println(newAmount);
                } else {
                    pw.println(oldAccountAmount);
                }
                for (int i = 0; i < 5; i++) {
                    pw.println(s.nextLine());
                }
            }
            s.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(oldfilename);
            newFile.renameTo(dump);
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        return true;
    }

    // updating the user PIN

    // appending account transaction to database

    // getting all account transactions

    public static void main(String[] args) {
        System.out.println("Hey there!!");
        Database testing = new Database();

        Card temp = new Card("1234123412341234", "Aditya", "");
        System.out.println(testing.verifyUser(temp, 1234));

        Customer tempCust = new Customer(12346);
        System.out.println(testing.retrieve(tempCust));

        System.out.println(testing.updateAccountAmount(new Account(1234567, 0, 0.0, null), 20.2));
    }
}
