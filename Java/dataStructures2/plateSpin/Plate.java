package plateSpin;
//--------------------------------------------------------
//DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
//--------------------------------------------------------
public class Plate {
	private int number;
	
	public Plate(int number) {
		this.number = number;
	}
	
	public String toString() {
		return "("+((char)(this.number+96))+")";
	}
	
	public int getNumber() {
		return this.number;
	}
}
