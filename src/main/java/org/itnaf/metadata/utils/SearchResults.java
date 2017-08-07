package org.itnaf.metadata.utils;

import java.util.HashMap;

public class SearchResults {
	public String type;
	public String key;
	public HashMap <String, Object> attributes;
	
	public SearchResults(String type, String key, HashMap<String, Object> attributes) {
		this.type = type;
		this.key = key;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "UserSearchResults [" + type + "=" + key + ", attributes=" + attributes + "]";
	}
}
