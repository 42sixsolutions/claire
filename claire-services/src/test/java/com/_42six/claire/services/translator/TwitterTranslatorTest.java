package com._42six.claire.services.translator;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.commons.response.TwitterDrugDetailCollection;

/**
 * Test translation of twitter results
 */
public class TwitterTranslatorTest {
	
	private TwitterTranslator twitterTranslator;
	private ResponseMapper mapper;
	
	@Before
	public void setup() {
		this.twitterTranslator = new TwitterTranslator();
		this.mapper = new ResponseMapper();
	}
	
	/**
	 * After generating tweets with the Topsy client, this method can be used to 
	 * translate and score those tweets, marshaling the results to disk. The 
	 * resulting json file can then be placed in the claire-web app so the UI
	 * can serve up this data.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void executeTwitterTranslator() throws Exception {
		File[] fileList = new File("/tmp/topsy").listFiles();
		TwitterDrugDetailCollection details = twitterTranslator.translate(fileList);
		this.mapper.marshalObject(details, new File("src/test/resources/twitterDetails.json"));
	}
	
	@Test
	public void testTwitterTranslator() throws Exception {
		File[] fileList = new File[] { new File("src/test/resources/topsyResults.json") };
		TwitterDrugDetailCollection details = twitterTranslator.translate(fileList);
		Assert.assertEquals(1, details.getDrugs().size());
	}
	
	@Test
	public void testUnMarshalTwitterDetails() throws JsonParseException, JsonMappingException, IOException {
		TwitterDrugDetailCollection details = this.mapper.unmarshalFile(new File("src/test/resources/twitterDetails.json"), TwitterDrugDetailCollection.class);
		Assert.assertEquals(66, details.getDrugs().size());
	}

}
