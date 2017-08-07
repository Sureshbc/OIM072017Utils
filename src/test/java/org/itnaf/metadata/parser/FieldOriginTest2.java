package org.itnaf.metadata.parser;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.utils.Client;

import oracle.iam.platform.OIMClient;

public class FieldOriginTest2 {
	final static Logger LOGGER = Logger.getLogger(FieldOriginTest2.class.getName());
	FieldOrigin fieldOrigin = null;
	FieldOriginResult fieldOriginResult = null;

	OIMClient oimClient = null;

	@Before
	public void loginToOIM() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";
		
		oimClient= Client.getOIMClient(userName, password, url);
		fieldOrigin = new FieldOrigin(oimClient);
	}

	@Test
	public void userNotFound() throws Exception {
		
		fieldOrigin.setProcessInstanceKey("1382");
		
		
		String key = "PF.UD_GENER_SERVER";
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap(key);
		LOGGER.info(key + ":" + fieldOriginResult.toString());
		LOGGER.info("Value: " + fieldOriginResult.getValues().get("UD_GENER_SERVER"));

	}
}
