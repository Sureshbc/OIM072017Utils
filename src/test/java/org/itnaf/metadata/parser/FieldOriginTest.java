package org.itnaf.metadata.parser;

import java.util.ArrayList;

import org.itnaf.metadata.utils.FieldOrigin;
import org.itnaf.metadata.utils.FieldOriginResult;
import org.itnaf.utils.Client;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

import oracle.iam.platform.OIMClient;
import org.itnaf.metadata.exceptions.OIMNotInitializedException;


public class FieldOriginTest {
	final static Logger LOGGER = Logger.getLogger(FieldOriginTest.class.getName());
	private Client client = null;
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
		fieldOrigin.setUserKey(1L);
		
	}

	@Test
	public void userNotFound() throws Exception {
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("UP.Postal Code");
		LOGGER.info(fieldOriginResult.toString());

		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("UP.Postal Address");
		LOGGER.info(fieldOriginResult.toString());

		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("UP.Email");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("UP.Last Name");
		LOGGER.info(fieldOriginResult.toString());

		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("UP.User ID");
		LOGGER.info(fieldOriginResult.toString());


		/*
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.endDate");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.creationDate");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.reqID");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.requestStatus");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.requestState");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.REQUEST_OBJECT.requesterKey");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.FORM_FIELD.Service Account");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.FORM_FIELD.Password");
		LOGGER.info(fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("RD.FORM_FIELD.Server");
		LOGGER.info(fieldOriginResult.toString());
				
		fieldOrigin = new FieldOrigin(client.getOIMClient(), "testuser66", "121");
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("OPF.DBAT1User.Server");
		LOGGER.info("OPF.DBAT1User.Server" + fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("PF.Server");
		LOGGER.info("PF.Server" + fieldOriginResult.toString());
		
		for (int i = 0; i < 10; i++) {
			fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("OPF.DBAT2User.Server");
		}
		LOGGER.info("OPF.DBAT2User.Server" + fieldOriginResult.toString());
		
		*/
		/*
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("PCF.UD_DBAT1GRP");
		LOGGER.info("PCF.UD_DBAT1GRP" + fieldOriginResult.toString());
		
		fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("OPCF.DBAT1User.UD_DBAT1GRP");
		LOGGER.info("OPCF.DBAT1User.UD_DBAT1GRP" + fieldOriginResult.toString());
		*/
		/*
		fieldOrigin = new FieldOrigin(client.getOIMClient(), "testuser6", "121");
		
//		fieldOriginResult = fieldOrigin.getFieldOriginValues("PF.FORM_FIELD.Server");
//		LOGGER.info(fieldOriginResult.toString());
		
		for (int i = 0; i < 100; i++) {
			fieldOriginResult = fieldOrigin.getFieldOriginValuesHashMap("OPF.DBAT1User.Server");
		}
		if (fieldOriginResult != null) {
			LOGGER.info(fieldOriginResult.toString());
		}	*/
	}
	
	/*
	@Test
	public void findEmail() throws Exception {
		HashMap <String, String> userSearchParameters = new HashMap <String, String>();
		userSearchParameters.put("User Login", "xelsysadm");
		ArrayList <SearchResults> email = userClient.getUserAttributes(userSearchParameters, "Email");
		LOGGER.info(email.toString());
		assertThat(email).isNotNull();
	}
	
	@Test
	public void findEmails() throws Exception {
		HashMap <String, String> userSearchParameters = new HashMap <String, String>();
		userSearchParameters.put("User Login", "*");
		ArrayList <SearchResults> email = userClient.getUserAttributes(userSearchParameters, "Email");
		LOGGER.info(email.toString());
		assertThat(email).isNotNull();
	}
	
	@Test
	public void findAllAttributes() throws Exception {
		HashMap <String, String> userSearchParameters = new HashMap <String, String>();
		userSearchParameters.put("User Login", "xelsysadm");
		ArrayList <SearchResults> email = userClient.getUserAttributes(userSearchParameters, null);
		LOGGER.info(email.toString());
		assertThat(email).isNotNull();
	}
	
	@Test(expected=java.lang.Exception.class)
	public void userKeyNotFound3() throws Exception {
		HashMap <String, String> userSearchParameters = new HashMap <String, String>();
		userSearchParameters.put("User Login", "xelsysadm");
		ArrayList <SearchResults> email = userClient.getUserAttributes(userSearchParameters, "Email2");
		LOGGER.info(email.toString());
		assertThat(email).isNotNull();
	}
	*/
}
