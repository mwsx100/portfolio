package project5;

//This structure is an array where each entry is the head of its own
//linked list. The linked lists are made up of "bare nodes" (i.e.
//they are not "wrapped" in a nice linked list class). Each node
//in each linked list contains a key-value pair (rather than an single
//value).

//This class will form the baseline for creating a hash table for a
//map that uses separate chaining (each entry in a map is a key-value
//pair). This class will also form a baseline for creating an adjacency
//list (where each entry is a key-value pair where keys are the
//"adjacent" node and values are the connection between them). This way
//we have a universal way for to access your internal structures when
//grading these two classes.

//You have a lot of freedom in how you design this class, as long as
//the provided code work as described. However, because this is only
//a baseline for the other classes you are writing, it would be bad
//design on your part to add in anything specific to hash tables
//(such as rehashing) or specific to graphs (such as source/destination
//information for edges). Our advice to you is: (1) keep it simple
//and (2) think before you code.

//Read the "do not edit" section carefully so that you know what is
//already available. This should help you form some ideas of what
//types of things are missing.

//You may: Add additional methods or variables of any type (even
//public!), but again, you _must_ use the provided Node class, the
//provided "storage" instance variable, and all provided methods
//_must still work_.

//You may not import anything from java.util (and you may not use anything
//from java.util in your part of the code). We use java.util.ArrayList in
//the provided code, but it is not available to you.

