package project3;

/**
 * @author Matthew Souvannaphandhu
 * CS310 project 3 section 003
 * StrieNode class
 */
public class StrieNode { 
	// Complete all the methods in this class even if you are not using them for your Strie class implementation
	// You MAY NOT use your own variable for a purpose that has been addressed by one of the private variables provided in this template
	// For example, You may not use your own 'marker' to indicate the end of a word as the provided 'endMarker' variable addresses this purpose
	
	// NOTE: One easy way to convert characters from an alphabet to integer indices is to subtract a character from the the first letter (char) of the alphabet
	// Example from ASCII table: 'a': 97  and 'z': 122. You have a char variable ch. You get an integer if you do: ch - 'a'
	// Example continued: If ch contains 'a', then you get a 0. If  ch contains 'b', you get 1.....and so on.
	
	private static final int NUM_CHILDREN = 26;   // Number of children for each node. Do not change this value.

	private StrieNode[] children = new StrieNode[NUM_CHILDREN];  // Use this array to hold children nodes
	private boolean endMarker;  // Marks the end of a word
	private boolean removed;  // Marks a character/child-node as 'removed' if this node/char denoted the end of a word before and that word has been removed by a remove() operation
	private char data;
	private int size=0;
	private StrieNode parent;	
/*	public String getData() {
		String x = "";				//	used this method for testing purposes
		x += data;
		return x;
	}
	*/	
	/**
	 * constructor
	 */
	public StrieNode(){
		endMarker = false;
		removed = false;
		// constructor
		// initialize anything that needs initialization
	}

	/**
	 * sets the node as removed
	 */
	public void setRemoved(){
		parent.size--;
		removed = true;
		// set the "removed" status 
		// O(1)
		
	}

	/**
	 * unsets the node as removed
	 */
	public void unsetRemoved(){
		parent.size++;
		removed = false;
		//size++;
		// unset the "removed" status
		// O(1)
		
	}
	
	/**
	 * @return true if the node is removed and false if it is not
	 */
	public boolean isRemoved(){
		return removed;
		// returns the 'removed' status of a node
		// O(1)
	}

	/**
	 * @param ch the character that we are dealing with
	 * @return true if node contains a child pertaining to that character
	 */
	public boolean containsChar(char ch){
		if (children[ch - 'a'] != null && !children[ch - 'a'].isRemoved()) return true;
		// returns true if node contains ch
		// O(1)0
		return false;
	}

	/**
	 * @param ch the character that we are dealing with
	 * @return child pertaining to ch, returns null otherwise
	 */
	public StrieNode getChild(char ch){
		if (this.containsChar(ch) == true) 
			return children[ch-'a'];
		// returns the node that contains ch
		// O(1)
		return null;
	}

	/**
	 * @param ch the character that we are dealing with
	 * @param node will be set as a child to the current node
	 */
	public void putChild(char ch, StrieNode node){
		node.data = ch;
		node.parent = this;
		children[ch-'a'] = node;
		size++;
		// sets a a node (that contains ch) to node
		// O(1)	
	}
	
	/**
	 * @return array of the children of the specified node
	 */
	public StrieNode[] getAllChildren(){
		// returns the children of a node
		// O(1)
		return children;
	}
	
	
	/**
	 * @return the amount of children the node has
	 */
	public int getNumChildren(){
		// returns the number of children
		// O(1)	
		return size;
	}

	/**
	 * sets node as and end Node
	 */
	public void setEnd(){
		endMarker = true;
		// Sets the end marker to indicate the end of a string/word
		// O(1)
		
	}
	/**
	 * unsets node as an end node
	 */
	public void unsetEnd(){
		endMarker = false;
		// Unsets a previously set end marker
		// O(1)
	}
	/**
	 * @return true if the node is an end node
	 */
	public boolean isEnd(){
		return endMarker;
		// Checks whether the current node is marked as the end of a string/word
		// O(1)
	}

	/**
	 * @return true if the node is empty, false if it isn't
	 */
	public boolean isEmptyNode(){
		if (size == 0) return true;
		// checks if a node is empty
		// O(1)
		return false;
	}
	//you may add more methods if needed
	//you also don't have to have anything else than what's written
}
