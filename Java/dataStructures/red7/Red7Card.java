package red7;

public class Red7Card implements R7Comparable {
	
	private int number;
	private String color;
	
	public Red7Card(String clr, int num) {
		this.color = clr; this.number = num;
	}
	
	public int getNumber() {
		return number;
	}

	
	public String getColor() {
		return color;
	}
	
	public int compareTo(R7Comparable obj) {
		if (this.getOverallVal() > obj.getOverallVal()) 
			return 1;
		else if (this.getOverallVal() < obj.getOverallVal())
			return -1;
		else return 0;
	}
	
	public int getColorId() {
		return R7Comparable.getColorId(this.getColor());
	}
	
	public String toString() {
		return this.getNumber()+""+this.getColor()+","; //"|" + this.getColor() + " " + this.getNumber() +"|";	
		}
	

}
