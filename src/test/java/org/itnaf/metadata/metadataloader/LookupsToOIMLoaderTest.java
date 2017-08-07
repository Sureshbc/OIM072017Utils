package org.itnaf.metadata.metadataloader;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

import org.itnaf.utils.Client;

import oracle.iam.platform.OIMClient;

public class LookupsToOIMLoaderTest {
	final static Logger LOGGER = Logger.getLogger(LookupsToOIMLoaderTest.class.getName());
	final static String WORKING_DIRECTORY = "./";
	final static String INPUT_FILE = "src/test/resources/easample.xlsx";
	public static final String SAMPLE_PREFIX = "sampleapp.";

	OIMClient oimClient = null;

	@Before
	public void before() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo1234";
		String url = "t3://oiam11g:14000/";

		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");
	}

	@Test
	public void emptyLookupIsEmpty() {

		Lookups lookups = Lookups.loadLookups(WORKING_DIRECTORY + INPUT_FILE);
		LOGGER.info(lookups.toString());
		assertThat(!lookups.getAllLookups().isEmpty());
		ExcelToOIM.createOIMLookup(lookups, oimClient);
	}

}
