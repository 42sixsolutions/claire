package com._42six.claire.client.topsy;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.topsy.model.TopsyResults;
import com._42six.claire.client.topsy.model.TopsyResults.TopsyResponse.TopsyResult;

public class TopsyResultsReaderTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TopsyResultsReaderTest.class);

	private ResponseMapper mapper;

	public TopsyResultsReaderTest() {
	}

	@Before
	public void setup() throws ClientProtocolException, IOException {
		this.mapper = new ResponseMapper();
	}

	@Test
	public void testReader() throws Exception {
		for (File f : new File("src/test/resources/json").listFiles()) {
			TopsyResults results = this.mapper.unmarshalFile(f, TopsyResults.class);
			Assert.assertNotNull(results.response.list);
			Assert.assertEquals(10, results.response.list.length);
			logger.info("Found [" + results.response.list.length + "] results of [" + results.response.total + "] total");
			for (TopsyResult result : results.response.list) {
				Assert.assertNotNull(result.content);
			}
		}
	}

	@After
	public void teardown() throws IOException {
	}

}
