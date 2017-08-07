package org.itnaf.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.itnaf.metadata.metadataloader.Lookup;
import org.itnaf.metadata.metadataloader.Lookups;
import org.itnaf.metadata.utils.AttributeListUtils;
import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.metadata.utils.ParseUtils;
import org.itnaf.metadata.utils.Quad;
import org.itnaf.provisioning.Init;
import org.itnaf.utils.Client;
import org.itnaf.utils.OIMUtils;

import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ChildTableRecord;
import oracle.iam.provisioning.vo.ChildTableRecord.ACTION;

public class DemoTest3 {
	public static final String PREFIX = "sandp";
	public static final String OUD_SINGLE_VALUES = "oudSingleValues";
	public static final String JMS_SINGLE_VALUES = "jmsSingleValues";
	public static final String OUD_MULTI_VALUES = "oudMultiValues";
	public static final String JMS_MULTI_VALUES = "jmsMultiValues";
	public static final String [] listOfLookups = { OUD_SINGLE_VALUES, JMS_SINGLE_VALUES, OUD_MULTI_VALUES, JMS_MULTI_VALUES };
	public tcUserOperationsIntf userOperationsIntf = null;
	public ProvisioningService provisioningOperationsIntf = null;
	public tcFormInstanceOperationsIntf fiIntf = null;
	public long userKey  = 0L;
	
	ArrayList <ArrayList <String>> listOfApps = new ArrayList <ArrayList <String>>();
	FieldOrigin fieldOrigin = null;
	FieldOriginResult fieldOriginResult = null;
	String appName = null;

	OIMClient oimClient = null;

	public static void main(String[] args) throws Exception {
		DemoTest3 demo = new DemoTest3();
		demo.init();
	}
	
	public void init() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

//		oimClient= Client.getOIMClient(userName, password, url);
		
		String TARGET = "OIAM11GMAC";		
		
        oimClient = Init.init(TARGET);


		userOperationsIntf = oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
		fiIntf = oimClient.getService(Thor.API.Operations.tcFormInstanceOperationsIntf.class);
		provisioningOperationsIntf = oimClient.getService(oracle.iam.provisioning.api.ProvisioningService.class);
		userKey = OIMUtils.getUserKey(userOperationsIntf,("CN1N0Z7A"));
		fieldOrigin = new FieldOrigin(oimClient);
		
		
		OIMUtils.modifyUserResourceParentDataOld(userOperationsIntf, fiIntf, 
				"UD_LDAP_USR", "UD_LDAP_USR_LOCATION", userKey, "new location");
		
		
		
		
		
		OIMUtils.modifyUserResourceAccountParentData(""+userKey, "UD_LDAP_USR", "UD_LDAP_USR_LOCATION", 
	    		"blahhh", provisioningOperationsIntf);
		
		
		HashMap a = OIMUtils.getChildTableValues(""+userKey, "UD_LDAP_USR", "UD_MV1", "UD_MV1_VALUE", provisioningOperationsIntf);
		System.out.println(a);
		
		OIMUtils.addChildRecord(""+userKey, "UD_LDAP_USR", "UD_MV1", "UD_MV1_VALUE", "engineer5", provisioningOperationsIntf);
        // Stage Modify Child Record
        //HashMap<String,Object> modifyRecordData = new HashMap<String,Object>();
        //modifyRecordData.put(CHILD_ATTRIBUTE_NAME, "Engineer II");
        //ChildTableRecord modifyRecord = new ChildTableRecord();
        //modifyRecord.setChildData(modifyRecordData);
        //modifyRecord.setAction(ACTION.Modify);
        //modifyRecord.setRowKey(getChildRecordKeyByValue(CHILD_PROCESS_FORM_NAME, CHILD_ATTRIBUTE_NAME,  "Engineer", resourceAccount)); // <UD_TABLE>_KEY (Child Record Key)
        //modRecords.add(modifyRecord);
         
        // Stage Remove Child Record
        //HashMap<String,Object> removeRecordData = new HashMap<String,Object>();
        //ChildTableRecord removeRecord = new ChildTableRecord();
        //removeRecord.setChildData(removeRecordData);
        //removeRecord.setAction(ACTION.Delete);
        //removeRecord.setRowKey(getChildRecordKeyByValue(CHILD_PROCESS_FORM_NAME, CHILD_ATTRIBUTE_NAME,  "Engineer", resourceAccount)); // <UD_TABLE>_KEY (Child Record Key)
        //modRecords.add(removeRecord);
         
                 

//		Account account = OIMUtils.getUserResourceAccount("" + userKey, "UD_LDAP_USR", provisioningOperationsIntf);
//		System.out.println("" + account);
	}
}
