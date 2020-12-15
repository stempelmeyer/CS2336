package Redbox;

public class Node<E> {

	// declare members of node
	protected E title;
	int available;
	int rented;
	protected Node<E> left;
	protected Node<E> right;
	
	public Node() {
		title = null;
		available = -1;
		rented = -1;
		left = null;
		right = null;
	}
	
	public Node(E newTitle){
		title = newTitle;
		available = -1;
		rented = -1;
		left = null;
		right = null;
	}
	
	// overloaded constructors
	public Node(E newTitle, int newAvailable, int newRented){
		title = newTitle;
		available = newAvailable;
		rented = newRented;
		left = null;
		right = null;
	}
	
	// declare mutators and accessors
	public String getTitle() {
		return (String)title;
	}
	public void setTitle(E title) {
		this.title = title;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public int getRented() {
		return rented;
	}
	public void setRented(int rented) {
		this.rented = rented;
	}
	public Node<E> getLeft() {
		return left;
	}
	public void setLeft(Node<E> left) {
		this.left = left;
	}
	public Node<E> getRight() {
		return right;
	}
	public void setRight(Node<E> right) {
		this.right = right;
	}
	
	
} // end class node