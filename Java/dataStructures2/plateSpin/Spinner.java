package plateSpin;
import java.util.NoSuchElementException;

public class Spinner {
	//you may NOT add any additional instance variables or methods
	//to the Spinner, all needed instance variables are created in
	//the DO NOT EDIT section
	
	private static class Hand {
		//add any additional instance variables here
		//no additional methods (not even private ones)
		
		int space = 0;
		Plate held = null;
		
		
		public void catchPlate(Plate plate) {
			if (space > 0) {
				throw new RuntimeException("Catching hand not empty");
			}	
			if (space == 0) {
				if (plate == null) {
					throw new RuntimeException("Can't catch a plate that doesn't exist!");
				}
				held = plate;
				space++;
			}
			//if trying to catch a plate when full, throw a RuntimeException
			//with the message "Catching hand not empty"
			
			//if trying to catch a null plate, throw a RuntimeException
			//with the message "Can't catch a plate that doesn't exist!"
			
			//otherwise keep the plate in this hand
		}
		
		public Plate tossPlate() {		
			if (space == 0) {
				throw new RuntimeException("Tossing hand was empty");
			}
			Plate toss = held;
			held = null;
			space--;
			return toss;
			//if trying to throw a plate when this hand does not have
			//a plate, throw a RuntimeException with the message
			//"Tossing hand was empty"
			
			//otherwise remove the plate from this hand
			//and return the plate you removed
		}
		
		public boolean hasPlate() {
			if (space == 1) return true;
			return false;
			//return true if you have a plate, false otherwise
		}
		
		
		public String toString() {
			if (held == null) {
				return "    ";
			}
			
			else return held.toString();
			
			//if this hand is empty, return the string "   " (three spaces)
			//otherwise return the plate's string value
		}
	}
	
	public void passPlate() {
		hands[0].catchPlate(hands[1].tossPlate());
		//put a plate from hand 2 and pass it to hand 1
		//this can be done with a single line of code
	}
	
	public void putDownPlate() {
		bin.push(hands[0].tossPlate());
		//put a plate from hand 1 and put it in the bin
		//this can be done with a single line of code
	}
	
	public void pickUpPlate() {
		if (bin.isEmpty() == true) {
			throw new RuntimeException("Can't pickup a plate that doesn't exist!");
		}
		hands[0].catchPlate(bin.pop());
		//take a plate out of the bin and put it in hand 1
		
		//if there are no plates in the bin, throw a RuntimeException
		//with the message "Can't pickup a plate that doesn't exist!"
	}
	
	public void spinPlate() {
		if (air.size() >= air.MAX_CAPACITY) {
			throw new RuntimeException("Too many plates in the air!");
		}
		air.enqueue(hands[0].tossPlate());
		//take a plate from hand 1 and put it in the air
		
		//if the air can't hold anymore plates, throw a RuntimeException
		//with the message "Too many plates in the air!"
	}
	
	public void catchPlate() {
		if (air.isEmpty() == true) {
			throw new RuntimeException("Can't catch a plate that doesn't exist!");
		}
		hands[1].catchPlate(air.dequeue());
		//take a plate out of the air and put it in hand 2
		
		//if there are no plates in the air, throw a RuntimeException
		//with the message "Can't catch a plate that doesn't exist!"
	}
	
	// --------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	// --------------------------------------------------------
	public static void main(String[] args) {
		Spinner me = new Spinner(5);
		
		
		

		me.pickUpPlate();
		me.spinPlate();
		
		System.out.println(me);
	}
	
	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------
	
	private final Air air = new Air();
	private final Bin bin = new Bin();
	private final Hand[] hands = new Hand[2];

	//spinners have two hands and starts with a bin full of plates
	public Spinner(int totalPlates) {
		hands[0] = new Hand();
		hands[1] = new Hand();
		
		for(int i = totalPlates; i > 0; i--) {
			this.bin.push(new Plate(i));
		}
	}
	
	//this does the beautiful ascii art :)
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		String[] personParts = {
			"   "+air.toString()+"\n",
			"\n",
			"    "+hands[0].toString()+"( )"+hands[1].toString()+"\n",
			"     \\__|__/\n",
			"        |\n",
			"        |\n",
			"       / \\\n",
			"      /   \\\n"
		};
		
		String[] stackParts = this.bin.toString().split("[|]");
		
		int total = (this.bin.size() < personParts.length) ? personParts.length : this.bin.size();
		for(int i = total; i >= 0; i--) {
			sb.append((this.bin.size()-1 < i) ? "   " : stackParts[stackParts.length-i-1]);
			if(i < personParts.length) {
				sb.append(personParts[personParts.length-i-1]);
			}
			else {
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}
}
