package org.itnaf.metadata.metadataloader;

import java.util.ArrayList;
import java.util.HashMap;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.HashMapUtils;

public class Lookup {
	private String name;
	private HashMap<String, String> lookupEntries = null;
	private ArrayList <String> userProfileEntries = new ArrayList<String>();
	private ArrayList <String> requestDataEntries = new ArrayList<String>();
	private ArrayList <String> processFormEntries = new ArrayList<String>();
	private ArrayList <String> processChildFormEntries = new ArrayList<String>();
	private ArrayList <String> otherProcessFormEntries = new ArrayList<String>();
	private ArrayList <String> otherProcessChildFormEntries = new ArrayList<String>();
	private ArrayList <String> applicationStatusEntries = new ArrayList<String>();
	private ArrayList <String> otherEntries = new ArrayList<String>();
	
	public String getName() {
		return name;
	}

	public HashMap<String, String> getLookupEntries() {
		return lookupEntries;
	}

	public Lookup(String name) {
		this.name = name;
		lookupEntries = new HashMap<String, String>();
	}
	
	public String toString() {
		return "Lookup Name: " + name + " values: " +
				HashMapUtils.getHashMapKeyValuePairs(lookupEntries);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
	    if (other == this) {
	    	return true;
	    }
	    if (!(other instanceof Lookup)) {
	    	return false;
	    }
	    Lookup otherLookup = (Lookup)other;
		return (otherLookup.name.equals(this.name));
	}
	
	public void addLookupEntry(String key, String value) {
		if (!key.isEmpty() && !value.isEmpty()) {
			if (key.startsWith(FieldOrigin.USER_PROFILE + ".")) {
				userProfileEntries.add(key);
			} else if (key.startsWith(FieldOrigin.REQUEST_DATA + ".")) {
				requestDataEntries.add(key);
			} else if (key.startsWith(FieldOrigin.PROCESS_FORM + ".")) {
				processFormEntries.add(key);
			} else if (key.startsWith(FieldOrigin.PROCESS_CHILD_FORM + ".")) {
				processChildFormEntries.add(key);
			} else if (key.startsWith(FieldOrigin.OTHER_PROCESS_FORM + ".")) {
				otherProcessFormEntries.add(key);
			} else if (key.startsWith(FieldOrigin.OTHER_PROCESS_CHILD_FORM + ".")) {
				otherProcessChildFormEntries.add(key);
			} else if (key.startsWith(FieldOrigin.APPLICATION_STATUS + ".")) {
				applicationStatusEntries.add(key);
			} else {
				otherEntries.add(key);
			}			
			lookupEntries.put(key, value);
		}
	}
	
	public boolean hasUserProfileEntries() {
		return (userProfileEntries.size() > 0);
	}
	
	public ArrayList <String> getUserProfileEntries() {
		return (userProfileEntries);
	}
	
	public boolean hasRequestDataEntries() {
		return (requestDataEntries.size() > 0);		
	}
	
	public ArrayList <String> getRequestDataEntries() {
		return (requestDataEntries);		
	}
	
	public boolean hasProcessFormEntries() {
		return (processFormEntries.size() > 0);
	}
	
	public ArrayList <String> getProcessFormEntries() {
		return (processFormEntries);
	}
	
	public boolean hasProcessChildFormEntries() {
		return (processChildFormEntries.size() > 0);
	}
	
	public ArrayList <String> getProcessChildFormEntries() {
		return (processChildFormEntries);
	}
	
	public boolean hasOtherProcessFormEntries() {
		return (otherProcessFormEntries.size() > 0);
	}
	
	public ArrayList <String> getOtherProcessFormEntries() {
		return (otherProcessFormEntries);
	}
	
	public boolean hasOtherProcessChildFormEntries() {
		return (otherProcessChildFormEntries.size() > 0);
	}
	
	public ArrayList <String> getOtherProcessChildFormEntries() {
		return (otherProcessChildFormEntries);
	}
	
	public boolean hasApplicationStatusEntries() {
		return (applicationStatusEntries.size() > 0);
	}
	
	public ArrayList <String> getApplicationStatusEntries() {
		return (applicationStatusEntries);
	}
	
	public boolean hasOtherEntries() {
		return (otherEntries.size() > 0);
	}
	
	public ArrayList <String> getOtherEntries() {
		return (otherEntries);
	}
}