package Tickets;

public class BaseNode {
	// define members
	private int row;
	private char seat;
	private boolean reserved;
	private char ticketType;
	
	// define methods, using protected which makes methods acessible to child classes
	protected BaseNode(){
		row = -1;
		seat = '-';
		reserved = false;
		ticketType = '-';
	}
	// overload constructer called by derived class
	BaseNode(int newRow, char newSeat, boolean newReserved, char newTicketType){
		row = newRow;
		seat = newSeat;
		reserved = newReserved;
		ticketType = newTicketType;
	}
	// mutators
	protected void setRow(int newRow) {
		row = newRow;
	}
	protected void setSeat(char newSeat) {
		seat = newSeat;
	}
	protected void setReserved(boolean newReserved) {
		reserved = newReserved;
	}
	protected void setTicketType(char newTicketType) {
		ticketType = newTicketType;
	}
	// accessors
	public int getRow() {
		return row;
	}
	public char getSeat() {
		return seat;
	}
	public boolean getReserved() {
		return reserved;
	}
	public char getTicketType() {
		return ticketType;
	}

} // end class BaseNode

