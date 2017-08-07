package org.itnaf.metadata.parser;


import java.util.logging.Logger;

import org.itnaf.metadata.metadataloader.Lookup;
import org.itnaf.metadata.metadataloader.Lookups;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LookupsParserTest {
	final static Logger LOGGER = Logger.getLogger(LookupsParserTest.class.getName());
	public static final String SAMPLE_PREFIX = "sampleapp.";
	Lookup sa = new Lookup("sampleapp.sa");
	Lookup saUIMapping = new Lookup(SAMPLE_PREFIX + "sa.uimapping");
	Lookup saNoUIMapping = new Lookup(SAMPLE_PREFIX + "sa.nouimapping");
			
	Lookups loadedLookups;

	@Before
	public void before() {
		loadedLookups = Lookups.loadLookups(
				"./src/test/resources/fieldorigins.xlsx");	
		LOGGER.info(loadedLookups.toString());
	}

	@Test
	public void testLookupParser() {
		assertThat(loadedLookups.getAllLookups())
		.hasSize(3)
		.containsOnly(sa, saUIMapping, saNoUIMapping);
		
		Lookup otherLookup = loadedLookups.getLookup(sa.getName());
		assertThat(otherLookup).isNotNull();
		assertThat(otherLookup.getOtherEntries()).hasSize(2);
		assertThat(otherLookup.getUserProfileEntries()).hasSize(0);
		assertThat(otherLookup.getRequestDataEntries()).hasSize(0);
		assertThat(otherLookup.getProcessFormEntries()).hasSize(0);
		assertThat(otherLookup.getProcessChildFormEntries()).hasSize(0);
		assertThat(otherLookup.getOtherProcessFormEntries()).hasSize(0);
		assertThat(otherLookup.getOtherProcessChildFormEntries()).hasSize(0);
		assertThat(otherLookup.getApplicationStatusEntries()).hasSize(0);
		
		Lookup nouimapping = loadedLookups.getLookup(saNoUIMapping.getName());
		assertThat(nouimapping).isNotNull();
		assertThat(nouimapping.getOtherEntries()).hasSize(14);
		assertThat(nouimapping.getUserProfileEntries()).hasSize(0);
		assertThat(nouimapping.getRequestDataEntries()).hasSize(0);
		assertThat(nouimapping.getProcessFormEntries()).hasSize(0);
		assertThat(nouimapping.getProcessChildFormEntries()).hasSize(0);
		assertThat(nouimapping.getOtherProcessFormEntries()).hasSize(0);
		assertThat(nouimapping.getOtherProcessChildFormEntries()).hasSize(0);
		assertThat(nouimapping.getApplicationStatusEntries()).hasSize(0);
		
		Lookup uimapping = loadedLookups.getLookup(saUIMapping.getName());
		assertThat(uimapping).isNotNull();
		assertThat(uimapping.getOtherEntries()).hasSize(0);
		assertThat(uimapping.getUserProfileEntries()).hasSize(2);
		assertThat(uimapping.getRequestDataEntries()).hasSize(2);
		assertThat(uimapping.getProcessFormEntries()).hasSize(2);
		assertThat(uimapping.getProcessChildFormEntries()).hasSize(2);
		assertThat(uimapping.getOtherProcessFormEntries()).hasSize(2);
		assertThat(uimapping.getOtherProcessChildFormEntries()).hasSize(2);
		assertThat(uimapping.getApplicationStatusEntries()).hasSize(2);
	}
}
