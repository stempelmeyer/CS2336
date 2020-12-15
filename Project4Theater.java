package Tickets;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Theater {
	// define attributes
	Auditorium auditorium1;
	Auditorium auditorium2;
	Auditorium auditorium3;
	
	// define members
	// default constructor
	Theater(){
		auditorium1 = new Auditorium();
		auditorium2 = new Auditorium();
		auditorium3 = new Auditorium();
	}
	
	Theater(Scanner input, String fileName1, String fileName2, String fileName3) throws FileNotFoundException{
		// declare scanner variable and open file
		java.io.File file1 = new java.io.File(fileName1);
		input = new Scanner(file1); // create a scanner for the file
		auditorium1 = new Auditorium(input, file1);
		
		java.io.File file2 = new java.io.File(fileName2);
		input = new Scanner(file2); // create a scanner for the file
		auditorium2 = new Auditorium(input, file2);
		
		java.io.File file3 = new java.io.File(fileName3);
		input = new Scanner(file3); // create a scanner for the file
		auditorium3 = new Auditorium(input, file3);

	}
	
	
	public int getTotAdults() {
		return auditorium1.getAdult() + auditorium2.getAdult() + auditorium3.getAdult();
	}
	
	public int getTotSeniors() {
		return auditorium1.getSenior() + auditorium2.getSenior() + auditorium3.getSenior();
	}
	
	public int getTotChildren() {
		return auditorium1.getChild() + auditorium2.getChild() + auditorium3.getChild();
	}
	public int getTotalSeats() {
		return auditorium1.getTotalNumSeats() + auditorium2.getTotalNumSeats() + auditorium3.getTotalNumSeats();
	}
	public int getReserved() {
		return auditorium1.getTotalReservedSeats() + auditorium2.getTotalReservedSeats() + auditorium3.getTotalReservedSeats();
	}
	public double getTotSales() {
		return getTotAdults()*10 + getTotSeniors()*7.50 + getTotChildren()*5;
	}
	
} // end theater class
