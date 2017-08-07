package org.itnaf.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thortech.xl.vo.AccessPolicyResourceData;

import java.util.logging.Logger;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ChildTableRecord;
import oracle.iam.provisioning.vo.ChildTableRecord.ACTION;

public class OIMUtils {
	final static Logger LOGGER = Logger.getLogger(OIMUtils.class.getName());
	private static String childFormTableName = "Structure Utility.Table Name";
	private static String childFormDefKey = "Structure Utility.Child Tables.Child Key";

	/**
	 * Get the userKey for a user given her User ID
	 * 
	 * @param usrIntf
	 * @param usrLogin
	 * @return
	 */
	public static long getUserKey(tcUserOperationsIntf usrIntf, String usrLogin) {
		long usrKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Users.User ID", usrLogin);
			tcResultSet rs = usrIntf.findUsers(hm);
			rs.goToRow(0);
			usrKey = rs.getLongValue("Users.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrKey;
	}

	/**
	 * Get a group key given the group name
	 * 
	 * @param grpIntf
	 * @param grpName
	 * @return
	 */
	public static long getGroupKey(tcGroupOperationsIntf grpIntf, String grpName) {
		long usrKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Groups.Group Name", grpName);
			tcResultSet rs = grpIntf.findGroups(hm);
			rs.goToRow(0);
			usrKey = rs.getLongValue("Groups.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrKey;
	}

	public static long getObjectKey(tcObjectOperationsIntf objIntf,
			String objName) {
		long objKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Objects.Name", objName);
			tcResultSet rs = objIntf.findObjects(hm);
			rs.goToRow(0);
			objKey = rs.getLongValue("Objects.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objKey;
	}

	public static long getFormDefKey(tcFormDefinitionOperationsIntf fdIntf,
			String formName) {
		long childDefKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Structure Utility.Table Name", formName);
			tcResultSet rs = fdIntf.findForms(hm);
			rs.goToRow(0);
			childDefKey = rs.getLongValue("Structure Utility.Key");
			// printResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return childDefKey;
	}
	
	public static long getITResKey(tcITResourceInstanceOperationsIntf itinstIntf,
			String itresName) {
		long itinstKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("IT Resources.Name", itresName);
			tcResultSet rs = itinstIntf.findITResourceInstances(hm);
			rs.goToRow(0);
			itinstKey = rs.getLongValue("IT Resources.Key");
			// printResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return itinstKey;
	}	
	

	/**
	 * Get IT resource parameter given a parameter name an IT resource instance key
	 * 
	 * @param itinstIntf
	 * @param itinstKey
	 * @param parameterName
	 * @return
	 */
	public static String getITResParameter(tcITResourceInstanceOperationsIntf itinstIntf,
			String itinstKey, String parameterName) {
		try {
			HashMap <String, String> hm = new HashMap <String, String> ();
			hm.put("IT Resources.Key", itinstKey);
			tcResultSet rs = itinstIntf.findITResourceInstances(hm);			
			
			rs = itinstIntf.getITResourceInstanceParameters(Long.parseLong(itinstKey));
			for (int index = 0; index < rs.getRowCount(); index++) {
				rs.goToRow(index);
				String parameterFound = rs.getStringValue("IT Resources Type Parameter.Name");
				if (parameterName.equalsIgnoreCase(parameterFound)) {
					return rs.getStringValue("IT Resource.Parameter.Value");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parameterName;
	}	
	

	public static long getProcessInstanceKey(tcUserOperationsIntf usrIntf,
			long usrKey, long oiuKey) {
		long orcKey = 0;
		try {
			tcResultSet rs = usrIntf.getObjects(usrKey);

			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				long thisOiuKey = rs
						.getLongValue("Users-Object Instance For User.Key");

				if (thisOiuKey == oiuKey) {
					return rs.getLongValue("Process Instance.Key");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orcKey;
	}

	public static long getObjectInstanceKey(tcUserOperationsIntf usrIntf,
			long usrKey, long oiuKey) {
		long obiKey = 0;
		try {
			tcResultSet rs = usrIntf.getObjects(usrKey);

			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				long thisOiuKey = rs
						.getLongValue("Users-Object Instance For User.Key");

				if (thisOiuKey == oiuKey) {
					return rs.getLongValue("Object Instance.Key");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obiKey;
	}
	
	/**
	 * Return the orcKey for the first instance of the form found for the user
	 * 
	 * @param formName
	 * @param usrKey
	 * @return
	 * @throws Exception
	 */
	public static String getOrcKey(tcUserOperationsIntf usrIntf, String formName, long usrKey) throws Exception {


		tcResultSet rs = usrIntf.getObjects(usrKey);
		
		for (int userObjects = 0; userObjects < rs.getRowCount(); userObjects++) {
			rs.goToRow(userObjects);
			String processFormName = rs.getStringValue("Process.Process Definition.Process Form Name");
			if (formName.equalsIgnoreCase(processFormName)) {
				return rs.getStringValue("Process Instance.Key");
			}
		}
		return null;
	}
	
	/**
	 * Add child table entry
	 * 
	 * @param userKey
	 * @param resourceTableName
	 * @param childTableName
	 * @param childEntryName
	 * @param value
	 * @param provisioningOperationsIntf
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 * @throws AccountNotFoundException
	 */
	public static void addChildRecord(String userKey, String resourceTableName, String childTableName, 
			String childEntryName, String value, ProvisioningService provisioningOperationsIntf) 
			throws UserNotFoundException, GenericProvisioningException, AccountNotFoundException {

		Account resourceAccount = getUserResourceAccount(userKey, resourceTableName, provisioningOperationsIntf);
		
		// Get account's child data
        Map<String, ArrayList<ChildTableRecord>> childData = resourceAccount.getAccountData().getChildData();
        ArrayList<ChildTableRecord> childDataEntries = childData.get(childTableName);
        
        // If there are any childTable entries
        if (childDataEntries != null) {
	        // Check to see if the value already exists, and if so, return
	        for (ChildTableRecord entry: childDataEntries) {
	        	String entryValue = (String) entry.getChildData().get(childEntryName);
	        	if (entryValue.equals(value)) {
	        		return;
	        	}
	        }
        }
         
        // Staging objects
        HashMap<String, Object> modParentData = new HashMap<String, Object>();
        Map<String, ArrayList<ChildTableRecord>> modChildData = new HashMap<String, ArrayList<ChildTableRecord>>();
        ArrayList<ChildTableRecord> modRecords = new ArrayList<ChildTableRecord>(); 
         
        // Stage Add Child Record
        HashMap<String,Object> addRecordData = new HashMap<String,Object>();
        addRecordData.put(childEntryName, value);
        ChildTableRecord addRecord = new ChildTableRecord();
        addRecord.setAction(ACTION.Add);
        addRecord.setChildData(addRecordData);
        modRecords.add(addRecord);
         
		// Put Child Form Name and its modified child data
        modChildData.put(childTableName, modRecords); 
	
        // Modify resource account
        modifyUserResourceAccountParentAndChildData("" + userKey, resourceAccount, 
        		modParentData, modChildData, provisioningOperationsIntf);               
	}

	/** 
	 * Modify child table entry
	 * 
	 * @param userKey
	 * @param resourceTableName
	 * @param childTableName
	 * @param childEntryName
	 * @param value
	 * @param provisioningOperationsIntf
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 * @throws AccountNotFoundException
	 */
	public static void modifyChildRecord(String userKey, String resourceTableName, String childTableName, 
			String childEntryName, String value, ProvisioningService provisioningOperationsIntf) 
			throws UserNotFoundException, GenericProvisioningException, AccountNotFoundException {

		Account resourceAccount = getUserResourceAccount(userKey, resourceTableName, provisioningOperationsIntf);
		
		// Get account's child data
        Map<String, ArrayList<ChildTableRecord>> childData = resourceAccount.getAccountData().getChildData();
        ArrayList<ChildTableRecord> childDataEntries = childData.get(childTableName);
        
        // If there are any childTable entries
        if (childDataEntries != null) {
	        // Check to see if the value already exists, and if so, return
	        for (ChildTableRecord entry: childDataEntries) {
	        	String entryValue = (String) entry.getChildData().get(childEntryName);
	        	if (entryValue.equals(value)) {
	        		return;
	        	}
	        }
        }
         
        // Staging objects
        HashMap<String, Object> modParentData = new HashMap<String, Object>();
        Map<String, ArrayList<ChildTableRecord>> modChildData = new HashMap<String, ArrayList<ChildTableRecord>>();
        ArrayList<ChildTableRecord> modRecords = new ArrayList<ChildTableRecord>(); 
         
        // Stage Add Child Record
        HashMap<String,Object> addRecordData = new HashMap<String,Object>();
        addRecordData.put(childEntryName, value);
        ChildTableRecord addRecord = new ChildTableRecord();
        addRecord.setAction(ACTION.Add);
        addRecord.setChildData(addRecordData);
        modRecords.add(addRecord);
         
		// Put Child Form Name and its modified child data
        modChildData.put(childTableName, modRecords); 

        // Modify resource account
        modifyUserResourceAccountParentAndChildData("" + userKey, resourceAccount, 
        		modParentData, modChildData, provisioningOperationsIntf);               
	}

	/**
	 * Delete child table entry
	 * 
	 * @param userKey
	 * @param resourceTableName
	 * @param childTableName
	 * @param childEntryName
	 * @param value
	 * @param provisioningOperationsIntf
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 * @throws AccountNotFoundException
	 */
	public static void deleteChildRecord(String userKey, String resourceTableName, String childTableName, 
			String childEntryName, String value, ProvisioningService provisioningOperationsIntf) 
			throws UserNotFoundException, GenericProvisioningException, AccountNotFoundException {

		Account resourceAccount = getUserResourceAccount(userKey, resourceTableName, provisioningOperationsIntf);
		
		// Get account's child data
        Map<String, ArrayList<ChildTableRecord>> childData = resourceAccount.getAccountData().getChildData();
        ArrayList<ChildTableRecord> childDataEntries = childData.get(childTableName);
        
        // If there are any childTable entries
        if (childDataEntries != null) {
	        // Check to see if the value already exists, and if so, return
	        for (ChildTableRecord entry: childDataEntries) {
	        	String entryValue = (String) entry.getChildData().get(childEntryName);
	        	if (entryValue.equals(value)) {
	        		return;
	        	}
	        }
        }
         
        // Staging objects
        HashMap<String, Object> modParentData = new HashMap<String, Object>();
        Map<String, ArrayList<ChildTableRecord>> modChildData = new HashMap<String, ArrayList<ChildTableRecord>>();
        ArrayList<ChildTableRecord> modRecords = new ArrayList<ChildTableRecord>(); 
         
        // Stage Add Child Record
        HashMap<String,Object> addRecordData = new HashMap<String,Object>();
        addRecordData.put(childEntryName, value);
        ChildTableRecord addRecord = new ChildTableRecord();
        addRecord.setAction(ACTION.Add);
        addRecord.setChildData(addRecordData);
        modRecords.add(addRecord);
         
		// Put Child Form Name and its modified child data
        modChildData.put(childTableName, modRecords); 

        // Modify resource account
        modifyUserResourceAccountParentAndChildData("" + userKey, resourceAccount, 
        		modParentData, modChildData, provisioningOperationsIntf);               
	}


	public static HashMap<String, String> getChildTableValues(String userKey, String resourceTableName, String childTableName, 
			String childEntryName, ProvisioningService provisioningOperationsIntf) throws UserNotFoundException, GenericProvisioningException {
		HashMap<String, String> entries = new HashMap<String, String> ();
		
		Account resourceAccount = getUserResourceAccount(userKey, resourceTableName, provisioningOperationsIntf);
		
		// Get account's child data
        Map<String, ArrayList<ChildTableRecord>> childData = resourceAccount.getAccountData().getChildData();
        ArrayList<ChildTableRecord> childDataEntries = childData.get(childTableName);
        
        // If there are any childTable entries
        if (childDataEntries != null) {
	        // Check to see if the value already exists, and if so, return
	        for (ChildTableRecord entry: childDataEntries) {
	        	String entryValue = (String) entry.getChildData().get(childEntryName);
	        	entries.put(entryValue, null);
	        }
        }
         
        return entries;
	}
	
	/**
	 * Get a user Primary resource based on the table name (the value we get from FieldOrigin)
	 * @param userKey
	 * @param tableName
	 * @param provisioningOperationsIntf
	 * @return
	 * @throws UserNotFoundException
	 * @throws GenericProvisioningException
	 */
    public static Account getUserResourceAccount(String userKey, String tableName, 
    		ProvisioningService provisioningOperationsIntf) throws UserNotFoundException, GenericProvisioningException {
        boolean populateAccountData = true;
        List<Account> accounts = provisioningOperationsIntf.getAccountsProvisionedToUser(userKey, populateAccountData);
         
        
        for (Account account : accounts) {
            String accountId = account.getAccountID();
            String appInstName = account.getAppInstance().getApplicationInstanceName();
            Map<String, Object> accountData = account.getAccountData().getData();
            
            if (!accountData.keySet().isEmpty()) {
            	Iterator iterator = accountData.keySet().iterator();
            	String firstKey = (String) iterator.next();
            	if (!firstKey.startsWith(tableName)) {
            		continue;
            	}
            }
            String accountStatus = account.getAccountStatus();
            LOGGER.info("Account Id: [" + accountId + "], Application Instance Name: [" + appInstName + 
            		"], Account Status: ["  + accountStatus + "], Account Data:[" + accountData + "]" );
             
            if (account.getAccountType().equals(ACCOUNT_TYPE.Primary)) {
	            // Only return enabled, provisioned, or disabled account
	            if(ProvisioningConstants.ObjectStatus.PROVISIONED.getId().equals(accountStatus) || 
	            		ProvisioningConstants.ObjectStatus.ENABLED.getId().equals(accountStatus) || 
	            		ProvisioningConstants.ObjectStatus.DISABLED.getId().equals(accountStatus)) {
	            	LOGGER.info("Return Account Id: [{0}] " + accountId);
	                return account;
	            }
            }
        }
        return null;
    }
     
    /**
     * Modifies a resource account of an OIM user (replaces parameter with name key with newValue
     * 
     * @param userKey           OIM usr_key
     * @param resourceAccount   Existing resource account to modify
     * @param modAttrs          Attributes to modify on the parent form
     * @throws AccountNotFoundException
     * @throws GenericProvisioningException
     * @throws UserNotFoundException 
     */
    public static void modifyUserResourceAccountParentData(String userKey, String tableName, String key, 
    		String newValue, ProvisioningService provOps) 
    		throws AccountNotFoundException, GenericProvisioningException, UserNotFoundException {
    	
        // Stage resource account modifications
    	Account resourceAccount = getUserResourceAccount(userKey, tableName, provOps);
    	String accountId  = resourceAccount.getAccountID();
        String processFormInstanceKey = resourceAccount.getProcessInstanceKey();
        Account modAccount = new Account(accountId, processFormInstanceKey, userKey);
    
        String formKey = resourceAccount.getAccountData().getFormKey();
        String udTablePrimaryKey = resourceAccount.getAccountData().getUdTablePrimaryKey();
        
        AccountData accountData = resourceAccount.getAccountData();
        Map<String, Object> modAttrs = accountData.getData();
        if (modAttrs.containsKey(key)) {
        	modAttrs.put(key, newValue);
        } else {
        	return;
        }
        
        AccountData newAccountData = new AccountData(formKey, udTablePrimaryKey , modAttrs);
        newAccountData.setChildData(accountData.getChildData());
        newAccountData.setEntitlements(accountData.getEntitlements());
            
        // Set necessary information to modified account
        modAccount.setAccountData(newAccountData);
        modAccount.setAppInstance(resourceAccount.getAppInstance());
    
        // Modify resource account
        provOps.modify(resourceAccount);

        LOGGER.info("Modification successful.");
    }
      
    /**
     * Deprecated way to modify an account on an OIM user
     * @param userOperationsIntf
     * @param fiIntf
     * @param tableName
     * @param fieldName
     * @param usrKey
     * @param value
     * @throws Exception
     */
    public static void modifyUserResourceParentDataOld(tcUserOperationsIntf userOperationsIntf, tcFormInstanceOperationsIntf fiIntf, 
    		String tableName, String fieldName, long usrKey, String value) throws Exception {
		String orcKey = OIMUtils.getOrcKey(userOperationsIntf, tableName, usrKey);
		long orcKeyLong = Long.parseLong(orcKey);
		String columnName = fieldName;
		Map <String, String> updateData = new HashMap <String, String> ();
		updateData.put(columnName, value);
		fiIntf.setProcessFormData(orcKeyLong, updateData);

    }
    
    /**
     * Modifies an account on an OIM user
     * @param userKey           OIM usr_key
     * @param resourceAccount   Existing resource account to modify
     * @param modAttrs          Attributes to modify on the parent form
     * @throws AccountNotFoundException
     * @throws GenericProvisioningException
     */
    public static void modifyUserResourceAccountParentAndChildData(String userKey, Account resourceAccount, HashMap<String, Object> modAttrs, 
    		Map<String, ArrayList<ChildTableRecord>> modChildData, ProvisioningService provOps) 
    		throws AccountNotFoundException, GenericProvisioningException {
        // Stage resource account modifications
        String accountId  = resourceAccount.getAccountID();
        String processFormInstanceKey = resourceAccount.getProcessInstanceKey();
        Account modAccount = new Account(accountId, processFormInstanceKey, userKey);
 
        // Setup account data object
        String formKey = resourceAccount.getAccountData().getFormKey();
        String udTablePrimaryKey = resourceAccount.getAccountData().getUdTablePrimaryKey();
        AccountData accountData = new AccountData(formKey, udTablePrimaryKey, modAttrs);
        accountData.setChildData(modChildData); // set child data
         
        // Set necessary information to modified account
        modAccount.setAccountData(accountData);
        modAccount.setAppInstance(resourceAccount.getAppInstance());
 
        // Modify resource account
        provOps.modify(modAccount);
        LOGGER.info("Modification successful.");
    }
     
    /**
     * Prints the child data in each child form of a resource account.
     * @param childData   Child data of user's resource account
     */
    public static void printResourceAccountChildData(Map<String, ArrayList<ChildTableRecord>> childData) {
        // Child Data Iterator 
        Iterator iter = childData.entrySet().iterator();
         
        // Iterator each child form
        while(iter.hasNext()) {
            Map.Entry pairs = (Map.Entry) iter.next();
            String childFormName = (String) pairs.getKey();
            ArrayList<ChildTableRecord> childFormData = (ArrayList<ChildTableRecord>) pairs.getValue();
            System.out.println("[Child Form Name: {0}], [Child Form Data: {1}] " +  childFormName + " cfd " + childFormData);
             
            // Iterate records in a child form
            for (ChildTableRecord record : childFormData) {
                ACTION action = record.getAction();
                Map<String, Object> childRecordData = record.getChildData();
                String rowKey = record.getRowKey();
                System.out.println("[Action: {0}], [Child Record Data: {1}], [Row Key: {2}] " + action + " cRD = " + childRecordData + " rowkey " + rowKey);
            }
        }
    }   

	/**
	 * Finds the child form definition key for the specified child table and
	 * returns it
	 * 
	 * @param fiIntf
	 * @param orcKeyLong
	 * @param childTable
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static long findChildFormDefKey(tcFormInstanceOperationsIntf fiIntf, long orcKeyLong, String childTable) throws Exception {
		long formDefKey = fiIntf.getProcessFormDefinitionKey(orcKeyLong);
		
		int processFormVersion = fiIntf.getProcessFormVersion(orcKeyLong);
		tcResultSet formDefChildResults = fiIntf.getChildFormDefinition(formDefKey, processFormVersion);
		int childCount = formDefChildResults.getRowCount();
		for (int i = 0; i < childCount; i++) {
			formDefChildResults.goToRow(i);
			String thisTableName = formDefChildResults.getStringValue(childFormTableName);
			if (childTable.equalsIgnoreCase(thisTableName)) {
				return formDefChildResults.getLongValue(childFormDefKey);
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param fiIntf
	 * @param childFormDefKey
	 * @param processInstanceKey
	 * @param childColumnName
	 * @param data
	 */
	public static void addToChildForm(tcFormInstanceOperationsIntf fiIntf, long childFormDefKey, 
			long processInstanceKey, String childColumnName, String data) {
		Map<String, String> childData = new HashMap<String, String>();
		childData.put(childColumnName, data);
		try {
			tcResultSet formDefChildResults = fiIntf.getProcessFormChildData(childFormDefKey, processInstanceKey);
			for (int index = 0; index < formDefChildResults.getRowCount(); index++) {
				formDefChildResults.goToRow(index);
				String existingValue = formDefChildResults.getStringValue(childColumnName);
				if (existingValue.equalsIgnoreCase(data)) {
					System.out.println("Data " + data + " already exists ");
					return;
				}
			}
			fiIntf.addProcessFormChildData(childFormDefKey, processInstanceKey, childData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating " + childColumnName + " with data " + data + e);
		}
	}
	
	public static void removeFromChildForm(tcFormInstanceOperationsIntf fiIntf, long childFormDefKey, 
			long processInstanceKey, String tableName, String childColumnName, String data) {
		Map<String, String> childData = new HashMap<String, String>();
		childData.put(childColumnName, data);
		try {
			tcResultSet formDefChildResults = fiIntf.getProcessFormChildData(childFormDefKey, processInstanceKey);
			for (int index = 0; index < formDefChildResults.getRowCount(); index++) {
				formDefChildResults.goToRow(index);
				String existingValue = formDefChildResults.getStringValue(childColumnName);			
				if (existingValue.equalsIgnoreCase(data)) {
		            long rowKey = formDefChildResults.getLongValue(tableName + "_KEY");
		            fiIntf.removeProcessFormChildData(childFormDefKey, rowKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error updating " + childColumnName + " with data " + data + e);
		}
	}
	
	public static HashMap <String, String>  getEntries(tcFormInstanceOperationsIntf fiIntf, long childFormDefKey, 
			long processInstanceKey, String childColumnName) {
		HashMap <String, String> results = new HashMap <String, String> ();
		try {
			tcResultSet formDefChildResults = fiIntf.getProcessFormChildData(childFormDefKey, processInstanceKey);
			for (int index = 0; index < formDefChildResults.getRowCount(); index++) {
				formDefChildResults.goToRow(index);
				String existingValue = formDefChildResults.getStringValue(childColumnName);
				results.put(existingValue, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Utility class to print resultSet
	 * 
	 * @param rs
	 * @throws Exception
	 */
	public static void printResultSet(tcResultSet rs) throws Exception {

		System.out.println("COUNT = " + rs.getRowCount() + "\n\n");
		String[] cols = rs.getColumnNames();

		for (int i = 0; i < rs.getRowCount(); ++i) {
			rs.goToRow(i);

			for (int j = 0; j < cols.length; j++) {
				if (cols[j].indexOf("Row Version") == -1) {
					System.out.println(cols[j] + "\t\t:"
							+ rs.getStringValue(cols[j]));
				}
			}
			System.out.println();
		}
	}

	public static void printCollection(Collection col) throws Exception {

		Iterator it = col.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}
	}

	public static Map mapFromRS(tcResultSet rs) throws Exception {

		HashMap map = new HashMap();
		String[] cols = rs.getColumnNames();

		// for (int i = 0; i < rs.getRowCount(); ++i) {
		rs.goToRow(0);

		for (int j = 0; j < cols.length; j++) {
			String col = cols[j];
			map.put(col, rs.getStringValue(col));

		}

		// }
		return map;
	}

	public static void printMap(Map m) throws Exception {

		Iterator it = m.entrySet().iterator();

		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			System.out.println(e.getKey() + " ==> " + e.getValue());
		}
	}
	
	public static void printAPRD(AccessPolicyResourceData aprd) throws Exception {
		System.out.println("Object name = " + aprd.getObjectName());
		System.out.println("Object key = " + aprd.getObjectKey());
		
		System.out.println("Form name = " + aprd.getFormName());
		System.out.println("Form key = " + aprd.getFormDefinitionKey());
		System.out.println("Form type = " + aprd.getFormType());
		
		HashMap hm = aprd.getFormData();
		
		System.out.println("Form data is: ");
		printMap(hm);
		
		return;

	}

	public static long[] toNativeLongArray(Long[] lArr) {
		long[] arr = new long[lArr.length];
		for (int i=0 ; i<arr.length ; i++) {
			arr[i] = lArr[i].longValue();
		}
		
		return arr;
	}
	
	public static long[] toNativeLongArray(String[] lArr) {
		long[] arr = new long[lArr.length];
		for (int i=0 ; i<arr.length ; i++) {
			arr[i] = Long.parseLong(lArr[i]);
		}
		
		return arr;
	}
}
