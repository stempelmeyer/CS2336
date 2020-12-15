package Tickets;

public interface MyMap<K,V> {
	// remove all of the entries form this map
	public void clear();
	// return true if the specified key is in the map
	public boolean containsKey(K key);
	// return true if this map contains the specified value
	public boolean containsValue(V value);
	// return a set of entries in the map
	public java.util.Set<Entry<K,V>> entrySet();
	// return the value that matches the specified key
	public String get(K key);
	// return true if the map doesnt contain any entries
	public boolean isEmpty();
	// return a set consisting of the keys in this map
	public java.util.Set<K> keySet();
	// add an entry (key, value) into the map
	public String put(K key, String passW);
	// remove an entry for the specified key
	public void remove(K key);
	// return the number of mappings in this map
	public int size();
	// return a set consisting of the values in this map
	public java.util.Set<V> values();
	
	// define an inner class for entry
	public static class Entry<K, V> {
		K key;
		String password;
		
		public Entry(K key, String password) {
			this.key = key;
			this.password = password;
		}
		public K getKey() {
			return key;
		}
		public String getPassword() {
			return password;
		}
		@Override
		public String toString() {
			return "[" + key + ", " + password + "]";
		}
	}
}
