package App;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ATMSystemMain implements ActionListener {

    public static JFrame WIN;
    private JPanel panel;
    private JPasswordField password;
    private JLabel status, title, rcpt, display;
    private JLabel error = new JLabel("", SwingConstants.CENTER);
    private JButton login, opt1, opt2, opt3, opt4, opt5, opt6, opt7, doAction, back;
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

    public ATMSystemMain(Database db) {
        this.db = db;
        atm = new ATM(db);
        custinterface = new CustomerInterface(atm);
        empinterface = new EmployeeInterface(atm);
        WIN = new JFrame();
        WIN.setSize(1000, 1000);
        WIN.setResizable(false);
        WIN.setLocationRelativeTo(null);
        WIN.setTitle("SecureCash");
        WIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(65,105,225));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBounds(0,0,500,1000);

        ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/SecureCashLogo.png");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imgLabel = new JLabel(scaledIcon);
        imgLabel.setBounds(100,350,200,200);

        JLabel enterCard = new JLabel("Enter Card Number:");
        enterCard.setBounds(530, 400, 500, 50);
        enterCard.setFont(new Font("Georgia", Font.PLAIN, 20));

        card = new JTextField();
        card.setBounds(530, 450,  410, 50);
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        card.setOpaque(true);
        card.setFont(new Font("Georgia", Font.PLAIN, 20));

        JLabel enterPass = new JLabel("Enter Password:");
        enterPass.setBounds(530, 500, 500, 50);
        enterPass.setFont(new Font("Georgia", Font.PLAIN, 20));

        password = new JPasswordField(20);
        password.setBounds(530, 550, 410, 50);
        password.setBackground(new Color(240, 240, 240));
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setOpaque(true);
        password.setFont(new Font("Georgia", Font.PLAIN, 20));

        ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/login.png");
        Image image1 = icon1.getImage();
        Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

        login = new JButton(scaledIcon1);
        login.setBounds(650, 650, 200, 75);
        login.addActionListener(this);
        login.setFont(new Font("Georgia", Font.PLAIN, 25));
        login.setBackground(new Color(42, 189, 189));
        login.setContentAreaFilled(false);
        login.setBorderPainted(false);
        login.setFocusPainted(false);

        status = new JLabel("", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 20));
        status.setBounds(650, 750, 200, 50);
        status.setForeground(Color.red);

        title = new JLabel("<html><center>Welcome to SecureCash<center><html>", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 50));
        title.setBounds(0, 150, 500, 200);
        title.setForeground(new Color(0, 70, 0));

        JLabel loginLabel = new JLabel("Enter Credentials", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Verdana", Font.BOLD, 35));
        loginLabel.setBounds(500, 200, 500, 200);

        panel = new JPanel();
        panel.add(leftPanel);
        panel.add(loginLabel);
        panel.setBackground(new Color(42, 189, 189));
        panel.setLayout(null);
        panel.add(enterPass);
        panel.add(password);
        panel.add(login);
        panel.add(status);
        leftPanel.add(title);
        panel.add(enterCard);
        panel.add(card);
        leftPanel.add(imgLabel);

        WIN.add(panel);
        WIN.setVisible(true);
    }

    public static void main(String[] args) {
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!(type == "employee" || type == "customer")) {
                    System.out.println("Timeout Error");
                    System.exit(0);
                }
            }
        }, 30000);

        Database db = new Database();
        ATMSystemMain ATMSystem = new ATMSystemMain(db);
        dbContents = db.extractDatabase();
        for (retrieveDatabaseData i : dbContents) {
            if (i.getCardNumber().length() < 16) {
                Employee e = new Employee(i.getFirstName(), i.getLastName(), i.getDateOfBirthay(), i.getCustomerID(),
                        i.getPin());
                empList.add(e);
            } else {
                Customer c = new Customer(i.getCustomerID(), null, null);
                for (int idx = 0; idx < i.getAccountType().size(); idx++) {
                    if (i.getAccountType().get(idx).equals("saving")) {
                        SavingsAccount s = new SavingsAccount(i.getAccounts().get(idx), i.getCustomerID(),
                                i.getAmounts().get(idx), null, 0);
                        c.setSavingsAccount(s);
                    }
                    if (i.getAccountType().get(idx).equals("checking")) {
                        ChequeingsAccount che = new ChequeingsAccount(i.getAccounts().get(idx), i.getCustomerID(),
                                i.getAmounts().get(idx), null);
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
                Double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountInput.getText());
                } catch (Exception e) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                }
                if (amount <= 0) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                } else {
                    String accType = accTypeInput.getText();
                    String receipt = custinterface.withdraw(currCust, amount, accType);

                    if (receipt.equals("")) {
                        error.setText("ERROR: invalid inputs.");
                        options(type);
                    } else {
                        if (rcpt != null) {
                            rcpt.setText("");
                        }

                        rcpt = new JLabel(receipt);
                        rcpt.setBounds(300, 10, 400, 200);
                        rcpt.setFont(new Font("Georgia", Font.PLAIN, 25));

                        panel.add(rcpt);
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            } else if (choice == 2) {
                Double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountInput.getText());

                } catch (Exception e) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                }
                if (amount <= 0) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                } else {
                    String accType = accTypeInput.getText();
                    String receipt = custinterface.deposit(currCust, amount, accType);

                    if (receipt.equals("")) {
                        error.setText("ERROR: invalid inputs.");
                        options(type);
                    } else {
                        if (rcpt != null) {
                            rcpt.setText("");
                        }
                        rcpt = new JLabel(receipt);
                        rcpt.setBounds(300, 10, 400, 200);
                        rcpt.setFont(new Font("Georgia", Font.PLAIN, 25));

                        panel.add(rcpt);
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            } else if (choice == 3) {
                Double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountInput.getText());

                } catch (Exception e) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                }
                if (amount <= 0.0) {
                    error.setText("ERROR: invalid amount input.");
                    options(type);
                } else {
                    String accType = accTypeInput.getText();
                    String receiver = recvInput.getText();
                    String receiverAccType = null;
                    if (receiver.length() != 7) {
                        error.setText("ERROR: no such account found.");
                        options(type);
                    }
                    try {
                        if ((int) (Integer.parseInt(receiver) / 100000) == 72) {
                            receiverAccType = "savings";
                        } else {
                            receiverAccType = "checking";
                        }
                    } catch (Exception e) {
                        error.setText("ERROR: no such account found.");
                        options(type);
                    }
                    try {
                        
                        if (receiverAccType.equals("checking")) {
                            for (Customer c : custList) {
                                if (c.getChequeingsAccount().getAccountNumber() == Integer.parseInt(receiver)) {
                                    String receipt = custinterface.transfer(currCust, c, amount, accType, receiverAccType);
                                    if (rcpt != null) {
                                        rcpt.setText("");
                                    }
                                    rcpt = new JLabel(receipt);
                                    rcpt.setBounds(300, 10, 400, 200);
                                    rcpt.setFont(new Font("Georgia", Font.PLAIN, 25));
    
                                    panel.add(rcpt);
                                    panel.revalidate();
                                    panel.repaint();
                                }
                            }
                        }
                        if (receiverAccType.equals("savings")) {
                            for (Customer c : custList) {
                                if (c.getSavingsAccount().getAccountNumber() == Integer.parseInt(receiver)) {
                                    String receipt = custinterface.transfer(currCust, c, amount, accType, receiverAccType);
                                    if (rcpt != null) {
                                        rcpt.setText("");
                                    }
                                    rcpt = new JLabel(receipt);
                                    rcpt.setBounds(300, 10, 400, 200);
                                    rcpt.setFont(new Font("Georgia", Font.PLAIN, 25));
    
                                    panel.add(rcpt);
                                    panel.revalidate();
                                    panel.repaint();
                                }
                            }
                        }
                    } catch (Exception e) {
                        options(type);
                        error.setText("ERROR: transfer not possible, invalid inputs provided.");
                    }
                }
            } else if (choice == 4) {
                String accType = null;
                if (checkingChckBx.isSelected() && savingsChckBx.isSelected()) {
                    checkingChckBx.setSelected(false);
                    savingsChckBx.setSelected(false);
                } else if (checkingChckBx.isSelected()) {
                    accType = "checking";
                } else if (savingsChckBx.isSelected()) {
                    accType = "savings";
                }
                if (accType != null) {
                    if (rcpt != null) {
                        rcpt.setText("");
                    }
                    panel.revalidate();
                    panel.repaint();
                    String receipt = df.format(custinterface.checkBalance(currCust, accType));
                    rcpt = new JLabel(receipt, SwingConstants.CENTER);
                    rcpt.setBounds(300, 100, 400, 200);
                    rcpt.setFont(new Font("Georgia", Font.PLAIN, 50));

                    panel.add(rcpt);
                    panel.revalidate();
                    panel.repaint();
                }
            } else if (choice == 5) {
                int newPin = 0;
                try {
                    newPin = Integer.parseInt(pinInput.getText());
                } catch (Exception e) {
                    error.setText("ERROR: Invalid pin entry.");
                    options(type);
                }
                if ((currCust.getCard().getPin() == newPin) || (Integer.toString(newPin).length() != 4)) {
                    error.setText("ERROR: Invalid pin entry.");
                    options(type);
                } else {
                    if (custinterface.changePin(currCust, newPin) == true) {
                        String receipt = "Success!";
                        rcpt = new JLabel(receipt, SwingConstants.CENTER);
                        rcpt.setBounds(300, 100, 400, 200);
                        rcpt.setFont(new Font("Georgia", Font.PLAIN, 50));
                        panel.add(rcpt);
                        panel.revalidate();
                        panel.repaint();
                    } else {
                        error.setText("ERROR: pin change unsuccessful.");
                        options(type);
                    }
                }
            } else if (choice == 6) {
                String accType = null;
                if (checkingChckBx.isSelected() && savingsChckBx.isSelected()) {
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
                        panel.revalidate();
                        panel.repaint();
                        JLabel notran = new JLabel("No transactions to display.");
                        notran.setBounds(200, 500, 1000, 50);
                        notran.setFont(new Font("Georgia", Font.PLAIN, 25));

                        panel.add(notran);
                        panel.revalidate();
                        panel.repaint();
                    } else {
                        panel.revalidate();
                        panel.repaint();

                        display.setText(res);
                        
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            }

        } else if (type == "employee") {

            if (choice == 1) {
                double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountInput.getText());
                } catch (Exception e) {
                    error.setText("ERROR: please enter a valid amount.");
                    options(type);
                }
                if (amount <= 0) {
                    error.setText("ERROR: Amount cannot be less than or equal to 0.");
                    options(type);
                } else {
                    double receipt = empinterface.refillATM(amount);
                    if (rcpt != null) {
                        rcpt.setText("");
                    }
                    if (receipt == 0.0) {
                        rcpt = new JLabel("Success!", SwingConstants.CENTER);
                    } else {
                        rcpt = new JLabel("Overflow: " + Double.toString(receipt));
                    }
                    rcpt.setBounds(300, 10, 400, 200);
                    rcpt.setFont(new Font("Georgia", Font.PLAIN, 40));
    
                    panel.add(rcpt);
                    panel.revalidate();
                    panel.repaint();
                }
            } else if (choice == 2) {
                double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountInput.getText());
                } catch (Exception e) {
                    error.setText("ERROR: Amount is invalid.");
                    options(type);
                }
                if (amount <= 0) {
                    error.setText("ERROR: Amount cannot be less than or equal to 0.");
                    options(type);
                } else {

                    double amountATM = empinterface.withdrawATM(amount);
                    if (amountATM == -1.0) {
                        error.setText("ERROR: Please enter valid amount.");
                        options(type);
                    } else {
                        String receipt = df.format(amountATM);
                        if (rcpt != null) {
                            rcpt.setText("");
                        }
                        rcpt = new JLabel(receipt, SwingConstants.CENTER);
                        rcpt.setBounds(300, 10, 400, 200);
                        rcpt.setFont(new Font("Georgia", Font.PLAIN, 45));
    
                        panel.add(back);
                        panel.add(rcpt);
                        panel.revalidate();
                        panel.repaint();
                    }
                }

            } else if (choice == 3) {
                panel.removeAll();
                String receipt = df.format(empinterface.getATMBalance());
                JLabel atmBalance = new JLabel("ATM Balance:", SwingConstants.CENTER);
                atmBalance.setBounds(200, 200, 600, 300);
                atmBalance.setFont(new Font("Georgia", Font.PLAIN, 50));
                
                if (rcpt != null) {
                    rcpt.setText("");
                }
                rcpt = new JLabel(receipt, SwingConstants.CENTER);
                rcpt.setBounds(300, 400, 400, 200);
                rcpt.setFont(new Font("Georgia", Font.PLAIN, 35));

                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(atmBalance);
                panel.add(back);
                panel.add(rcpt);
                panel.revalidate();
                panel.repaint();
            }
        }
    }

    public void handleOption(int choice, String type) {
        currChoice = choice;
        WIN.setLocationRelativeTo(null);
        if (type == "customer") {
            if (choice == 1) {
                panel.removeAll();
                
                JLabel amountLabel = new JLabel("Enter Amount");
                amountLabel.setBounds(300, 250, 400, 50);
                amountLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
                
                amountInput = new JTextField(20);
                amountInput.setBounds(300, 300, 400, 50);
                amountInput.setFont(new Font("Georgia", Font.PLAIN, 20));
                
                JLabel accountTypeLabel = new JLabel("Enter Account Type (savings/checking)");
                accountTypeLabel.setBounds(300, 400, 400, 50);
                accountTypeLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
                
                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(300, 450, 400, 50);
                accTypeInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);

                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(accountTypeLabel);
                panel.add(amountLabel);
                panel.add(back);
                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 2) {
                panel.removeAll();

                JLabel amountLabel = new JLabel("Enter Amount");
                amountLabel.setBounds(300, 250, 400, 50);
                amountLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                amountInput = new JTextField(20);
                amountInput.setBounds(300, 300, 400, 50);
                amountInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                JLabel accountTypeLabel = new JLabel("Enter Account Type (savings/checking)");
                accountTypeLabel.setBounds(300, 400, 400, 50);
                accountTypeLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
                
                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(300, 450, 400, 50);
                accTypeInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(accountTypeLabel);
                panel.add(amountLabel);
                panel.add(back);
                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 3) {
                panel.removeAll();

                JLabel amountLabel = new JLabel("Enter Amount");
                amountLabel.setBounds(300, 200, 400, 50);
                amountLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                amountInput = new JTextField(20);
                amountInput.setBounds(300, 250, 400, 50);
                amountInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                JLabel accountTypeLabel = new JLabel("Enter Account Type (savings/checking)");
                accountTypeLabel.setBounds(300, 350, 400, 50);
                accountTypeLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                accTypeInput = new JTextField(20);
                accTypeInput.setBounds(300, 400, 400, 50);
                accTypeInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                JLabel recvLabel = new JLabel("Enter Receiving Account");
                recvLabel.setBounds(300, 500, 400, 50);
                recvLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                recvInput = new JTextField(20);
                recvInput.setBounds(300, 550, 400, 50);
                recvInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 700, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(recvLabel);
                panel.add(accountTypeLabel);
                panel.add(amountLabel);
                panel.add(back);
                panel.add(amountInput);
                panel.add(accTypeInput);
                panel.add(recvInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 4) {
                panel.removeAll();

                checkingChckBx = new JCheckBox("Checking");
                checkingChckBx.setBounds(200, 300, 200, 50);
                checkingChckBx.setFont(new Font("Georgia", Font.PLAIN, 40));
                checkingChckBx.setBackground(new Color(42, 189, 189));
                
                savingsChckBx = new JCheckBox("Savings");
                savingsChckBx.setBounds(600, 300, 200, 50);
                savingsChckBx.setFont(new Font("Georgia", Font.PLAIN, 40));
                savingsChckBx.setBackground(new Color(42, 189, 189));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(back);
                panel.add(checkingChckBx);
                panel.add(savingsChckBx);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 5) {
                panel.removeAll();

                JLabel pinLabel = new JLabel("Enter New Pin:");
                pinLabel.setBounds(400, 300, 200, 50);
                pinLabel.setFont(new Font("Georgia", Font.PLAIN, 20));
                
                pinInput = new JTextField(20);
                pinInput.setBounds(400, 350, 200, 50);
                pinInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(pinLabel);
                panel.add(back);
                panel.add(pinInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 6) {
                panel.removeAll();

                checkingChckBx = new JCheckBox("Checking");
                checkingChckBx.setBounds(200, 600, 200, 50);
                checkingChckBx.setFont(new Font("Georgia", Font.PLAIN, 40));
                checkingChckBx.setBackground(new Color(42, 189, 189));
                
                savingsChckBx = new JCheckBox("Savings");
                savingsChckBx.setBounds(600, 600, 200, 50);
                savingsChckBx.setFont(new Font("Georgia", Font.PLAIN, 40));
                savingsChckBx.setBackground(new Color(42, 189, 189));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 800, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                display = new JLabel("");
                display.setFont(new Font("Georgia", Font.PLAIN, 40));
                JScrollPane scroll = new JScrollPane(display);
                scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroll.setBounds(200, 150, 600, 400);

                panel.add(scroll);
                panel.add(back);
                panel.add(checkingChckBx);
                panel.add(savingsChckBx);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            }

        } else if (type == "employee") {

            if (choice == 1) {
                panel.removeAll();

                JLabel amountLabel = new JLabel("Enter Amount");
                amountLabel.setBounds(300, 250, 400, 50);
                amountLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                amountInput = new JTextField(20);
                amountInput.setBounds(300, 300, 400, 50);
                amountInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(amountLabel);
                panel.add(back);
                panel.add(amountInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 2) {
                panel.removeAll();

                JLabel amountLabel = new JLabel("Enter Amount");
                amountLabel.setBounds(300, 250, 400, 50);
                amountLabel.setFont(new Font("Georgia", Font.PLAIN, 20));

                amountInput = new JTextField(20);
                amountInput.setBounds(300, 300, 400, 50);
                amountInput.setFont(new Font("Georgia", Font.PLAIN, 20));

                ImageIcon icon1 = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/enter.png");
                Image image1 = icon1.getImage();
                Image scaledImage1 = image1.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);

                doAction = new JButton(scaledIcon1);
                doAction.setBackground(new Color(42, 189, 189));
                doAction.setContentAreaFilled(false);
                doAction.setBorderPainted(false);
                doAction.setFocusPainted(false);
                doAction.setBounds(400, 600, 200, 75);
                doAction.addActionListener(this);
                
                ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/back.png");
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                back = new JButton(scaledIcon);
                back.setContentAreaFilled(false);
                back.setBorderPainted(false);
                back.setFocusPainted(false); 
                back.setBounds(0, 0, 200, 75);
                back.setFont(new Font("Georgia", Font.PLAIN, 20));
                back.addActionListener(this);

                panel.add(amountLabel);
                panel.add(back);
                panel.add(amountInput);
                panel.add(doAction);

                panel.revalidate();
                panel.repaint();
            } else if (choice == 3) {
                doActions(choice, type);
            } else if (choice == 4) {
                System.exit(0);
            }

        }
    }

    public void options(String type) {

        if (type == "customer") {
            panel.removeAll();

            JLabel select = new JLabel("Select Transaction Type", SwingConstants.CENTER);
            select.setFont(new Font("Verdana", Font.BOLD, 35));
            select.setBounds(0, 50, 1000, 100);

            opt1 = new JButton(">");
            opt1.setBounds(50, 200,100, 75);
            opt1.addActionListener(this);
            opt1.setBackground(new Color(128, 128, 128));
            opt1.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt1.setForeground(new Color(100, 100, 100));
            
            JLabel opt1Label = new JLabel("Withdraw");
            opt1Label.setBounds(200,175,300,125);
            opt1Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt2 = new JButton(">");
            opt2.setBounds(50, 400,100, 75);
            opt2.addActionListener(this);
            opt2.setBackground(new Color(128, 128, 128));
            opt2.setFont(new Font("Georgia", Font.PLAIN,40));
            opt2.setForeground(new Color(255, 255, 255));
            opt2.setForeground(new Color(100, 100, 100));

            JLabel opt2Label = new JLabel("Deposit");
            opt2Label.setBounds(200,375,300,125);
            opt2Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt3 = new JButton(">");
            opt3.setBounds(50, 600,100, 75);
            opt3.addActionListener(this);
            opt3.setBackground(new Color(128, 128, 128));
            opt3.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt3.setForeground(new Color(255, 255, 255));
            opt3.setForeground(new Color(100, 100, 100));

            JLabel opt3Label = new JLabel("Transfer");
            opt3Label.setBounds(200,575,300,125);
            opt3Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt4 = new JButton("<");
            opt4.setBounds(850, 200, 100, 75);
            opt4.addActionListener(this);
            opt4.setBackground(new Color(128, 128, 128));
            opt4.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt4.setForeground(new Color(255, 255, 255));
            opt4.setForeground(new Color(100, 100, 100));

            JLabel opt4Label = new JLabel("Check Balance", SwingConstants.RIGHT);
            opt4Label.setBounds(500,175,300,125);
            opt4Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt5 = new JButton("<");
            opt5.setBounds(850, 400, 100, 75);
            opt5.addActionListener(this);
            opt5.setBackground(new Color(128, 128, 128));
            opt5.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt5.setForeground(new Color(255, 255, 255));
            opt5.setForeground(new Color(100, 100, 100));

            JLabel opt5Label = new JLabel("Change Pin", SwingConstants.RIGHT);
            opt5Label.setBounds(500,375,300,125);
            opt5Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt6 = new JButton("<");
            opt6.setBounds(850, 600, 100, 75);
            opt6.addActionListener(this);
            opt6.setBackground(new Color(128, 128, 128));
            opt6.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt6.setForeground(new Color(255, 255, 255));
            opt6.setForeground(new Color(100, 100, 100));

            JLabel opt6Label = new JLabel("Print Statements", SwingConstants.RIGHT);
            opt6Label.setBounds(500,575,300,125);
            opt6Label.setFont(new Font("Verdana", Font.BOLD, 25));

            ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/logout.png");
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            opt7 = new JButton(scaledIcon);
            opt7.setContentAreaFilled(false);
            opt7.setBorderPainted(false);
            opt7.setFocusPainted(false);    
            opt7.setBounds(400, 800, 200, 75);
            opt7.addActionListener(this);
            opt7.setBackground(Color.red);
            opt7.setFont(new Font("Georgia", Font.PLAIN, 20));
            opt7.setForeground(new Color(255, 255, 255));

            error.setBounds(0,900, 1000, 50);
            error.setFont(new Font("Arial", Font.PLAIN, 20));
            error.setForeground(Color.red);

            panel.add(select);
            panel.add(opt1Label);
            panel.add(opt2Label);
            panel.add(opt3Label);
            panel.add(opt4Label);
            panel.add(opt5Label);
            panel.add(opt6Label);
            panel.add(opt1);
            panel.add(opt2);
            panel.add(opt3);
            panel.add(opt4);
            panel.add(opt5);
            panel.add(opt6);
            panel.add(opt7);
            panel.add(error);

            panel.revalidate();
            panel.repaint();
        } else if (type == "employee") {
            panel.removeAll();

            JLabel select = new JLabel("Select Admin Transaction Type", SwingConstants.CENTER);
            select.setFont(new Font("Verdana", Font.BOLD, 35));
            select.setBounds(0, 50, 1000, 100);

            opt1 = new JButton(">");
            opt1.setBounds(50, 200,100, 75);
            opt1.addActionListener(this);
            opt1.setBackground(new Color(128, 128, 128));
            opt1.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt1.setForeground(new Color(100, 100, 100));
            
            JLabel opt1Label = new JLabel("Refill");
            opt1Label.setBounds(200,175,300,125);
            opt1Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt2 = new JButton(">");
            opt2.setBounds(50, 400,100, 75);
            opt2.addActionListener(this);
            opt2.setBackground(new Color(128, 128, 128));
            opt2.setFont(new Font("Georgia", Font.PLAIN,40));
            opt2.setForeground(new Color(255, 255, 255));
            opt2.setForeground(new Color(100, 100, 100));

            JLabel opt2Label = new JLabel("Withdraw");
            opt2Label.setBounds(200,375,300,125);
            opt2Label.setFont(new Font("Verdana", Font.BOLD, 25));

            opt3 = new JButton(">");
            opt3.setBounds(50, 600,100, 75);
            opt3.addActionListener(this);
            opt3.setBackground(new Color(128, 128, 128));
            opt3.setFont(new Font("Georgia", Font.PLAIN, 40));
            opt3.setForeground(new Color(255, 255, 255));
            opt3.setForeground(new Color(100, 100, 100));

            JLabel opt3Label = new JLabel("<html><center>Check ATM Balance</center></html>");
            opt3Label.setBounds(200,575,300,125);
            opt3Label.setFont(new Font("Verdana", Font.BOLD, 25));

            ImageIcon icon = new ImageIcon("CPS406_BANK_ATM_SYSTEM/App/logout.png");
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(200, 75, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            opt4 = new JButton(scaledIcon);
            opt4.setContentAreaFilled(false);
            opt4.setBorderPainted(false);
            opt4.setFocusPainted(false);    
            opt4.setBounds(400, 800, 200, 75);
            opt4.addActionListener(this);
            opt4.setBackground(Color.red);
            opt4.setFont(new Font("Georgia", Font.PLAIN, 20));
            opt4.setForeground(new Color(255, 255, 255));

            error.setBounds(0,900, 1000, 50);
            error.setFont(new Font("Arial", Font.PLAIN, 20));
            error.setForeground(Color.red);

            panel.add(select);
            panel.add(opt1);
            panel.add(opt2);
            panel.add(opt3);
            panel.add(opt4);
            panel.add(opt1Label);
            panel.add(opt2Label);
            panel.add(opt3Label);
            panel.add(error);

            panel.revalidate();
            panel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        if (click.getSource() == login) {
            String num = null;
            int pin = 0;
            num = card.getText();
            try {
                pin = Integer.parseInt(new String(password.getPassword()));
            } catch (Exception e) {
                status.setText("Verification Error");
                panel.revalidate();
                panel.repaint();
            }
            if (num != null & pin != 0) {
                Card card = new Card(num, null, null, pin);
                if (db.verifyUser(card, pin)) {
                    if (num.length() == 16) {
                        for (Customer c : custList) {
                            if (c.getCard().getCardNumber().equals(num)) {
                                currCust = c;
                            }
                        }
                        type = "customer";
                        options("customer");
                    } else {
                        for (Employee e : empList) {
                            if (Integer.toString(e.getEmployeeID()).equals(num)) {
                                currEmp = e;
                            }
                        }
                        type = "employee";
                        options("employee");
                    }
                } else {
                    status.setText("Verification Error");
                    panel.revalidate();
                    panel.repaint();
                }
            } else {
                status.setText("Verification Error");
                panel.revalidate();
                panel.repaint();
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
        } else if (click.getSource() == opt7) {
            System.exit(0);
        } else if (click.getSource() == doAction) {
            doActions(currChoice, type);
        } else if (click.getSource() == back) {
            WIN.setLocationRelativeTo(null);
            error.setText("");
            options(type);
        }
    }
}
