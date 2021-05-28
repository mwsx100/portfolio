package plateSpin;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AttachedList<T> implements List<T> {
	//for more information on these methods
	//read the documentation of the list interface
	//here: https://docs.oracle.com/javase/8/docs/api/java/util/List.html
	//keep in mind that we are doing a _linked_ list
	//but the documentation is for general lists (like array lists)
	
	//NOTE: the documentation above is not optional, it tells you things
	//like what exceptions to throw
	
	private static class Node<T> {
		//you may NOT change these instance variables and/or
		//add any additional instance variables here
		//(so you may not doubly link your list)
		T value;
		Node<T> next;
		
		Node(T data) {
			this.value = data;
		}
		
		public String toString() {
			String lStr = this.value.toString();
			return lStr;
		}
		//you may add more methods here... and they may be public!
		//note: a constructor _is_ a method (just a special type of method)
		//note: you don't have to add anything if you don't want
		//      this will work as-is
	}
	
	
	private Node<T> head = null;
	//You _MUST_ use head defined above, we will be "breaking into"
	//your class for testing and we'll be using this "head" variable
	//as part of the tests. If you rename or change it, you will
	//not pass the unit tests.
	
	
	//Note: if you're interested on what "breaking in" means, it means
	//we'll be using reflection to access your private instance variables.
	//Interested? See: https://docs.oracle.com/javase/tutorial/reflect/index.html
	
	private int size = 0;
	private Node<T> tail = head;
	
	private Node<T> walk(Node<T> node, int steps) {	
	if (steps == 0) return node;
	steps--;
	if (node.next != null) {
		return walk(node.next, steps);
	}
	else return node;
	}
	
	private void backWalk(Node<T> node, AttachedList<T> copy) {	
		if (node.next != null) {
			backWalk(node.next, copy);
			copy.add(node.value);
				}
		else {
			copy.add(node.value);
		}
		}
	//you may add more private methods and instance variables here if you want
	//you may add additional private helper functions as well
	//no new protected or public variables or methods
	
	public AttachedList() {	
		//initialize anything you want here...
	}
	
	@Override
	public int size() {
		return size;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(1)
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) return true;
		return false;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(1)
	}

	@Override
	public int indexOf(Object o) {
		
		
		if (head.value.equals(o)) {
			return 0;
		}
		
		if (tail.value.equals(o)) {
			int x = size;
			return x-1;
		}
		
		Node<T> rm = head;
		int i = 0;
		while (rm.next != null) {
			//System.out.println(rm.next);
			if (rm.value.equals(o)) {
			break;	
			}
			rm = rm.next;
			i++;
		}
		return i;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
		//yes, nulls are allowed to be searched for
	}
	
	/**
	 * @return true if attached list contains object in argument
	 */
	@Override
	public boolean contains(Object o) {
		if (head.value.equals(o)) {
			return true;
		}
		
		if (tail.value.equals(o)) {
			return true;
		}
		
		Node<T> rm = head;
		
		while (rm.next != null) {
			if (rm.value.equals(o)) {
			break;	
			}
			rm = rm.next;
		}
		if (rm.value.equals(o)) return true;	
		return false;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
		//yes, nulls are allowed to be searched for
	}
	
	/**
	 * (non-Javadoc)
	 * @return true if add operation successful
	 */
	@Override
	public boolean add(T e) {
		size++;
		if (head == null) {
			Node<T> newN = new Node(e);
			head = newN;
			tail = head;
			return true;
		}
		if (tail.next == null && tail != null) {
			Node<T> newN = new Node<T>(e);
			tail.next = newN;
			tail = tail.next;
			return true;
		}
		else return false;	
		//this should append to the end of the list
		//O(1) <-- not a typo... think about it!
		//yes, nulls are allowed to be added
	}

	@Override
	public void add(int index, T element) {
		Node<T> newNode = new Node<T>(element);
		 if (index ==0) {
			 if (head == null) {
					Node<T> newN = new Node(element);
					head = newN;
					tail = head;
					size++;
					return;
					
			 }
			 newNode.next = head;
			 head = newNode;
			 size++;
			 return;
		 }
		if (index == size) {
			tail.next = newNode;
			tail = tail.next;
			size++;
			return;
		}
		if (index > size) {
			return;
		}
		 
		
		 
		 Node<T> next = this.walk(head, index); 
		 this.walk(head, index -1).next = newNode;
		 newNode.next = next;
		 size++;	
		//O(n)
		//yes, nulls are allowed to be added
	}
	
	@Override
	public T remove(int index) {
		if(index >= size) {
			return null;
		}
		
		if (index == 0) {
			Node<T> rm = head;
			head = head.next;
			size--;
			return rm.value;
		}
		
		Node<T> rm = this.walk(head, index -1);
		T val = rm.next.value;
		rm.next = rm.next.next;
		this.walk(head, index -1).next = rm.next;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
		size--;
		return val;
	}

	@Override
	public boolean remove(Object o) {
		if (head.value.equals(o)) {
			head = head.next;
			size--;
			return true;
		}
	
		if (tail.equals(o)) {
			tail = this.walk(head, size-1);
			tail.next = null;
			size--;
			return true;
		}
		
		Node<T> rm = head;
		while (rm.next != null) {
			System.out.println(rm.next);
			if (rm.next.value.equals(o)) {
			break;	
			}
			rm = rm.next;
		}
		if (rm.next == null && rm.value.equals(o) == false) {
			return false;
		}
		if (rm.next.value.equals(o)) {
			rm.next = rm.next.next;
			size--;
			return true;
		}
		
		return false;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
		//yes, nulls are allowed to removed
	}

	@Override
	public void clear() {
	//	AttachedList<T> slice = new AttachedList<T>();
		head = null;
		tail = head;
		size = 0;
		//O(1) <-- not a typo... think about it!
	}

	@Override
	public T get(int index) {
		if (index == 0) return head.value;
		if (index >= size) {
			return null;
		}
		return this.walk(head, index).value;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
	}

	@Override
	public T set(int index, T element) {
		return this.walk(head, index).value = element;
		//throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		//O(n)
	}

	public AttachedList<T> slice(int fromIndex, int toIndex) {
		AttachedList<T> slice = new AttachedList<T>();
		for (int i = fromIndex;	i <= toIndex; i++) {
			slice.add(this.remove(fromIndex));
		}
		return slice;
	//	throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		
		//removes a "slice" from fromIndex to toIndex (inclusive)
		//return the slice as a new AttachedList
		//throws IndexOutOfBoundsException if fromIndex _or_ toIndex is invalid
		
		//O(n)
	}

	public AttachedList<T> reverseCopy() {
		AttachedList<T> copy = new AttachedList<T>();
		this.backWalk(head, copy);
		return copy;	
//		throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");	
		//returns a copy of the list with the elements reversed
		//does not alter the original list	
		//O(n)
	}
	
	public static <E> AttachedList<E> flatten(AttachedList<AttachedList<E>> packedList) {
		AttachedList<AttachedList<E>> flat = new AttachedList<AttachedList<E>>();
		int newSize = 0;
		for(int i = 0; i< packedList.size; i++) {    //clones the list so we don't alter the original lists
		 AttachedList<E> node = new AttachedList<E>();
		 for(int j = 0; j < packedList.get(i).size; j++) {
		 node.add(packedList.get(i).get(j));
		 }
		 newSize += packedList.get(i).size;
		 flat.add(node);
		}

		for(int i = 0; i < flat.size; i++) {
			//System.out.println(i);
			if (flat.get(i+1) != null)
			flat.get(i).tail.next = flat.get(i+1).head;
		}
		flat.head.value.size = newSize;
		
		return flat.head.value;
		//given a 2D list of lists (packedList), "flatten" the list into 1D
		//Example 1: [[1,2,3],[4,5],[6]] becomes [1,2,3,4,5,6]
		//Example 2: [[null],[1,3],[5],[6]] becomes [null,1,3,5,6]
		//IMPORTANT: the above examples are _lists NOT arrays_
		
	}
	
	public static <E> AttachedList<AttachedList<E>> pack(AttachedList<E> flatList) {
		AttachedList<AttachedList<E>> pack = new AttachedList<AttachedList<E>>();
				
		int end=0;
		int index = 0;
		
		for(int i =0; i < flatList.size; i++) {
			AttachedList<E> node = new AttachedList<E>();
			pack.add(node);
			for (int j=end+1; j < flatList.size; j++) {
				if (flatList.get(i).equals(flatList.get(j))) {
					pack.get(index).add(flatList.get(i));
					end = j;
				}
				else break;
			}
			if (flatList.get(end+1) != null && i ==0) {
				pack.get(index).add(flatList.get(i));
			}
			i = end;
			index++;
			continue;
		}
		//given a 1D (flatList), "pack" sequential items together
		//to form a 2D list of values	
		return pack;
	//	throw new UnsupportedOperationException("Not supported yet. Replace this line with your implementation.");
		
		//Example 1: [1,1,2,3,3] becomes [[1,1],[2],[3,3]]
		//Example 1: [1,1,2,1,1,2,2,2,2] becomes [[1,1],[2],[1,1],[2,2,2,2]]
		//Example 3: [1,2,3,4,5] becomes [[1],[2],[3],[4],[5]]
		//IMPORTANT: the above examples are _lists NOT arrays_
		
		//promise: we will never test this with nulls in the flatList
		//though there's no harm in coding it to work with nulls
		
	}

	@Override
	public Iterator<T> iterator() {
		//this method is outlined for you... just fill out next() and hasNext()
		//NO ADDITIONAL ANYTHING (METHODS, VARIABLES, ETC.) INSIDE THE ANONYMOUS CLASS
		//You may NOT override remove() or any other iterator methods
		return new Iterator<T>() {
			//starts at the head
			private Node<T> current = head;

			@Override
			public boolean hasNext() {
				if (current == null) return false;
				if (current.next != null) {
					return true;
				}
				else return false;
				//O(1)
			}

			@Override
			public T next() {
				if (this.hasNext() == true) {
					current = current.next;
					return current.value;
				}
				else return null;
				//O(1)
			}
		};
	}
	
	// --------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	// --------------------------------------------------------
	public String toString() {
		//you may edit this to make string representations of your
		//list for testing
		//System.out.println(head.next);
		if (this.head == null) return null;
		String str = "{" + this.head.toString();
		Iterator<T> iter = this.iterator();
		while (iter.hasNext() == true) {
			str += iter.next();
		}
		return str + "}";
	}
	
	public static void main(String[] args) {
		AttachedList<Integer> list = new AttachedList();
		AttachedList<Integer> list6;
	//	list6.add(0, 1);
		list.add(0, 1);
		System.out.println(list.add(3));
		System.out.println(list.toString());
		System.out.println(list.add(4));
		System.out.println(list.add(5));
		System.out.println(list.add(8));
		System.out.println(list.toString());
		Integer x = new Integer(1);	
		System.out.println(list.toString());
		list.remove(1);
		System.out.println(list.toString());

		System.out.println(list.toString());
		System.out.println(list.get(1));
		list.set(1, 7);
		list.add(9);
		list.add(0);
		list.add(4,x);
		System.out.println(list.toString());
		System.out.println(list.indexOf(x));
		System.out.println(list.contains(2));
		AttachedList newList = list.reverseCopy();
		System.out.println(newList.toString());
		System.out.println(list.toString());
	//	list.add(null);
		System.out.println(list.toString());		
		AttachedList<Integer> list2 = new AttachedList();	
		for (int i = 0; i <= 7; i++) {
			list2.add(i);
		}		
		AttachedList<AttachedList<Integer>> big = new AttachedList<AttachedList<Integer>>();
		big.add(list); big.add(list2);
		System.out.println(big);
		//System.out.println(flatten(big));
		AttachedList<Integer> flat = flatten(big);
		
		System.out.println(flat);
		System.out.println(flat.size);
		
		AttachedList<Integer> list3 = new AttachedList();
		AttachedList<Integer> list4 = new AttachedList();
		//list3.add(1); list3.add(1); 
		list3.add(1); list3.add(2);
		 list3.add(2);  list3.add(4); list3.add(3); list3.add(3); list3.add(3); list3.add(3);
		 list3.add(3);
		 
		// System.out.println(list3);
		 
		 list4.add(2); list4.add(1); list4.add(1); list4.add(2);
		 list4.add(2); list4.add(5); list4.add(3); list4.add(5); list4.add(3);
		 list4.add(3);
	
		 
		 AttachedList<AttachedList<Integer>> big2 = pack(list3);
		 System.out.println(list3);
		AttachedList<AttachedList<Integer>> big3 = pack(list4);
		 System.out.println(list4);
		 System.out.println(big2.toString());
		 System.out.println(big2.size);
		 System.out.println(list3);
		 System.out.println(big3.toString());
		 System.out.println(big3.remove(1));
		 System.out.println(big3.toString());
		 System.out.println(big3.size);
		 System.out.println(list.toString());
		// System.out.println(big);
		 AttachedList<AttachedList<Integer>> big4 = pack(list);
		 System.out.println(big4.toString());
		 big4.clear();
		 System.out.println(big4.toString());
		 System.out.println(list);
		 list.add(0, 1);
		 
	
		
	}
	
	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------
	
	@Override
	public Object[] toArray() {
		Object[] items = new Object[this.size()];
		int i = 0;
		for(T val : this) {
			items[i++] = val;
		}
		return items;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		return (T[]) this.toArray();
	}

	@Override public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException("Not supported."); }
	@Override public boolean addAll(Collection<? extends T> c) { throw new UnsupportedOperationException("Not supported."); }
	@Override public boolean addAll(int index, Collection<? extends T> c) { throw new UnsupportedOperationException("Not supported."); }
	@Override public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException("Not supported."); }
	@Override public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException("Not supported."); }
	@Override public int lastIndexOf(Object o) { throw new UnsupportedOperationException("Not supported."); }
	@Override public ListIterator<T> listIterator() { throw new UnsupportedOperationException("Not supported."); }
	@Override public ListIterator<T> listIterator(int index) { throw new UnsupportedOperationException("Not supported."); }
	@Override public List<T> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException("Not supported."); }
}