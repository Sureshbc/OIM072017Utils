package org.itnaf.metadata.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.utils.Client;
import org.itnaf.utils.OIMUtils;
import org.itnaf.utils.UserClient;

import org.itnaf.metadata.exceptions.OIMNotInitializedException;

import Thor.API.Operations.tcUserOperationsIntf;
import oracle.iam.platform.OIMClient;

public class FieldOriginTest4 {
	final static Logger LOGGER = Logger.getLogger(FieldOriginTest2.class.getName());
	FieldOrigin fieldOrigin = null;
	FieldOriginResult fieldOriginResult = null;

	OIMClient oimClient = null;

	public static void main(String[] args) throws Exception {
		FieldOriginTest4 fot = new FieldOriginTest4();
		fot.userNotFound();
	}

	public void userNotFound() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

		oimClient= Client.getOIMClient(userName, password, url);
		Client client = new Client(oimClient);
		UserClient userClient = new UserClient(client);
		tcUserOperationsIntf userOperationIntf = client.getUserOperationsIntf();
		
		long userKey = OIMUtils.getUserKey(userOperationIntf,("t20"));
		
		
		fieldOrigin = new FieldOrigin(oimClient);
		
		fieldOrigin.setUserKey(userKey);
		
		String key = "UP.EMAIL, UP.LAST NAME,";
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap(key);
		LOGGER.info(key + " " + fieldOriginResult.toString());
	
		key = "UP.EMAIL";
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap(key);
		LOGGER.info(key + " " + fieldOriginResult.toString());
	

	}
}
