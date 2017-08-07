package org.itnaf.metadata.parser;

import org.itnaf.utils.Client;

import oracle.iam.platform.OIMClient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LoginTest {
	OIMClient oimClient = null;

	@Test
	public void loginToOIM() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo1234";
		String url = "t3://oiam11g:14000/";
		
		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");
		assertThat(oimClient).isNotNull();
	}

	@Test
	public void loginToOIMWithAuthConf() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo1234";
		String url = "t3://oiam11g:14000/";
		
		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");
		assertThat(oimClient).isNotNull();
	}
	
	@Test(expected=javax.security.auth.login.LoginException.class)
	public void loginToOIMWithWrongPassword() throws Exception {
		String userName = "xelsysadm";
		String password = "demo1234";
		String url = "t3://oiam11g:14000/";
		
		oimClient= Client.getOIMClient(userName, password, url);
		assertThat(oimClient).isNull();
	}
}
