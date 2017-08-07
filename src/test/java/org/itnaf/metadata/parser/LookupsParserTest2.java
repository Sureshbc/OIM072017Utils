package org.itnaf.metadata.parser;


import java.util.logging.Logger;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.utils.Client;

import oracle.iam.platform.OIMClient;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class LookupsParserTest2 {
	final static Logger LOGGER = Logger.getLogger(LookupsParserTest.class.getName());
	FieldOriginResult fieldOriginResult = null;
	HashMap <String, String> searchCriteria = new HashMap <String, String> ();

	OIMClient oimClient = null;
	OIMClient oimClient2 = null;

	@Before
	public void before() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo1234";
		String url = "t3://oiam11g:14000/";
		searchCriteria.put("User Login", "xelsysadm");
		
		oimClient= Client.getOIMClient(userName, password, url);
		FieldOrigin fo = new FieldOrigin(oimClient, searchCriteria);
		
		fieldOriginResult = fo.getFieldOriginValuesHashMap("aaaa.bbbb.cccc.dddd");
		System.out.println(fieldOriginResult);
		
		fieldOriginResult = fo.getFieldOriginValuesHashMap("UP.Email");
		System.out.println(fieldOriginResult);

		fieldOriginResult = fo.getFieldOriginValuesHashMap("UP.Last Name");
		System.out.println(fieldOriginResult);

		oimClient2 = Client.getOIMClient(userName, password, url);
		FieldOrigin fo2 = new FieldOrigin(oimClient2, "xelsysadm", null);

		fieldOriginResult = fo2.getFieldOriginValuesHashMap("UP.Email");
		System.out.println(fieldOriginResult);

		fieldOriginResult = fo2.getFieldOriginValuesHashMap("UP.Last Name");
		System.out.println(fieldOriginResult);


//		fieldOriginResult = fo.parseKey("UP" + ".bbbb.cccc.dddd");
//		System.out.println(fieldOriginResult);
	}

	@Test
	public void testLookupParser() {
	}
}
