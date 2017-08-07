package org.itnaf.metadata.parser;

import org.itnaf.utils.Client;
import org.itnaf.utils.OIMUtils;

import oracle.iam.platform.OIMClient;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ITResourceTest {
	OIMClient oimClient = null;

	@Test
	public void testITResources() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";
		
		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");
		org.itnaf.utils.Client client = new org.itnaf.utils.Client(oimClient);
		
		String result = OIMUtils.getITResParameter(client.getItResourceInstanceOperationsIntf(), "201", "appName");
		System.out.println("Result - " + result);
		assertThat(result).isNotNull();
	}

}
