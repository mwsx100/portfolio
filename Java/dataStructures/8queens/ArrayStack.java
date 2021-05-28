package queensG;



public class ArrayStack<T> implements Stackable<T>{
	   private T[] data;
	   private int topInd;
	    
	   public ArrayStack(int size){
	      data = (T[])(new Object[size]);
	      topInd = -1;
	   }   
	   
	   public void resize() {
		   T[] newData = (T[])(new Object[this.getSize() +1]);
	        for(int i=0; i<data.length; i++)
	            newData[i]=data[i];
	        data = newData;
	   }
	   
	   public int getSize() {
		   return data.length;
	   }
	   
	   public int peek() {
		   return topInd;
	   }
	   public void push(T dt){
	      if(topInd==data.length-1) throw new StackOutOfBoundsException("FULL");
	      topInd++;
	      this.data[topInd] = dt;
	   }
	   public T pop(){
	      if(topInd==-1) throw new StackOutOfBoundsException("EMPTY");
	      return data[topInd--];
	   }
	   
	  
	   
	   public String toString(){
		   String stringy = "";
		   for (int i = 0; i < data.length; i++) {
			   stringy += data[i].toString();
		   }
		   return stringy;
		   
	   }

	}