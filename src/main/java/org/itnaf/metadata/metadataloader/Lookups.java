package org.itnaf.metadata.metadataloader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.thortech.xl.dataaccess.tcDataProvider;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidAttributeException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.platform.OIMClient;

/**
 * Class that stores all different lookups that comprise the configuration
 * for a given application instance.
 * 
 * @author Marco Fanti
 *
 */
public class Lookups {
	final static Logger LOGGER = Logger.getLogger(Lookups.class.getName());
	private ArrayList<Lookup> lookups = new ArrayList<Lookup>();

	public void add(Lookup newLookup) {
		lookups.add(newLookup);
	}

	public ArrayList<Lookup> getAllLookups() {
		return lookups;
	}
	
	/**
	 * Return true is the array of lookup entries is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (lookups == null || lookups.size() == 0);
	}

	public ArrayList<Lookup> getAllLookupsThatStartWith(String prefix) {
		ArrayList<Lookup> matchingLookups = new ArrayList<Lookup>();
		for (Lookup lookup : lookups) {
			if (lookup.getName().startsWith(prefix)) {
				matchingLookups.add(lookup);
			}
		}
		return matchingLookups;
	}

	public Lookup getLookup(String name) {
		for (Lookup lookup : lookups) {
			if (lookup.getName().equals(name)) {
				return (lookup);
			}
		}
		return null;
	}

	public String toString() {
		String toString = "";
		for (Lookup lookup : lookups) {
			toString += "\nLookup -> " + lookup.toString();
		}
		return toString;
	}

	/**
	 * Load matching lookups from OIM using a dataProvider
	 * 
	 * @param prefix
	 * @param shortName
	 * @param dataProvider
	 * @return
	 */
	public static Lookups loadLookupsFromOIM(String prefix, String shortName, tcDataProvider dataProvider) {
		tcLookupOperationsIntf lookupIntf = null;
		try {
			lookupIntf = (tcLookupOperationsIntf)tcUtilityFactory.getUtility(dataProvider, 
					Thor.API.Operations.tcLookupOperationsIntf.class.getName());
		} catch (tcAPIException e) {
			e.printStackTrace();
		}
		return retrieveLookup(prefix, shortName, lookupIntf); 
	}

	/**
	 * Load matching lookups from OIM using an oimClient
	 * 
	 * @param prefix
	 * @param shortName
	 * @param oimClient
	 * @return
	 */
	public static Lookups loadLookupsFromOIM(String prefix, String shortName, OIMClient oimClient) {
		tcLookupOperationsIntf lookupIntf = ExcelToOIM.getLookupService(oimClient);
		return retrieveLookup(prefix, shortName, lookupIntf); 
	}

	public static Lookups retrieveLookup(String prefix, String shortName, tcLookupOperationsIntf lookupIntf) {
		Lookups lookups = new Lookups();
		
		ArrayList<HashMap<String, String>> entries = null;
		
		try {
			HashMap <String, String> map = new HashMap <String, String> ();
			map.put("Lookup Definition.Code", prefix + "." + shortName + "*");
			tcResultSet lookupRS = lookupIntf.findAvailableLookups(map);
			for (int i = 0; i < lookupRS.getRowCount(); i++) {
				lookupRS.goToRow(i);
				Lookup lookup = new Lookup(lookupRS.getStringValue("Lookup Definition.Code"));
				LOGGER.info("Getting entries for Lookup " + lookup.getName());
				tcResultSet lookupEntries = lookupIntf.getLookupValues(lookup.getName());
				for (int j = 0; j < lookupEntries.getRowCount(); j++) {
					lookupEntries.goToRow(j);
					lookup.addLookupEntry(lookupEntries.getStringValue("Lookup Definition.Lookup Code Information.Code Key"), 
							lookupEntries.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
				}
				LOGGER.info("Added " + lookup.getLookupEntries().size() + "  entries for Lookup " + lookup.getName());
				lookups.add(lookup);
			}
		} catch (tcAPIException e) { 
			e.printStackTrace();
		} catch (tcInvalidLookupException e) {
			e.printStackTrace();
		} catch (tcInvalidAttributeException e) {
			e.printStackTrace();			
		} catch (tcColumnNotFoundException e) {
			e.printStackTrace();
		}
		return lookups;
	}

	private static ArrayList<HashMap<String, String>> getRSData(tcResultSet rs) {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String[] columnNames = rs.getColumnNames();
		try {
			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				HashMap<String, String> hm = new HashMap<String, String>();
				for (int j = 0; j < columnNames.length; j++) {
					String columName = columnNames[j];
					hm.put(columName, rs.getStringValue(columName));
				}
				data.add(hm);
			}
		} catch (tcAPIException e) { 
			e.printStackTrace();
		} catch (tcColumnNotFoundException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Public method to load related lookup entries into a Lookups object from an Excel file
	 * 
	 * @param fileName
	 * @return
	 */
	public static Lookups loadLookups(String fileName) {
		Lookups lookups = new Lookups();
		try {
			FileInputStream file = new FileInputStream(new File(fileName));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			int activeSheetIndex = workbook.getActiveSheetIndex();
			LOGGER.info("Reading " + (activeSheetIndex + 1) + " pages");
			for (int sheetIndex = 0; sheetIndex <= activeSheetIndex; sheetIndex++) {
				LOGGER.info("" + sheetIndex + " ");
				XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
				String sheetName = sheet.getSheetName();
				LOGGER.info("Lookup name = " + sheetName);
				Lookup lookup = new Lookup(sheetName);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				loadSheet: while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					int rowIndex = row.getRowNum();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();

					String[] newEntry = new String[2];
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						int columnIndex = cell.getColumnIndex();
						// Check the cell type and format accordingly
						if (columnIndex < 2) {
							String cellValue = cell.getStringCellValue();
							if (cellValue != null) {
								cellValue = cellValue.trim();
								if (cellValue.isEmpty()) {
									break loadSheet;
								}
							}
							newEntry[columnIndex] = cellValue;
							System.out.print(rowIndex + ":" + columnIndex + ":" + cell.getStringCellValue() + " s");
						} else {
							break;
						}

					}
					if (newEntry[0] != null && newEntry[1] != null) {
						lookup.addLookupEntry(newEntry[0], newEntry[1]);
					}
				}
				lookups.add(lookup);

			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lookups;
	}

}
