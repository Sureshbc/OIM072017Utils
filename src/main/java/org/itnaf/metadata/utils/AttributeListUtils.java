package org.itnaf.metadata.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


import oracle.iam.identity.exception.UserSearchException;

public class AttributeListUtils {
	final static Logger LOGGER = Logger.getLogger(AttributeListUtils.class.getName());
	
	private void processData(ArrayList <Quad> quadValues, FieldOrigin fieldOrigin) {
		for (int index = 0; index < quadValues.size(); index++) {
			try {
				String key = quadValues.get(index).getAttributeList();
				LOGGER.severe("key = " + key + " target " + quadValues.get(index).getTarget());
				HashMap <String, String> results = fieldOrigin.getFieldOriginValuesHashMap(key).getValues();
				LOGGER.severe(key + results.toString());
			} catch (UserSearchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param entries lookup entries
	 * @param attributePrefix String to search
	 * 
	 * @return
	 */
	public static HashMap <String, Quad> getQuadValues(HashMap<String, String> entries, String attributePrefix) {
		HashMap <String, Quad> quadEntries = new  HashMap <String, Quad> ();
		
		String ATTRIBUTELIST_KEY = "attributeList";
		String RULE_KEY = "rule";
		String TRANSFORMATION_KEY = "transformation";
		String TARGET_KEY = "target";

		for (String key: entries.keySet()) {
			ArrayList <String> parsedKey = ParseUtils.parseDotDelimited(key);
			
			Quad processing = null;
			if (quadEntries.containsKey(parsedKey.get(0))) {
				processing = quadEntries.get(parsedKey.get(0));
			} else {
				processing = new Quad();
				quadEntries.put(parsedKey.get(0), processing);
			}
			
			if (ATTRIBUTELIST_KEY.equalsIgnoreCase(parsedKey.get(1))) {
				processing.setAttributeList(entries.get(key));	
			} else if (RULE_KEY.equalsIgnoreCase(parsedKey.get(1))) {
				processing.setRule(entries.get(key));	
			} if (TRANSFORMATION_KEY.equalsIgnoreCase(parsedKey.get(1))) {
				processing.setTransformation(entries.get(key));	
			} if (TARGET_KEY.equalsIgnoreCase(parsedKey.get(1))) {
				processing.setTarget(entries.get(key));	
			} 
		}
		return quadEntries;
	}
		


}
