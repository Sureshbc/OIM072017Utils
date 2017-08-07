package org.itnaf.provisioning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itnaf.utils.ITResourceUtilities2;

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

public class TestITResourceUtilities {
	private static String TARGET = "OIAM11G"; 
	private static OIMClient oimClient = null;

	
	public static void main(String[] args) throws Exception {

		if (args.length < 3) {	
			System.out.println(args.length + " arguments");
			System.exit(0);
		}
		
        oimClient = Init.init(args[0], args[1], args[2]);
        
        if (oimClient == null) {
        	System.out.println("OIMClient is null");
        	System.exit(0);
        }
        
        TestITResourceUtilities tcpl = new TestITResourceUtilities();

        tcpl.getITResources("D7UV4RL8"); 
        
        oimClient.logout();
        System.exit(0);
	}
	

	public void getITResources(String type) {
        tcITResourceInstanceOperationsIntf tcITResourceIntf = oimClient.getService(tcITResourceInstanceOperationsIntf.class);
        ITResourceUtilities2 itr2 = new ITResourceUtilities2(tcITResourceIntf);
        
        long[] itrKeys = itr2.findAllITResourceInstances(tcITResourceIntf);
        
        for (int i = 0; i < itrKeys.length; i++) {
        	System.out.println(itrKeys[i]);
        	
        	Hashtable itResInfo = itr2.findNameValuePairsForITResourceInstance(tcITResourceIntf, itrKeys[i]);
        	System.out.println(itResInfo + "");
        }
	}
	
	public void exit() {
		oimClient.logout();
		oimClient = null;
//		System.exit(0);
	}

}