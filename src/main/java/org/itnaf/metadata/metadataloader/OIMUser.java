package org.itnaf.metadata.metadataloader;

import java.util.HashMap;

public class OIMUser {
	private static HashMap <String, String> userMap;
	
	public OIMUser() {
		userMap = new HashMap<String, String>();
		userMap.put("FirstName", "Marco");
		userMap.put("LastName", "Fanti");
		userMap.put("MiddleInitial", "A");
		userMap.put("Email", "Marco");
		userMap.put("DisplayName", "Marco A. Fanti");
		userMap.put("UserLogin", "MFANTI");
		userMap.put("TimeZone", "EST");
	}

	public void print() {
		System.out.println("User: ");
		for (String key : OIMUser.userMap.keySet()) {
			System.out.println(key + " : " + OIMUser.userMap.get(key));
		}
	}
}
