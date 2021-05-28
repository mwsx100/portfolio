package project3;
import java.util.ArrayList;  // Use ArrayList, if you must, ONLY LOCALLY

/**
 * @author mwsx1
 *
 */
public class Strie{
	// Do NOT remove any method even if you are not implementing it
	
	private StrieNode root;  // the root of a strie
	private StrieNode current;
	private int size;
	
	public Strie(){
		root = new StrieNode();
		current = root;
		// constructor
		// initialize anything that needs initialization
	}
	
	public static boolean isEmptyStrie(Strie myStrie){
		// returns true if myStrie is empty
		// O(1)
		return myStrie.root.isEmptyNode();
	}

	public void insert(String word){
		if (!isValid(word)) throw new RuntimeException("Invalid character entered. Characters must be between 'a' and 'z' ");
		if (word.length() <= 1) { //base case
			if (current.containsChar(fC(word))== false) {	//if the letter isn't in Strie
				current.putChild(fC(word), new StrieNode());	//put it there
				size++;											//increment size
			}
			current = current.getChild(fC(word));			//current points to the child with the letter now
			current.setEnd();								//this node gets set as an end node
			current = root;									//current gets set to point at root
			return;
		}
		
		
		if (current.containsChar(fC(word))== false) { //if there isn't a letter there yet
			current.putChild(fC(word), new StrieNode()); //put one there
			size++;
			step(word);  //recursive call
			return;
		}
		
		if (current.containsChar(fC(word))==true) {
			step(word);
			return;
			
		}
		
		// inserts word into Strie. 
		// word must contain characters between 'a' and 'z'. If not, throw RuntimeException with message "Invalid character entered. Characters must be between 'a' and 'z' "
		// O(n), where n is the number of characters in word
		
	}

	private void step(String word) {
		current = current.getChild(fC(word));	//current gets the child pertaining to the first letter of the word 
		insert(word.substring(1));				//calls the insert method again with the word having its first letter cut off
	}

	public boolean contains(String word){
		if (!isValid(word)) return false; 
		if (isEmptyStrie(this) == true) return false; //if strie is empty return false
		
		if (word.length() <= 1) {   //base case when we get to the end of the string
			if (current.isEmptyNode()) {	
				current = root;
				return false;
			}
			if (current.containsChar(fC(word)) == true) { 
				if (!current.getChild(fC(word)).isEnd()) {
					current = root;
					return false;
				}
				current = root;   //current references root now
				return true;
			}
			else {
				current = root;
				return false;
			}
		}
		if (current.containsChar(fC(word)) == true) {
			current = current.getChild(fC(word));
			return contains(word.substring(1)); //cuts off the first letter of the word and puts it in the contains method
		}								//recursive call
		else {
			current = root;
			return false;
		}	
		// returns true if Strie contains word
		// word must contain characters between 'a' and 'z', otherwise, returns false.
		// O(n), where n is the number of characters in word	
		//return false;
	}

