package Redbox;
import java.io.PrintWriter;

public class BSTree<E extends Comparable<E>> { // must use generics
	
	// declare members of the binary search tree
	private Node<E> root = null;
	private int maxLength = 0;

	// constructor
	public BSTree() {
	}
	
	// declare mutator and acessor
	public Node<E> getRoot() {
		return root;
	}

	public void setRoot(Node<E> root) {
		this.root = root;
	}

	// recursive insertion for title within the tree
	public boolean insert(E addedTitle, int available, int rented) {
		// create root if none exists
		if (root == null) {
			root = createNewNode(addedTitle, available, rented); 
			return true;
		}
		else {
			Node<E> parent = null;
			Node<E> current = root;
			// create new node and attach to parent node
			return insertRecursive(addedTitle, current, parent, available, rented);
		}
	} // end insert method
	
	public boolean insertRecursive(E addedTitle, Node<E> current, Node<E> parent, int available, int rented) {
		if(current == null) { // insert a new node
			if (addedTitle.compareTo(parent.title) < 0) // added title is attached to left
				parent.left = createNewNode(addedTitle, available, rented);
			else if((addedTitle.compareTo(parent.title) > 0)) // added title is attached to right
				parent.right = createNewNode(addedTitle, available, rented);
			if(((String)addedTitle).length() > maxLength) // check if title length is the longest
				maxLength = ((String)addedTitle).length();
			return true;
		}
		else if(addedTitle.compareTo(current.title) < 0) { // if title is less than, add to the left
			insertRecursive(addedTitle, current.left, current, available, rented);
		}
		else if(addedTitle.compareTo(current.title) > 0) { // if title is greater than, add to the right
			insertRecursive(addedTitle, current.right, current, available, rented);
		}
		else { // duplicate node is not inserted
			return false;
		}
		return false;
	}
	
	public void transaction(E title, int avail, int rented) {
		// create root if none exists
		if (root == null) {
			return;
		}
		else {
			Node<E> current = root;
			while (current.left != null || current.right != null) {
				// locate parent node
				if(title.compareTo(current.title) < 0) // if title is less than, add to the left
					current = current.left;
				else if(title.compareTo(current.title) > 0) // if title is greater than, add to the right
					current = current.right;
				else if(title.equals(current.title)) { // found node, update available and rented
					break;
				}
				else 
					return; // title does not match
			} // end while loop
			current.setAvailable(current.getAvailable() + avail);
			current.setRented(current.getRented() + rented);
			if(current.getAvailable() + current.getRented() ==  0) // if none available and none rented
				delete(title);
		}
	}
	
	// recursive search for title within tree
	public boolean search(E searchTitle) {
		Node<E> current = root;
		return searchRecursive(searchTitle, current);
	}
	
	public boolean searchRecursive(E searchTitle, Node<E> current) {
		if (current == null)
			return false;
		else if(searchTitle.compareTo(current.title) < 0) { // if title is less than, search to the left
			return searchRecursive(searchTitle, current.left);
		}
		else if(searchTitle.compareTo(current.title) > 0) { // if title is less than, search to the right
			return searchRecursive(searchTitle, current.right);
		}
		else
			return true;
	}
	
	// deletes node from the tree
	public boolean delete(E deleteTitle) {
		Node<E> parent = null;
		Node<E> current = root;
		while (current != null) {
			if(deleteTitle.compareTo(current.title) < 0) {
				parent = current;
				current = current.left;
			}
			else if(deleteTitle.compareTo(current.title) > 0) {
				parent = current;
				current = current.right;
			}
			else
				break; // current now points at element
		} // end while loop
		
		// element is not found in tree
		if (current == null)
			return false; 
		
		// CASE 1: current has no left child
		if (current.left == null) {
			// connect parent with right child of current node
			if(parent == null) // want to delete root
				root = current.right;
			else {
				if(deleteTitle.compareTo(parent.title) < 0)
					parent.left = current.right;
				else
					parent.right = current.right;
			}
		}
		else {
		// CASE 2: current node has left child
		// locate rightmost node of left subtree and it's parent
		Node<E> parentOfRightMost = current;
		Node<E> rightMost = current.left;
		
		// keep moving until find right most
		while(rightMost.right != null) {
			parentOfRightMost = rightMost;
			rightMost = rightMost.right;
		}
		
		// replace current element with rightmost element
		current.setTitle((E)rightMost.getTitle());
		current.setAvailable(rightMost.getAvailable());
		current.setRented(rightMost.getRented());
		
		// eliminate rightmost node
		if (parentOfRightMost.right == rightMost)
			parentOfRightMost.right = rightMost.left;
		else
			parentOfRightMost.left = rightMost.left;
		}
		return true;
	}
	
	protected Node<E> createNewNode(E newTitle, int available, int rented) {
		return new Node<E>(newTitle, available, rented);
	}
	
	public void traverseInorder(PrintWriter output){
		output.printf("%-30s %s\t%s\n", "Title" , "Available", "Rented");
		output.println();
		inorder(output, root);
	}
	
	private void inorder(PrintWriter output, Node<E> curRoot) {
		if (curRoot == null)
			return;
		inorder(output, curRoot.left);
		output.printf("%-30s\t%d \t\t%d", curRoot.getTitle(), curRoot.getAvailable(), curRoot.getRented());
		output.println();
		inorder(output, curRoot.right);
	}
	
} // end class BSTree