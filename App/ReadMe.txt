SecureCash ATM 

Setting up the ATM system: 

To set up the proper paths for the database, go into Database.java and change the String called absolutePath
on line 14 to the absolute path leading to the CPS406_BANK_ATM_SYSTEM folder on your computer. 

Starting up the app: 

Run the ATMSystemMain.java file and the app should pop up in a new window. 

Logging in to the System: 

To log into the system as a customer, go into bankDatabase.txt and the first line in the file is a customer 
card number. Enter this number in the first text field in the log in screen. The second line is the customer's 
pin. Enter this value into the second text field on the login screen. 

To log into the system as an employee, go into bankDatabase.txt. On line 19 there is an employee ID. Enter this 
number in the first text field on the login screen. On line 20 there is an employee pin. Enter this number into 
the second text field on the login screen.  

To understand the organization of bankDatabase.txt, see databaseSchema.txt.

Running the test cases: 

If you have Unit testing for Java enabled in your VS code, simply click on the testing tab, then click on the
run tests button at the top of the testing menu.  
