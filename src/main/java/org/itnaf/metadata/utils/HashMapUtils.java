package org.itnaf.metadata.utils;

import java.util.HashMap;

public class HashMapUtils {
	public static String getHashMapKeyValuePairs(HashMap <String, String> hashMap) {
		String result = "";
		for (String key : hashMap.keySet()) {
			result += "\n" + key + " : " + hashMap.get(key);
		}
		return result;
	}

}
