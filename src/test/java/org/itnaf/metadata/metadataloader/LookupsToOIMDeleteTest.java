package org.itnaf.metadata.metadataloader;

import static org.assertj.core.api.Assertions.assertThat;

import org.itnaf.utils.Client;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

import oracle.iam.platform.OIMClient;

public class LookupsToOIMDeleteTest {
	final static Logger LOGGER = Logger.getLogger(LookupsToOIMDeleteTest.class.getName());
	final static String WORKING_DIRECTORY =  ""; // "./";
	final static String INPUT_FILE =  "D:/temp/demo3.xlsx"; // "src/test/resources/demo.xlsx";
	public static final String SAMPLE_PREFIX = "sampleapp.";
	
	OIMClient oimClient = null;

	@Before
	public void before() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo1234";
		String url = "t3://oiam11g:14000/";
		
		boolean mac = false;
		boolean aws = false;
		
		if (mac) {
			password = "Demo12345";
			url = "t3://oiam11gmac:14000/";
		}
		
		if (aws) {
			password = "Oracle123";
			url = "t3://snp2.aurionprosena.org:14000/";
			
		}
		
		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");

	}

	@Test
	public void emptyLookupIsEmpty() {
		
		ExcelToOIM.findAndDeleteAllLookupsThatMatch("sandp", oimClient);
	}

}

