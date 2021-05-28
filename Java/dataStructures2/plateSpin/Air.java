package plateSpin;
import java.util.NoSuchElementException;

public class Air implements Queue<Plate> {
	//Maximum allowed items in the air... don't allow more than this!
	public static final int MAX_CAPACITY = 13;
	//this is your internal storage
	public AttachedList<Plate> internalList;
	
	//you may _NOT_ add any additional instance variables, use the list above!
	//you should not need any additional private helper methods, but you
	//may add them if you like
	
	public Air() {
		internalList = new AttachedList<Plate>();
		//any initialization goes here
	}
	
	public boolean enqueue(Plate value) {
		
		internalList.add(value);
		return true;
	}
	public Plate dequeue() {
		if (this.isEmpty() == true) {
			throw new NoSuchElementException();
		}
		return internalList.remove(0);
		//throw NoSuchElementException if nothing to dequeue
	}
	public int size() {
		return internalList.size();
	}
	public boolean isEmpty() {
		if (internalList.size() == 0) return true;
		return false;
	}
	public void clear() {
		internalList.clear();
	}
	
	//override all the required methods
	//all methods must be O(1)
	//all methods can be written in 3 lines or less of code
	//if you're writing much more than that, something is probably wrong...
	
	// --------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	// --------------------------------------------------------
	public static void main(String[] args) {
		Air air = new Air();
		Plate dish = new Plate(1);
		Plate dish2 = new Plate(2);
		Plate dish3 = new Plate(3);
		air.enqueue(dish); air.enqueue(dish2); air.enqueue(dish3);
		
		System.out.println(air);
	}
	
	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------
	public String toString() {
		String returnString = "";
		for(Plate p : internalList) {
			returnString = p+returnString;
		}
		return returnString;
	}
}
