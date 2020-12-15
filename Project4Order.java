package Tickets;

import java.util.ArrayList;

public class Order {
	// define members
	ArrayList <BaseNode> [] auditoriumOrders = new ArrayList[3];
	// adult, child, senior ticket amounts for auditorium 1,2,3
	int[][] ticketAmounts = new int [3][3];
	
	// declare getters and setters
	Order() {
		for (int i = 0; i < 3; i++) {
			auditoriumOrders[i] = new ArrayList();
		}
	}
	
	public int getAdultTix(int aud) {
		return ticketAmounts[aud][0];
	}
	public void setAdultTix(int aud, int adultTix) {
		ticketAmounts[aud][0] = adultTix;
	}
	public int getChildTix(int aud) {
		return ticketAmounts[aud][1];
	}
	public void setChildTix(int aud, int childTix) {
		ticketAmounts[aud][1] = childTix;
	}
	public int getSeniorTix(int aud) {
		return ticketAmounts[aud][2];
	}
	public void setSeniorTix(int aud, int seniorTix) {
		ticketAmounts[aud][2] = seniorTix;
	}
	
	public void displayOrder(int numAud) {
			if (getAdultTix(numAud) + getChildTix(numAud) + getSeniorTix(numAud) == 0 || auditoriumOrders[numAud].size() == 0)
				return;
			System.out.println("Auditorium: " + (numAud+1));
			System.out.println("Seats: ");
			
			// display seat row and column numbers
			for (int j= 0; j < auditoriumOrders[numAud].size(); j++) {
					System.out.println(auditoriumOrders[numAud].get(j).getRow() + " " + auditoriumOrders[numAud].get(j).getSeat());
			}
			
		System.out.println("Number of tickets per type (adult, child, senior): " + getAdultTix(numAud) + " " + getChildTix(numAud) + " " + getSeniorTix(numAud));
	}
	
	public int contains(int row, char seat) {
		for (int i = 0; i < 3; i ++) {
			for (int j= 0; j < auditoriumOrders[i].size(); j++) {
					if (auditoriumOrders[i].get(j).getRow() == row && auditoriumOrders[i].get(j).getSeat() == seat)
						return j;
			}
		}
		return -1;
	}
	
	public double orderTotal(int aud) {
		return ticketAmounts[aud][0]*10 + ticketAmounts[aud][1]*5 + ticketAmounts[aud][2]*7.50;
	}
	
}
