package heaps;

public class Dummy implements Comparable<Dummy> {
	private int a;
	
	public Dummy(int a) {
		this.a = a;
	}
	
	public int compareTo(Dummy ab) {
		int a = this.a;
		int b = ab.a;
		
		if (a > b) return 1;
		else if (b > a) return -1;
		else return 0;
	}
	
	

}
