package org.itnaf.metadata.metadataloader;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LookupsTest {
	public static final String SAMPLE_PREFIX = "sampleapp.";
	Lookup sa = new Lookup("sampleapp.sa");
	Lookup saMapping = new Lookup(SAMPLE_PREFIX + "sa.mapping");
	Lookup saMapping1 = new Lookup(SAMPLE_PREFIX + "sa.mapping1");
	Lookup saMapping2 = new Lookup(SAMPLE_PREFIX + "sa.mapping2");
	Lookup saTransformation = new Lookup(SAMPLE_PREFIX + "sa.transformation");
			
	Lookups emptyLookups;
	Lookups notEmptyLookups;

	@Before
	public void before() {
		emptyLookups = new Lookups();
		notEmptyLookups = new Lookups();
		notEmptyLookups.add(sa);
		notEmptyLookups.add(saMapping);
		notEmptyLookups.add(saMapping1);
		notEmptyLookups.add(saMapping2);
		notEmptyLookups.add(saTransformation);		
	}

	@Test
	public void emptyLookupIsEmpty() {
		assertThat(emptyLookups.getAllLookups().isEmpty());
	}

	@Test
	public void notEmptyLookupIsNotEmpty() {
		assertThat(notEmptyLookups.getAllLookups())
		.hasSize(5)
        .containsOnly(sa, saMapping, saMapping1, saMapping2, saTransformation);
	}

	@Test
	public void testMatchingLookups() {
		assertThat(notEmptyLookups.getAllLookupsThatStartWith(""))
		.hasSize(5)
        .containsOnly(sa, saMapping, saMapping1, saMapping2, saTransformation);
		assertThat(notEmptyLookups.getAllLookupsThatStartWith(SAMPLE_PREFIX + "sa"))
		.hasSize(5)
        .containsOnly(sa, saMapping, saMapping1, saMapping2, saTransformation);
		assertThat(notEmptyLookups.getAllLookupsThatStartWith(SAMPLE_PREFIX + "sa.mapping"))
		.hasSize(3)
        .containsOnly(saMapping, saMapping1, saMapping2);
		assertThat(notEmptyLookups.getAllLookupsThatStartWith(SAMPLE_PREFIX + "sa.mapping1"))
		.hasSize(1)
        .containsOnly(saMapping1);
		assertThat(notEmptyLookups.getAllLookupsThatStartWith(SAMPLE_PREFIX + "sa.transformation"))
		.hasSize(1)
        .containsOnly(saTransformation);
	}
}
