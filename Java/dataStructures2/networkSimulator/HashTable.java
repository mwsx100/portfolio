package project5;

//Hash table with separate chaining. Each key and value gets
//placed together as a single entry in the table. The hash code
//of a key is used to place the pair in the table and to look
//for it again. Note that KeyValuePair is a structure for
//ArrayOfListsOfPairs, this part of the code needs to be able to
//deal with keys and values separately.

import java.util.Collection;

/**
 * @author Matthew Souvannaphandhu
 *
 * @param <K>
 *            the key
 * @param <V>
 *            the value
 */
public class HashTable<K, V> {

	
	// This is the minimum number of slots in the hash table
	// Do not change this.
	/**
	 * This is the minimum number of slots in the hash table
	 */
	private static final int MIN_SLOTS = 2;

	/**
	 * size of hash table
	 */
	private int size = 0;

	// You must use this as your internal storage, you may not change
	// the type, name, privacy, or anything else about this variable.
	/**
	 * Internal storage of hashtable
	 */
	protected ArrayOfListsOfPairs<K, V> storage;

	// If the number of slots requested is less than the minimum
	// number of slots, use the minimum instead.
	/**
	 * constructor for HashTable
	 * 
	 * @param numSlots
	 *            number of slots in the table
	 */
	public HashTable(int numSlots) {
		if (numSlots < MIN_SLOTS) {
			storage = new ArrayOfListsOfPairs<K, V>();
			return;
		}
		storage = new ArrayOfListsOfPairs<K, V>(numSlots);
	}

	// The number of key-value entries in the table.
	// O(1)
	/**
	 * The number of key-value entries in the table.
	 * 
	 * @return size
	 */
	public int size() {
		return size;
	}

	// Returns the number of slots in the table.
	// O(1)
	/**
	 * returns number of slots
	 * @return number of slots in the table
	 */
	public int getNumSlots() {
		return storage.getNumLists();
	}

	// Returns the load on the table.
	// O(1)
	/**
	 * gives you the load
	 * @return load on the table
	 */
	public double getLoad() {
		double s = size();
		double n = getNumSlots();
		return s / n;
	}

	// If the key is not in the table, add the key-value entry to the table
	// and return true. If unable to add the entry, return false. Keys and
	// values are _not_ allowed to be null in this table, so return false if
	// either of those are provided instead of trying to add them.

	// If the load goes above 3 after adding an entry, this method should
	// rehash to three times the number of slots.

	// Must run in worst case O(n) and average case O(n/m) where n is the
	// number of key-value pairs in the table and m is the number of "slots"
	// in the table.
	/**
	 * adds a KVP to the table.
	 * @param key the key you want
	 * @param value the value you want
	 * @return if the add was successful
	 */
	public boolean add(K key, V value) {
		if (key == null || value == null)
			return false;
		if (contains(key))
			return false;
		storage.insert(hashFunc(key), key, value);
		size++;
		if (getLoad() > 3)
			rehash(3 * getNumSlots());
		return true;
	}

	/**
	 * computes an index for key
	 * @param key the key you want
	 * @return index for the key
	 */
	private int hashFunc(K key) {
		return Math.abs(key.hashCode() % getNumSlots());
	}
	// Rehashes the table to the given new size (new number of
	// slots). If the new size is less than the minimum number
	// of slots, use the minimum instead.

	// Must run in worst case O(n+m) where n is the number of
	// key-value pairs in the table and m is the number of
	// "slots" in the table.
	/**
	 * rehashes the table to new size.
	 * @param newSize the new size you want
	 */
	public void rehash(int newSize) {
		ArrayOfListsOfPairs<K, V> newStorage = new ArrayOfListsOfPairs<K, V>(newSize);
		KeyValuePair<K, V>[] KVArr = storage.getKVArr();
		storage = newStorage;
		int sizeSave = size;
		for (int i = 0; i < KVArr.length; i++) {
			add(KVArr[i].getKey(), KVArr[i].getValue());
		}
		size = sizeSave;
	}

	// If the key requested is in the table, change the associated value
	// to the provided value and return true. Otherwise return false.

	// Must run in worst case O(n) and average case O(n/m) where n is the
	// number of key-value pairs in the table and m is the number of "slots"
	// in the table.
	/**
	 * replaces a value for a KVP
	 * @param key the key you want
	 * @param value the valye you're replacing with
	 * @return if the action was successful
	 */
	public boolean replace(K key, V value) {
		return storage.replace(key, value, hashFunc(key));
	}

	// If the key requested is in the table, remove the key-value entry
	// and return true. Otherwise return false.

	// Must run in worst case O(n) and average case O(n/m) where n is the
	// number of key-value pairs in the table and m is the number of "slots"
	// in the table.
	/**
	 * removes a KVP.
	 * @param key key you wanna remove
	 * @return if the action worked.
	 */
	public boolean remove(K key) {
		K removed = storage.remove(key, hashFunc(key));
		if (removed == null)
			return false;
		size--;
		return true;
	}

	// If the key requested is in the table, return true. Otherwise return
	// false.

	// Must run in worst case O(n) and average case O(n/m) where n is the
	// number of key-value pairs in the table and m is the number of "slots"
	// in the table.
	/**
	 * If the key requested is in the table, return true. Otherwise return false.
	 * @param key key we're looking for
	 * @return if key is contained
	 */
	public boolean contains(K key) {
		return storage.contains(key, hashFunc(key));
	}

	// If the key requested is in the table, return the associated value.
	// Otherwise return null.

	// Must run in worst case O(n) and average case O(n/m) where n is the
	// number of key-value pairs in the table and m is the number of "slots"
	// in the table.
	/**
	 * gets requested key value
	 * @param key requested key
	 * @return value for key
	 */
	public V get(K key) {
		return storage.get(key, hashFunc(key));
	}

	// --------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	// --------------------------------------------------------

	public String toString() {
		// you may edit this to make string representations of your
		// lists for testing
		return storage.toString();
	}

	public static void main(String[] args) {
		// Some example testing code...

		// make a hash table and add something to it
		HashTable<Integer, String> ht = new HashTable<>(2);
		ht.add(2, "Apple");

		// get all pairs at location 0
		Collection<KeyValuePair<Integer, String>> pairs = ht.getInternalTable().getAllPairs(0);

		// should be one pair there...
		if (pairs.size() == 1) {
			// get the first pair from the list
			KeyValuePair<Integer, String> pair = pairs.iterator().next();

			// make sure it's the pair expected
			if (pair.getKey().equals(2) && pair.getValue().equals("Apple")) {
				System.out.println("Yay");
			}
		}
	}

	// --------------------------------------------------------
	// DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
	// --------------------------------------------------------

	// This will be used to check that you are setting
	// the storage up correctly... it's very important
	// that you (1) are using the ArrayOfListsOfPairs
	// provided and (2) don't edit this at all
	/**
	 * gets internal table.
	 * @return internal table
	 */
	public ArrayOfListsOfPairs<K, V> getInternalTable() {
		return storage;
	}
}