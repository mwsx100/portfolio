package plateSpin;
import java.util.NoSuchElementException;

public class Bin implements Stack<Plate> {
	//this is your internal storage
	public AttachedList<Plate> internalList;
	
	//you may _NOT_ add any additional instance variables, use the list above!
	//you should not need any additional private helper methods, but you
	//may add them if you like
	
	public Bin() {
		internalList = new AttachedList<Plate>();
		//any initialization goes here
	}
	
	@Override
	public Plate pop(){
		return internalList.remove(0);
	}
	
	@Override
	public boolean push(Plate dish) {
		internalList.add(0, dish);
		//System.out.println(internalList);
		return true;
	}
	
	@Override
	public int size() {
		return internalList.size()-1;
	}
	
	@Override
	public boolean isEmpty() {
		if (internalList.size() == 0) return true;
		return false;
	}
	
	@Override
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
		Bin bin = new Bin();
		Plate dish = new Plate(1);
		Plate dish2 = new Plate(2);
		Plate dish3 = new Plate(3);
		
		System.out.println(dish.toString());
		System.out.println(dish2.toString());
		System.out.println(dish3.toString());
		bin.push(dish);
		bin.push(dish2);
		bin.push(dish3);

		System.out.println(bin.toString());
		System.out.println(bin.pop());
	}
	
	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------
	public String toString() {
		String returnString = "";
		boolean first = true;
		for(Plate p : internalList) {
			if(first) { returnString = returnString+p; first = false; }
			else {returnString = returnString+"|"+p; }
		}
		return returnString;
	}
}
