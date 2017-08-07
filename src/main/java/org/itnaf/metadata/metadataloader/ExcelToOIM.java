package org.itnaf.metadata.metadataloader;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.logging.Logger;

import org.itnaf.utils.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcDuplicateLookupCodeException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.platform.OIMClient;

public class ExcelToOIM {
	final static Logger LOGGER = Logger.getLogger(ExcelToOIM.class.getName());
	
	public static tcLookupOperationsIntf getLookupService(OIMClient oimClient) {
		return oimClient.getService(tcLookupOperationsIntf.class);
	}

	
	public static void findAndDeleteAllLookupsThatMatch(String prefix, OIMClient oimClient) {
		tcLookupOperationsIntf lookupIntf = getLookupService(oimClient);
		
        if (prefix == null || prefix.length() < 4) {
        	LOGGER.severe("Lookup prefix can not be null or less than 4 characters: " + prefix);
        	return;
        }
        try {
			HashMap <String, String> map = new HashMap <String, String> ();
			map.put("Lookup Definition.Code", prefix + "*");
			tcResultSet lookupRS = lookupIntf.findAvailableLookups(map);
			for (int i = 0; i < lookupRS.getRowCount(); i++) {
				lookupRS.goToRow(i);
				Lookup lookup = new Lookup(lookupRS.getStringValue("Lookup Definition.Code"));
				tcResultSet lookupEntries = lookupIntf.getLookupValues(lookup.getName());
				for (int j = 0; j < lookupEntries.getRowCount(); j++) {
					lookupEntries.goToRow(j);
					lookup.addLookupEntry(lookupEntries.getStringValue("Lookup Definition.Lookup Code Information.Code Key"), 
							lookupEntries.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
				}
				lookupIntf.removeLookupCode(lookup.getName());

				LOGGER.info("Deleted " + lookup.getName());
			}
		} catch (Exception e) {
			LOGGER.severe("Error deleting lookups " + prefix + ":" + e);
		}
	}
	
	
	/**
	 * Load the populated Lookups object into OIM
	 * 
	 * @param lookups
	 * @param oimClient
	 */
	public static void createOIMLookup(Lookups lookups, OIMClient oimClient) {
		
		tcLookupOperationsIntf lookupIntf = getLookupService(oimClient);
		
        // Create Lookup
        try {
        	HashMap <String, String> lookupEntries = null;
        	ArrayList <Lookup> allLookups = lookups.getAllLookups();
			for (Lookup lookup: allLookups) {
				String name = lookup.getName();
				LOGGER.info("Creating lookup " + name);;
				
				try {
					lookupIntf.removeLookupCode(name);
				} catch (Exception e) {
					LOGGER.warning("Lookup did not exist " + name);;
				}
				
				lookupIntf.addLookupCode(name);
				LOGGER.info("Added lookup " + name);;
									
				lookupEntries = lookup.getLookupEntries();
				
				for (String key : lookupEntries.keySet()) {
					lookupIntf.addLookupValue(name, key, lookupEntries.get(key), "en", "US");
					LOGGER.info("Adding lookup entry " + key + ":" + lookupEntries.get(key) + " to " + name);;
				}
			}
		} catch (tcAPIException e) {
			e.printStackTrace();
		} catch (tcInvalidLookupException e) {
			e.printStackTrace();
		} catch (tcInvalidValueException e) {
			e.printStackTrace();
		} catch (tcDuplicateLookupCodeException e) {
			e.printStackTrace();
		}
        System.out.println("Created Lookup Table");
	}
}
