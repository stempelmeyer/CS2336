package DrinkRewards;
// Platinum customers are derived from the customer base class
// gold customer is platinum if spent at least $200 after gold discounts have been applied
public class CustomerPlat 
		extends CustomerBase {
	// declare member
	private int bonusBucks = -1;
	// for every $5 over $200 spent, receive 1 bonus buck (use mod division)
	// if a customer has spent 217 and makes purchase of 17 then earn 2 bonus bucks, one for 220 and one for 225
	// bonus bucks applied to next order
	
	// declare methods
	// overload constructor chained to base class constructor
	public CustomerPlat() {
		setAmount((getAmount() - 200)/5);
	}
	public CustomerPlat(String fName, String lName, String iD, double amount, int bucks){
		super(fName, lName, iD, amount);
		bonusBucks = bucks;
	}
	
	public void copy(CustomerPlat another) {
		this.setFirstName(another.getFirstName());
		this.setLastName(another.getLastName());
		this.setID(another.getID());
		this.setAmount(another.getAmount());
		this.bonusBucks = another.getBonusBucks();
	}
	// accessor
	public int getBonusBucks() {
		return bonusBucks;
	}
	
	// mutator
	public void setBonusBucks(int bucks) {
		
		bonusBucks = bucks;
	}
}
