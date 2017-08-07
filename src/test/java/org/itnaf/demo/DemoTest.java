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

public class DemoTest {
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
		DemoTest demo = new DemoTest();
		demo.init();
		demo.doSomething();
	}
	
	public void init() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

//		oimClient= Client.getOIMClient(userName, password, url);
		
		String TARGET = "MAC";		
		
        oimClient = Init.init(TARGET);


		userOperationsIntf = oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
		fiIntf = oimClient.getService(Thor.API.Operations.tcFormInstanceOperationsIntf.class);
		provisioningOperationsIntf = oimClient.getService(oracle.iam.provisioning.api.ProvisioningService.class);
		userKey = OIMUtils.getUserKey(userOperationsIntf,("BSCTM8H"));
//		userKey = OIMUtils.getUserKey(userOperationsIntf,("STEST16"));
		fieldOrigin = new FieldOrigin(oimClient);
		
		listOfApps.add(new ArrayList<String>(Arrays.asList("pa", "2204")));
		listOfApps.add(new ArrayList<String>(Arrays.asList("ba", "1603")));
		listOfApps.add(new ArrayList<String>(Arrays.asList("rdr", "1623")));
		listOfApps.add(new ArrayList<String>(Arrays.asList("wfm", "1622")));
		listOfApps.add(new ArrayList<String>(Arrays.asList("wfm", "1962")));
		fieldOrigin.setUserKey(userKey);
		
	}
	
	public void doSomething() throws UserSearchException {
		
		// Load the data
		Lookups demoLookup = Lookups.loadLookups("D:/temp/demo3.xlsx");
		System.out.println(demoLookup.toString());
		
		
		for (int index = 0; index < listOfApps.size(); index++) {
			appName = listOfApps.get(index).get(0);
			fieldOrigin.setProcessInstanceKey(listOfApps.get(index).get(1));
			
			fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("PF.SERVER");
			
			String orcKey;
			try {
				orcKey = OIMUtils.getOrcKey(userOperationsIntf, "UD_LDAP_USR", userKey);
				long orcKeyLong = Long.parseLong(orcKey);
				
				long childFormDefKey = OIMUtils.findChildFormDefKey(fiIntf, orcKeyLong, "UD_MV1");
				
				
				OIMUtils.addToChildForm(fiIntf, childFormDefKey, orcKeyLong, "UD_MV1_VALUE", "mv212b");
				OIMUtils.removeFromChildForm(fiIntf, childFormDefKey, orcKeyLong, "UD_MV1", "UD_MV1_VALUE", "mv212b");
				HashMap<String, String> hm = OIMUtils.getEntries(fiIntf, childFormDefKey, orcKeyLong, "UD_MV1_VALUE");
				System.out.println(hm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int lookupIndex = 0; lookupIndex < listOfLookups.length; lookupIndex++) {
			
				String lookupKey = PREFIX + "." + appName + "." + listOfLookups[lookupIndex];
				
				System.out.println("Processing  " + lookupKey + " +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				Lookup appLookup = demoLookup.getLookup(lookupKey);
				
				if (appLookup == null) {
					continue;
				}
				HashMap <String, Quad> quadValuesHM = AttributeListUtils.getQuadValues(appLookup.getLookupEntries(), "Attr");
				System.out.println(quadValuesHM);
				
				// Now process each set of values
				for (String key: quadValuesHM.keySet()) {
					Quad processing = quadValuesHM.get(key);
					
					if (processing.getAttributeList() != null && processing.getAttributeList().trim().length() > 0) {
						FieldOriginResult result = fieldOrigin.getFieldOriginValuesHashMap(processing.getAttributeList());
						if (result.hasMultiValuedAttributes()) {
							ArrayList <FieldOriginResult>fieldOriginResultList = result.getAllRows();
							for (int rows = 0; rows < fieldOriginResultList.size(); rows++) {
								FieldOriginResult childResult = fieldOriginResultList.get(rows);
								
								if (childResult.allEntriesHaveValues()) {
								System.out.println("------------------------------- target " + processing.getTarget() + "---------------------------------\n" + 
										ParseUtils.processTemplate(childResult.getValues(), processing.getTransformation()));
								System.out.println("-------------------------------------------------------------------------------------\n");	
								} else {
									System.out.println("Values missing " + childResult.getValues());
								}
							}
							//System.out.println("Last row only: " + result.getLastRowOnly());
						} else {
							//System.out.println("AttributeList: " +  processing.getAttributeList() + ":" + result);
							System.out.println("------------------------------- target " + processing.getTarget() + "---------------------------------\n" + 
									ParseUtils.processTemplate(result.getValues(), processing.getTransformation()));
							System.out.println("------------------------------------------------------------------------------------------\n");
						}
					} else {
						//System.out.println("Quad " + processing);
					}
				}
			}
		}
	}
}
