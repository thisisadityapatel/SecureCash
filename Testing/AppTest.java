package Testing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import App.*;
import java.util.*;

public class AppTest {

    private Database db;
    private ArrayList<Customer> custList;
    private ArrayList<Employee> empList;
    private ArrayList<retrieveDatabaseData> dbContents;
    private Customer currCust;     
    private Employee currEmp;
    private ATM atm;
    private CustomerInterface custinterface;
    private EmployeeInterface empinterface;

    @Before  
    public void init () {
        //Setting up all the classes and instances needed to test the ATM system.
        custList = new ArrayList<Customer>();
        empList = new ArrayList<Employee>();
        dbContents = new ArrayList<retrieveDatabaseData>();
        db = new Database();
        dbContents = db.extractDatabase();
        atm = new ATM(db);
        custinterface = new CustomerInterface(atm);
        empinterface = new EmployeeInterface(atm);

        // Getting all the data needed from the database
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

        //initializing a customer and employee for the tests

        String custID = "1234123412341234";
        for (Customer c : custList) {
            if (c.getCard().getCardNumber().equals(custID)) {
                currCust = c;
            }
        }
        int empID = 56789;
        for (Employee e : empList) {
            if (e.getEmployeeID() == empID) {
                currEmp = e;
            }
        }
        // change pin before doing tests
        custinterface.changePin(currCust, 1234);
        // Setting ATM balance to right amount before the tests
        empinterface.withdrawATM(150000.00);
    }
   
    @Test // Documented as Test Case 1.01
    public void CustomerLoginTest() {
        String cardNumber = "1234123412341234";
        int pin = currCust.getCard().getPin(); //fix this somehow
        for (Customer c : custList) {
            if (c.getCard().getCardNumber().equals(cardNumber)) {
                currCust = c;
            }
        }
        boolean loggedIn = (pin == currCust.getCard().getPin()) && (cardNumber.equals(currCust.getCard().getCardNumber()));
        assertEquals(true, loggedIn);
    }

    @Test // Documented as Test Case 1.02
    public void CustomerLoginErrorTest() {
        String cardNumber = "1234123412341234";
        int pin = 123;
        boolean loggedIn = (pin == currCust.getCard().getPin()) && (cardNumber.equals(currCust.getCard().getCardNumber()));
        assertEquals(loggedIn, currCust.login(currCust.getCard(), pin));
    }

    @Test // Documented as Test Case 1.03
    public void DepositTest() {
        double balance = currCust.getSavingsAccount().getBalance();
        balance = balance + 300.0;
        custinterface.deposit(currCust, 300.0, "savings");
        assertEquals(balance, custinterface.checkBalance(currCust, "savings"), 0.001);
    }

    @Test // Documented as Test Case 1.04
    public void CustomerDepositInsufficientFundTest() {
        currCust = custList.get(0);
        currCust.getSavingsAccount().setBalance(1379.5);
        currCust.getChequeingsAccount().setBalance(13000.0);
        double savBalance = currCust.getSavingsAccount().getBalance();
        double cheBalance = currCust.getChequeingsAccount().getBalance();
        double atmBalance = atm.getCashTotal();
        custinterface.deposit(currCust, 200000, "savings");
        assertEquals(savBalance,custinterface.checkBalance(currCust,"savings"), 0.001);
        assertEquals(atmBalance, atm.getCashTotal(), 0.001);
        custinterface.deposit(currCust, 200000, "checking");
        assertEquals(cheBalance,custinterface.checkBalance(currCust,"checking"), 0.001);
        assertEquals(atmBalance, atm.getCashTotal(), 0.001);
    }

    @Test // Documented as Test Case 1.05
    public void WithdrawlTest () {
        double balance = currCust.getSavingsAccount().getBalance();
        balance = balance - 100.0;
        custinterface.withdraw(currCust, 100.0, "savings");
        assertEquals(balance,custinterface.checkBalance(currCust,"savings"), 0.001);
    }

    @Test // Documented as Test Case 1.06
    public void CustomerWithdrawInsufficientFundsCust() {   
        currCust = custList.get(0);
        currCust.getSavingsAccount().setBalance(1379.5);
        currCust.getChequeingsAccount().setBalance(13000.0);
        double savBalance = currCust.getSavingsAccount().getBalance();
        double cheBalance = currCust.getChequeingsAccount().getBalance();
        double atmBalance = atm.getCashTotal();
        custinterface.withdraw(currCust, 5000, "savings");
        assertEquals(savBalance,custinterface.checkBalance(currCust,"savings"), 0.001);
        assertEquals(atmBalance,atm.getCashTotal(), 0.001);
        custinterface.withdraw(currCust, 15000, "checking");
        assertEquals(cheBalance,custinterface.checkBalance(currCust,"checking"), 0.001);
        assertEquals(atmBalance,atm.getCashTotal(), 0.001);
    }

    @Test // Documented as Test Case 1.07
    public void CustomerWithdrawInsufficientFundsATM() 
    {
        double cheBalance = currCust.getChequeingsAccount().getBalance();
        double atmBalance = atm.getCashTotal();
        custinterface.withdraw(currCust, 13000, "checking");
        assertEquals(cheBalance,custinterface.checkBalance(currCust,"checking"), 0.001);
        assertEquals(atmBalance, atm.getCashTotal(), 0.001);
    }

