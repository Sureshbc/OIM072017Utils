package org.itnaf.metadata.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class FieldOriginResult {
//	protected String type = null;
//	protected String key = null;
	protected HashMap <String, String>  values = null;
	protected String processKey = null;
	protected String requestKey = null;
	protected String userKey = null;

	
	/**
	 * Generic return type for form and request data
	 * @param type
	 * @param key
	 * @param values
	 */
	public FieldOriginResult(/* String type, String key, */ HashMap <String, String> values, 
			String processKey, String requestKey, String userKey) {
		super();
//		this.type = type;
//		this.key = key;
		this.values = values;
		this.processKey = processKey;
		this.requestKey = requestKey;
		this.userKey = userKey;
	}
	
//	public String getType() {
//		return type;
//	}

//	public String getKey() {
//		return key;
//	}
	
	public HashMap <String, String> getValues() {
		return values;
	}
	
	public void setValues(HashMap <String, String> values) {
		this.values = values;
	}

	public boolean allEntriesHaveValues() {
		for (String key: values.keySet()) {
			String value = values.get(key);
			if (value == null || value.trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean hasMultiValuedAttributes() {
		for (String key: values.keySet()) {
			if (key.indexOf("]") > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get last row only from multi-result list
	 * @return
	 */
	public FieldOriginResult getLastRowOnly() {
		return getRows(true).get(0); 
	}
	
	/**
	 * Get all rows only from multi-result list
	 * @return
	 */
	public ArrayList <FieldOriginResult> getAllRows() {
		return getRows(false); 
	}
	
	/**
	 * Get all rows from a multi-valued attribute list of results
	 * 
	 * @return
	 */
	public ArrayList <FieldOriginResult> getRows(boolean getLastOnly) {
		ArrayList <FieldOriginResult> fieldOriginResultList = new ArrayList <FieldOriginResult>();
		HashMap <String, String> rowKeys = new HashMap <String, String>();
		for (String key: values.keySet()) {
			if (key.indexOf("]") > 0) {
				String rowKey = key.substring(key.indexOf("]") + 1);
				if (!rowKeys.containsKey(rowKey)) {
					rowKeys.put(rowKey, null);
				}
			}
		}		
		
		int maxRowKey = -1;
		for (String rowKey: rowKeys.keySet()) {
			FieldOriginResult fieldOriginResult = null;
			HashMap <String, String>  rowValues = new HashMap <String, String>();
			int rowKeyInt = Integer.parseInt(rowKey);
			
			if (getLastOnly && rowKeyInt < maxRowKey) {
				continue;
			} else {
				maxRowKey = rowKeyInt;
				for (String key: values.keySet()) {
					if (key.indexOf("]") > 0) {
						if (key.endsWith(rowKey)) {
							String newKey = key.substring(1, key.indexOf("]"));
							rowValues.put(newKey, values.get(key));
						}					
					} else {
						rowValues.put(key, values.get(key));
					}
				}
			}
			fieldOriginResult = new FieldOriginResult(rowValues, processKey, requestKey, userKey);
			if (getLastOnly) {
				fieldOriginResultList = new ArrayList <FieldOriginResult>();
			}
			fieldOriginResultList.add(fieldOriginResult);
		}
		return fieldOriginResultList;
	}
	
	@Override
	public String toString() {
		String result = "FieldOriginResult [" + // type=" + type + ", key=" + key + 
				"value=" + values;
		if (processKey != null) {
			result += ", processKey=" + processKey;
		}
		if (requestKey != null) {
			result += ", requestKey=" + requestKey;
		}
		if (userKey != null) {
			result += ", userKey=" + userKey;
		}
		return  result + "]";
	}
}
