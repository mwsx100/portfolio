package graphs;

public class Heap<T extends Comparable<T>>{
	
	private T[] datum;
	private int size;
	
	public Heap() {
		T[] ts = (T[])(new Comparable[10]);
		datum = ts;
		size = -1;
	}
	
	public void enqueue(T data) {
		if(size==datum.length-1) growArr();
        size++;
        datum[size] = data;									//sets the data at the end of the array
        if (size == 0) return;
        
        int n = size;
        T newDa = datum[n];
        
        while (true) {
        	if (datum[n].compareTo(datum[getParent(n)]) > 0) { //if the data at index n is greater than the parent
        		datum[n] = datum[getParent(n)];  				//the parent and the new data switch places in the tree
        		datum[getParent(n)] = newDa;					
        		n = getParent(n);								//n becomes the index of the parent and we loop to check 
        	}													//the parent of the parent
        	else return;
        	
        }
        
	}
	
	public boolean isEmpty() {
		if (size == -1) return true;
		return false;
					
	}
	
	private int getLeft(int n) {
		return 2*n +1;
	}
	
	private int getRight(int n) {
		return 2*n + 2;
	}
	private int getParent(int n) {
		return (n -1)/2;
	}
	
	public T dequeue() {
		if (size == -1) {
			System.err.println("Queue is empty.");
			return null;
		}
		T rData = datum[0];
		T data = datum[size];
		datum[size] = null;
		datum[0] = data; 
		int n = 0;
		size--;
		while (true) {
		if (datum[getLeft(n)] == null && datum[getRight(n)] == null) return	rData;
		
		if (datum[getLeft(n)] == null && datum[getRight(n)] != null) {  //if the left is null but the right isnt (you're at the end)
			if (datum[getRight(n)].compareTo(datum[n]) > 0) {   //if the right is greater than n
			T data0 = datum[n];									//create new variable to reference datum[n]
			datum[n] = datum[getRight(n)] ; 					//datum[n] now references the right
			datum[getRight(n)] = data0;  						//the right now references n, so they switch
			n = getRight(n); 
			}
			return rData; 
		}
		
		if (datum[getRight(n)] == null && datum[getLeft(n)] != null) {
			if (datum[getLeft(n)].compareTo(datum[n]) > 0) {
			T data2 = datum[n];
			datum[n] = datum[getLeft(n)] ; 
			datum[getLeft(n)] = data2;
			n = getLeft(n); 
			}
			return rData;
		}
			
		if (datum[getLeft(n)].compareTo(datum[getRight(n)]) > 0){
			T data2 = datum[n];
			datum[n] = datum[getLeft(n)] ; 
			datum[getLeft(n)] = data2;
			n = getLeft(n);
			continue;
		}
		//System.out.println(datum[getLeft(n)] + " "+ datum[getRight(n)]);
		if (datum[getLeft(n)].compareTo(datum[getRight(n)]) < 0){
			T data2 = datum[n];
			datum[n] = datum[getRight(n)] ; 
			datum[getRight(n)] = data2;
			n = getRight(n);
		}
		
		}
		
		
	}
	
	public int getSize() {
		return size+1;
	}
	
	private void growArr(){
        T[] newArr = (T[])(new Comparable[datum.length*2]);
        for(int i=0; i<datum.length; i++)
            newArr[i]=datum[i];
        datum = newArr;
    }
	
	public int getLevels() {
		int n = 1;
		int levels = 0;
		while (true) {
			if (size > n){ n = 2*n+1; levels++;}
			else return levels;
			}
		}
		
	
	
	public String toString() {
		 String stringy = "";
		 for(int i=0;i<= size+1;i++){
		        for(int j=0;j<Math.pow(2,i)&&j+Math.pow(2,i)<=size+1;j++){
		            stringy +=  datum[j+(int)Math.pow(2,i)-1]+" ";

		        }
		        stringy += "\n";
		 }
		   return stringy;
		   
	   }
	}


