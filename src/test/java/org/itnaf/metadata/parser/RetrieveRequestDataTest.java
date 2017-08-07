package org.itnaf.metadata.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import org.itnaf.utils.Client;
import org.itnaf.utils.ProvisioningClient;
import org.itnaf.utils.RequestClient;

import static org.assertj.core.api.Assertions.assertThat;

import oracle.iam.platform.OIMClient;
import oracle.iam.request.vo.RequestConstants;

public class RetrieveRequestDataTest {
	private RequestClient requestClient = null;
	private ProvisioningClient provisioningClient = null;
	
	OIMClient oimClient = null;
	
	@Before
	public void loginToOIM() throws Exception {
		String userName = "xelsysadm";
		String password = "Welcome1";
		String url = "t3://host01:14000/";
		
		Client client = new Client(Client.getOIMClient(userName, password, url));
		requestClient = new RequestClient(client);
		provisioningClient = new ProvisioningClient(client);
	}

	@Test
	public void findUserKey() throws Exception {
		//String userKey = requestClient.getRequestAttributes("22");
		//assertThat(userKey).isNotNull();
	}
/*	
	@Test(expected=java.lang.Exception.class)
	public void userNotFound() throws Exception {
		String email = clientUser.getUserAttributes("some user", "Email");
		assertThat(email).isNotNull();
	}
	*/
	@Test
	public void findRequest() throws Exception {
		provisioningClient.getSpecificRequestAttributes("D7UV4RL8");
	}
	
	/*
	@Test(expected=java.lang.Exception.class)
	public void userKeyNotFound3() throws Exception {
		String email = clientUser.getUserAttributes("xelsysadm", "Email2");
		assertThat(email).isNotNull();
	} */
}
