package org.itnaf.demo;

import java.util.HashMap;
import java.util.Map;

import org.itnaf.utils.Client;
import org.itnaf.utils.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.platform.OIMClient;

public class DemoTest2 {
	private static String childFormTableName = "Structure Utility.Table Name";
	private static String childFormDefKey = "Structure Utility.Child Tables.Child Key";

	OIMClient oimClient = null;
	long userKey = 0L;
	public static tcUserOperationsIntf userOperationsIntf = null;
	private tcFormInstanceOperationsIntf fiIntf;
//	public static tcUserOperationsIntf usrIntf = null;

	public static void main(String[] args) throws Exception {
		DemoTest2 demo = new DemoTest2();
		demo.init();
		demo.doSomething();
	}
	
	public void init() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

		oimClient= Client.getOIMClient(userName, password, url);

		userOperationsIntf = oimClient.getService(Thor.API.Operations.tcUserOperationsIntf.class);
		fiIntf = oimClient.getService(Thor.API.Operations.tcFormInstanceOperationsIntf.class);

	}

	public void doSomething() throws Exception {
		userKey = OIMUtils.getUserKey(userOperationsIntf,("AAZY86J"));
		String orcKey = OIMUtils.getOrcKey(userOperationsIntf, "UD_LDAP_USR", userKey);
		System.out.println("updating " + orcKey);
		long orcKeyLong = Long.parseLong(orcKey);
		String columnName = "UD_LDAP_USR_LAST_NAME";
		Map updateData = new HashMap();
		updateData.put(columnName, "new last");
		fiIntf.setProcessFormData(orcKeyLong, updateData);
		System.out.println("processInstanceKey ********************* " + orcKeyLong);
		long childFormDefKey = OIMUtils.findChildFormDefKey(fiIntf, orcKeyLong, "UD_LDAP_GRP");
		System.out.println("********************* " + childFormDefKey);
		childFormDefKey = OIMUtils.findChildFormDefKey(fiIntf, orcKeyLong, "UD_MV1");
		System.out.println("********************* " + childFormDefKey);
		OIMUtils.addToChildForm(fiIntf, childFormDefKey, orcKeyLong, "UD_MV1_VALUE", "This is a value 2");
		childFormDefKey = OIMUtils.findChildFormDefKey(fiIntf, orcKeyLong, "UD_MV2");
		System.out.println("********************* " + childFormDefKey);
		OIMUtils.addToChildForm(fiIntf, childFormDefKey, orcKeyLong, "UD_MV2_VALUE", "This is a value 1");



	}
}
