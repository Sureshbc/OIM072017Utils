package org.itnaf.utils;

import java.util.*;

import Thor.API.*;
import Thor.API.Exceptions.*;
import Thor.API.Operations.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
/**
 * Iterate through ITResource instances in Xellerate.
 * 
 * @author fantim
 */
public class ITResourceUtilities2 {
    static Log logger = LogFactory.getLog(ITResourceUtilities2.class);

	private tcITResourceInstanceOperationsIntf itrIntf;
	
	HashArray itResourceInstances = null;

    /**
     * Default Constructor - Used by Xellerate, but not needed since all methods
     * are static
     */
    public ITResourceUtilities2() {
    }

	/**
	 * Initialize database connection.
	 * 
	 * @param dataBase
	 * @throws tcAPIException
	 */
	public ITResourceUtilities2(com.thortech.xl.dataaccess.tcDataProvider dataBase) throws tcAPIException {
			itrIntf = (tcITResourceInstanceOperationsIntf) tcUtilityFactory.getUtility(dataBase,
			"Thor.API.Operations.tcITResourceInstanceOperationsIntf");
	}

    /**
     * Used when calling from another class that doesn't have a database handle
     * but does have the ITResource factory handle.
     * 
     * @param _itrIntf
     */
    public ITResourceUtilities2(tcITResourceInstanceOperationsIntf _itrIntf) {
        itrIntf = _itrIntf;
    }
    
    /**
     * Method to find a given parameter in a specific IT Resource Type, for
     * example a specific domain in all "AD Server" domains
     * 
     * @param itResources
     *            tcUtilityFactory
     * @param itResourceTypeName
     *            String - IT Resource Type Name, e.g. "AD Server"
     * @param parameterName
     *            String - Parameter to search for
     * @param parameterValue
     *            String - Value to search for
     * @param ignoreCase
     *            boolean - Ignore case during comparison
     * @return long - IT Resource Instance, or -1 if not found
     */
    public static long findITResourceInstanceByParameter(
            tcITResourceInstanceOperationsIntf itResources,
            String itResourceTypeName, String parameterName,
            String parameterValue, boolean ignoreCase) {
        long[] itResourceInstances = findITResourceInstancesByITResourceTypeName(
                itResources, itResourceTypeName);

        logger.debug("Found " + itResourceInstances.length
                + " resource instances of type " + itResourceTypeName);
        for (int i = 0; i < itResourceInstances.length; i++) {
            Hashtable ht = findNameValuePairsForITResourceInstance(itResources,
                    itResourceInstances[i]);
            logger.debug("instance " + i + " to compare has values " + ht);
            logger.debug("Comparing " + parameterName + " to " + parameterValue
                    + ":" + ht);
            if (ht.containsKey(parameterName)) {
                logger.debug("Found key");
                if (ignoreCase) {
                    if (((String) ht.get(parameterName))
                            .equalsIgnoreCase(parameterValue)) {
                        logger.info("Returning" + ht.get(parameterName)
                                + " ---> " + itResourceInstances[i]);
                        return itResourceInstances[i];
                    }
                } else {
                    if (((String) ht.get(parameterName)).equals(parameterValue)) {
                        logger.info("Returning" + ht.get(parameterName)
                                + " ---> " + itResourceInstances[i]);
                        return itResourceInstances[i];
                    }
                }
            }
        }
        return -1;
    }
    
    public long findITResourceInstanceByParameter(
            String itResourceTypeName, String parameterName,
            String parameterValue, boolean ignoreCase) {
        return findITResourceInstanceByParameter(
                itrIntf,
                itResourceTypeName, parameterName,
                parameterValue, ignoreCase);
    }

    /**
     * @param itResources tcUtilityFactory
     * @param itResourceTypeName IT Resource type
     * @return Array of matching IT Resource instance keys
     */
    public static long[] findAllITResourceInstances(
            tcITResourceInstanceOperationsIntf itResources) {

        long[] keys = null;
        HashMap map = new HashMap();
        try {
            tcResultSet rs = itResources.findITResourceInstances(map);
            keys = new long[rs.getRowCount()];
            for (int i = 0; i < rs.getRowCount(); i++) {
                rs.goToRow(i);
                String[] columnNames = rs.getColumnNames();
                for (int j = 0; j < columnNames.length; j++) {
                    logger.debug("Column " + columnNames[j] + " has value "
                            + rs.getStringValue(columnNames[j]));
                    System.out.println("Column " + columnNames[j] + " has value "
                            + rs.getStringValue(columnNames[j]));
                }
                long key = rs.getLongValue("IT Resource.Key");
                keys[i] = key;
            }
        } catch (tcAPIException api) {
            logger.error(
                    "Error in findITResourcesByITResourceTypeName finding ", api);
        } catch (tcColumnNotFoundException cnf) {
            logger.error(
                    "Error in findITResourcesByITResourceTypeName finding ", cnf);
        }
        if (keys != null) {
            logger.debug("Returning " + keys.length + " keys ");
        } else {
            logger.debug("Returning 0 keys ");
        }
        return keys;
    }


