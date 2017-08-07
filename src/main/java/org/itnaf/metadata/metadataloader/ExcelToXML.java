package org.itnaf.metadata.metadataloader;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


public class ExcelToXML {
	final static Logger LOGGER = Logger.getLogger(ExcelToXML.class.getName());
	final static String WORKING_DIRECTORY = 
			"./";
	final static String INPUT_FILE = 
			"src/test/resources/easample.xlsx";
	final static String OUTPUT_FILE = 
			"src/test/resources/easample.xml";
	final static String PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<xl-ddm-data version=\"11.1.2.2.0\" user=\"XELSYSADM\" " +
			"database=\"jdbc:oracle:thin:@localhost:1521/OIAMDB\" " +
			"exported-date=\"1448485290288\" description=\"\">";
	
	final static String LOOKUP_PART1 = "<Lookup repo-type=\"RDBMS\" name=\"";
	final static String LOOKUP_PART2 = "\" subtype=\"Lookup\">";
	
	
	final static String LOOKUP_PART3 = "<LKU_UPDATE>1413429931000</LKU_UPDATE>" +
			"<LKU_DATA_LEVEL>0</LKU_DATA_LEVEL>" +
			"<LKU_TYPE>l</LKU_TYPE>" + 
			"<LKU_MEANING>";
	final static String LOOKUP_PART4 = "</LKU_MEANING>";
	final static String LOOKUP_ENTRY_PART1 = "<LookupValue repo-type=\"RDBMS\" id=\"LKV395\">" +
				"<LKV_DECODED>"; 
	final static String LOOKUP_ENTRY_PART2 = "</LKV_DECODED>" +
				"<LKV_DISABLED>0</LKV_DISABLED>" +
				"<LKV_LANGUAGE>en</LKV_LANGUAGE>" +
				"<LKV_UPDATE>1413429931000</LKV_UPDATE>" +
				"<LKV_DATA_LEVEL>0</LKV_DATA_LEVEL>" +
				"<LKV_COUNTRY>US</LKV_COUNTRY>" +
				"<LKV_ENCODED>";
	final static String LOOKUP_ENTRY_PART3 = "</LKV_ENCODED>" +
				"</LookupValue>";
	final static String LOOKUP_PART5 = "</Lookup>";
	final static String SUFFIX = "</xl-ddm-data>";

	/**
	 * Generate OIM valid XML from a loaded Lookups object
	 * 
	 * @param lookups
	 * @param fileName
	 */
	public static void generateXML(Lookups lookups, String fileName) {
		PrintWriter writer = null;
		HashMap<String, String> lookupEntries = null;
		StringBuffer xml = new StringBuffer(PREFIX);
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			ArrayList <Lookup> allLookups = lookups.getAllLookups();
			for (Lookup lookup: allLookups) {
				String name = lookup.getName();
				xml.append(LOOKUP_PART1 + name + LOOKUP_PART2 + LOOKUP_PART3 + name + LOOKUP_PART4);
					
				lookupEntries = lookup.getLookupEntries();
				
				for (String key : lookupEntries.keySet()) {
					xml.append(LOOKUP_ENTRY_PART1 + key + LOOKUP_ENTRY_PART2 + 
							lookupEntries.get(key) + LOOKUP_ENTRY_PART3);
				}
				xml.append(LOOKUP_PART5);	
			}
			xml.append(SUFFIX);
			writer.print(xml);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static void main(String[] args) {
		Lookups lookups = Lookups.loadLookups(
				WORKING_DIRECTORY + INPUT_FILE);	
		LOGGER.info(lookups.toString());
		generateXML(lookups, WORKING_DIRECTORY + OUTPUT_FILE);
	}
}
