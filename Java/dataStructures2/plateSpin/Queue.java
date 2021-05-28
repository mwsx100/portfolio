package plateSpin;
//--------------------------------------------------------
//DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
//--------------------------------------------------------
interface Queue<T> {
	public boolean enqueue(T value);
	public T dequeue(); //throw NoSuchElementException if nothing to dequeue
	public int size();
	public boolean isEmpty();
	public void clear();
}