/**
 * @author Matthew Souvannaphandhu an array of lists of key value pairs
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class ArrayOfListsOfPairs<K, V> {
	// This is your internal structure, you must use this
	// you may not change the type, name, privacy, or anything
	// else about this variable. It is initialized in the
	// provided constructor (see the do-not-edit section)
	// and the Node class is also defined there.
	/**
	 * Array for storying nodes of key value pairs.
	 */
	private Node<K, V>[] storage;
	/**
	 * number of elements in storage.
	 */
	private int size = 0;

	// Your code goes here!

	/**
	 * default constructor.
	 */
	public ArrayOfListsOfPairs() {
		storage = new Node[2];
	}

	/**
	 * adds to the array
	 * @param key the key to be added
	 */
	public void add(K key) {
		insert(size, key, null);
		size++;
	}

	/**
	 * inserts an element into the array at specified index.
	 * 
	 * @param index
	 *            the index where the new value will be inserted
	 * @param value
	 *            the key value pair that the new node will contain
	 */
	public void insert(int index, K key, V value) {
		KeyValuePair<K, V> newKV = new KeyValuePair<K, V>(key, value);
		if (storage[index] != null) {
			Node<K, V> current = storage[index];
			while (current.next != null) {
				current = current.next;
			}
			current.next = new Node<K, V>(newKV);
			size++;
			return;
		}
		storage[index] = new Node<K, V>(newKV);
		size++;
	}

	/**
	 * @param index the index where to remove
	 * @return if the remove was successful
	 */
	public boolean removeAll(int index) {
		int i = 0;
		if (storage[index] == null)
			return false;
		Node<K, V> current = storage[index];
		while (current.next != null) {
			i++;
			current = current.next;
		}
		storage[index] = null;
		size -= i;
		return true;
	}

	/**
	 * removes a node from the array.
	 * @param key
	 * @param index
	 * @return
	 */
	public K remove(K key, int index) {
		KeyValuePair<Node<K, V>, Node<K, V>> nodes = grabNodes(key, index);
		if (nodes == null)
			return null;
		Node<K, V> current = nodes.getKey();
		if (current == storage[index]) {
			if (current.next == null)
				storage[index] = null;
			else
				storage[index] = current.next;
			size--;
			return nodes.getKey().pair.getKey();
		}
		current = current.next;
		if (current.next != null) {
			nodes.getKey().next = current.next;
			size--;
			return nodes.getValue().pair.getKey();
		}
		nodes.getKey().next = nodes.getValue().next;
		size--;
		return nodes.getValue().pair.getKey();
	}

	/**
	 * grabs an array of all the KV pairs.
	 * @return an array of every key value pair in order
	 */
	public KeyValuePair<K, V>[] getKVArr() {
		KeyValuePair<K, V>[] KVArr = (KeyValuePair<K, V>[]) new KeyValuePair[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < getNumLists(); j++) {
				if (i >= size)
					break;
				if (storage[j] != null) {
					Node<K, V> current = storage[j];
					if (current.next == null) {
						// if (i >= size) break;
						KVArr[i] = current.pair;
						i++;
						// if (i >= size) break;
					}
					while (current.next != null) {
						KVArr[i] = current.pair;
						current = current.next;
						i++;
						if (i >= size)
							break;
						if (current.next == null) {
							KVArr[i] = current.pair;
							i++;
							// if (i >= size) break;
						}
					}
				}
			}
		}
		return KVArr;
	}

	/**
	 * tells if the array contains that specific key.
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public boolean contains(K key, int index) {
		if (storage[index] == null)
			return false;
		Node<K, V> current = storage[index];
		if (current.pair.getKey().equals(key))
			return true;
		else {
			while (current.next != null) {
				current = current.next;
				if (current.pair.getKey().equals(key))
					return true;
			}
		}
		return false;
	}

	/**
	 * gets all the indexs where the key appears
	 * @param key the key
	 * @param size the size
	 * @return array of indexes where this key is located
	 */
	public int[] getIndexes(K key, int size) {
		int[] vertices;
		vertices = new int[size];
		for (int i = 0; i < size; i++) {
			if (grabNode(key, i).pair.getKey().equals(key)) {
				vertices[i] = i;
			}
			if (storage[i].pair.getKey().equals(key)) {
			}
		}
		return vertices;
	}

	/**
	 * when vertices are added to storage with no edges, they are added as nodes with null values.
	 * when vertices are given edges, we fix how they are represented in storage.
	 * @param index index where the vertex is
	 */
	public void fix(int index) {
		if (storage[index].pair.getValue() == null)
			storage[index] = storage[index].next;
	}

	/**
	 * gets the specified value.
	 * 
	 * @param key key of the value to be returned
	 * @param index where the vertex is
	 * @return
	 */
	public V get(K key, int index) {
		V val = grabNode(key, index).pair.getValue();
		if (val == null) throw new NullPointerException("here");
		return grabNode(key, index).pair.getValue();
	}

	/**
	 * grabs the node with the specified key
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	private Node<K, V> grabNode(K key, int index) {
		if (storage[index] == null)
			return null;
		Node<K, V> current = storage[index];
		if (current.pair.getKey().equals(key))
			return current;
		else {
			while (current.next != null) {
				current = current.next;
				if (current.pair.getKey().equals(key))
					return current;
			}
		}
		return null;
	}

	/**
	 * grabs the node with the specified key, as well as the previous one if one
	 * exists.
	 * 
	 * @param key key we want to find the node for
	 * @param index location of the key
	 * @return
	 */
	private KeyValuePair<Node<K, V>, Node<K, V>> grabNodes(K key, int index) {
		if (storage[index] == null)
			return null;
		Node<K, V> current = storage[index];
		if (current.pair.getKey().equals(key)) {
			KeyValuePair<Node<K, V>, Node<K, V>> nodes = new KeyValuePair<Node<K, V>, Node<K, V>>(current,
					current.next);
			return nodes;
		} else {
			while (current.next != null) {
				KeyValuePair<Node<K, V>, Node<K, V>> nodes = new KeyValuePair<Node<K, V>, Node<K, V>>(current,
						current.next);
				current = current.next;
				if (current.pair.getKey().equals(key))
					return nodes;
			}
		}
		return null;
	}

	/**
	 * replaces the node pertaining to the specified key with a new value.
	 * 
	 * @param key key we want
	 * @param value value we replace with
	 * @param index where the key is
	 * @return
	 */
	public boolean replace(K key, V value, int index) {
		if (storage[index] == null)
			return false;
		Node<K, V> current = storage[index];
		if (current.pair.getKey().equals(key) && !current.pair.getValue().equals(value)) {
			Node<K, V> next = current.next;
			KeyValuePair<K, V> KV = new KeyValuePair<K, V>(key, value);
			current = new Node<K, V>(KV, next);
			storage[index] = current;
			return true;
		} else {
			while (current.next != null) {
				Node<K, V> prev = current;
				current = current.next;
				if (current.pair.getKey().equals(key) && !current.pair.getValue().equals(value)) {
					Node<K, V> next = current.next;
					KeyValuePair<K, V> KV = new KeyValuePair<K, V>(key, value);
					current = new Node<K, V>(KV, next);
					prev.next = current;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the number of elements in the structure
	 */
	public int size() {
		return size;
	}

	/**
	 * returns all pairs up until a certain point of the list.
	 * 
	 * @param end
	 *            the place where you wanna stop
	 * @return all pairs up to end
	 */
	public java.util.ArrayList<KeyValuePair<K, V>> getSomePairs(int end) {
		java.util.ArrayList<KeyValuePair<K, V>> lst = new java.util.ArrayList<>();

		for (int i = 0; i < end; i++) {
			lst.addAll(getAllPairs(i));
		}
		return lst;
	}

	// --------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	// --------------------------------------------------------

	/** 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String KVStr = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < getNumLists(); j++) {				
				if (storage[j] == null) {
					KVStr += "(" + j + ") \n";
					i++;
					if (i >= size) break;
				//	continue;
					
				}
				if (storage[j] != null) {
					KVStr += "(" + j + ")";
					Node<K, V> current = storage[j];
					if (current.next == null) {
						if (current.pair.getValue() != null) {
							KVStr += current.pair.toString() + "\n";
							i++;
						}
						i++;
						if (i >= size)
							break;
					}

					while (current.next != null) {
						if (current.pair.getValue() != null) {
							KVStr += current.pair.toString() + "--";
							i++;
						}
						current = current.next;
						i++;
						 if (i >= size) break;
						if (current.next == null) {
							KVStr += current.pair.toString() + "\n";
							i++;
							if (i >= size)
								break;
						}
					}
				}
			}
		}
		return KVStr;
	}

	public static void main(String[] args) {
	}

	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------

	// This is what one node in one linked list looks like
	/**
	 * @author mwsx1
	 *	node of KV pairs.
	 * @param <K> key
	 * @param <V> value
	 */
	public static class Node<K, V> {
		// it contains one key-value pair
		public KeyValuePair<K, V> pair;

		// and one pointer to the next node
		public Node<K, V> next;

		// convenience constructor
		public Node(KeyValuePair<K, V> pair) {
			this.pair = pair;
		}

		// convenience constructor
		public Node(KeyValuePair<K, V> pair, Node<K, V> next) {
			this.pair = pair;
			this.next = next;
		}
	}

	// Creates an array with the specified number of lists-of-pairs
	/**
	 * Creates an array with the specified number of lists-of-pairs.
	 * @param numLists how many lists you want
	 */
	@SuppressWarnings("unchecked")
	public ArrayOfListsOfPairs(int numLists) {
		storage = (Node<K, V>[]) new Node[numLists];
	}

	// Returns the number of lists in this collection
	/**
	 * Returns the number of lists in this collection
	 * @return storage.length
	 */
	public int getNumLists() {
		return storage.length;
	}

	// Returns all key-value pairs in the specified sublist of this collection
	/**
	 * Returns all key-value pairs in the specified sublist of this collection.
	 * @param listId specified sublist
	 * @return lst
	 */
	public java.util.ArrayList<KeyValuePair<K, V>> getAllPairs(int listId) {
		java.util.ArrayList<KeyValuePair<K, V>> lst = new java.util.ArrayList<>();

		Node<K, V> current = storage[listId];
		while (current != null) {
			lst.add(current.pair);
			current = current.next;
		}

		return lst;
	}

	// Returns all key-value pairs in this collection
	/**
	 * Returns all key-value pairs in this collection.
	 * @return lst
	 */
	public java.util.ArrayList<KeyValuePair<K, V>> getAllPairs() {
		java.util.ArrayList<KeyValuePair<K, V>> lst = new java.util.ArrayList<>();

		for (int i = 0; i < storage.length; i++) {
			lst.addAll(getAllPairs(i));
		}

		return lst;
	}
}
