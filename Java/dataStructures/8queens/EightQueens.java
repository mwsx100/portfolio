package queensG;

public class EightQueens {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ChessBoard a = new ChessBoard();
		ArrayStack<Point> data = new ArrayStack(8);
		ArrayStack<Point>[] values = new ArrayStack[92];
		
		
		for (int i = 0; i < 8; i++) {
			a.markUp(1);
		}
		
		data.push(a.getPoint(1, 8));
		
		int i = 0;
		int curCol = 2;
		int curRow = 1;
		boolean good = true;
		while (i < 92) {
			while(good == true) {
			//System.out.println("we are in the loop");
						//System.out.println(data.peek());
			if (data.peek() == 7) {
				System.out.println("data is full");
				System.out.println(a.toString());
				values[i] = data;
				//System.out.println(a.toString());
				System.out.println(data.pop().getRC());
				//System.out.println(data.peek());
				data.pop();
				i++;
				a.reset(curCol);
				curCol--;
				curRow = a.getColumn(curCol).getQPos() ;
			
				if (curRow == 8){ a.reset(curCol); curCol--;data.pop(); curRow = a.getColumn(curCol).getQPos() ; a.markUp(curCol);}
				System.out.println(data.peek());
				System.out.println("you have found " + i); 
				
				
				
				}
			
				
		//	System.out.println(curRow + "" + curCol);
					
			if (a.getPoint(curCol, curRow).getRow() == 8 && a.getPoint(curCol, curRow).isSafe() == false) {
				System.out.println(curCol + "xx row = 8"); 
				a.reset(curCol); System.out.println(data.peek());
				//a.markUp2(curCol);
				System.out.println(data.pop().getRC()); System.out.println(data.peek());
				
				
				curCol--;
				//System.out.println("still alive");
				
				curRow = a.getColumn(curCol).getQPos();
				System.out.println(a.getColumn(curCol).getQPos());
				if (curCol == 1) {
					a.markDown(curCol);
					//System.out.println(data.pop());
					data.push(a.getPoint(1, curRow-1));
					curCol++;
					curRow = 1;
					
					System.out.println(a.toString());
				//	a.markUp(curCol);
				
				}
				//a.markUp(curCol);
			//	if (a.getColumn(curCol).getQPos() > 8) {a.markDown(curCol);System.out.println(a.toString()); }
				if (curRow ==8) {
					a.reset(curCol); 
					curCol--;
					data.pop();
					curRow = a.getColumn(curCol).getQPos()  ;
					//System.out.println(a.getColumn(curCol).getQPos() + "queen");
					
				}
				//System.out.println("still alive");
				//System.out.println(curCol);
				//System.out.println(data.peek());
				break;
			}
			if (a.getPoint(curCol, curRow ).isSafe()== true) {
				//System.out.println(curCol +" space is safe, occupied");
				
				System.out.println(a.getPoint(curCol, curRow).getRC());
				System.out.println(data.peek());
				data.push(a.getPoint(curCol, curRow));
				System.out.println(data.peek());
				
				//System.out.println(data.peek());
				//System.out.println("this is the data " +data.peek());
				//if (curCol == 8) {
				//	System.out.println("this is the data " +data.peek());
				//
				
				if (a.getColumn(curCol).getQPos()  <8) {
					a.markUp(curCol); System.out.println(a.getColumn(curCol).getQPos() + "data");
				}
				
				
				if (curCol < 8) {
					curCol++;
				//a.markUp(curCol);
				curRow = 1; }
				//System.out.println(a.toString());
			}
			else if (a.getPoint(curCol, curRow).isSafe() == false) {
				if (curRow == 8) {
					System.out.println(curCol + " row = 8");
					a.getColumn(curCol).reset();
					curCol--;
					
					data.pop();
					curRow = a.getColumn(curCol).getQPos() ;
				}
				//System.out.println(curCol+ " space unsafe. moving up");
				//System.out.println(a.getColumn(curCol).peek());
				//System.out.println("rowwww " + curRow );
				//System.out.println("row "+a.getPoint(curCol, curRow).getRow());
				//System.out.println(a.getColumn(curCol).getQPos());
				a.markUp(curCol);
				//System.out.println(a.getColumn(curCol).getQPos());
			//	System.out.println("row " +a.getPoint(curCol, curRow + 1).getRow());
				curRow++;
				
				
			}
			

		}
		
		//System.out.println(a.toString());
			}
	}
		
		
		

	}


