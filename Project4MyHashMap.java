package Tickets;
import java.util.LinkedList;

public class MyHashMap<K,V> implements MyMap<K,V> {
	// define the default hash-table size. Must be a power of 2
	public static int DEFAULT_INITIAL_CAPACITY = 4;
	
	// define the max hash-table size. 1<< 30 is the same as 2^30
	public static int MAXIMUM_CAPACITY = 1 << 30;
	
	// current hash-table capacity. Capacity is a power of 2
	private int capacity;
	
	private static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
	
	private float loadFactorThreshold;
	
	private int size = 0;
	
	// hash table is an array with each cell being a linked list
	LinkedList<MyMap.Entry<K,V>>[] table;
	
	// construct a map with the default capacity and load factor
	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
	}
	
	public MyHashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
	}
	
	public MyHashMap(int initialCapacity, float loadFactorThreshold) {
		if (initialCapacity > MAXIMUM_CAPACITY) // IS THIS CORRECT??? 
			this.capacity = MAXIMUM_CAPACITY;
		else
			this.capacity = trimToPowerOf2(initialCapacity);
		
		this.loadFactorThreshold = loadFactorThreshold;
		table = new LinkedList[capacity];		
	}
	
	// remove all entries from the map
	@Override 
	public void clear() {
		size = 0;
		removeEntries();
	}
	
	@Override
	public boolean containsKey(K key) {
		if(get(key) != null)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean containsValue(V value) {
		for (int i = 0; i < capacity; i++) {
			if (table[i] != null) {
				LinkedList<Entry<K, V>> bucket = table[i];
				for(Entry<K, V> entry : bucket) {
					if (entry.getPassword().equals(value))
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public java.util.Set<Entry<K,V>> entrySet(){
		java.util.Set<MyMap.Entry<K, V>> set = new java.util.HashSet<>();
	
		for (int i = 0; i < capacity; i++) {
			if (table[i] != null) {
				LinkedList<Entry<K, V>> bucket = table[i];
				for(Entry<K, V> entry : bucket) 
					set.add(entry);
			}
		}
		return set;
	}
	
	// return value that matches with the specified key
	@Override
	public String get(K key) {
		if (key == null)
			return null;
		int bucketIndex = hash(key.hashCode());
		if(table[bucketIndex] != null) {
			LinkedList<Entry<K,V>> bucket = table[bucketIndex];
			for(Entry<K,V> entry : bucket) {
				if(entry.getKey().equals(key))
					return entry.getPassword();
			}
		}
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	// return a set consisting of keys in the map
	@Override
	public java.util.Set<K> keySet(){
		java.util.Set<K> set = new java.util.HashSet<K>();
		for(int i = 0; i < capacity; i++) {
			if(table[i] != null) {
				LinkedList<Entry<K,V>> bucket = table[i];
				for(Entry<K,V> entry: bucket)
					set.add(entry.getKey());
			}
		}
		return set;
	}
	
	// add an entry (key,value) into the map
	@Override
	public String put(K key,  String value) {
		// the key is already in the map
		if(get(key) != null) { 
			int bucketIndex = hash(key.hashCode());
			LinkedList<Entry<K,V>> bucket = table[bucketIndex];
			for(Entry<K,V> entry: bucket) {
				if(entry.getKey().equals(key)) {
					String oldValue = entry.getPassword();
					entry.password = value;
					return oldValue;
				}
			}
		}
		
		// check load factor
		if(size >= capacity*loadFactorThreshold) {
			if (capacity == MAXIMUM_CAPACITY)
				throw new RuntimeException("Exceeding maximum capacity");
			
			rehash();
		}
		int bucketIndex = hash(key.hashCode());
		
		// create a linked list for the bucket if not already created
		if(table[bucketIndex] == null) {
			table[bucketIndex] = new LinkedList<Entry<K,V>>();
		}
		
		// add a new entry (key, value) to the hash table[index]
		table [bucketIndex].add(new MyMap.Entry<K,V>(key, value));
		
		size++;
		
		return value;
	}
	
	// remove the entries for the specified key
	@Override
	public void remove(K key) {
		int bucketIndex = hash(key.hashCode());
		
		// remove the first entry that matches the key from the bucket
		if(table[bucketIndex] != null) {
			LinkedList<Entry<K,V>> bucket = table[bucketIndex];
			for(Entry<K,V> entry : bucket) {
				if(entry.getKey().equals(key)) {
					bucket.remove(entry);
					size--;
					break; // remove just one entry that matches the key
				}
			}
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	// return a set consisiting of the values on this map
	@Override
	public java.util.Set<V> values(){
		java.util.Set<V> set = new java.util.HashSet<>();
		
		for (int i = 0; i < capacity; i++) {
			if (table[i] != null) {
				LinkedList<Entry<K,V>> bucket = table[i];
				for (Entry<K,V> entry: bucket)
					set.add((V)entry.getPassword());
			}
		}
		return set;
	}
	
	private int hash(int hashCode) {
		return supplementalHash(hashCode) & (capacity-1);
	}
	private static int supplementalHash(int h) {
		h ^= (h >>> 20) ^ (h >>> 12);
		return (h >>> 7) ^ (h >>> 4);
	}
	// return a power of 2 for initial capacity
	private int trimToPowerOf2(int initialCapacity) {
		int capacity = 1;
		while(capacity < initialCapacity)
			capacity <<= 1; // same as capaciy *= 2
		
		return capacity;
	}
	// remove all entries from bucket
	private void removeEntries() {
		for (int i = 0; i < capacity; i++) {
			if (table[i] != null) 
				table[i].clear();
		}
	}
	// rehash the map
	private void rehash() {
		java.util.Set<Entry<K,V>> set = entrySet();
		capacity <<=1;
		table = new LinkedList[capacity]; // create new hash table
		size = 0;
		
		for(Entry<K,V> entry: set)
			put(entry.getKey(), entry.getPassword()); // store to new table
	}
	
	// return a string representation for this map
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		
		for(int i =0; i < capacity; i++) {
			if(table[i] != null && table[i].size() > 0) {
				for (Entry<K,V> entry: table[i])
					builder.append(entry);
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	public int indexOfKey(K key) {
		int tracker = 0;
		if (key == null)
			return -1;
		int bucketIndex = hash(key.hashCode());
		if(table[bucketIndex] != null) {
			LinkedList<Entry<K,V>> bucket = table[bucketIndex];
			for(Entry<K,V> entry : bucket) {
				tracker++;
				if(entry.getKey().equals(key))
					return tracker;
			}
		}
		return -1; 
	}
}
