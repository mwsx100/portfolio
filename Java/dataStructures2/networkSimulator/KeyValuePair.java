package project5;

//This class represents a key-value pair. It is completed for you,
//but you need to add JavaDocs.

//--------------------------------------------------------
//DO NOT EDIT ANYTHING BELOW THIS LINE (except to add JavaDocs)
//--------------------------------------------------------

public class KeyValuePair<K, V> {
	private K key;
	private V value;

	public KeyValuePair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public boolean equals(Object o) {
		if (o instanceof KeyValuePair) {
			return key.equals(((KeyValuePair) o).key);
		}
		return false;
	}

	public int hashCode() {
		return key.hashCode();
	}

	public String toString() {
		return "(" + key + "," + value + ")";
	}
}