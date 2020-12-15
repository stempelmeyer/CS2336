package Redbox;
//Author:          Sarah G. Tempelmeyer
//Course:          CS 2336.003
//Date:            4/2/2019
//Assignment:      Project 3 - Redbox Inventory System
//Compiler:        Eclipse 2018_12
//Net ID:          sgt170030

//Description: This program demonstrates binary search trees, inheritance, and classes
//			   in java programming

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.*;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		
		// declare scanner variable which reads in each line and open file
		java.io.File file = new java.io.File("inventory5.dat");
		Scanner input = new Scanner(file); 
		input.useDelimiter("\n"); 
		
		// create binary search tree
		BSTree bst = new BSTree();
		
		// read in first line from file to send through while loop
		String line = input.next(); // read in next line from file // or nextLine()
		String [] lineArr;
		ArrayList<String> errorOutput = new ArrayList<>();
		
		boolean flag = true;
		// while able to read in non-empty line from file
		while(line != "" && flag != false) {
			if (!input.hasNext())
				flag = false; // at end of file

			else // remove last endline if not at the end of the file
				line = line.substring(0, line.length()-1);
			lineArr = line.split(",");
			int avail = Integer.valueOf(lineArr[1]);
			int rented = Integer.parseInt(lineArr[2]);
			line = lineArr[0].replace("\"", "");
			
			bst.insert(line, avail, rented);
			
			if(input.hasNext()) 
				line = input.next(); //read in next line
		}
		
		file = new java.io.File("transaction5.log.txt");
		input = new Scanner(file);
		input.useDelimiter("\n");
		
		line = input.next();
		flag = true;
		String action;
		// while able to read in non-empty line from transaction file
		while(line != "" && flag != false) {
			if (!input.hasNext())
				flag = false; // at end of file
			else // remove last endline if not at the end of the file
				line = line.substring(0, line.length()-1);
			
			action = line.substring(0, line.indexOf(' ')); // the first word is the action
			line = line.substring(line.indexOf(' ') + 1); // line after action
			// make sure parenthesis in correct places
			if (validate(line, action)) {
				line = line.replace("\"", ""); // delete quotations
				if(action.equals("add")) {
					lineArr = line.split(",");
					if (bst.search(lineArr[0]) == false) { // create and insert node if adding new title
						bst.insert(lineArr[0], Integer.valueOf(lineArr[1]), 0); 
					}
					else
						bst.transaction(lineArr[0], Integer.valueOf(lineArr[1]), 0);
				}
				else if(action.equals("rent") && bst.search(line)){ // make sure title exists to rent
					bst.transaction(line, -1, 1); // rent 1, which will make available -1
				}
				else if(action.equals("return") && bst.search(line)) { // make sure title exists to return
					bst.transaction(line, 1, -1); // return 1, which will make available +1
				}
				else if(action.equals("remove")) { // make sure title exists to remove
					lineArr = line.split(",");
					if (bst.search(lineArr[0]))
						bst.transaction(lineArr[0], Integer.valueOf(lineArr[1]) * -1, 0);
				}
				else {
					errorOutput.add(action + " " + line);
				}
			} // end if statement (valid line)
			else {
				errorOutput.add(action + " " + line);
			}
			
			if(input.hasNext())
				line = input.next(); //read in next line
		} // end while transation loop
		
		// output to file
		PrintWriter output = new PrintWriter("redbox_kiosk.txt");
		bst.traverseInorder(output);
		output.close();
		
		if(!errorOutput.isEmpty()) {
			output = new PrintWriter("error.log");
			for (String i: errorOutput)
				output.println(i);
			output.close();
		}
	} // end function main
	
	public static boolean validate(String line, String action) {
		
		if(action.equals("rent") && action.equals("remove") && action.equals("add") && action.equals("return"))
			return false;
		else if(line.charAt(0) != '\"')
			return false;
		else if((action.equals("add") || action.equals("remove")) && line.indexOf(',') == -1)
			return false;
		else if((action.equals("add") || action.equals("remove")) && !(Character.isDigit(line.charAt(line.length() - 1))))
			return false;
		else if((action.equals("add") || action.equals("remove")) && !(Character.isDigit(line.charAt(line.indexOf(',') +1))))
			return false;
		
		return true;
	}
}
