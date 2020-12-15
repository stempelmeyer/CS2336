package Tickets;

//Author:          Sarah G. Tempelmeyer
//Course:          CS 2336.003
//Date:            4/15/2019
//Assignment:      Project 4 - Avengers 4 Ticket Reservation System (Director's Cut)
//Compiler:        Eclipse 2018_12
//Net ID:          sgt170030

//Description: This program demonstrates hashmaps, inheritance, and classes
//			   in java programming

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.*;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		// declare scanner variable and open file
		Scanner input = null; // create a scanner for the file
		
		Theater theater = new Theater(input, "A1base.txt","A2base.txt","A3base.txt");
		
		System.out.println("Auditorium 1");
		theater.auditorium1.displayAuditorium();
		
		System.out.println("\nAuditorium 2");
		theater.auditorium2.displayAuditorium();
		
		System.out.println("\nAuditorium 3");
		theater.auditorium3.displayAuditorium();
		System.out.println();
		
		// create a map
		MyHashMap<String,String> map = new MyHashMap<>();
		int numUsers = initializeMap(input, "userdb.dat", map);
		
		// create array list of array list for users and their orders
		Order[] userOrders= new Order[numUsers];
		
		for (int i = 0; i < numUsers; i ++) {
			userOrders[i] = new Order();
		}
		
		while(mainMenu(input, theater, map, userOrders)) { // if evaluates to false then the while loop will not check again
			System.out.println("continuing from starting point again");
		}
	
		System.out.println("Done! :)");

	} // end function main
	
	/* this function initializes the user hash map */
	public static int initializeMap(Scanner input, String fileName, MyMap<String,String> map) throws FileNotFoundException {
		java.io.File file = new java.io.File(fileName);
		input = new Scanner(file); // create a scanner for the file
		int numUsers = 0;
		
		// add username to hashmap
		String userN = input.next(); // read in next line from file
		String passW = input.next();
		boolean flag = true;
		while(userN != "" && passW != "" && flag != false) {
			numUsers++;
			map.put(userN, passW);
			
			if (!input.hasNext())
				flag = false;
			if(input.hasNext()) {
				userN = input.next(); //read in next username
				passW = input.next(); // read in next password
			}
		}
		
		// close file
		input.close();
		return numUsers; // return how many users exist
	} // end function initializeMap
	
	/* this function holds the main menu */
	public static boolean mainMenu(Scanner console, Theater theater, MyHashMap<String,String> map, Order[] userOrders) throws FileNotFoundException {
		// initialize scanner for console input and output
		console = new Scanner(System.in);
		int userOption = 0;
		String userNam = null;
		String userPass = null;
		do {
			// if user has tried 3 times, reset number of tries and prompt username again
			if (userOption == 3) { 
				userOption = 0;
				userNam = null;
			}
			// if map doesn't contain username, repeat
			while (map.containsKey(userNam) == false) { 
				System.out.print("Please enter username: ");
				userNam = console.nextLine();
			}
			// prompt user for password
			System.out.print("Please enter password: ");
			userPass = console.nextLine();
			if (map.get(userNam).equals(userPass)) // break from loop if password is correct
				break;
		} while (map.get(userNam) != userPass && userOption++ < 3); // continue if password doesn't match
		
		userOption = 1;
		
		// check if administration
		if(userNam.equals("admin")) {
			do{
				try {
				System.out.println("1. Print Report");
				System.out.println("2. Logout");
				System.out.println("3. Exit");
				userOption = console.nextInt();
				}
				catch (InputMismatchException ex) {
					System.out.println("Please enter a valid input");
					console.nextLine();
					userOption = 0;
				}
			} while (userOption != 1 && userOption != 2 && userOption != 3);
			switch(userOption) {
				// print report
				case 1:
					System.out.println("                Open Seats  Total Reserved Seats  Adult Seats  Senior Seats  Child Seats  Ticket Sales");
					System.out.printf(" Auditorium 1:  %-12d%-22d%-13d%-14d%-13d%.2f\n", (theater.auditorium1.getTotalNumSeats()-theater.auditorium1.getTotalReservedSeats()), theater.auditorium1.getTotalReservedSeats(), theater.auditorium1.getAdult(), theater.auditorium1.getSenior(), theater.auditorium1.getChild(), theater.auditorium1.getTotalSales());
					System.out.printf(" Auditorium 2:  %-12d%-22d%-13d%-14d%-13d%.2f\n", (theater.auditorium2.getTotalNumSeats()-theater.auditorium2.getTotalReservedSeats()), theater.auditorium2.getTotalReservedSeats(), theater.auditorium2.getAdult(), theater.auditorium2.getSenior(), theater.auditorium2.getChild(), theater.auditorium2.getTotalSales());
					System.out.printf(" Auditorium 3:  %-12d%-22d%-13d%-14d%-13d%.2f\n", (theater.auditorium3.getTotalNumSeats()-theater.auditorium3.getTotalReservedSeats()), theater.auditorium3.getTotalReservedSeats(), theater.auditorium3.getAdult(), theater.auditorium3.getSenior(), theater.auditorium3.getChild(), theater.auditorium3.getTotalSales());
					System.out.printf(" Total       :  %-12d%-22d%-13d%-14d%-13d%.2f\n", (theater.getTotalSeats()-theater.getReserved()), theater.getReserved(), theater.getTotAdults(), theater.getTotSeniors(), theater.getTotChildren(), theater.getTotSales());
					break;
				// logout: return to starting point
				case 2:
					console.close();
					return true;
				// exit: and then end program
				case 3:
					outputToFiles(theater, "A1.txt", "A2.txt", "A3.txt");
					console.close();
					return false;
			}
		}
		// normal customer menu
		else {
			while (userOption != 5) {
				do{
					try {
					System.out.println("1. Reserve Seats");
					System.out.println("2. View Orders");
					System.out.println("3. Update Order");
					System.out.println("4. Display Receipt");
					System.out.println("5. Log Out");
					userOption = console.nextInt();
					}
					catch (InputMismatchException ex) {
						System.out.println("Please enter a valid input");
						console.nextLine();
						userOption = 0;
					}
				} while (userOption > 5 || userOption < 1);
							
				
				switch(userOption) {
				case 1: // reserve seats
					do{
						try {
						System.out.println("1. Auditorium 1");
						System.out.println("2. Auditorium 2");
						System.out.println("3. Auditorium 3");
						userOption = console.nextInt();
						}
						catch (InputMismatchException ex) {
							System.out.println("Please enter a valid input");
							console.nextLine();
						}
					} while (userOption > 3 && userOption < 1);
					
					// reserve based on auditorium chosen
					if (userOption == 1) 
						reservationMenu(console, theater.auditorium1, true, userOrders[map.indexOfKey(userNam)], 0);
					else if(userOption == 2)
						reservationMenu(console, theater.auditorium2, true, userOrders[map.indexOfKey(userNam)], 1);
					else if(userOption == 3)
						reservationMenu(console, theater.auditorium3, true, userOrders[map.indexOfKey(userNam)], 2);
					continue; // go back to while loop for main menu reservation
				case 2: // view orders
					for(int i = 0; i < 3; i++) {
						if (userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].size() != 0) {
						// display order which consists of auditorium, seats, number of tickets per type
						System.out.println("Order " + (i+1));
						userOrders[map.indexOfKey(userNam)].displayOrder(i);
						System.out.println();
						}
					}
					break;
				case 3: // update order
					boolean flag = true;
					// keeps iterating through menu unless flag triggered
					while (flag) { 
						// display orders (consists of auditorium, seats, number of tickets per type)
						int selectedOrder = 0;
						int i = 0;
						if (userOrders[map.indexOfKey(userNam)].auditoriumOrders[0].size() + userOrders[map.indexOfKey(userNam)].auditoriumOrders[1].size() + userOrders[map.indexOfKey(userNam)].auditoriumOrders[2].size() == 0) {
							System.out.println("There are no orders to update");
							flag = false;
							break; // break from switch case statement
						}
						// and ask user to select an order to update
						int k = 1;
						int aud1 = 0;
						int aud2 = 0;
						int aud3 = 0;
						for(i = 0; i < 3; i++) {
							// display order which consists of auditorium, seats, number of tickets per type... if orders exist
							if (userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].size() != 0) {
								System.out.println("Order " + k);
								userOrders[map.indexOfKey(userNam)].displayOrder(i);
								System.out.println();
								k++; 
								if (i == 0) {
									aud1++; 
									aud2++; 
									aud3++;
								}
								else if (i ==1) {
									aud2++; 
									aud3++;
								}
								else{ // i==3
									aud3++;
								}
							}
						}
						do {
							try {
							System.out.println("Please select an order to adjust");
							selectedOrder = console.nextInt();
							}
							catch (InputMismatchException ex) {
								System.out.println("Please enter a valid input");
								console.nextLine();
							}
							// if order is empty or not an option 1-3
						} while(selectedOrder < 1 || selectedOrder > 3 && userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].size() == 0);
						
						if (aud1 == selectedOrder)
							selectedOrder = 1;
						else if(aud2 == selectedOrder)
							selectedOrder = 2;
						else if(aud3 == selectedOrder)
							selectedOrder = 3;
						
						do{
							try {
							System.out.println("1. Add tickets to order");
							System.out.println("2. Delete tickets from order");
							System.out.println("3. Cancel Order");
							userOption = console.nextInt();
							}
							catch (InputMismatchException ex) {
								System.out.println("Please enter a valid input");
								console.nextLine();
								userOption = 0;
							}
						} while (userOption > 3 || userOption < 1);
						if (userOption == 1) { // add tickets to order of corresponding auditorium chosen
							if (selectedOrder == 1) 
								reservationMenu(console, theater.auditorium1, false, userOrders[map.indexOfKey(userNam)], 0);
							else if (selectedOrder == 2)
								reservationMenu(console, theater.auditorium2, false, userOrders[map.indexOfKey(userNam)], 1);
							else if (selectedOrder == 3)
								reservationMenu(console, theater.auditorium3, false, userOrders[map.indexOfKey(userNam)], 2);
							flag = false;
						}
						else if (userOption == 2) { // delete tickets from order
							// ask row and seat number 
							int selectedRow = 0;
							char selectedSeat = 65;
							do { 
								try{
									System.out.println("Please enter the row number you would like to delete: ");
									selectedRow = console.nextInt();
								}
								catch (InputMismatchException ex) {
									System.out.println("Please enter valid input");
									console.nextLine();
								}
							} while(selectedRow <= 0);
							do { 
								try{
									System.out.println("Please enter the starting seat letter: ");
									selectedSeat = console.next().charAt(0); // convert seat number to ASCII value
								}
								catch (InputMismatchException ex) {
									System.out.println("Please enter a valid input");
									console.nextLine();
								}
							} while(selectedSeat < 'A' || selectedSeat > 'Z');
							
							// check if seat is unavailable and make available
							if (selectedOrder == 1) {
								System.out.println(userOrders[map.indexOfKey(userNam)].contains(selectedRow, selectedSeat));
								if (seatAvailability(theater.auditorium1, 1, selectedRow, selectedSeat) == false && userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder-1].size() != 0 && userOrders[map.indexOfKey(userNam)].contains(selectedRow, selectedSeat) != -1) 
									deleteTickets(theater.auditorium1, userOrders[map.indexOfKey(userNam)], selectedRow, selectedSeat, selectedOrder);
								else { // seats are invalid
									System.out.println("Seat invalid to delete");
									continue; // continue to update orders submenu
								}
							}
							else if (selectedOrder == 2) {
								if (seatAvailability(theater.auditorium2, 1, selectedRow, selectedSeat) == false && userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder-1].size() != 0 && userOrders[map.indexOfKey(userNam)].contains(selectedRow, selectedSeat) != -1) 
									deleteTickets(theater.auditorium2, userOrders[map.indexOfKey(userNam)], selectedRow, selectedSeat, selectedOrder);
								else { // seats are invalid
									System.out.println("Seat invalid to delete");
									continue; // continue to update orders submenu
								}
							}
							else if (selectedOrder == 3) {
								if (seatAvailability(theater.auditorium3, 1, selectedRow, selectedSeat) == false && userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder-1].size() != 0 && userOrders[map.indexOfKey(userNam)].contains(selectedRow, selectedSeat) != -1) 
									deleteTickets(theater.auditorium3, userOrders[map.indexOfKey(userNam)], selectedRow, selectedSeat, selectedOrder);
								else { // seats are invalid
									System.out.println("Seat invalid to delete");
									continue; // continue to update orders submenu
								}
							}
							
							// if you have removed all the tickets in the order, delete the order
							if (userOrders[map.indexOfKey(userNam)].getAdultTix(selectedOrder - 1) + userOrders[map.indexOfKey(userNam)].getChildTix(selectedOrder - 1) + userOrders[map.indexOfKey(userNam)].getSeniorTix(selectedOrder - 1) == 0)
								userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].clear();
							flag = false;
						}
						else { // user option is 3 and cancel order
							// change seats to available and decrease number in auditorium
								
							// deletes each individal seat in the for loop
							for (int j = 0; j < userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].size(); j++) {
								if (selectedOrder == 1)
									deleteTickets(theater.auditorium1, userOrders[map.indexOfKey(userNam)], userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getRow(), userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getSeat(), selectedOrder);
								else if (selectedOrder == 2)
									deleteTickets(theater.auditorium1, userOrders[map.indexOfKey(userNam)], userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getRow(), userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getSeat(), selectedOrder);
								else if (selectedOrder == 3)
									deleteTickets(theater.auditorium1, userOrders[map.indexOfKey(userNam)], userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getRow(), userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].get(j).getSeat(), selectedOrder);
							}
							userOrders[map.indexOfKey(userNam)].setAdultTix(selectedOrder-1, 0);
							userOrders[map.indexOfKey(userNam)].setChildTix(selectedOrder-1, 0);
							userOrders[map.indexOfKey(userNam)].setSeniorTix(selectedOrder-1, 0);
							
							userOrders[map.indexOfKey(userNam)].auditoriumOrders[selectedOrder - 1].clear();
						
						flag = false;
						}
					}
					break;
				case 4: // display receipt
					int totalAdultSeats = 0;
					int totalSeniorSeats = 0;
					int totalChildSeats = 0;
					double customerTotal = 0;
					System.out.println("                Auditorium  Row and seat  Adult Seats  Senior Seats  Child Seats  Order Total");
					for(int i = 0; i < 3; i++) {
						if (userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].size() != 0) {
							// display seat row and column numbers
							System.out.printf("Order  " + (i+1) + ":        %-25d%-13d%-14d%-13d%.2f\n", (i+1), userOrders[map.indexOfKey(userNam)].getAdultTix(i), userOrders[map.indexOfKey(userNam)].getSeniorTix(i), userOrders[map.indexOfKey(userNam)].getChildTix(i), userOrders[map.indexOfKey(userNam)].orderTotal(i));
							for (int j= 0; j < userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].size(); j++) {
								System.out.println("                            " + userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].get(j).getRow() + " " + userOrders[map.indexOfKey(userNam)].auditoriumOrders[i].get(j).getSeat());
							}
							totalAdultSeats += userOrders[map.indexOfKey(userNam)].getAdultTix(i);
							totalSeniorSeats += userOrders[map.indexOfKey(userNam)].getSeniorTix(i);
							totalChildSeats += userOrders[map.indexOfKey(userNam)].getChildTix(i);
							customerTotal = userOrders[map.indexOfKey(userNam)].orderTotal(0) + userOrders[map.indexOfKey(userNam)].orderTotal(1) + userOrders[map.indexOfKey(userNam)].orderTotal(2);
						}
					}
					System.out.printf("Order Totals   : %-25C%-13d%-14d%-13d%.2f\n", ' ', totalAdultSeats, totalSeniorSeats, totalChildSeats, customerTotal);
					break;
				case 5:
					return true; // returns to starting point
				} // end switch statement
				userOption = 0;
			} // end while loop
		}
		
		return false;
	} // end function main menu
	
	public static void reservationMenu(Scanner console, Auditorium auditorium, boolean implementBetterSeat, Order specificOrder, int numAud) {
		int userRow = 0, userAdult = -1, userChild = -1, userSenior = -1;
		char userStartingSeat = '-';

			auditorium.displayAuditorium(); // display available seats to purchase
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
				return;
			}
			
			// remember that the starting seat is the ASCII value for the uppercase alphabet
			if (seatAvailability(auditorium, userAdult + userChild + userSenior, userRow, userStartingSeat)) {
				reserveSeats(auditorium, userAdult, userChild, userSenior, userRow, userStartingSeat, specificOrder, numAud); // reserve seats
				System.out.println("The seats you have requested are available and now reserved!");
				auditorium.displayAuditorium();
				// update orders for the user
					
				System.out.println("Please reselect an option");
			}
			// if not available and not updating an order, check if there are better seats available
			else if (implementBetterSeat){
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
						reserveSeats(auditorium, userAdult, userChild, userSenior, betterRow, betterSeat, specificOrder, numAud); // reserve seats, beginning at better seat
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
			else { // function called by update order and there arent selected seats, dont offer better
				System.out.println("There are not " + (userAdult + userChild + userSenior) + " seats available in row " +
						userRow);
			}
	}
	
	public static void outputToFiles(Theater theater, String fileName1, String fileName2, String fileName3) throws FileNotFoundException{
		// declare printwriter variable and open file
		java.io.File file1 = new java.io.File(fileName1);
		PrintWriter output = new PrintWriter(file1);
		//output auditorium to file
		TheaterSeat ptr = theater.auditorium1.getHead();
		TheaterSeat rowHead = theater.auditorium1.getHead();
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
		
		java.io.File file2 = new java.io.File(fileName2);
		output = new PrintWriter(file2);
		ptr = theater.auditorium2.getHead();
		rowHead = theater.auditorium2.getHead();
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
		
		java.io.File file3 = new java.io.File(fileName3);
		output = new PrintWriter(file3);
		ptr = theater.auditorium3.getHead();
		rowHead = theater.auditorium3.getHead();
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
	}
	
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
	public static void reserveSeats(Auditorium auditorium, int adult, int child, int senior, int row, char startingSeat,  Order specificOrder, int numAud) {
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
			specificOrder.auditoriumOrders[numAud].add(new BaseNode(row, (char)(startingSeat + i), true, 'A'));	
		}
		startingSeat = (char)((int)startingSeat + adult); // move the starting seat forward if reserved adult seats
		// reserve child seats
		for(int i = 0; i < child; i++) {
			ptr.setTicketType('C');
			ptr.setReserved(true);
			ptr = ptr.getRight();
			specificOrder.auditoriumOrders[numAud].add(new BaseNode(row, (char)(startingSeat + i), true, 'C'));	
		}
		startingSeat = (char)((int)startingSeat + child); // move the starting seat forward if reserved child seats
		// reserve senior seats
		for(int i = 0; i < senior; i++){
			ptr.setTicketType('S');
			ptr.setReserved(true);
			ptr = ptr.getRight();
			specificOrder.auditoriumOrders[numAud].add(new BaseNode(row, (char)(startingSeat + i), true, 'S'));	
		} 
		startingSeat = (char)((int)startingSeat + senior); // move the starting seat forward if reserved senior seats		
		
		specificOrder.setAdultTix(numAud, specificOrder.getAdultTix(numAud)+adult);	
		specificOrder.setChildTix(numAud, specificOrder.getChildTix(numAud)+child);
		specificOrder.setSeniorTix(numAud, specificOrder.getSeniorTix(numAud)+senior);
		
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
					distance = Math.sqrt(Math.pow((auditorium.getRows()+1)/2.0 - (i+1), 2)  +  Math.pow((auditorium.getColumns()+1)/2.0 - ((j+1)+ (request-1)/2.0), 2));
					
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
	
	public static void deleteTickets(Auditorium auditorium, Order audOrders, int row, char seat, int selectedOrder) { 
		TheaterSeat ptr = auditorium.getHead();
		for(int rowPtr = 1; rowPtr < row; rowPtr++) // this for loop moves pointer to the correct row
			ptr = ptr.getDown();
		if (ptr == null)
			return;
		while(ptr.getSeat() != seat) { // this for loop moves pointer to the correct starting seat
			ptr = ptr.getRight();
		}
		
		if (ptr.getTicketType() == 'A') {
			auditorium.setNumAdult(auditorium.getAdult() - 1);
			audOrders.auditoriumOrders[selectedOrder - 1].remove(audOrders.contains(row, seat));
			audOrders.setAdultTix(selectedOrder-1, audOrders.getAdultTix(selectedOrder - 1) - 1);
		}
		else if (ptr.getTicketType() == 'C') {
			auditorium.setNumChild(auditorium.getChild() - 1);
			audOrders.auditoriumOrders[selectedOrder - 1].remove(audOrders.contains(row, seat));
			audOrders.setChildTix(selectedOrder-1, audOrders.getChildTix(selectedOrder - 1) - 1);
		}
		else if (ptr.getTicketType() == 'S') {
			auditorium.setNumSenior(auditorium.getSenior() - 1);
			audOrders.auditoriumOrders[selectedOrder - 1].remove(audOrders.contains(row, seat));
			audOrders.setSeniorTix(selectedOrder-1, audOrders.getSeniorTix(selectedOrder - 1) - 1);
		}
		
		ptr.setTicketType('.');
		ptr.setReserved(false);
		
	}
	
} // end class main
