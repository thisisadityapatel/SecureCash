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

    // get ATM balance
    public double getATMBalance() {
        return this.atm.getCashTotal();
    }
}
