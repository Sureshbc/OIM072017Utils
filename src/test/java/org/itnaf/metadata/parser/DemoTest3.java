package org.itnaf.metadata.parser;

import org.itnaf.provisioning.CreateUsers;
import org.itnaf.provisioning.TestCreateAndProvisionSayHello;
import org.itnaf.provisioning.TestSayHello;
import org.testng.annotations.*;
import org.testng.Assert;

import java.util.logging.Logger;

public class DemoTest3 {
	final static Logger LOGGER = Logger.getLogger(DemoTest3.class.getName());
	private String login = null;
	
	@BeforeClass
	public void selectUser() throws Exception {
		login = "T" + CreateUsers.getRandomString(7).toUpperCase();
		String newLogin = CreateUsers.run(login);
		Assert.assertEquals(login, newLogin);
	}

	@Test(priority=1)
	public void provisionSayHello() throws Exception {
	}
	
	@Test(priority=2)
	public void userNotFound() throws Exception {
		TestSayHello tsh = new TestSayHello();
/*		for (int i = 0; i < 10; i++) {
			if (tsh.testSayHello(login)) {
				break;
			}
			System.out.println("Waiting 1s");
			Thread.sleep(1000);
		}
		System.out.println(login); */
		boolean res = tsh.testSayHello(login);
		tsh.exit();
		Assert.assertNotEquals(res, true);
		
	} 
	
	@AfterClass
	public void exit() {
		//System.exit(0);
	}
} 
