package org.itnaf.metadata.parser;

import org.itnaf.provisioning.CreateUsers;
import org.itnaf.provisioning.TestCreateAndProvisionSayHello;
import org.itnaf.provisioning.TestSayHello;
import org.testng.annotations.*;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class DemoTest {
	final static Logger LOGGER = Logger.getLogger(DemoTest.class.getName());
	private String login = null;
	
	@BeforeClass
	public void selectUser() throws Exception {
		
		Date dNow = new Date( );
	      SimpleDateFormat ft = 
	      new SimpleDateFormat ("ddhhmmss");

	    System.out.println("Current Date: " + ft.format(dNow));

		login = "O" + ft.format(dNow); //CreateUsers.getRandomString(7).toUpperCase();
		String newLogin = CreateUsers.run(login);
		Assert.assertEquals(login, newLogin);
	}

	@Test(priority=1)
	public void provisionSayHello() throws Exception {
		TestCreateAndProvisionSayHello tcpsh = new TestCreateAndProvisionSayHello();
		tcpsh.provisionSayHello(login, 20000);
		tcpsh.exit();
	}
	
	@Test(priority=2)
	public void userNotFound() throws Exception {
		TestSayHello tsh = new TestSayHello();
		for (int i = 0; i < 10; i++) {
			if (tsh.testSayHello(login)) {
				break;
			}
			System.out.println("Waiting 1s");
			Thread.sleep(1000);
		}
		System.out.println(login);
		boolean res = tsh.testSayHello(login);
		tsh.exit();
		Assert.assertEquals(res, true);
	}
}
