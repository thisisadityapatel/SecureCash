import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ATMSystemMain implements ActionListener {

    public static JFrame WIN;
    private JPanel panel;
    private JPasswordField password;
    private JLabel status, title;
    private JButton login;
    private JTextField card;
    private Database db;
    public static ArrayList<Customer> custList = new ArrayList<Customer>();

    public ATMSystemMain (Database db) {
        this.db = db;
        WIN = new JFrame();
        WIN.setSize(400, 300);
        WIN.setResizable(false);
        WIN.setLocationRelativeTo(null);
        WIN.setTitle("SecureCash");
        WIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel enterCard = new JLabel("Enter Card Number:", SwingConstants.CENTER);
        enterCard.setBounds(0, 60, 400, 20);

        card = new JTextField();
        card.setBounds(0, 80, 400, 20);

        JLabel enterPass = new JLabel("Enter Password:", SwingConstants.CENTER);
        enterPass.setBounds(0, 110, 400, 20);

        password = new JPasswordField(20);
        password.setBounds(0, 130, 400, 20);
        
        login = new JButton("Login");
        login.setBounds(142, 200, 100, 20);
        login.addActionListener(this);

        status = new JLabel("");
        status.setFont(new Font("Arial", Font.BOLD, 20));
        status.setBounds(100, 140, 200, 20);
        status.setForeground(Color.red);

        title = new JLabel("Secure Cash Login", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setBounds(0, 0,400, 50);
        
        panel = new JPanel();
        panel.setLayout(null);
        panel.add(enterPass);
        panel.add(password);
        panel.add(login);
        panel.add(status);
        panel.add(title);
        panel.add(enterCard);
        panel.add(card);

        WIN.add(panel);
        WIN.setVisible(true);
    }

    public static void main(String[] args) {
        Database db = new Database();
        ATMSystemMain ATMSystem = new ATMSystemMain(db);
        SavingsAccount acc = new SavingsAccount(0, 0, 100000.0, null, 0);
        Customer cust = new Customer(0, acc, null, null);
        cust.setCard(new Card("1234123412341234", "Aditya", null, 1234));
        custList.add(cust);
    }

    public void options(String type) {

        ATM atm = new ATM(db);

        CustomerInterface custinterface = new CustomerInterface(atm);
        // instance of EmployeeInterface (instance of ATM) -empinterface
        EmployeeInterface empinterface = new EmployeeInterface(atm);
        // instance of ATM
        // instance of CustomerInterface (instance of ATM) -custinterface
        // instance of EmployeeInterface (instance of ATM) -empinterface

        // getting all the data friom the database

        // create all the instances and store them

        //
        // user login - methods:
        System.out.println("here");
        Scanner sc = new Scanner(System.in);
        System.out.println(type + "test");
        WIN.setVisible(false);
        if (type == "customer") {
            while (true) {
                System.out.println("Enter your choice:");
                System.out.println("1: Withdraw");
                System.out.println("2: Deposit");
                System.out.println("3: Transfer");
                System.out.println("4: Check Balance");
                System.out.println("5: Change PIN");
                System.out.println("6: Print Statements");
                int choice = sc.nextInt();
                double amount = 0.0;
                String accType = null;
                String receiver = null;
                String receiverAccType = null;
                int newPin = 0;
                switch (choice) {
                    case 1:
                        // input amount
                        System.out.println("Enter amount: ");
                        amount = sc.nextDouble();
                        // input account type
                        System.out.println("Enter account type (savings/checking): ");
                        accType = sc.next();
                        custinterface.withdraw(custList.get(0), amount, accType);
                        break;
                    case 2:
                        // input amount
                        System.out.println("Enter amount: ");
                        amount = sc.nextDouble();
                        // input account type
                        System.out.println("Enter account type (savings/checking): ");
                        accType = sc.next();
                        custinterface.deposit(custList.get(0), amount, accType);
                        break;
                    case 3:
                        // input amount
                        System.out.println("Enter amount: ");
                        amount = sc.nextDouble();
                        // input account type
                        System.out.println("Enter account type (savings/checking): ");
                        accType = sc.next();
                        System.out.println("Enter receiver account number: ");
                        receiver = sc.next();
                        System.out.println("Enter receiver account type");
                        receiverAccType = sc.next();
                        for (Customer c : custList) {
                            if (c.getCard().getCardNumber() == receiver) {
                                custinterface.transfer(custList.get(0), c, amount, accType, receiverAccType);
                            }
                        }
                        break;
                    case 4:
                        // input account type
                        System.out.println("Enter account type (savings/checking): ");
                        accType = sc.next();
                        System.out.println(custinterface.checkBalance(custList.get(0), accType));
                        break;
                    case 5:
                        // input account type
                        System.out.println("Enter newPin: ");
                        newPin = sc.nextInt();
                        custinterface.changePin(custList.get(0), newPin);
                        break;
                    case 6:
                        System.out.println("Not implemented yet :(");
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        if (click.getSource() == login) {
            boolean verify = false;
            String type = null;
            String num = null;
            int pin = 0;
            while (verify!=true) {
                // take inputs 
                //System.out.println("Enter card number: ");
                num = card.getText();
                //System.out.println("Enter password: ");          
                pin = Integer.parseInt(new String(password.getPassword()));
                // verification process
                if (num!=null & pin != 0){
                    Card card = new Card(num, null, null, pin);
                    if (db.verifyUser(card, pin)){
                        System.out.println("verified");
                        verify = true;
                        if (num.length()==16){
                            // customer
                            type = "customer";
                            options(type);
                        }else{
                            // employee
                            type = "employee";
                            options(type);
                        }
                    } else {
                        System.out.println("Verification Error");
                    }         
                } else{
                    System.out.println("Input error.");
                }
            }
        }
    } 
}
