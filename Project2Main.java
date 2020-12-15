package Tickets;
//Author:          Sarah G. Tempelmeyer
//Course:          CS 2336.003
//Date:            3/8/2019
//Assignment:      Project 2 - Avengers 4 Ticket Reservation System (Extended Edition)
//Compiler:        Eclipse 2018_12
//Net ID:          sgt170030

//Description: This program demonstrates double-linked lists, inheritance, and classes
//			   in java programming

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.*;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		// declare scanner variable and open file
		java.io.File file = new java.io.File("A1.txt");
		Scanner input = new Scanner(file); // create a scanner for the file
		
		// declare scanner for console input and output
		Scanner console = new Scanner(System.in);
		
		if(!file.exists()) {
			System.out.println("File does not exist");
			System.exit(1);
		}
		
		// create and fill auditorium
		Auditorium auditorium = new Auditorium(input, file);
		mainMenu(console, auditorium);
		
		System.out.println("\nThis is the linked list");
		
		// displays linked list
		TheaterSeat ptr = auditorium.getHead();
		TheaterSeat rowHead = auditorium.getHead();
		while(ptr != null && ptr != null) {
			System.out.print(ptr.getTicketType());
			if(ptr.getRight() != null)
				ptr = ptr.getRight(); // move pointer to the right
			else if(ptr.getDown() != null) {
				System.out.println();
				ptr = rowHead.getDown();
				rowHead = rowHead.getDown();
			}
			else
				break;
		}
		
	PrintWriter output = new PrintWriter("A1.txt");
			
		//output auditorium to file
		ptr = auditorium.getHead();
		rowHead = auditorium.getHead();
		while(ptr != null && ptr != null) {
			output.print(ptr.getTicketType());
			if(ptr.getRight() != null)
				ptr = ptr.getRight(); // move pointer to the right
			else if(ptr.getDown() != null) {
				output.println();
				ptr = rowHead.getDown();
				rowHead = rowHead.getDown();
			}
			else
				break;
		}
		output.close(); // close file
		
		// how many types of tickets are there
		double total = auditorium.getAdult()*10 + auditorium.getSenior()*7.50 + auditorium.getChild()*5;
			
		// display auditorium and ticket summary to console
		displayAuditorium(auditorium);
		System.out.println("Total Seats in Auditorium:  " + (auditorium.getRows()*auditorium.getColumns()));
		System.out.println("Total Tickets Sold:         " + (auditorium.getAdult() + auditorium.getSenior() + auditorium.getChild()));
		System.out.println("Adult Tickets Sold:         " + auditorium.getAdult());
		System.out.println("Child Tickets Sold:         " + auditorium.getChild());
		System.out.println("Senior Tickets Sold:        " + auditorium.getSenior());
		System.out.print("Total Tickets Sales:        $");
		System.out.printf("%.2f", total);

	} // end function main
	
	/* this function holds the main menu */
	public static void mainMenu(Scanner console, Auditorium auditorium) {
		int userOption = 0;
		do{
			try {
			System.out.println("1. Reserve Seats");
			System.out.println("2. Exit");
			userOption = console.nextInt();
			}
			catch (InputMismatchException ex) {
				System.out.println("Please enter a valid input");
				console.nextLine();
			}
		} while (userOption != 1 && userOption != 2);
		
		int userRow = 0, userAdult = -1, userChild = -1, userSenior = -1;
		char userStartingSeat = '-';
		
		while(userOption != 2) {
			displayAuditorium(auditorium); // display available seats to purchase
			do { 
				try{
					System.out.println("Please enter the row number you would like to reserve: ");
					userRow = console.nextInt();
					if (userRow <= 0 || userRow > auditorium.getRows())
						System.out.println("Not a valid row number");
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter valid input");
					console.nextLine();
				}
			} while(userRow <= 0 || userRow > auditorium.getRows());
			do { 
				try{
					System.out.println("Please enter the starting seat letter: ");
					userStartingSeat = console.next().charAt(0); // convert seat number to ASCII value
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
				}
			} while((int)userStartingSeat < 65 || (int)userStartingSeat >= auditorium.getColumns() + 65);
			do { 
				try{
					System.out.println("Please enter the number of adult tickets you would like to reserve: ");
					userAdult = console.nextInt();
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
				}
			} while(userAdult < 0);
			do { 
				try{
					System.out.println("Please enter the number of child tickets you would like to reserve: ");
					userChild = console.nextInt();
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
				}
			} while(userChild < 0);
			do { 
				try{
					System.out.println("Please enter the number of senior tickets like to reserve: ");
					userSenior = console.nextInt();
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
				}
			} while(userSenior < 0);
			if (userAdult + userChild + userSenior == 0) { // if the user doesnt reserve enough tickets
				System.out.println("You did not reserve any seats, please try again");
				continue;
			}
			
			// remember that the starting seat is the ASCII value for the uppercase alphabet
			if (seatAvailability(auditorium, userAdult + userChild + userSenior, userRow, userStartingSeat)) {
				reserveSeats(auditorium, userAdult, userChild, userSenior, userRow, userStartingSeat); // reserve seats
				System.out.println("The seats you have requested are available and now reserved!");
				displayAuditorium(auditorium);
				System.out.println("Please reselect an option");
			}
			// if not available, check if there are better seats available
			else {
				// check starting location of a better available seat
				String betterString = betterSeats(auditorium, userAdult + userChild + userSenior);
				char betterSeat = betterString.charAt(2);
				int betterRow = (int)(betterString.charAt(0)) - 48;
				// if there is a better seat available
				if(betterSeat != '!') { 
					char option;
					System.out.println("Your requested seats are unavailable. Dont fret! There is a better seat option available at row " + betterRow + " starting seat " +
						betterSeat	+ ". Would you like to reserve these seats? (Y or N): ");
					option = console.next().charAt(0);
					// input validation
					while (Character.toUpperCase(option) != 'Y' && Character.toUpperCase(option) != 'N') {
						System.out.println("Please choose a valid option");
						option = console.next().charAt(0);
					}
					// read in char from user
					if(Character.toUpperCase(option) == 'Y') {
						reserveSeats(auditorium, userAdult, userChild, userSenior, betterRow, betterSeat); // reserve seats, beginning at better seat
					}
					else { // user doesn't want better seat
						System.out.println("Okay! Please reselect an option");
					}
				}
				else {
					System.out.println("There are not " + (userAdult + userChild + userSenior) + " seats available in row " +
				userRow);
					System.out.println("Please reselect an option");
				}
			}
			
			// display menu again
			do {
				try {
				System.out.println("1. Reserve Seats");
				System.out.println("2. Exit");
				userOption = console.nextInt();
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
					userOption = 0;
				}
			} while (userOption != 1 && userOption != 2);
			
		} // exit menu if user selects option 2
	}
	
	/* this function travels through array and displays auditorium */
	public static void displayAuditorium(Auditorium auditorium) {
		// display alphabet	after spaces
		// should equal the number of columns in the theater
		System.out.print("  ");
		for(int alphabet = 65; alphabet < 65 + auditorium.getColumns(); alphabet++) {
			System.out.print((char)alphabet);
		}
		System.out.print("\n");
			
		TheaterSeat ptr = auditorium.getHead();
		TheaterSeat rowHead = auditorium.getHead();
		System.out.print(1 + " ");
		for(int i = 2; ptr != null && ptr != null;) {
			if (ptr.getTicketType() == 'A' || ptr.getTicketType() == 'C' || ptr.getTicketType() == 'S')
				System.out.print("#"); // if seat is taken
			else
				System.out.print("."); // if seat is open
			
			if(ptr.getRight() != null)
				ptr = ptr.getRight(); // move pointer to the right
			else if(ptr.getDown() != null) {
				System.out.println();
				System.out.print(i + " ");
				ptr = rowHead.getDown();
				rowHead = rowHead.getDown();
				i++;
			}
			else
				break;
		}
		System.out.println();
		
	} // end function displayAuditorium()
	
	/* make sure there are seats available in the requested row */
	public static boolean seatAvailability(Auditorium auditorium, int numRequest, int row, char startingSeat) {
		// this for loop checks if every seat from the starting seat to the requested number of reserved seats is empty
		TheaterSeat ptr = auditorium.getHead();
		for(int rowPtr = 1; rowPtr < row; rowPtr++) // this for loop moves pointer to the correct row
			ptr = ptr.getDown();
		while(ptr.getSeat() != startingSeat) { // this for loop moves pointer to the correct starting seat
			ptr = ptr.getRight();
		}
		if (ptr.getReserved() == true)
			return false;
		for(int i = 1; i <= numRequest; i++) {
			if(ptr == null)
				return false; 	// return false if seat doesnt exist
			else if(ptr.getReserved() == true)
				return false;	// return false if seat is taken
			else	
				ptr = ptr.getRight();
		}
		return true; 
	} // end function seatAvailability
	
	/* this function changes the auditorium to reserve your seats */
	public static void reserveSeats(Auditorium auditorium, int adult, int child, int senior, int row, char startingSeat) {
		TheaterSeat ptr = auditorium.getHead();
		for(int rowPtr = 1; rowPtr < row; rowPtr++) // this for loop moves pointer to the correct row
			ptr = ptr.getDown();
		while(ptr.getSeat() != startingSeat) { // this for loop moves pointer to the correct starting seat
			ptr = ptr.getRight();
		}
		// reserve adult seats
		for(int i = 0; i < adult; i++) {
			ptr.setTicketType('A');
			ptr.setReserved(true);
			ptr = ptr.getRight();
		}
		startingSeat = (char)((int)startingSeat + adult); // move the starting seat forward if reserved adult seats
		// reserve child seats
		for(int i = 0; i < child; i++) {
			ptr.setTicketType('C');
			ptr.setReserved(true);
			ptr = ptr.getRight();
		}
		startingSeat = (char)((int)startingSeat + child); // move the starting seat forward if reserved child seats
		// reserve senior seats
		for(int i = 0; i < senior; i++){
			ptr.setTicketType('S');
			ptr.setReserved(true);
			ptr = ptr.getRight();
		}
		startingSeat = (char)((int)startingSeat + senior); // move the starting seat forward if reserved senior seats		
		
		// update auditorium child, adult, and senior tickets
		auditorium.setNumAdult(auditorium.getAdult() + adult);
		auditorium.setNumChild(auditorium.getChild() + child);
		auditorium.setNumSenior(auditorium.getSenior() + senior);
	} // end function reserveSeats()
	
	// this function checks to see if there are better seats available
	public static String betterSeats(Auditorium auditorium, int request) {
		char bestStartingSeat = (char)'!'; 
		int bestRow = 33;
		double distance = 100, bestDistance = 1000; 
		for(int i = 0; i < auditorium.getRows(); i ++) {
			for(int j = 0; j <= auditorium.getColumns() - request; j++) {
				
				if(seatAvailability(auditorium, request, i+1, (char)(j + 'A'))) {
					//System.out.println("better seats at row " + (i+1) + " and column " + (char)(j + 'A') + ": ");
					distance = Math.sqrt(Math.pow((auditorium.getRows()+1)/2.0 - (i+1), 2)  +  Math.pow((auditorium.getColumns()+1)/2.0 - ((j+1)+ (request-1)/2.0), 2));
					// System.out.println("This is the distance " + distance);
					
					if(  (distance < bestDistance) || ((distance == bestDistance) && ((Math.abs((auditorium.getRows()+1)/2.0 - (i+1)) < (Math.abs( (auditorium.getRows()+1) /2.0 - bestRow) /*math ab right*/) /*math abs left*/)) /*distance*/) /*if*/) { // if this distance is smaller or the row is closer (with equal distance)
						bestStartingSeat = (char)(j + 'A');
						bestDistance = distance;
						bestRow = i + 1;
					}
				}
			} // end inner column for loop
		}// end outer row for loop
		return bestRow + " " + bestStartingSeat;
	} // end function betterSeats()
	
} // end class Main
