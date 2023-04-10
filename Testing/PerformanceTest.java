package Testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import App.*;
import java.util.*;

public class PerformanceTest {
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
    }

    @Test //Documented as Test Case 2.01
    public void TestLoginTime() {
        long startTime = System.nanoTime();
        int empID = 56789;
        int pin = 1239;
        for (Employee e : empList) {
            if ((e.getEmployeeID() == empID) && (pin == e.getPin())) {
                currEmp = e;
            }
        }
        if (currEmp != null) {
            System.out.println("Verified.");
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000; //divide by 10000000 to get milliseconds
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.02
    public void WithdrawlTimeTest() {
        long startTime = System.nanoTime();
        custinterface.withdraw(currCust, 100.0, "savings");
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.03
    public void DepositTimeTest() {
        long startTime = System.nanoTime();
        custinterface.deposit(currCust, 300.0, "savings");
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.04
    public void CheckBalanceTimeTest() {
        long startTime = System.nanoTime();
        currCust.getSavingsAccount().getBalance();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.05
    public void TransferTimeTest() {
        long startTime = System.nanoTime();
        custinterface.transfer(currCust, currCust, 200.0, "savings", "checkings");
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.06
    public void ChangePinTimeTest() {
        long startTime = System.nanoTime();
        int newPin = 7689;
        custinterface.changePin(currCust, newPin);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.07
    public void RefillATMTimeTest() {
        long startTime = System.nanoTime();
        empinterface.refillATM(500.0);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }

    @Test //Documented as Test Case 2.08
    public void WithdrawATMTimeTest() {
        long startTime = System.nanoTime();
        empinterface.withdrawATM(300.0);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }
    
    @Test //Documented as Test Case 2.09
    public void CheckATMBalanceTimeTest() {
        long startTime = System.nanoTime();
        empinterface.getATMBalance();
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        totalTime = totalTime / 10000000;
        assertEquals(true, totalTime < 3);
    }
    


}
