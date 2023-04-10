package App;
public class Employee extends User {

    private int pin;
    private int employeeID;

    public Employee() {
        this.employeeID = 0;
        this.pin = 0;
    }

    public Employee(String firstName, String lastName, String dateOfBirth, int employeeID, int pin) {
        super(firstName, lastName, dateOfBirth);
        this.employeeID = employeeID;
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public boolean equals(Object otherObject) {
        Employee other = (Employee) otherObject;
        return employeeID == other.employeeID;
    }

    public String toString() {
        return "Employee #" + employeeID + "\nFirst Name: " + this.getFirstName() + "\nLast Name: " + this.getLastName()
                + "\nDate of Birth: " + this.getDateOfBirth() + "\n";
    }

}