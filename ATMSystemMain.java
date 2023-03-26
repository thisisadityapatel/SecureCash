import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;

public class ATMSystemMain implements ActionListener {

    public static JFrame WIN;
    private JPanel panel;
    private JPasswordField password;
    private JLabel status, title;
    private JButton login, opt1, opt2, opt3, opt4, opt5, opt6, opt7, doAction;
    private JTextField card, amountInput, accTypeInput, recvInput, pinInput;
    private Database db;
    public static ArrayList<Customer> custList = new ArrayList<Customer>();
    public static ArrayList<Employee> empList = new ArrayList<Employee>();
    public static ArrayList<retrieveDatabaseData> dbContents = new ArrayList<retrieveDatabaseData>();
    public static Customer currCust = new Customer(0, null, null);     
    public static Employee currEmp = new Employee();
    public static String type = null;
    public static ATM atm;
    public static CustomerInterface custinterface;
    public static EmployeeInterface empinterface;
    public static int currChoice = 0;
    private JCheckBox savingsChckBx, checkingChckBx;
    public static DecimalFormat df = new DecimalFormat("$0.00");

    public ATMSystemMain (Database db) {
        this.db = db;
        atm = new ATM(db);
        custinterface = new CustomerInterface(atm);
        empinterface = new EmployeeInterface(atm);
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
        dbContents = db.extractDatabase();
        for (retrieveDatabaseData i : dbContents) {
            if (i.getCardNumber().length() < 16) {
                Employee e = new Employee(i.getFirstName(), i.getLastName(), i.getDateOfBirthay(), i.getCustomerID(), i.getPin());
                empList.add(e);
            } else {
                Customer c = new Customer(i.getCustomerID(), null, null);
                for (int idx = 0; idx < i.getAccountType().size(); idx++) {
                    System.out.println(i.getAccountType().get(idx));
                    if (i.getAccountType().get(idx).equals("saving")) {
                        SavingsAccount s = new SavingsAccount(i.getAccounts().get(idx), i.getCustomerID(),
                                i.getAmounts().get(idx), null, 0);
                        // System.out.println(s);
                        c.setSavingsAccount(s);
                    }
                    if (i.getAccountType().get(idx).equals("checking")) {
                        ChequeingsAccount che = new ChequeingsAccount(i.getAccounts().get(idx), i.getCustomerID(),
                                i.getAmounts().get(idx), null);
                        System.out.println(che);
                        c.setChequingAccount(che);
                    }
                }
                Card atmCard = new Card(i.getCardNumber(), i.getFirstName(), null, i.getPin());
                c.setCard(atmCard);
                custList.add(c);
            }
        }
    }

