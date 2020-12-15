package Tickets;

public class Auditorium {
	// define members
	private TheaterSeat first; // head pointer of linked list
	private int rows = 0;
	private int columns = 0;
	private int numSenior = 0;
	private int numAdult = 0;
	private int numChild = 0;
	
	// constructor builds auditorium grid and fills each node based on file input
	Auditorium(java.util.Scanner input, java.io.File file){
		
		String line = input.next(); // read in next line from file // or nextLine()
		boolean flag = true;
		columns = line.length();
		while(line != "" && flag != false) {
			rows++; // increment number of rows
			if (!input.hasNext())
				flag = false;
			parseLine(line, rows, columns); // send line, array, and rows to a parse function
			if(input.hasNext())
				line = input.next(); //read in next line
		}
	}
	// mutator
	public void setHead(TheaterSeat newHead) {
		first = newHead;
	}
	public void setNumChild(int children) {
		numChild = children;
	}
	public void setNumAdult(int adults) {
		numAdult = adults;
	}
	public void setNumSenior(int seniors) {
		numSenior = seniors;
	}
	
	// accessor
	public TheaterSeat getHead() {
		return first;
	}
	public int getRows() {
		return rows;
	}
	public int getColumns() {
		return columns;
	}
	public int getAdult() {
		return numAdult;
	}
	public int getSenior() {
		return numSenior;
	}
	public int getChild() {
		return numChild;
	}
	
	
	private void parseLine(String line, int rows, int columns) {
		if(rows == 1) {
			// create head linked node to point at first seat
			TheaterSeat newFirst = new TheaterSeat();
			first = newFirst;
			newFirst.setRow(1); // assign row, seat, reserved and ticket type
			newFirst.setSeat('A');
			newFirst.setReserved(line.charAt(0) != '.'); // true if not .
			newFirst.setTicketType(line.charAt(0));
			if(line.charAt(0) == 'A')
				numAdult++;
			else if(line.charAt(0) == 'S')
				numSenior++;
			else if(line.charAt(0) == 'C')
				numChild++;
			TheaterSeat prevLeft = first;
			for(int i = 1; i < columns; i++) {
				TheaterSeat newPerson = new TheaterSeat();
				newPerson.setLeft(prevLeft); // set previous as left of new seat
				prevLeft.setRight(newPerson); // set new seat as right of previous
				prevLeft = newPerson; // move pointer foward
				
				// assign info: row, seat, reserved and ticket type
				newPerson.setRow(1);
				newPerson.setSeat((char)(65 + i));
				newPerson.setReserved(line.charAt(i) != '.'); // true if not .
				newPerson.setTicketType(line.charAt(i));
				if(line.charAt(i) == 'A')
					numAdult++;
				else if(line.charAt(i) == 'S')
					numSenior++;
				else if(line.charAt(i) == 'C')
					numChild++;
			}
		} // end if row 1
		else { // not row 1, so will be connecting up and down pointers
			TheaterSeat prevRow = first;
			for(int j = 0; j < rows - 2; j++) { // transverse prev row pointer through array
				prevRow = prevRow.getDown();
			}
			// create head linked node to point at first seat of row
			TheaterSeat newRowHead = new TheaterSeat();
			// connect up and down to previous row
			newRowHead.setUp(prevRow);
			prevRow.setDown(newRowHead);
			prevRow = prevRow.getRight(); // move prev row pointer to the right
			
			// assign info
			newRowHead.setRow(rows); // assign row, seat, reserved and ticket type
			newRowHead.setSeat('A');
			newRowHead.setReserved(line.charAt(0) != '.'); // true if not .
			newRowHead.setTicketType(line.charAt(0));
			if(line.charAt(0) == 'A')
				numAdult++;
			else if(line.charAt(0) == 'S')
				numSenior++;
			else if(line.charAt(0) == 'C')
				numChild++;
			TheaterSeat prevLeft = newRowHead;
			for(int i = 1; i < columns; i++) {
				TheaterSeat newPerson = new TheaterSeat();
				newPerson.setLeft(prevLeft); // set previous as left of new seat
				prevLeft.setRight(newPerson); // set new seat as right of previous
				newPerson.setUp(prevRow); // connect new seat up 
				prevRow.setDown(newPerson); // conect previous pointer row down
				prevLeft = newPerson; // move pointer foward
				prevRow = prevRow.getRight();
				
				// assign info: row, seat, reserved and ticket type
				newPerson.setRow(rows);
				newPerson.setSeat((char)(65 + i));
				newPerson.setReserved(line.charAt(i) != '.'); // true if not .
				newPerson.setTicketType(line.charAt(i));
				if(line.charAt(i) == 'A')
					numAdult++;
				else if(line.charAt(i) == 'S')
					numSenior++;
				else if(line.charAt(i) == 'C')
					numChild++;
			}
		} // end else
	}
}