    @Test // Documented as Test Case 1.08
    public void CustomerTransferFunds() 
    {
        Customer otherCust = custList.get(1);
        double senderCheBalance = currCust.getChequeingsAccount().getBalance();;
        double recieverCheBalance = otherCust.getChequeingsAccount().getBalance();;
        double senderSavBalance = currCust.getSavingsAccount().getBalance();
        double recieverSavBalance = otherCust.getSavingsAccount().getBalance();

        senderCheBalance = senderCheBalance - 666;
        recieverCheBalance = recieverCheBalance + 666;
        senderSavBalance = senderSavBalance - 999;
        recieverSavBalance = recieverSavBalance + 999;

        custinterface.transfer(currCust, otherCust, 999, "savings", "savings");
        assertEquals(senderSavBalance,custinterface.checkBalance(currCust,"savings"), 0.001);
        assertEquals(recieverSavBalance,custinterface.checkBalance(otherCust,"savings"), 0.001);

        custinterface.transfer(currCust, otherCust, 666, "checking", "checking");
        assertEquals(senderCheBalance,custinterface.checkBalance(currCust,"checking"), 0.001);
        assertEquals(recieverCheBalance,custinterface.checkBalance(otherCust,"checking"), 0.001);
    }

    @Test // Documented as Test Case 1.09
    public void CustomerTransferFundsInsufficientTest() 
    {   
        Customer otherCust = custList.get(1);
        double senderCheBalance = currCust.getChequeingsAccount().getBalance();;
        double recieverCheBalance = otherCust.getChequeingsAccount().getBalance();;
        double senderSavBalance = currCust.getSavingsAccount().getBalance();
        double recieverSavBalance = otherCust.getSavingsAccount().getBalance();
        
        custinterface.transfer(currCust, otherCust, 200000, "savings", "savings");
        assertEquals(senderSavBalance,custinterface.checkBalance(currCust,"savings"), 0.001);
        assertEquals(recieverSavBalance,custinterface.checkBalance(otherCust,"savings"), 0.001);
        
        custinterface.transfer(currCust, otherCust, 14000, "checking", "checking");
        assertEquals(senderCheBalance,custinterface.checkBalance(currCust,"checking"), 0.001);
        assertEquals(recieverCheBalance,custinterface.checkBalance(otherCust,"checking"), 0.001);
    }

    @Test // Documented as Test Case 1.10
    public void CheckBalanceTest () {
        double balance = currCust.getSavingsAccount().getBalance();
        assertEquals(balance, custinterface.checkBalance(currCust,"savings"), 0.001);
    }

    @Test //Documented as Test Case 1.11
    public void ChangePinTest() {
        int oldPin = currCust.getCard().getPin();
        int newPin = 7689;
        custinterface.changePin(currCust, newPin);
        assertNotEquals(oldPin, currCust.getCard().getPin());
    }

    @Test //Documented as Test Case 1.12
    public void IncorrectPinTest() {
        int oldPin = currCust.getCard().getPin();
        int newPin = 123456;
        custinterface.changePin(currCust, newPin);
        assertEquals(oldPin, currCust.getCard().getPin());
    }

    @Test //Documented as Test Case 1.13
    public void GetStatementTest(){
        String statement = custinterface.printStatement(currCust, "savings");
        String statement1 = atm.getLog(currCust, "savings");
        assertEquals(statement, statement1);

    }
    @Test //Documented as Test Case 1.14
    public void EmployeeLoginTest() {
        int empID = 56789;
        int pin = 1239;
        for (Employee e : empList) {
            if (e.getEmployeeID() == empID) {
                currEmp = e;
            }
        }
        boolean isLoggedIn = (pin == currEmp.getPin());
        assertEquals(true, isLoggedIn);
    }

    @Test // Documented as Test Case 1.15
    public void EmployeeATMRefillTest() {
        double balance = atm.getCashTotal();
        balance = balance + 500;
        empinterface.refillATM(500);
        assertEquals(balance, atm.getCashTotal(), 0.001);
    }

    @Test //Documented as Test Case 1.16
    public void EmployeeATMRefillErrorTest() {
        empinterface.refillATM(1600000.0);
        assertEquals(200000, atm.getCashTotal(), 0.001);
    }

    @Test // Documented as Test Case 1.17
    public void GetBalanceTest() {
        double total = atm.getCashTotal();
        assertEquals(empinterface.getATMBalance(), total, 0.001);
    }

    @Test //Documented as Test Case 1.18
    public void EmployeeATMWithdrawTest() {
        double balance = atm.getCashTotal();
        balance = balance - 300;
        empinterface.withdrawATM(300);
        assertEquals(balance, atm.getCashTotal(), 0.001);
    }

    @Test //Documented as Test Case 1.19
    public void EmployeeATMWithdrawIncorrectTest() {
        double balance = atm.getCashTotal();
        empinterface.withdrawATM(1600000.0);
        assertEquals(balance, atm.getCashTotal(), 0.001);
    }

}


