package org.itnaf.metadata.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.utils.Client;

import oracle.iam.platform.OIMClient;

public class FieldOriginTest3 {
	final static Logger LOGGER = Logger.getLogger(FieldOriginTest2.class.getName());
	FieldOrigin fieldOrigin = null;
	FieldOriginResult fieldOriginResult = null;

	OIMClient oimClient = null;

	public static void main(String[] args) throws Exception {
		FieldOriginTest3 fot = new FieldOriginTest3();
		fot.userNotFound();
	}

	public void userNotFound() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

		oimClient= Client.getOIMClient(userName, password, url);
		
		
		fieldOrigin = new FieldOrigin(oimClient);

		fieldOrigin.setProcessInstanceKey("1602");
		
		String key = "PF.FF1, PF.FF2";
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap(key);
		LOGGER.info(key + fieldOriginResult.toString());

		key = "PCF.CFF1, PCF.CFF2, PCF.CFF3, PCF.PARENTKEY";
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap(key);
		LOGGER.info(key + fieldOriginResult.getAllRows());
	}
}
