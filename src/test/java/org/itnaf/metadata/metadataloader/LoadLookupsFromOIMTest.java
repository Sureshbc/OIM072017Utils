package org.itnaf.metadata.metadataloader;

import static org.assertj.core.api.Assertions.assertThat;

import org.itnaf.utils.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

import oracle.iam.platform.OIMClient;

public class LoadLookupsFromOIMTest {
	final static Logger LOGGER = Logger.getLogger(LoadLookupsFromOIMTest.class.getName());
	final static String WORKING_DIRECTORY = "./";
	final static String INPUT_FILE = "src/test/resources/easample.xlsx";
	public static final String LOOKUP_PREFIX = "sandp";
	public static final String SAMPLE_PREFIX = "pa";

	OIMClient oimClient = null;

	@Before
	public void before() throws Exception {
		String userName = "xelsysadm";
		String password = "Demo12345";
		String url = "t3://oiam11gmac:14000/";

		oimClient= Client.getOIMClient(userName, password, url, "./src/test/resources/authwl.conf");
	}

	@Test
	public void emptyLookupIsEmpty() {

		Lookups lookups = Lookups.loadLookupsFromOIM(LOOKUP_PREFIX, SAMPLE_PREFIX, oimClient);
		ArrayList <Lookup> al = null;
		LOGGER.info(lookups.toString());
		al = lookups.getAllLookups();
		LOGGER.info(al.toString());
		assertThat(!al.isEmpty());
		al = lookups.getAllLookupsThatStartWith("s");
		LOGGER.info(al.toString());
		assertThat(!al.isEmpty());
		al = lookups.getAllLookupsThatStartWith("sandp.pa.oud");
		LOGGER.info(al.toString());
		assertThat(!al.isEmpty());
		
		LOGGER.info("Here before");
		Lookup l1 = lookups.getLookup("sandp.pa.oud.mapping");
		LOGGER.info("Here after");
		LOGGER.info("should be null " + l1);
//		assertThat(!l1.isNull());

//		ExcelToOIM.createOIMLookup(lookups, oimClient);
	}

}
