package Tickets;
// Author:          Sarah G. Tempelmeyer
// Course:          CS 2336.003
// Date:            1/24/2019
// Assignment:      Project 0 - Avengers 4 Ticket Reservation System
// Compiler:        Eclipse 2018_12
// Net ID:          sgt170030

// Description: This program reviews java fundamental programming including
// file I/O, loops, conditional statements, arrays, and functions. 

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

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
		
		String line = input.next(); // read in next line from file // or nextLine()
		boolean flag = true;
		int rows = 0;
		int columns = line.length();
		char tempArr[][] = new char[10][columns]; //10 rows at most, and however many columns
		// read in file to 2d array through parse function
		while(line != "" && flag != false) {
			if (!input.hasNext())
				flag = false;
			parseLine(line, tempArr, rows); // send line, array, and rows to a parse function
			rows++; // increment number of rows
			if(input.hasNext())
				line = input.next(); //read in next line
		}
		// function call to trim array
		char arr[][] = new char[rows][columns];
		trimArray(arr, tempArr, rows, columns);
		int totalReservations = idReservations(arr, rows, columns);
		input.close(); // close the file
		
		displayAuditorium(arr, rows, columns);
		
		// function call menu for user
		totalReservations += mainMenu(console, arr, rows, columns);
		
		PrintWriter output = new PrintWriter("A1.txt");
		
		//output auditorium to file
		for (int i = 0; i < rows; i++) {
			if ( i!= 0)
				output.println();
			for(int j = 0; j < columns;j++) {
				output.print(arr[i][j]);
			}
		}
		output.close(); // close file
		
		// how many types of tickets are there
		System.out.println("total reservations: " + totalReservations);
		int senior = totalReservations % 1000;
		totalReservations /= 1000;
		int child = totalReservations % 1000;
		totalReservations /= 1000;
		int adult = totalReservations;
		double total = adult*10 +senior*7.50 +child*5;
		
		// display auditorium and ticket summary to console
		displayAuditorium(arr, rows, columns);
		System.out.println("Total Seats in Auditorium:  " + (rows*columns));
		System.out.println("Total Tickets Sold:         " + (adult+senior+child));
		System.out.println("Adult Tickets Sold:         " + adult);
		System.out.println("Child Tickets Sold:         " + child);
		System.out.println("Senior Tickets Sold:        " + senior);
		System.out.print("Total Tickets Sales:        $");
		System.out.printf("%.2f", total);
		
	} // end function main()
	
	/* this function reads each line from the file into the 2d array*/
	public static void parseLine(String line, char[][] array, int row) {
		// use for loop to assign characters to array from a string
		for(int i = 0; i < line.length(); i++)
			array[row][i] = line.charAt(i); 
	} // end function parseLine()
	
	/* this function copies the old array to a new trimmed array*/
	public static void trimArray(char newArray[][], char[][] array, int rows, int columns){
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				newArray[i][j] = array[i][j];
			}
		}
	}
	
	/* this function identifies how many adult, child, and senior reservations there are */
	public static int idReservations(char[][] arr, int rows, int columns) {
		// the for loop analyzes how many adult(millions place), child(thousands place), and senior(hundreds place) tickets exist
		int total = 0;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				if(arr[i][j] == 'A')
					total += 1000000;
				else if(arr[i][j] == 'C')
					total += 1000;
				else if(arr[i][j] == 'S')
					total += 1;
			}
		}
		return total;
	}
	
	public static void displayAuditorium(char[][] array, int rows, int columns) {
		// display alphabet	after spaces
		// should equal the number of columns in the theater
		System.out.print("  ");
		for(int alphabet = 65; alphabet < 65 + columns; alphabet++) {
			System.out.print((char)alphabet);
		}
		System.out.print("\n");

		for(int i = 0; i < rows; i++) {
			System.out.print((i+1) + " ");
			for(int j = 0; j < columns; j++) {
				if (array[i][j] == 'A' || array[i][j] == 'C' || array[i][j] == 'S')
					System.out.print("#"); // if seat is taken
				else
					System.out.print("."); // if seat is open
		}	System.out.println(); // new line for next column
		}
	} // end function displayAuditoriun()
	
	/* make sure there are seats available in the requested row */
	public static boolean seatAvailability(char[][] arr, int request, int row, int startingSeat, int columns) {
		// this for loop checks if every seat from the starting seat to the requested number of reserved seats is empty
		for(int i = startingSeat; i < request + startingSeat; i++) {
			if(arr[row - 1][i] != '.' || (startingSeat+request > columns))
				return false;
		} // end for loop
		return true; // returns true if none of the desired seats are taken
	}
	
	/* this function changes the array to reserve your seats */
	public static void reserveSeats(char[][] arr, int adult, int child, int senior, int row, int startingSeat) {
		// reserve adult seats
		for(int i = 0; i < adult; i++)
			arr[row-1][startingSeat + i] = 'A';
		startingSeat += adult; // move the starting seat forward if reserved adult seats
		// reserve child seats
		for(int i = 0; i < child; i++)
			arr[row-1][startingSeat + i] = 'C';
		startingSeat += child; // move the starting seat forward if reserved child seats
		// reserve senior seats
		for(int i = 0; i < senior; i++)
			arr[row-1][startingSeat + i] = 'S'; 
		startingSeat += senior; // move the starting seat forward if reserved senior seats
			
	}
	
	// this function checks to see if there are better seats available
	public static int betterSeats(char[][] arr, int request, int row, int columns) {
		int bestStartingSeat = -1; 
		double distance = 100, bestDistance = 100; 
		for(int i = 0; i <= columns - request; i++) {
			if(seatAvailability(arr, request, row, i, columns)) {
				distance = Math.abs((columns/2.0) - (request/2.0 + i));
				if(distance < bestDistance) {
					bestStartingSeat = i;
					bestDistance = distance;
				}
			}
		}
		return bestStartingSeat;
	}
	
	public static int mainMenu(Scanner console, char[][] arr, int rows, int columns) {
		int total = 0;
		System.out.println("1. Reserve Seats");
		System.out.println("2. Exit");
		int userOption = console.nextInt();
		while (userOption != 1 && userOption != 2) {
			System.out.println("Please enter valid input");
			System.out.println("1. Reserve Seats");
			System.out.println("2. Exit");
			userOption = console.nextInt();
		}
		int userRow = 0, userStartingSeat = 0, userAdult = -1, userChild = -1, userSenior = -1;
		
		while(userOption != 2) {
			displayAuditorium(arr, rows, columns); // display available seats to purchase
			do { System.out.println("Please enter the row number you would like to reserve: ");
			userRow = console.nextInt();
			} while(userRow <= 0 || userRow > rows);
			do { System.out.println("Please enter the starting seat letter: ");
			userStartingSeat = (int)console.next().charAt(0); // convert seat number to ASCII value
			} while(userStartingSeat < 65 || userStartingSeat >= columns + 65);
			do { System.out.println("Please enter the number of adult tickets you would like to reserve: ");
			userAdult = console.nextInt();
			} while(userAdult < 0);
			do { System.out.println("Please enter the number of child tickets you would like to reserve: ");
			userChild = console.nextInt();
			} while(userChild < 0);
			do { System.out.println("Please enter the number of senior tickets like to reserve: ");
			userSenior = console.nextInt();
			} while(userSenior < 0);
			
			// remember that the starting seat is the ASCII value for the uppercase alphabet
			if (seatAvailability(arr, userAdult + userChild + userSenior, userRow, userStartingSeat-65, columns)) {
				reserveSeats(arr, userAdult, userChild, userSenior, userRow, userStartingSeat - 65); // reserve seats
				total = total + (userAdult * 1000000) + (userChild* 1000) + userSenior; // add to the total number of tickets purchased
				System.out.println("The seats you have requested are available and now reserved!");
				displayAuditorium(arr, rows, columns);
				System.out.println("Please reselect an option");
			}
			// if not available, check if there are better seats available
			else {
				// check starting location of a better available seat
				int betterSeat = betterSeats(arr, userAdult + userChild + userSenior, userRow, columns);
				if(betterSeat != -1) {
					char option;
					System.out.println("Your requested seats are unavailable. Dont fret! There is a better seat option available at starting seat " +
						(char)(betterSeat+65)	+ ". Would you like to reserve these seats? (Y or N): ");
					option = console.next().charAt(0);
					// input validation
					while (option != 'Y' && option != 'N') {
						System.out.println("Please choose a valid option");
						option = console.next().charAt(0);
					}
					// read in char from user
					if(option == 'Y') {
						reserveSeats(arr, userAdult, userChild, userSenior, userRow, betterSeat); // reserve seats, beginning at better seat
						total = total + (userAdult * 1000000) + (userChild* 1000) + userSenior; // add to the total number of tickets purchased
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
			System.out.println("1. Reserve Seats");
			System.out.println("2. Exit");
			userOption = console.nextInt();
			while (userOption != 1 && userOption != 2) {
				System.out.println("Please enter valid input");
				System.out.println("1. Reserve Seats");
				System.out.println("2. Exit");
				userOption = console.nextInt();
			}
		} // exit menu if user selects option 2
		return total;
	} // end function mainMenu()
	

} // end class Main
