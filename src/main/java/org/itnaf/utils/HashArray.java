package org.itnaf.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Work with Hashtables and Arrays within Adaptor Factory.
 * Starts with an empty instance of each, and provides
 * an 'add' method for each major java type.
 *
 * @author mfanti
 */
public class HashArray {
	static Log log = LogFactory.getLog(HashArray.class);

	HashMap map;
	Vector vector;

	/**
	 * Initialize with zero elements in both the hashmap and array.
	 */
	public HashArray() {
		map = new HashMap();
		vector = new Vector();
	}
	
	/**
	 * Initialize with zero elements in the array and with an existing hashtable.
	 * 
	 * @param table Existing hashtable to be copied into the hashmap.  Can be null.
	 */
	public HashArray(Hashtable table) {
		map = new HashMap();
		vector = new Vector();
		Object s;
		
		if (table != null) {
			Enumeration e = table.keys();
			while (e.hasMoreElements()) {
			    s = e.nextElement();
			    map.put(s, table.get(s));
			}
		}
	}
	
	/**
	 * Initialize with zero elements in the hashtable and an existing array of values.
	 * 
	 * @param values Array of values to be copied into the array.  Can be null.
	 */
	public HashArray(long []values) {
		map = new HashMap();
		vector = new Vector();
	    array(values);
	}
	
	/**
	 * Add key=value to hashmap.
	 * 
	 * @param key
	 * @param value
	 * @return Updated map
	 */
	public Map hash(String key, int value) {
		map.put(key, Long.toString(value));
		return map;
	}

	/**
	 * Add key=value to hashmap.
	 * 
	 * @param key
	 * @param value
	 * @return Updated map
	 */
	public Map hash(String key, long value) {
		map.put(key, Long.toString(value));
		return map;
	}

	/**
	 * Add key=value to hashmap.
	 * 
	 * @param key
	 * @param value
	 * @return Updated map
	 */
	public Map hash(String key, String value) {
		map.put(key, value);
		return map;
	}

	/**
	 * Just return the map without adding any pairs.
	 * 
	 * @return Current map
	 */
	public Map hash() {
		return map;
	}
	
	/**
	 * Add multiple attribute-value pairs to the hash, represented by an LF-separated string.
	 * 
	 * @param elements "attr1: value1\nattr2: value2\nattr3: value3\nOK" 
	 * @return empty string if last pair was simply "OK", else the last pair (error string)
	 */
	public String addToHash(String elements) {
		log.debug(elements);
		String array[] = elements.split("\n");
		
		if (array[array.length-1].compareTo("OK") != 0) {
		    log.debug("Not processing attributes due to error: " + array[array.length-1]);
			return array[array.length-1];  // error string
		}
		
		for (int i = 0; i < array.length-1; i++) {
			String pair[] = array[i].split(": ");
			if (pair.length == 2) {  // sanity check (some attrs have no values)
				hash(pair[0], pair[1]);
			}
		}
		return "";
	}
		
	/**
	 * Lookup one attribute value in the hashtable.
	 * 
	 * @param attribute Name of attribute
	 * @return Value of attribute, or null if not found
	 */
	public String getHashValue(String attribute) {
		return (String)map.get(attribute);
	}
	
	/**
	 * Add one or more String values to array.  Once you've started using this method,
	 * do NOT add other types to the array or you will get a ClassCastException when
	 * you try to retrieve them.
	 * 
	 * @param values Either a single value, or multiple values separated by a linefeed
	 * @return Updated array converted to String type
	 */
	public Object[] array(String values) {
		String[] array = values.split("\n");
		
		for (int i = 0; i < array.length; i++) {
		    vector.add(array[i]);
		}
		return vector.toArray(array);  // 'array' here is just to set runtime type	
	}
	
	/**
	 * Add one long value to array.  Once you've started using this method,
	 * do NOT add other types to the array or you will get a ClassCastException when
	 * you try to retrieve them.
	 * 
	 * @param value
	 * @return Updated array converted to long type
	 */
	public long[] array(long value) {
		vector.add(new Long(value));  // hope garbage collection frees it later
		return this.array();
	}
	
	/**
	 * Add multiple long value to array.  Once you've started using this method,
	 * do NOT add other types to the array or you will get a ClassCastException when
	 * you try to retrieve them.
	 * 
	 * @param values - array of values.  Can be null.
	 * @return Updated array converted to long type
	 */
	public long[] array(long[] values) {
	    if (values != null) {
		    for (int i = 0; i < values.length; i++) {
		        vector.add(new Long(values[i]));  // hope garbage collection frees it later
		    }
	    }
		return this.array();
	}
	
	/**
	 * Just return the array without adding any elements.
	 * 
	 * @return Array converted to long type
	 */
	public long[] array() {
		/*
		 * Because vectors must contain objects, we can't directly insert the value above.
		 * Must now manually convert to an array of longs which is what caller wants.  Ugh.
		 */
		long array [] = new long[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			array[i] = ((Long) vector.elementAt(i)).longValue();
		}
		return array;		
	}
	
	/**
	 * Remove the first element from the array, decreasing the size of the array.
	 * Only use this method if you added String types earlier.
	 * 
	 * @return Element converted to String type.  Returns null if the array is empty.
	 */
	public String nextArrayElementAsString() {
	    if (vector.size() == 0) {
	        return null;
	    } else {
	        return (String)vector.remove(0);
	    }
	}
	
	/**
	 * Remove the first element from the array, decreasing the size of the array.
	 * Only use this method if you added long types earlier.
	 * 
	 * @return Element converted to long type.  Returns 0 if the array is empty.
	 */
	public long nextArrayElementAsLong() {
	    if (vector.size() == 0) {
	        return 0;
	    } else {
	        return ((Long) vector.remove(0)).longValue();
	    }
	}
	
	public static void main(String []argv) {
	    String s;
	    long l;
		HashArray h = new HashArray();
		
		System.out.println(h.addToHash("attr1: value1\nattr2: value2\nattr3: value3\nOK"));
		System.out.println(h.getHashValue("attr2"));
		System.out.println(h.getHashValue("attr4"));
		System.out.println();
		
		h = new HashArray();
		h.array("mfanti\nmoorej\n");
		do {
		    s = h.nextArrayElementAsString();
		    System.out.println(s);
		} while (s != null);
		System.out.println();
		
		h = new HashArray();
		h.array("23\n553\n4");
		do {
		    l = h.nextArrayElementAsLong();
		    System.out.println(l);
		} while (l != 0);
	}
}
