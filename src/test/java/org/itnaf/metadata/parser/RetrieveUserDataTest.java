package org.itnaf.metadata.parser;

import org.itnaf.metadata.utils.SearchResults;
import org.itnaf.utils.Client;
import org.itnaf.utils.RequestClient;
import org.itnaf.utils.UserClient;
import org.itnaf.utils.UserSearch;

import oracle.iam.platform.OIMClient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

public class RetrieveUserDataTest {
	final static Logger LOGGER = Logger.getLogger(UserSearch.class.getName());
	private UserClient userClient = null;

	OIMClient oimClient = null;

	@Before
	public void loginToOIM() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";
		
		Client client = new Client(Client.getOIMClient(userName, password, url));
		userClient = new UserClient(client);
	}

	@Test
	public void findUserKey() throws Exception {
		String userKey = userClient.getUserKey("xelsysadm");
		assertThat(userKey).isNotNull();
	}
	/*
	@Test(expected=java.lang.Exception.class)
	public void userNotFound() throws Exception {
		HashMap <String, String> userSearchParameters = new HashMap <String, String>();
		userSearchParameters.put("User Login", "some user");
		ArrayList <SearchResults> email = userClient.getUserAttributes(userSearchParameters, "Email");
		LOGGER.info(email.toString());
		assertThat(email).isNotNull();
	}
	
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