    public void doActions(int choice, String type) {
        if (type == "customer") {

            if (choice == 1) {
                Double amount = Double.parseDouble(amountInput.getText());
                if (amount <= 0){
                    System.out.println("Invalid amount.");
                    options(type);
                }
                else {
                    String accType = accTypeInput.getText();
                    String receipt = custinterface.withdraw(currCust, amount, accType);
                    JLabel rcpt = new JLabel(receipt);
                    rcpt.setBounds(300, 500, 200, 200);
                    panel.add(rcpt);
                    panel.revalidate();
                    panel.repaint();
                }
                /*String regex = "^-?\\d+(?:\\.\\d{2})?$";
                if (!(Double.toString(amount).matches(regex))){
                    System.out.println("Invalid amount.");
                    options(type);
                    System.out.println("why is it here 2");
                }*/
                // input account type
            } else if (choice == 2) {
                // input amount
                Double amount = Double.parseDouble(amountInput.getText());
                if (amount <= 0){
                    System.out.println("Invalid amount.");
                } else {
                    String accType = accTypeInput.getText();
                    String receipt = custinterface.deposit(currCust, amount, accType);
                    JLabel rcpt = new JLabel(receipt);
                    rcpt.setBounds(300, 500, 200, 200);
                    panel.add(rcpt);
                    panel.revalidate();
                    panel.repaint();
                }
                /*if (!(Double.toString(amount).matches(regex))){
                    System.out.println("Invalid amount.");
                    break;
                }*/
                // input account type
            } else if (choice == 3) {
                Double amount = Double.parseDouble(amountInput.getText());
                // input account type
                String accType = accTypeInput.getText();
                String receiver = recvInput.getText();
                String receiverAccType = null;
                if ((int) (Integer.parseInt(receiver) / 100000) == 72) {
                    receiverAccType = "savings";
                } else {
                    receiverAccType = "checking";
                }
                // System.out.println("Enter receiver account type");
                // receiverAccType = sc.next();
                if (receiverAccType.equals("checking")) {
                    for (Customer c : custList) {
                        if (c.getChequeingsAccount().getAccountNumber() == Integer.parseInt(receiver)) {
                            System.out.println(custinterface.transfer(currCust, c, amount, accType, receiverAccType));
                        }
                    }
                }
                if (receiverAccType.equals("savings")) {
                    for (Customer c : custList) {
                        if (c.getSavingsAccount().getAccountNumber() == Integer.parseInt(receiver)) {
                            String receipt = custinterface.transfer(currCust, c, amount, accType, receiverAccType);
                            JLabel rcpt = new JLabel(receipt);
                            rcpt.setBounds(300, 500, 200, 200);
                            panel.add(rcpt);
                            panel.revalidate();
                            panel.repaint();
                        }
                    }
                }
            } else if (choice == 4) {
                // input account type
                String accType = null;
                if (checkingChckBx.isSelected() && savingsChckBx.isSelected()) {
                    System.out.println("cant select both");
                    checkingChckBx.setSelected(false);
                    savingsChckBx.setSelected(false);
                } else if (checkingChckBx.isSelected()) {
                    accType = "checking";
                } else if (savingsChckBx.isSelected()) {
                    accType = "savings";
                }
                if (accType != null) {
                    System.out.println(df.format(custinterface.checkBalance(currCust, accType)));
                }
                // System.out.println(currCust.getSavingsAccount().getAccountNumber());
                // System.out.println(currCust.getCustomerID());
            } else if (choice == 5) {
                // input account type
                int newPin = Integer.parseInt(pinInput.getText());
                if (custinterface.changePin(currCust, newPin) == true) {
                    System.out.println("Successfully Changed Pin");
                } else {
                    System.out.println("Error Changing Pin");
                }
            } else if (choice == 6) {
                String accType = null;
                if (checkingChckBx.isSelected() && savingsChckBx.isSelected()) {
                    System.out.println("cant select both");
                    checkingChckBx.setSelected(false);
                    savingsChckBx.setSelected(false);
                } else if (checkingChckBx.isSelected()) {
                    accType = "checking";
                } else if (savingsChckBx.isSelected()) {
                    accType = "savings";
                }

                if (accType != null) {
                    String res = custinterface.printStatement(currCust, accType);
                    if (res.equals("")) {
                        System.out.println("No Transactions");
                    } else {
                        JEditorPane display = new JEditorPane("text/html", res);
                        display.setEditable(false); // set textArea non-editable
                        JScrollPane scroll = new JScrollPane(display);
                        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scroll.setBounds(200, 500, 600, 400);
                        scroll.setBackground(new Color(255, 255, 0));
                        panel.add(scroll);
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            }

        } else if (type == "employee") {

            if (choice == 1) {
                double amount = Double.parseDouble(amountInput.getText());
                double receipt = empinterface.refillATM(amount);
                JLabel rcpt = new JLabel(Double.toString(receipt));
                rcpt.setBounds(300, 500, 200, 200);
                panel.add(rcpt);
                panel.revalidate();
                panel.repaint();
            } else if (choice == 2) {
                String receipt = df.format(empinterface.getATMBalance());
                JLabel rcpt = new JLabel(receipt);
                rcpt.setBounds(300, 500, 200, 200);
                panel.add(rcpt);
                panel.revalidate();
                panel.repaint();
            } else if (choice == 3) {
                double amount = Double.parseDouble(amountInput.getText());
                double amountATM = empinterface.withdrawATM(amount);
                if (amountATM == -1.0){
                    System.out.println("Error Withdrawing amount: Please enter valid amount.");
                } else{
                    String receipt = df.format(amountATM);
                    JLabel rcpt = new JLabel(receipt);
                    rcpt.setBounds(300, 500, 200, 200);
                    panel.add(rcpt);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        }
    }

    public void handleOption(int choice, String type) {
        currChoice = choice;
        System.out.println("choice: " + choice);
        WIN.setSize(1000, 1000);
        WIN.setLocationRelativeTo(null);
        System.out.println(type);
        if (type == "customer") {

            if (choice == 1) {
                panel.removeAll();
                System.out.println("cust c1");

                amountInput = new JTextField(20);
                amountInput.setBounds(450, 100, 100, 20);
                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(450, 200, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 2) {
                panel.removeAll();
                System.out.println("cust c1");
                
                amountInput = new JTextField(20);
                amountInput.setBounds(450, 100, 100, 20);
                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(450, 200, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 3) {
                panel.removeAll();
                System.out.println("cust c1");
                
                amountInput = new JTextField(20);
                amountInput.setBounds(450, 100, 100, 20);
                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(450, 200, 100, 20);
                recvInput = new JTextField(20);
                recvInput.setBounds(450, 300, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(recvInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 4) {
                panel.removeAll();
                System.out.println("cust c1");

                checkingChckBx = new JCheckBox("Checking");
                checkingChckBx.setBounds(450, 100, 100, 20);
                savingsChckBx = new JCheckBox("Savings");
                savingsChckBx.setBounds(450, 200, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(checkingChckBx);
                panel.add(savingsChckBx);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 5) {
                panel.removeAll();
                System.out.println("cust c1");
                
                pinInput = new JTextField(20);
                pinInput.setBounds(450, 100, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(pinInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 6) {
                panel.removeAll();
                System.out.println("cust c1");

                checkingChckBx = new JCheckBox("Checking");
                checkingChckBx.setBounds(450, 100, 100, 20);
                savingsChckBx = new JCheckBox("Savings");
                savingsChckBx.setBounds(450, 200, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(checkingChckBx);
                panel.add(savingsChckBx);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 7) {
                System.exit(0);
            }

        } else if (type == "employee") {

            if (choice == 1) {
                panel.removeAll();
                System.out.println("emp c1");

                amountInput = new JTextField(20);
                amountInput.setBounds(450, 100, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(amountInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 2) {
                doActions(choice, type);
            } else if (choice == 3) {
                panel.removeAll();
                System.out.println("emp c1");

                amountInput = new JTextField(20);
                amountInput.setBounds(450, 100, 100, 20);

                doAction = new JButton("Enter");
                doAction.setBounds(450, 400, 100, 20);
                doAction.addActionListener(this);

                panel.add(amountInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 4) {
                System.exit(0);
            }

        }
        /*if (type == "customer") {
            double amount = 0.0;
            String accType = null;
            String receiver = null;
            String receiverAccType = null;
            int newPin = 0;
            switch (choice) {
                case 1:
                    // input amount
                    System.out.println("Enter amount: ");
                    amount = sc.nextDouble(); //!!!!!!!!!!!!!!
                    // input account type
                    System.out.println("Enter account type (savings/checking): ");
                    accType = sc.next();  //!!!!!!!!!!!!!!
                    System.out.println(custinterface.withdraw(custList.get(0), amount, accType));
                    break;
                case 2:
                    // input amount
                    System.out.println("Enter amount: ");
                    try {
                        amount = sc.nextDouble();  //!!!!!!!!!!!!!!
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        sc.next();  //!!!!!!!!!!!!!!
                    }
                    // input account type
                    System.out.println("Enter account type (savings/checking): ");
                    accType = sc.next();  //!!!!!!!!!!!!!!
                    System.out.println(custinterface.deposit(custList.get(0), amount, accType));
                    break;
                case 3:
                    // input amount
                    System.out.println("Enter amount: ");
                    amount = sc.nextDouble();
                    // input account type
                    System.out.println("Enter account type (savings/checking): ");
                    accType = sc.next();  //!!!!!!!!!!!!!!
                    System.out.println("Enter receiver account number: ");
                    receiver = sc.next();  //!!!!!!!!!!!!!!
                    System.out.println("Enter receiver account type");
                    receiverAccType = sc.next();  //!!!!!!!!!!!!!!
                    for (Customer c : custList) {
                        if (c.getCard().getCardNumber() == receiver) {
                            System.out.println(custinterface.transfer(custList.get(0), c, amount, accType, receiverAccType));
                        }
                    }
                    break;
                case 4:
                    // input account type
                    System.out.println("Enter account type (savings/checking): ");
                    accType = sc.next();  //!!!!!!!!!!!!!!
                    System.out.println(df.format(custinterface.checkBalance(custList.get(0), accType)));
                    break;
                case 5:
                    // input account type
                    System.out.println("Enter newPin: ");
                    newPin = sc.nextInt();  //!!!!!!!!!!!!!!
                    if (custinterface.changePin(custList.get(0), newPin) == true){
                        System.out.println("Successfully Changed Pin");
                    }else{
                        System.out.println("Error Changing Pin");
                    }
                    break;
                case 6:
                    System.out.println("Not implemented yet :(");
                    break;
                case 7:
                    System.out.println("Not implemented yet :(");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        else if (type == "employee") {
            while (true) {
                System.out.println("Enter your choice:");
                System.out.println("1: Refill ATM Balance");
                System.out.println("2: Get ATM Balance");
                System.out.println("3: Logout");
                double amount = 0.0;
                switch (choice) {
                    case 1:
                        // input amount
                        System.out.println("Enter amount: ");
                        amount = sc.nextDouble();  //!!!!!!!!!!!!!!
                        System.out.println(empinterface.refillATM(amount));
                        break;
                    case 2:
                        // input amount
                        System.out.println("Balance is: ");
                        System.out.println(df.format(empinterface.getATMBalance()));
                        break;
                    case 3:
                        System.out.println("Not implemented yet :(");
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }*/
    }

    public void options(String type) {
        // instance of ATM
        // instance of CustomerInterface (instance of ATM) -custinterface
        // instance of EmployeeInterface (instance of ATM) -empinterface

        // getting all the data friom the database

        // create all the instances and store them

        //
        // user login - methods:
        System.out.println("here");
        
        if (type == "customer") {
            System.out.println(type + "test");
            panel.removeAll();
            opt1 = new JButton("Withdraw");
            opt1.setBounds(0, 0, 400, 20);
            opt1.addActionListener(this);

            opt2 = new JButton("Deposit");
            opt2.setBounds(0, 35, 400, 20);
            opt2.addActionListener(this);

            opt3 = new JButton("Transfer");
            opt3.setBounds(0, 70, 400, 20);
            opt3.addActionListener(this);

            opt4 = new JButton("Check Balance");
            opt4.setBounds(0, 105, 400, 20);
            opt4.addActionListener(this);

            opt5 = new JButton("Change Pin");
            opt5.setBounds(0, 140, 400, 20);
            opt5.addActionListener(this);

            opt6 = new JButton("Print Statements");
            opt6.setBounds(0, 175, 400, 20);
            opt6.addActionListener(this);

            opt7 = new JButton("Logout");
            opt7.setBounds(0, 210, 400, 20);
            opt7.addActionListener(this);        

            panel.add(opt1);
            panel.add(opt2);
            panel.add(opt3);
            panel.add(opt4);
            panel.add(opt5);
            panel.add(opt6);
            panel.add(opt7);

            panel.revalidate();
            panel.repaint();
        } else if (type == "employee") {
            panel.removeAll();
            opt1 = new JButton("Refill");
            opt1.setBounds(0, 50, 400, 20);
            opt1.addActionListener(this);

            opt2 = new JButton("Withdraw");
            opt2.setBounds(0, 150, 400, 20);
            opt2.addActionListener(this);

            opt3 = new JButton("Check ATM Balance");
            opt3.setBounds(0, 250, 400, 20);
            opt3.addActionListener(this);

            panel.add(opt1);
            panel.add(opt2);
            panel.add(opt3);

            panel.revalidate();
            panel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        if (click.getSource() == login) {
            // boolean verify = false;
            String num = null;
            int pin = 0;
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
                    // verify = true;
                    if (num.length()==16){
                        for (Customer c : custList) {
                            if (c.getCard().getCardNumber().equals(num)) {
                                currCust = c;
                            }
                        }
                        // customer
                        type = "customer";
                        options("customer");
                        System.out.println("options worked");
                    } else {
                        for (Employee e : empList) {
                            if (Integer.toString(e.getEmployeeID()).equals(num)) {
                                currEmp = e;
                            }
                        }
                        // employee
                        type = "employee";
                        options("employee");
                    }
                } else {
                    System.out.println("Verification Error");
                }         
            } else{
                System.out.println("Input error.");
            }
        } else if (click.getSource() == opt1) {
            handleOption(1, type);
        } else if (click.getSource() == opt2) {
            handleOption(2, type);
        } else if (click.getSource() == opt3) {
            handleOption(3, type);
        } else if (click.getSource() == opt4) {
            handleOption(4, type);
        } else if (click.getSource() == opt5) {
            handleOption(5, type);
        } else if (click.getSource() == opt6) {
            handleOption(6, type);
        } else if (click.getSource() == doAction) {
            System.out.println("doing actions");
            doActions(currChoice, type);
        }
    } 
}
