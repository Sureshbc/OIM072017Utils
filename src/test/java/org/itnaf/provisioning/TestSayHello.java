package org.itnaf.provisioning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itnaf.utils.Client;
import org.itnaf.utils.OIMUtils;
import org.itnaf.utils.UserClient;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import oracle.iam.api.OIMService;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestData;
import oracle.iam.vo.OperationResult;

public class TestSayHello {
	private static final String RESOURCE_ATTR_NAME = "Objects.Name"; // Attribute Name for Name
	private static final String RESOURCE_ATTR_KEY = "Objects.Key"; // Attribute Name for Key
	private static String TARGET = "OIAM11G"; 
	private static OIMClient oimClient = null;
	private static Client client = null;

	public TestSayHello() throws Exception {
		TARGET = "OIAM11GMAC";		
		TARGET = "AWS";
		TARGET = "OIAM11G";				
		
		// Set the one to test as last
//		TARGET = "OIAM11GMAC";		
		
        oimClient = Init.init(TARGET);
        
        if (oimClient == null) {
        	System.out.println("OIMClient is null");
        	System.exit(0);
        }
        
        client = new Client(oimClient);
	}
	
	public static void main(String[] args) throws Exception {
		TestSayHello testSayHello = new TestSayHello();
        if (args.length > 0) {
        	testSayHello.testSayHello(args[0]); // LJZGDYJ");        	
        } else {        
        	testSayHello.testSayHello("D7UV4RL8"); // LJZGDYJ");
        	testSayHello.testSayHello("X.002"); // LJZGDYJ");
        	testSayHello.testSayHello("X.003"); // LJZGDYJ");
	        oimClient.logout();
        }
        System.exit(0);
	}
		
	public boolean testSayHello(String usrLogin) throws Exception {
		UserClient userClient = new UserClient(client);

		Init.APPLICATION_INSTANCE_NAME = "SAYH";
		Init.IT_RESOURCE_NAME = "SAYHELLO";
		Init.RESOURCE_ATTR_NAME_STRING = "SayHello User";

		ApplicationInstanceService aiSvc = oimClient.getService(ApplicationInstanceService.class);
        ProvisioningService provSvc = oimClient.getService(ProvisioningService.class);
        UserManager usrMgr = oimClient.getService(UserManager.class);
        tcITResourceInstanceOperationsIntf tcITResourceIntf = oimClient.getService(tcITResourceInstanceOperationsIntf.class);
        tcObjectOperationsIntf resourceService = oimClient.getService(tcObjectOperationsIntf.class);
        OIMService unifiedService = oimClient.getService(OIMService.class);
        
		SearchCriteria criteriaAi = new SearchCriteria(ApplicationInstance.APPINST_NAME, "*", SearchCriteria.Operator.BEGINS_WITH);
		
		List<ApplicationInstance> aiLst = aiSvc.findApplicationInstance(criteriaAi, new HashMap<String, Object>());
		
		ApplicationInstance applicationInstance = null;
		Long applicationInstanceKey = 0L;
		for(ApplicationInstance ai : aiLst) {
			if (ai.getApplicationInstanceName().equalsIgnoreCase(Init.APPLICATION_INSTANCE_NAME)) {
				applicationInstanceKey = ai.getApplicationInstanceKey();
				applicationInstance = ai;
				break;
			}
        	
        }

		String userKey = userClient.getUserKey(usrLogin);
		boolean isProvisioned = provSvc.isApplicationInstanceProvisionedToUser(userKey, applicationInstance);
		
		System.out.println(usrLogin + " is provisioned: " + isProvisioned);

		return (isProvisioned);
	}
	
	public void exit() {
		oimClient.logout();
		oimClient = null;
		client = null;
//		System.exit(0);
	}
}