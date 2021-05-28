package plateSpin;
//--------------------------------------------------------
//DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
//--------------------------------------------------------
interface Stack<T> {
	public boolean push(T value);
	public T pop(); //throw NoSuchElementException if nothing to pop
	public int size();
	public boolean isEmpty();
	public void clear();
}