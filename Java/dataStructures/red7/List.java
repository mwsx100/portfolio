package red7;

public class List<T> {
	
	private T data[];
	private int size;
	
	public List() {
		data = (T[])(new Object[50]);
		size = 0;
	}
	
	public void add(T data) {
		
		
		/*size++;
		if (index > size) System.err.println("You can't add there");
		else{ for (int i = index; i <= size; i++) {
			this.data[i+1] = this.data[i];
		}  
		this.data[index] = data;
		}*/
	/*T newArray[] = (T[])(new Object[this.getSize() + 1]);
	for (int i = 0; i < this.getSize(); i++) {
		newArray[i] = this.data[i];
	}
	this.data = newArray;*/
		
	}
	
	public T get(int index) {
		if (index > size) { System.err.println("You can't access there"); return null;}
		else return data[index];
	}
	
	public int getSize() {
		return size;
	}

}
