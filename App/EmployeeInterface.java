package App;
public class EmployeeInterface {

    ATM atm;

    // constructor for Employee Interface
    public EmployeeInterface(ATM atm) {
        this.atm = atm;
    }

    // refill ATM and return extra cash
    public double refillATM(double amount) {
        return this.atm.setCashTotal(amount);
    }

    // employee withdraw cash
    public double withdrawATM(double amount){
        return this.atm.employeeWithdraw(amount);
    }

    // get ATM balance
    public double getATMBalance() {
        return this.atm.getCashTotal();
    }
}
