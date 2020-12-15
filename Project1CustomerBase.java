package DrinkRewards;

public class CustomerBase {
	// declare members
	private String firstName;
	private String lastName;
	private String guestID;
	private double amountSpent;
	
	// declare methods
	public CustomerBase() {
		firstName = "";
		lastName = "";
		guestID = "";
		amountSpent = 0;
	}
	
	// overload constructor
	public CustomerBase(String fName, String lName, String ID, double amount) {
		firstName = fName;
		lastName = lName;
		guestID = ID;
		amountSpent = amount; // total spent doesn't count bonus bucks used
	}
	// accessors
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getID() {
		return guestID;
	}
	public double getAmount() {
		return amountSpent;
	}
	// mutators 
	public void setFirstName(String fname) {
		firstName = fname;
	}
	public void setLastName(String lname) {
		lastName = lname;
	}
	public void setID(String ID) {
		guestID = ID;
	}
	public void setAmount(double amount) {
		amountSpent = amount;
	}
	public void copy(CustomerBase another) {
		this.setFirstName(another.getFirstName());
		this.setLastName(another.getLastName());
		this.setID(another.getID());
		this.setAmount(another.getAmount());
	}
} // end CustomerBase class