    /**
     * @param itResources tcUtilityFactory
     * @param itResourceTypeName IT Resource type
     * @return Array of matching IT Resource instance keys
     */
    public static long[] findITResourceInstancesByITResourceTypeName(
            tcITResourceInstanceOperationsIntf itResources,
            String itResourceTypeName) {

        long[] keys = null;
        HashMap map = new HashMap();
        map.put("IT Resource Type Definition.Server Type", itResourceTypeName);
        try {
            tcResultSet rs = itResources.findITResourceInstances(map);
            keys = new long[rs.getRowCount()];
            for (int i = 0; i < rs.getRowCount(); i++) {
                rs.goToRow(i);
                String[] columnNames = rs.getColumnNames();
                for (int j = 0; j < columnNames.length; j++) {
                    logger.debug("Column " + columnNames[j] + " has value "
                            + rs.getStringValue(columnNames[j]));
                    System.out.println("Column " + columnNames[j] + " has value "
                            + rs.getStringValue(columnNames[j]));
                }
                long key = rs.getLongValue("IT Resource.Key");
                keys[i] = key;
                logger.debug("Key for it resource of type "
                        + itResourceTypeName + " = " + key);
                System.out.println("Key for it resource of type "
                        + itResourceTypeName + " = " + key);
            }
        } catch (tcAPIException api) {
            logger.error(
                    "Error in findITResourcesByITResourceTypeName finding "
                            + itResourceTypeName, api);
        } catch (tcColumnNotFoundException cnf) {
            logger.error(
                    "Error in findITResourcesByITResourceTypeName finding "
                            + itResourceTypeName, cnf);
        }
        if (keys != null) {
            logger.debug("Returning " + keys.length + " keys ");
        } else {
            logger.debug("Returning 0 keys ");
        }
        return keys;
    }

    /**
     * This is a wrapper around the method of the same name, for calling from XL adaptor factory
     * when we have already populated the instance handles.
     * 
     * It has one extra feature - it remembers the list of ITResource instance handles
     * by storing them in a HashArray.  This is because adaptor factory cannot cope with arrays
     * of longs.  We provide a method getNext() to iterate through them.
     * 
     * @param itResourceTypeName name of IT Resource type, e.g. "Database"
     */
    public long[] findITResourceInstancesByITResourceTypeName(String itResourceTypeName) {
        itResourceInstances = new HashArray(findITResourceInstancesByITResourceTypeName(itrIntf, itResourceTypeName));
        return itResourceInstances.array();
    }

    /**
     * Used by XL adaptor factory to iterate over IT Resources.
     * 
     * @return Next IT Resource instance key or 0 if there are no more
     */
    public long getNext() {
        if (itResourceInstances != null) {
            return itResourceInstances.nextArrayElementAsLong();
        } else {
            return 0;
        }
    }
    
    /**
     * 
     * @param itResources
     *            tcUtilityFactory
     * @param key
     *            long
     * @return Hashtable
     */
    public static Hashtable findNameValuePairsForITResourceInstance(
            tcITResourceInstanceOperationsIntf itResources, long key) {
        Hashtable ht[] = ITResourceUtilities2.getITResourceInstanceParameters(
                itResources, key);
        Hashtable ht2 = new Hashtable();
        Hashtable ht3 = new Hashtable();
        for (int j = 0; j < ht.length; j++) {
            try {
	            ht2.put(ht[j].get("IT Resources Type Parameter.Name"),
	            		ht[j].get("IT Resource.Parameter.Value"));
	            ht3.put(ht[j].get("IT Resources Type Parameter.Name"),
	            		ht[j].get("IT Resource.Parameter.Value"));
            } catch (Exception e) {
            	try {
    	            ht2.put(ht[j].get("IT Resource Type Definition.IT Resource Type Parametr.Field Name"),
    	            		ht[j].get("IT Resource.Parameter.Value"));
    	            ht3.put(ht[j].get("IT Resource Type Definition.IT Resource Type Parametr.Field Name"),
    	            		ht[j].get("IT Resource.Parameter.Value"));
            	} catch (Exception e1) {

            	}
            }
        }
        ht3.remove("Admin Password");
        logger.info("Got the following values for it resource instance "
                + key + ":" + ht3);
        return ht2;
    }

