package queensG;
import java.util.LinkedList;
public class ChessBoard {
	private ChessGrid a = new ChessGrid(1);
	private ChessGrid b = new ChessGrid(2);
	private ChessGrid c = new ChessGrid(3);
	private ChessGrid d = new ChessGrid(4);
	private ChessGrid e = new ChessGrid(5);
	private ChessGrid f = new ChessGrid(6);
	private ChessGrid g = new ChessGrid(7);
	private ChessGrid h = new ChessGrid(8);
	private ChessGrid[] board = {a,b,c,d,e,f,g,h};
	
	
	public ChessBoard() {
		
	}
	
	public int getRow(int column, int row) {
		return this.getColumn(column).getPoint(row).getRow();
	}
	
	public Point getPoint(int column, int row) {
		return this.getColumn(column).getPoint(row);
	}
	
	public ChessGrid getColumn(int column) {
		//System.out.println(column);
		switch(column){
			case 1:
				return a;
			case 2:
				return b;
			case 3:
				return c;
			case 4:
				return d;
			case 5:
				return e;
			case 6:
				return f;
			case 7:
				return g;
			case 8:
				return h;
			default:
				System.err.println("Something's wrong" + column);
				return null;
		}
				
				
	 }
	
	public void queensAlg() {
		for (int i = 0;  i < 8; i++) {
			
		}
	}
	
	public void reset(int column) {
		//int q = this.getColumn(column).getQPos()-1;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <=8; j++) {
				
				
					this.getPoint(i, j).markSafe(column); 
				 } }
		this.getColumn(column).reset();
	}
	
	public void markUp(int column) {
		
		
		this.getColumn(column).moveUp(0);
		int q = this.getColumn(column).getQPos()-1;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <=8; j++) {
				
				if (this.getPoint(i, j).getColumn() > this.getPoint(column, q).getColumn() && (
						this.getPoint(i, j).rowDiff() == this.getPoint(column, q).rowDiff() || this.getPoint(i, j).rowSum() == this.getPoint(column, q).rowSum() ||
						this.getPoint(i, j).getRow() == this.getPoint(column, q).getRow())){
					this.getPoint(i, j).markUnsafe(column);
				}
				else this.getPoint(i, j).markSafe(column);
			}
		}
		
		
	}
	
public void markDown(int column) {
		
		
		this.getColumn(column).moveDown();
		int q = this.getColumn(column).getQPos() -1;
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <=8; j++) {
				
				if (this.getPoint(i, j).getColumn() > this.getPoint(column, q).getColumn() && (
						this.getPoint(i, j).rowDiff() == this.getPoint(column, q).rowDiff() || this.getPoint(i, j).rowSum() == this.getPoint(column, q).rowSum() ||
						this.getPoint(i, j).getRow() == this.getPoint(column, q).getRow())){
					this.getPoint(i, j).markUnsafe(column);
				}
				else this.getPoint(i, j).markSafe(column);
			}
		}
		
		
	}
	
	public String toString() {
		String cString = "";
		int rowNo = 8;
		for(int i = 7; i >=0; i--){
			cString = cString + rowNo; rowNo--;
			for (int j = 0; j < 8; j++) {

				cString =cString + board[j].getPoints(i).toString(); } cString = cString + "\n";
			//rowNo++;
			
				//rowNo--;
				//if (j == 7) {
					//cString = cString + "|\n";
				}
			//}
		//}
		return cString + "  1 2 3 4 5 6 7 8";
		
			}
	
	
	
	

	
}