	public boolean remove(String word){
		if (!isValid(word)) return false; 
		StrieNode placeHolder = new StrieNode();
		placeHolder = current;
		if (this.contains(word) == false) {  //contains() changes where current is pointing
			return false;
		}
		current = placeHolder;    //the placeHolder puts current back to where it was before
		if(word.length() <= 1) {      //base case
			current.getChild(fC(word)).unsetEnd();   //unsets this node as an endmarker
			if (current.getChild(fC(word)).isEmptyNode() == false) return true; //if the node is part of another word return true
			current.getChild(fC(word)).setRemoved(); //setRemove for node if it isnt part of another word
			return true;
		}
			
		if(word.length() > 1) {   
			StrieNode node = new StrieNode();
			node = current; 						//placeholder node
			current = current.getChild(fC(word));   //current pointer moves 1 step deeper
			remove(word.substring(1));          //recursive call
			if (current == root) {           //if current is set to root we are done
				return true;
			}
			else if (current.isEnd()){        //if we run into an endmarker for another word
				current = root;			//set current to root and return
				return true;
			}
			else if (current != root && current.isEmptyNode() && !current.isEnd()) { //if current node isn't part of any other words
				current.setRemoved(); 						//we remove that node
				current = node;     		//current points to the parent node now
				if (current == root) return true;
			}
			else {
				current = root; //not sure if this code is nescessary but it hasn't been hurting me so I'm leaving it here for now
				return true;
			}
		}	
		// removes word from Strie
		// word must contain characters between 'a' and 'z', otherwise, returns false.
		// consider using recursion, might be helpful for this method
		return false;
	}
	
	
	private boolean isValid(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) > 122 || word.charAt(i) < 97) {
				return false;
			}
		}
		
		return true;
	}

	private char fC(String word) { //fC stands for first character
		return word.charAt(0);
	}


	public static String levelOrderTraversal(Strie myStrie){
		if (myStrie.root.isEmptyNode()) return "";		//gives an empty string if strie is empty
		StrieNode[] level = myStrie.root.getAllChildren();	//gives children of the root
		ArrayList<StrieNode> strieQ = new ArrayList<StrieNode>();	//a queue to hold strieNodes
		Character a = 'a';							//helps convert int to char
		String levelOrder = "";					//String to be returned, currently empty
		for (int i = 0; i < 26; i++) {			//loops through the children array
			if (level[i] != null) {				// if the child node isn't null
				char letter = toCh(a, i);			//makes the letter pertaining to the child
				if (!strieQ.contains(level[i])) strieQ.add(level[i]); //appends the child node
				levelOrder += letter+" ";					//adds child letter to the string
				
			}
		}
		for(int i = 0; i < myStrie.size; i++) {				//loops through the strie
			if (strieQ.get(i).isEmptyNode()) continue;		//if node in mind is empty go to next iteration
			else {
				StrieNode[] child = strieQ.get(i).getAllChildren();	//returns the children of current child node
				for (int j = 0; j < child.length; j++) {			//loops through the grandchildren array
					if (!strieQ.contains(child[j]) && child[j] != null) {	//if the grandchild isn't in the queue and isn't null
						char letter = toCh(a, j);
						strieQ.add(child[j]);								//appends the grandchild
						levelOrder += letter+" ";							//adds letter to the string
					}												//since grandchild is appended to the end of strieQ, it will be checked in the same loop
				}		
			}	
		}	
		return levelOrder;
	}

	private static char toCh(Character a, int i) {
		int data = i + a.charValue();	
		char letter = (char)data;
		return letter;
	}

	public static String[] getStrieWords(Strie myStrie){
		if (isEmptyStrie(myStrie)) throw new RuntimeException("Strie is empty");
		ArrayList<Integer> indexs = new ArrayList<Integer>(); //stores indexes representing the letters
		ArrayList<String> dict = new ArrayList<String>(); //stores the words themselves
		StrieNode[] child = myStrie.current.getAllChildren(); //children of the current node
		Character a = 'a'; //helps converts ints to their charValue
		for (int i = 0; i < 26; i++) {
			if (child[i] != null) {			//loops to grab all the children in the node
				indexs.add(i);				//adds all children indexs that are not null
			}
		}
		
		for (int i = 0; i< indexs.size(); i++) {   //loops through children obtained from previous loop
			int data = indexs.get(i) + a.charValue(); 
			char letter = (char)data;					//makes the current letter pertaining to the child node we are looking at
			if (child[indexs.get(i)].isEmptyNode() && !child[indexs.get(i)].isRemoved()) { //if the child letter is an empty node
				dict.add(letter + "");				//append it to the list				
				continue;							//increment i and jump to the beginning of the loop
			}
			if (!child[indexs.get(i)].isEmptyNode() && !child[indexs.get(i)].isRemoved()) { //if the child letter has children
				StrieNode placeHolder = new StrieNode(); //we make a placeholder here
				placeHolder = myStrie.current;				//placeHolder points to current value
				myStrie.current = myStrie.current.getChild(letter); //current now points to the child node with the letter
				if (myStrie.current.isEnd()) {  //if this is an end node
					dict.add(letter+"");		//append the letter to the list so it can show up before its children
				}
				String[] childArr = getStrieWords(myStrie);  //gives us an array containing the segments of the word that come after the current letter
				for (int j = 0; j < childArr.length; j++) {  //we loop through the childArr
					dict.add(letter +childArr[j]);			//we append the letter + the rest of the word to the list
				}
				myStrie.current = placeHolder;				//current returns to its previous value before the recursion
			}	
		}
					
		String[] words = new String[dict.size()];  //finally we make the array to return
		
		for(int i = 0; i< dict.size(); i++) {
			words[i] = dict.get(i);					//spit out all the words we picked up so far
		}	
		return words;
	}
		// returns all words currently stored in myStrie
		// if myStrie is empty, throw RuntimeException with message "Strie is empty"
		// returns words in alphabetical order
		// Example: myStrie contains: bat, cat, bar, barn. Returns: [bar, barn, bat, cat] 
		// recursion might be helpful for this method
		
		
	

	public static String[] getAllSuffixes(Strie myStrie, String query){
		// returns from myStrie all words with prefix query
		// query must contain characters between 'a' and 'z'
		// otherwise, throw RuntimeException with message "Invalid character entered. Characters must be between 'a' and 'z' " 
		// if no word is found in myStrie for the given prefix query, throw RuntimeException with message "No suffixes found with the given prefix"
		// example: Your Strie stores these words: tea, ted, teen, teeth, team, med
		//           query: tee
		//           returns: teen, teeth
		// returns words in alphabetical order
		
		return null;
	}

	
	public static String getLongestPrefix(Strie myStrie, String query){
		// Given query, returns the longest prefix stored in myStrie 
		// If no prefix can be found, throw RuntimeException with message "No prefix found!"
		// query must contain characters between 'a' and 'z'
		// otherwise, throw RuntimeException with message "Invalid character entered. Characters must be between 'a' and 'z' " 
		// O(n), where n is the number of characters in query
		// Example: Your Strie stores these words: ban, band, banned, banana, can 
		//          query: bandana
		//          returns: band
		
		return null;
	}

	public static String[] getClosestMatch(Strie myStrie, String query){
		// Returns the closest match(es) found in myStrie for the given query 
		// query must contain characters between 'a' and 'z'
		// otherwise, throw RuntimeException with message "Invalid character entered. Characters must be between 'a' and 'z' " 
		// closest match rules: return the word(s) with the smallest distance between word_in_myStrie and query
		// 1. length of query == length of word_in_myStrie
		//    distance = charcter mismatches between query and word_in_myStrie at each position (distance(barn, bird) = 2)
		// 2. length of query != length of word_in_myStrie
		//    find substrings by moving a sliding window of length = smaller(query, word_in_myStrie) 
		//    distance_substring = absolute_length_difference(word_in_myStrie, query) + mismatch of characters at each position of substring and query
		//    distance = smallest distance_substring
		//    example: query: bann, word_in_myStrie = banned. absolute_length_difference(word_in_myStrie, query) = 2
		//             substrings: bann, anne, nned
		//             distance_substring(bann, bann) = 2, distance_substring(bann, anne) = 5, distance_substring(bann, nned) = 6
		//             distance(bann, banned) = 2
		// return the words with minimum distance
		// if the minumim cost of the closest match >= length of query, throw RuntimeException with message "Can't suggest a word! No close word found!"
		// Example: your Strie stores these words: barn, ban, banana, banned, bird
		//          query: bann 
		//          returns: ban, barn 
		// returns words in alphabetical order
		
		return null;
	}

	// --------------------------------------------------------
	// example testing code... edit this as much as you want!
	// --------------------------------------------------------
	/**
	 * @param args
	 */
	public static void main(String[] args){
		Strie myStrie = new Strie();
		Strie x = new Strie();
		
		Character a = new Character('a');
		Character z = new Character('z');
		System.out.println(a.charValue()+0);
		System.out.println(z.charValue()+0);
		
		x.insert("bat");
		x.insert("bar");
		x.insert("barn");
		x.insert("barista");
		System.out.println(x.contains("barn"));
//		System.out.println(x.remove("barn"));
		System.out.println(x.contains("barista"));
		System.out.println(x.remove("barista"));
		//System.out.println(x.contains("bar"));
	//	System.out.println(x.contains("barn"));
	//	System.out.println(x.contains("bar"));
		System.out.println(x.contains("barista"));
		System.out.println(x.contains("barist"));
	
		System.out.println(x.contains("bar"));
		System.out.println(x.remove("bar"));
		System.out.println(x.contains("barn"));
		if(isEmptyStrie(myStrie) && myStrie.root.isEmptyNode())
			System.out.println("Yay 1");

		myStrie.insert("a");
		StrieNode[] children = myStrie.root.getAllChildren();
		if(!isEmptyStrie(myStrie) && children[0].isEmptyNode() && children[0].isEnd() && myStrie.root.containsChar('a'))
			System.out.println("Yay 2");

		myStrie.insert("bat");
		myStrie.insert("bar");
		myStrie.insert("barn");
		myStrie.insert("cat");

		if(myStrie.contains("cat"))
			System.out.println("Yay 3");
		
		//System.out.println(Strie.levelOrderTraversal(myStrie));

		String res = Strie.levelOrderTraversal(myStrie).trim();
		String actualOut = "a b c a a r t t n";
		if(res.equals(actualOut))
			System.out.println("Yay 4");

		String[] yourWords = Strie.getStrieWords(myStrie);
	//	System.out.println(yourWords[0]);
		String[] allWords = {"a", "bar", "barn", "bat", "cat"};
		int allMatches = 1;
		for(int i = 0; i < yourWords.length; i++){
			if(!yourWords[i].equals(allWords[i]))
				allMatches = 0;
		}	
		if(allMatches == 1)
			System.out.println("Yay 5");

		//System.out.println(myStrie.contains("cat"));
		//System.out.println(myStrie.remove("cat"));
		if(myStrie.remove("cat") && !myStrie.contains("cat"))
			System.out.println("Yay 6");
	//	System.out.println(myStrie.contains("cat"));
	}

}
