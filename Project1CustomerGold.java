package DrinkRewards;
// Gold customers are derived from the customer base class
public class CustomerGold 
		extends CustomerBase {
	// declare member
	private int discountPercent = -1;
	// if spent $50 = 5% discount
	// if spent $100 = 10% discount
	// if spent $150 = 15% discount
	
	// overload constructor chained to base class constructor
	public CustomerGold(){
		if (getAmount() >= 50 && getAmount() < 100)
			discountPercent = 5;
		else if (getAmount() >= 100 && getAmount() < 150)
			discountPercent = 10;
		else
			discountPercent = 15;
	}
	
	public CustomerGold(String fName, String lName, String iD, double amount, double discount){
		super(fName, lName, iD, amount);
		if (getAmount() >= 50 && getAmount() < 100)
			discountPercent = 5;
		else if (getAmount() >= 100 && getAmount() < 150)
			discountPercent = 10;
		else
			discountPercent = 15;
	}
	
	public void copy(CustomerGold another) {
		this.setFirstName(another.getFirstName());
		this.setLastName(another.getLastName());
		this.setID(another.getID());
		this.setAmount(another.getAmount());
		this.discountPercent = another.getDiscount();
	}
	
	// accessor
	public int getDiscount(){
		return discountPercent;
	}
	
	// mutator
	public void setDiscount() {
		if (getAmount() >= 50 && getAmount() < 100)
			discountPercent = 5;
		else if (getAmount() >= 100 && getAmount() < 150)
			discountPercent = 10;
		else
			discountPercent = 15;
	}
	// declare methods
}
