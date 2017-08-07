package org.itnaf.metadata.parser;

import org.itnaf.itresources.TestITResources;
import org.itnaf.provisioning.CreateUsers;
import org.itnaf.provisioning.TestCreateAndProvisionSayHello;
import org.itnaf.provisioning.TestSayHello;
import org.testng.annotations.*;
import org.testng.Assert;

import java.util.logging.Logger;

public class DemoTest2 {
	final static Logger LOGGER = Logger.getLogger(DemoTest2.class.getName());
	private String login = null;
	

	@Test(priority=1)
	public void testWS() throws Exception {
		TestITResources testITResources = new TestITResources();
		boolean testResult = testITResources.testFindITResource(); 		
		System.out.println(login);
		testITResources.exit(); 
		Assert.assertEquals(testResult, true);
	}
}