    public Hashtable findNameValuePairsForITResourceInstance(long key) {
        return findNameValuePairsForITResourceInstance(itrIntf, key);
    }

    public static Hashtable[] getITResourceInstanceParameters(
            tcITResourceInstanceOperationsIntf itResources, long key) {

        Hashtable[] result = null;
        try {
            HashMap map = new HashMap();
            map.put("IT Resource.Key", "" + key);
            tcResultSet rs = null;
            rs = itResources.getITResourceInstanceParameters(key);
            result = new Hashtable[rs.getRowCount()];
            for (int i = 0; i < rs.getRowCount(); i++) {
                result[i] = new Hashtable();
                rs.goToRow(i);
                String[] columnNames = rs.getColumnNames();
                for (int j = 0; j < columnNames.length; j++) {
                    logger.debug(columnNames[j] + ": "
                            + rs.getStringValue(columnNames[j]));
                    logger.debug(columnNames[j] + ": "
                            + rs.getStringValue(columnNames[j]));
                    result[i].put(columnNames[j], rs
                            .getStringValue(columnNames[j]));
                }
            }
        } catch (tcITResourceNotFoundException irnfe) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, irnfe);
        } catch (tcAPIException api) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, api);
        } catch (tcColumnNotFoundException cnf) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, cnf);
        }
        if (result != null) {
            for (int i = 0; i < result.length; i++) {
                logger.debug("getITResourceInstanceParameters returning [" + i
                        + "]:" + result[i]);
            }
        } else {
            logger.debug("getITResourceInstanceParameters returning null");
        }
        return result;
    }

    public static String getITResourceInstanceParametersValueForGivenName(
            String name, tcITResourceInstanceOperationsIntf itResources,
            long key) {

        try {
            tcResultSet rs = itResources.getITResourceInstanceParameters(key);
            for (int i = 0; i < rs.getRowCount(); i++) {
                rs.goToRow(i);
                if (rs.getStringValue(
                                "IT Resource Type Definition.IT Resource Type Parametr.Field Name")
                        .equals(name)) {
                    logger.debug("getITResourceInstanceParameters returning "
                            + rs.getStringValue("IT Resource.Parameter.Value"));
                    return (rs.getStringValue("IT Resource.Parameter.Value"));
                }
            }
        } catch (tcAPIException api) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, api);
        } catch (tcITResourceNotFoundException rnf) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, rnf);
        } catch (tcColumnNotFoundException cnf) {
            logger.error("Error in getITResourceInstanceParameters finding "
                    + key, cnf);
        }
        logger.debug("getITResourceInstanceParameters returning null");
        return null;
    }
    
    /**
     * This is a wrapper around the method of the same name, for calling from XL adaptor factory
     * when we have already populated the instance handles.
     * 
     * @param name Parameter key
     * @param key IT Resource handle
     * @return Parameter value
     */
    public String getITResourceInstanceParametersValueForGivenName(String name, long key) {
        return getITResourceInstanceParametersValueForGivenName(name, itrIntf, key);
    } 
    
    /**
     * Find an exact match for an IT Resource instance given the name and type
     * 
     * @param name Instance name, e.g. "Sybase dev server"
     * @param type Instance type, e.g. "Database"
     * @return Key of matching IT Resource instance, or -1 if not found
     * @throws tcAPIException
     * @throws tcColumnNotFoundException
     */
    public long findITResourceInstanceByNameAndType(String name, String type) throws tcAPIException, tcColumnNotFoundException {
	    tcResultSet instances;
	    HashMap map = new HashMap();
	    
	    map.put("IT Resource Type Definition.Server Type", type);
	    map.put("IT Resource.Name", name);
	    instances = itrIntf.findITResourceInstances(map);
	    if (instances.getRowCount() != 1) {
	        return -1;
	    } else {
	        return instances.getLongValue("IT Resource.Key");
	    }	
    }
}