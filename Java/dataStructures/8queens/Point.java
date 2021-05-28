package queensG;

public class Point {

	private int row;
	private int column;
	private boolean occupied;
	private boolean safe;
	private int[] safetyPoints = {1,1,1,1,1,1,1,1};
	
	public Point(int row, int column) {
		this.row = row;
		this.column = column;
		occupied = false;
		safe = true;
	}
	
	public int rowSum() {
		return row + column;
	}
	
	public int rowDiff() {
		return row - column;
	}
	
	
	
	public String toString() {
		if (occupied == true) 
			return "|q";
		else if (this.isSafe() == true) 
			return "|_";
		else return "|x";
	}
	
	public void markUnsafe(int column) {
		safetyPoints[column-1] = 0;
		safe = false;
	}
	
	public void occupy() {
		//if (row == q)
		 occupied = true;
		//else System.err.println("this is where the problem is \n");
	}
	
	public void unOccupy() {
		//if (row == q)
			occupied = false;
	}
	
	public boolean isSafe() {
		return safe;
	}
	
	public void markSafe(int column) {
		safetyPoints[column-1] = 1;
		int safeCount = 0;
		for (int i = 0; i < 8; i++) {
			if (safetyPoints[i] == 1) safeCount++;
		}
		if (safeCount == 8) safe = true;
		//if (safetyPoints > 0) safetyPoints--;
		
		//if (safetyPoints == 0) safe = true;
	}
	
	public String getRC() {
		return column + " " + row;
	}
	
	public void markUnsafe() {
	//	safetyPoints++;
		safe = false;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
