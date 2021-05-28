package queensG;

public class ChessGrid {
	
	
	//private ArrayStack<Integer> grids;
	private int[] grid;
	private int queenPos;
	private int column;
	Point[] points;
	//private boolean[] safezone = {true, true, true, true, true, true, true,
	//true};
	

	
	public ChessGrid(int column) {
		this.column = column;
		Point a = new Point(1, column); Point b = new Point(2, column);
		Point c = new Point(3, column); Point d = new Point(4, column);
		Point e = new Point(5, column); Point f = new Point(6, column);
		Point g = new Point(7, column); Point h = new Point(8, column);
		Point[] points = { a,b,c,d,e,f,g,h};
		this.points = points;
		ArrayStack<Integer> grids = new ArrayStack(8);
		//this.grids = grids;
		//queenPos = grids.peek() +2;
		queenPos = 0;
	}
	
	public void reset() {
		//ArrayStack<Integer> grids = new ArrayStack(8);
	//	this.grids = grids;
		//queenPos = grids.peek() +2;
		
		points[queenPos-1].unOccupy(); 
		//points[0].occupy();
		queenPos = 0;
	}
	
	public Point getPoints(int i) {
		return points[i];
	}
	
	public Point getPoint(int i) {
		return points[i-1];
	}
	
	public String toString() {
		String cString = "";
		for (int i = 0; i < 8; i++) {
			cString = cString + points[i].toString() + "\n";
		}
		return cString + this.column;
	}
	
	public int getQPos() {
		return queenPos+1;
	}
	
	public void occupy(int column, int row) {
		points[row-1].occupy();
		
	}
	
	public void moveUp(int x) {
		//if (queenPos == 8) this.reset();
		//queenPos;
		//grids.push(x);
		//queenPos = grids.peek() +1;
		if (queenPos >=7) System.out.println("here");
		queenPos++;
		
		points[queenPos-1].occupy(); 
		
		
		if (queenPos  > 1) {
			points[queenPos - 2].unOccupy();
		}
		
	}
public void moveUp2(int x) {
	
		//queenPos;
		//grids.push(x);
		//queenPos = grids.peek() +1;
		queenPos++;
		points[queenPos].occupy(); 
		
		
		if (queenPos  > 1) {
			points[queenPos - 2].unOccupy();
		}
		
	}
	
	public void moveDown() {
		//grids.pop();
		queenPos--;
	//	queenPos = grids.peek() +1; 
		System.out.println(queenPos);
		if (queenPos == 0) {
		points[queenPos].unOccupy();  }
		else {
			points[queenPos].unOccupy(); 
			System.out.println("unoccupying" + (queenPos ));
			points[queenPos -1].occupy();
			//points[queenPos].unOccupy(); 
			} 
		
		}
		
	
	
		/*if (queenPos == grids.getSize() - 1) {
			System.err.println("Stack full");
		}
		else{
			queenPos++;
			this.grid[queenPos] = x; }} */
	
	/*public void push(int x) {
		if (queenPos == grid.length - 1) {
			System.err.println("Stack full");
		}
		else{
			queenPos++;
			this.grid[queenPos] = x;
		}
	}*/
	
	
	public int peek() {
		return this.grid[queenPos];
	}

}
