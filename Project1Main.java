package DrinkRewards;
// Author:          Sarah G. Tempelmeyer
// Course:          CS 2336.003
// Date:            2/12/2019
// Assignment:      Project 1 - Disneyland Dining Rewards
// Compiler:        Eclipse 2018_12
// Net ID:          sgt170030

// Description: This program uses inheritance, standard input validation
// and polymmorphism to simulate membership upgrades and discounts in purchases. 

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		// declare scanner variable and open file
		File file = new File("preferred.dat");
		Scanner input;
		CustomerBase[] prefArray = new CustomerBase[50];
		// attempt to open preferred.dat file, if cannot then output notification to user
			try {
				input = new Scanner(file);
				// send to function to be parsed, returns the length of the array
				int length = parsePreferred(input, prefArray);
				// create a new trimmed array for preferred customers
				prefArray = trimArray(prefArray, length);
				// close file
				input.close();
			}
			catch (FileNotFoundException ex){
				System.out.println("preferred.dat does not exist");
			}
		
		// open next customer.dat file
		file = new File("customers.dat");
		input = new Scanner(file);
		// if file doesn't exist or is empty
		if(!file.exists()) {
			System.out.println("customers.dat does not exist");
			System.exit(1);
		}
		// declare customer array
		CustomerBase[] custArray = new CustomerBase[50];
		// send to function to be parsed
		int length = parseCustomers(input, custArray); 
		// trim customer array
		custArray = trimArray(custArray, length);
		// close file and attempt to open next orders.dat file
		input.close();
		
		
		file = new File("orders.dat");
		input = new Scanner(file);
		if(!file.exists()) {
			System.out.println("orders.dat does not exist");
			System.exit(1);
		}
		
		// send to function to be parsed
		CustomerBase [][] custPrefArray = parseOrders(input, custArray, prefArray);
		
		custArray = custPrefArray[0];
		prefArray = custPrefArray[1];
		
		// output regular and preferred customers back out to their respective files
		PrintWriter output = new PrintWriter("preferred.dat");
		
		// output to preferred file if array exists
		if(prefArray.length > 0) {
			for (int i = 0; i < prefArray.length  && prefArray[i] != null; i++) {
				if (prefArray[i] instanceof CustomerGold) {
				output.print(prefArray[i].getID() + " " + prefArray[i].getFirstName() + " " + prefArray[i].getLastName() + " ");
				output.printf("%.2f", prefArray[i].getAmount());
				output.println( " " + ((CustomerGold)prefArray[i]).getDiscount() + "%");	
				}
				// if platinum
				else {
					output.print(prefArray[i].getID() + " " + prefArray[i].getFirstName() + " " + prefArray[i].getLastName() + " ");
					output.printf("%.2f", prefArray[i].getAmount());
					output.println( " " + ((CustomerPlat)prefArray[i]).getBonusBucks());	
				}
			}
		}
		output.close();
		
		// output to customer file
		output = new PrintWriter("customers.dat");
		for (int i = 0; i < custArray.length && custArray[i] != null; i++) {
			output.print(custArray[i].getID() + " " + custArray[i].getFirstName() + " " + custArray[i].getLastName() + 
					" "); 
			output.printf("%.2f\n", custArray[i].getAmount());
		}
		output.close();
		
	} // end function Main()

	/* this function parses the preferred data file */
	public static int parsePreferred(Scanner input, CustomerBase[] prefArray) {
		// preferred.dat will read in customerID, firstName, lastName, amountSpent (determines gold or plat), int discount% or bonus bucks
		int discount;
		int i;
		for (i = 0; input.hasNext(); i++) {
		String ID = input.next();
		String firstName = input.next();
		String lastName = input.next();
		double amountSpent = input.nextDouble();
		String discString = input.next();
		if (discString.indexOf('%') != -1) { // they are a gold member
			discount = Integer.parseInt(discString.substring(0,discString.length()-1)); // trim of % at the end of the string
			prefArray[i] = new CustomerGold(firstName, lastName, ID, amountSpent, discount);
		}
		else { // they are a platinum member
			discount = Integer.parseInt(discString);
			prefArray[i] = new CustomerPlat(firstName, lastName, ID, amountSpent, discount);
		 }
		} // continue for loop if the file has more lines
		return i; // return the length of the array
	}
	
	/* this function copies the old preferred array to a new trimmed array*/
	public static CustomerBase[] trimArray(CustomerBase[] oldArray, int length) {
		CustomerBase[] newArray = new CustomerBase[length];
		for (int i = 0; i < newArray.length && oldArray[i] != null; i++) {
			if (oldArray[i] instanceof CustomerPlat) { // create new plat object to copy with constructor parameters
				newArray[i] = new CustomerPlat(oldArray[i].getFirstName(), oldArray[i].getLastName(), oldArray[i].getID(), oldArray[i].getAmount(), ((CustomerPlat)oldArray[i]).getBonusBucks());
			}
			else if (oldArray[i] instanceof CustomerGold) { // create new gold object to copy with constructor parameters
				newArray[i] = new CustomerGold(oldArray[i].getFirstName(), oldArray[i].getLastName(), oldArray[i].getID(), oldArray[i].getAmount(), ((CustomerGold)oldArray[i]).getDiscount());
			}
			else // copy normal by creating base object and sending in old parameters
				newArray[i] = new CustomerBase(oldArray[i].getFirstName(), oldArray[i].getLastName(), oldArray[i].getID(), oldArray[i].getAmount());
		}// end for loop
		return newArray;
	}
	
	/* this function parses the customers data file */
	public static int parseCustomers(Scanner input, CustomerBase[] custArray) {
		// customers.dat will read in customerID, firstName, lastName, amountSpent
		int i;
		for (i = 0; input.hasNext(); i++) {
			String ID = input.next();
			String firstName = input.next();
			String lastName = input.next();
			float amountSpent = input.nextFloat();
			custArray[i] = new CustomerBase(firstName, lastName, ID, amountSpent);
		} // continue for loop if the file has more lines
		
		return i;
	}
	
	/* this function parses the orders data file */
	public static CustomerBase[][] parseOrders(Scanner input, CustomerBase[] custArr, CustomerBase[] prefArr) {
		// orders.dat will read in customerID, size (SML), drinktype (soda,tea, punch), squareinchprice, quantity
		double totalSpent, orderTotal, discountedOrder1, discountedOrder2;
		// send to while loop while the file has information
		while (input.hasNext()) {
		String line = input.nextLine();
		String[] lineArray = line.split("[ ]");
		// send line array to validate function, which will return the index of the matched ID if valid
		int location = valid(lineArray, custArr); 
			if (location > -1) { 
				// declare variable to hold current amount of customer before ordering
				totalSpent = custArr[location].getAmount();
				// calculate order total, and apply first discount (nothing)
				orderTotal = calculateTotalSpent(lineArray[1], lineArray[2], (double)Double.parseDouble(lineArray[3]), (int)Integer.parseInt(lineArray[4]));
				custArr[location].setAmount(totalSpent + orderTotal);
				// send customer to function to see if eligible for upgrade (to gold)
				if(eligibleUpgrade(custArr[location])) {
					// send to function to upgrade customer to gold, creating new arrays for resized customer and preferred array
					CustomerBase [] newPrefArray = new CustomerBase[prefArr.length + 1];
					custArr = upgradeGold(location, custArr, prefArr, newPrefArray);
					// assign preferred array to point at newly added pref array
					prefArr = newPrefArray;
					
					// apply new discounts to this last customer in the preferred array and set new total spent
					discountedOrder1 = applyDiscounts(orderTotal, prefArr[prefArr.length - 1]);
					prefArr[prefArr.length -1].setAmount(totalSpent + discountedOrder1);
					if (totalSpent + discountedOrder1 < 200)
						continue; // member has been upgraded to gold, continue to read in next line of orders
					else { // upgrade member to plat
						prefArr[prefArr.length -1].setAmount(totalSpent + orderTotal); 
						upgradePlat(prefArr.length -1, prefArr); // upgrade gold to plat, and set bonus bucks
						((CustomerPlat)prefArr[prefArr.length -1]).setBonusBucks((int)((totalSpent+orderTotal - 200) / 5));
						continue; 
					}
				}
			}
			// send line array to validate function, which will return the index of the matched ID if valid
			location = valid(lineArray, prefArr);
			if (location > -1) {
				// declare variable to hold current amount of customer before ordering
				totalSpent = prefArr[location].getAmount();
				// calculate order total, and apply first discount
				orderTotal = calculateTotalSpent(lineArray[1], lineArray[2], (double)Double.parseDouble(lineArray[3]), (int)Integer.parseInt(lineArray[4]));
				discountedOrder1 = applyDiscounts(orderTotal, prefArr[location]);
				prefArr[location].setAmount(totalSpent + discountedOrder1); // set amount after first discount
				// send customer to function to see if eligible for upgrade (to platinum)
				if (betterDiscount(prefArr[location])) { // if customer gold not eligible for plat upgrade but needs a better discount
					((CustomerGold)prefArr[location]).setDiscount(); // set new discount
					discountedOrder2 = applyDiscounts(orderTotal, prefArr[location]);
					prefArr[location].setAmount(totalSpent + discountedOrder2); // set amount after second discount
				}
				else if (prefArr[location] instanceof CustomerPlat) { // if platinum
					// bonus bucks = (total spent + ordertotal)%5 - totalspent%5
					((CustomerPlat)prefArr[location]).setBonusBucks((int)((totalSpent+orderTotal)/5) - (int)((totalSpent))/5);
				}
				else if(eligibleUpgrade(custArr[location]) && !(custArr[location] instanceof CustomerPlat)) { // if gold can upgrade to plat
					upgradePlat(location, prefArr); // upgrade gold to plat, and add bonus bucks
					// bonus bucks = (total spent + ordertotal)%5 - totalspent%5
					((CustomerPlat)prefArr[location]).setBonusBucks((int)((totalSpent+orderTotal)/5) - (int)((totalSpent))/5);
				}
			}
			else {
				continue; // none of the ID's matched, if not passed through if statements, ignore the line and read in next 
			}
		} // continue while loop if the file has more lines
		
		CustomerBase[][] custPrefArray = new CustomerBase[2][];
		
		custPrefArray[0] = custArr;
		custPrefArray[1] = prefArr;
		
		return custPrefArray;
	}
	
	/* this function determines what kind of customer they are and if they are eligible for an upgrade based on total spent after current discount */
	public static boolean eligibleUpgrade(CustomerBase customer) {
		if (customer instanceof CustomerGold) { // check if gold can be upgraded to plat
			if (customer.getAmount() >= 200) // after gold discount if spent at least 200 
				return true;
		}
		else if (customer.getAmount() >=50 && !(customer instanceof CustomerPlat)) { // if base customer spent at least 50
			return true;
		}
		return false; // otherwise ineligible for upgrade
	}
	
	public static CustomerBase[] upgradeGold(int index, CustomerBase[] custArray, CustomerBase[] prefArray, CustomerBase[] newPrefArr) {
		// copy same pref array and add Gold member at end
		int length = 0; // length of the customer base, new cust array is one less element and new pref array is one element greater than current
		for (int i = 0; i < prefArray.length && prefArray[i] != null; i++)
			length = i + 1;
		CustomerBase [] newCustArr = new CustomerBase[custArray.length - 1];
		for (int i = 0; i < custArray.length && custArray[i] != null; i++) { // copy old customer array to new one, skipping over remmoved index
			if (i < index) { // copy normal before index
				newCustArr[i] = new CustomerBase(custArray[i].getFirstName(), custArray[i].getLastName(), custArray[i].getID(), custArray[i].getAmount()); 
			}
			if (i > index) { // copy with an offset of one element behind after index
				newCustArr[i-1] = new CustomerBase(custArray[i].getFirstName(), custArray[i].getLastName(), custArray[i].getID(), custArray[i].getAmount()); 
			}
		} // end for loop
		
		for (int i = 0; i < length; i++) {
			if (prefArray[i] instanceof CustomerPlat) {
				newPrefArr[i] =  new CustomerPlat(prefArray[i].getFirstName(), prefArray[i].getLastName(), prefArray[i].getID(), prefArray[i].getAmount(), ((CustomerPlat)prefArray[i]).getBonusBucks()); // copy plat member to array
			}
			else if (prefArray[i] instanceof CustomerGold) {
				newPrefArr[i] = new CustomerGold(prefArray[i].getFirstName(), prefArray[i].getLastName(), prefArray[i].getID(), prefArray[i].getAmount(), ((CustomerGold)prefArray[i]).getDiscount()); // copy gold member to array
			}
		}
		newPrefArr[prefArray.length] = new CustomerGold(custArray[index].getFirstName(), custArray[index].getLastName(), custArray[index].getID(), custArray[index].getAmount(), 0); // copy gold member to array
		((CustomerGold)newPrefArr[prefArray.length]).setDiscount(); // set discount for newly added gold customer

		return newCustArr;
	}
	
	public static boolean betterDiscount(CustomerBase customer) {
		if(customer instanceof CustomerPlat)
			return false;
		else if(((CustomerGold)customer).getDiscount() < 5 && (customer.getAmount() < 100 && customer.getAmount() >= 50))
			return true;
		else if(((CustomerGold)customer).getDiscount() < 10 && (customer.getAmount() < 150 && customer.getAmount() >= 100))
			return true;
		else if(((CustomerGold)customer).getDiscount() < 15 && (customer.getAmount() < 200 && customer.getAmount() >= 150))
			return true;
		return false;
	}
	
	/* upgrade object at gold to a platinum object */
	public static void upgradePlat(int index, CustomerBase[] prefArray) {
		prefArray[index] = new CustomerPlat(prefArray[index].getFirstName(), prefArray[index].getLastName(), prefArray[index].getID(), prefArray[index].getAmount(), 0);
		// change gold to plat
	}
	
	/* this function validates input for order file, returns position of array that ID is found */
	public static int valid(String[] line, CustomerBase[] arraySearch) {
		// validate number of fields
		if(line.length !=5)
			return -1;
		// validate size
		if(!(line[1].toUpperCase()).equals("S") && !(line[1].toUpperCase()).equals("M") && !(line[1].toUpperCase()).equals("L"))
			return -2;
		// validate drink type
		if(!(line[2].toLowerCase()).equals("soda") && !(line[2].toLowerCase()).equals("tea") && !(line[2].toLowerCase()).equals("punch"))
			return -3;
		// make sure sqInchPrice is a double
		try {
			Double.parseDouble(line[3]);
		}
		catch(NumberFormatException nfe) {
			return -4;
		}
		// make sure quantity is an integer
		try {
			Integer.parseInt(line[4]);
		}
		catch(NumberFormatException nfe) {
			return -4;
		}
		// validate that ID exists
		int exists = -5;
		for (int i = 0; i < arraySearch.length; i++) {
			if(arraySearch[i].getID().equals(line[0]))
				exists = i;
		}
			return exists; // will either return -1 for false or the position in which the ID is found in the array
	} // end function valid
	
	/* this function calculates the amount spent by the user */
	public static double calculateTotalSpent(String size, String drinkType, double sqInchPrice, int quantity) {
		double totalSpent = 0;
		
		// price per oz
		if (drinkType.toLowerCase().equals("soda"))
			totalSpent = 0.2;
		else if (drinkType.toLowerCase().equals("tea"))
			totalSpent = 0.12;
		else // equals punch
			totalSpent = 0.15;
		
		// drink sizes in oz
		// personalization cost of drinks 2piRH
		if (size.toLowerCase().equals("s")) {
			totalSpent *= 12.0;
			totalSpent += 2 * Math.PI * (4.0/2) * 4.5 * sqInchPrice;
		}
		else if (size.toLowerCase().equals("m")) {
			totalSpent *= 20.0;
			totalSpent += 2 * Math.PI * (4.5/2) * 5.75 * sqInchPrice;
		}
		else { // equals large
			totalSpent *= 32.0;
			totalSpent += 2 * Math.PI * (5.5/2) * 7 * sqInchPrice;
		}
		totalSpent *= quantity;

		return totalSpent;
	} // end function calculateTotalSpent()
	
	public static double applyDiscounts(double beforeDiscount, CustomerBase customer) {
		if (customer instanceof CustomerGold){
			return beforeDiscount * (1 - ((CustomerGold)customer).getDiscount()/100.0); // put %discount into decimal and apply to total
		}
		else if (customer instanceof CustomerPlat) {
			double returnValue;
			if (((CustomerPlat)customer).getBonusBucks() > beforeDiscount) { // when bonus bucks more than order total so they pay nothing
				returnValue = 0;
				((CustomerPlat)customer).setBonusBucks((int)(((CustomerPlat)customer).getBonusBucks() - beforeDiscount)); 
			}
			else {
			returnValue = beforeDiscount - ((CustomerPlat)customer).getBonusBucks();
			((CustomerPlat)customer).setBonusBucks(0); // about to subtract bonus bucks from total, so set to 0
			}
			return returnValue;
		}
		return 0;
	}
	
}// end main class