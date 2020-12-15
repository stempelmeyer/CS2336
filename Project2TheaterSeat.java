package Tickets;

public class TheaterSeat extends BaseNode {
	// define methods
	private TheaterSeat up;
	private TheaterSeat down;
	private TheaterSeat left;
	private TheaterSeat right;
	
	// inherited abstract class
	public void abstractMethod() {}
	
	// default constructor
	TheaterSeat(){
		up = null;
		down = null;
		left = null;
		right = null;
	}
	// overload constructor
	TheaterSeat(int newRow, char newSeat, boolean newReserved, char newTicketType) {
		// call BaseNode constructor with super to initialize variables
		super(newRow, newSeat, newReserved, newTicketType); 
	}
	// mutators
	public void setUp(TheaterSeat newUp) {
		up = newUp;
	}
	public void setDown(TheaterSeat newDown) {
		down = newDown;
	}
	public void setLeft(TheaterSeat newLeft) {
		left = newLeft;
	}
	public void setRight(TheaterSeat newRight) {
		right = newRight;
	}
	// accessors
	public TheaterSeat getUp() {
		return up;
	}
	public TheaterSeat getDown() {
		return down;
	}
	public TheaterSeat getLeft() {
		return left;
	}
	public TheaterSeat getRight() {
		return right;
	}

} // end class TheaterSeat
