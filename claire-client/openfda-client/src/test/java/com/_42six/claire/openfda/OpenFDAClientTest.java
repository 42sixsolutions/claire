package com._42six.claire.openfda;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._42six.claire.client.commons.response.Chart;
import com._42six.claire.client.commons.response.ChartCollection;
import com._42six.claire.client.commons.response.DrugDescriptionCollection;
import com._42six.claire.client.commons.response.DrugDescriptionCollection.DrugDescription;
import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.openfda.util.OpenFDAUtil;

/**
 * Unit and integration tests for the OpenFDA client
 */
public class OpenFDAClientTest {

	private static final Logger logger = LoggerFactory.getLogger(OpenFDAClientTest.class);

	private OpenFDAClient client;

	public OpenFDAClientTest() {
	}

	@Before
	public void setup() throws ClientProtocolException, IOException {
		OpenFDAProperties props = new OpenFDAProperties(new File("src/test/resources/openfda.properties"));
		this.client = new OpenFDAClient(props.getProperty(OpenFDAProperties.FIELD_API_KEY));
	}

	/**
	 * Test marshalling a collection of drug counts by date.
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testMarshalDrugDates() throws JsonParseException, JsonMappingException, IOException {
		ResponseMapper mapper = new ResponseMapper();
		ChartCollection chart = mapper.unmarshalFile(new File("src/test/resources/json/openFDADrugDates.json"), ChartCollection.class);

		Assert.assertNotNull(chart.charts);
		Assert.assertEquals(66, chart.charts.size());
		for (Chart c : chart.charts) {
			Assert.assertNotNull(c.getName());
			Assert.assertNotNull(c.getPoints());
		}	
	}

	/**
	 * Test unmarshalling drug descriptions
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testMarshalDrugDescriptions() throws JsonParseException, JsonMappingException, IOException {
		ResponseMapper mapper = new ResponseMapper();
		DrugDescriptionCollection descriptions = mapper.unmarshalFile(new File("src/test/resources/json/openFDADrugDescriptions.json"), DrugDescriptionCollection.class);

		Assert.assertNotNull(descriptions.descriptions);
		Assert.assertEquals(66, descriptions.descriptions.size());
		for (DrugDescription dd : descriptions.descriptions) {
			Assert.assertNotNull(dd.name);
			Assert.assertNotNull(dd.description);
			Assert.assertNotNull(dd.genericName);
		}	
	}
	
	/**
	 * Test unmarshalling drug recall data
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Test
	public void testMarshalDrugRecalls() throws JsonParseException, JsonMappingException, IOException {
		ResponseMapper mapper = new ResponseMapper();
		ChartCollection chart = mapper.unmarshalFile(new File("src/test/resources/json/openFDADrugRecalls.json"), ChartCollection.class);

		Assert.assertNotNull(chart.charts);
		Assert.assertEquals(41, chart.charts.size());
		for (Chart c : chart.charts) {
			Assert.assertNotNull(c.getName());
			Assert.assertNotNull(c.getPoints());
		}	
	}

	/**
	 * Searches adverse events for drugs and marshals them into a file.
	 * The resulting json file can then be placed in the claire-web app 
	 * so the UI can serve up this data.
	 */
	@Ignore
	@Test
	public void executeSearchAdverseEvents() throws Exception {
		String startDateStr = "2014-01-01 000000";
		String endDateStr = "2014-06-30 235959";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		Date startDate = dateFormat.parse(startDateStr);
		Date endDate = dateFormat.parse(endDateStr);

		ChartCollection chartCollection = new ChartCollection();
		chartCollection.charts = new ArrayList<Chart>();

		for (String name : OpenFDAUtil.DRUG_NAMES) {

			Chart response = this.client.searchAdverseEvents(name, "receivedate", startDate, endDate);
			chartCollection.charts.add(response);
			logger.info("Added chart for search term [" + name + "]");
			Thread.sleep(1000); //avoids rate limiting
		}

		ResponseMapper mapper = new ResponseMapper();
		mapper.marshalObject(chartCollection, new File("src/test/resources/json/openFDADrugDates.json"));
	}

	/**
	 * Searches drug descriptions and marshals them into a file.
	 * The resulting json file can then be placed in the claire-web app 
	 * so the UI can serve up this data.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void executeSearchDescriptions() throws Exception {

		DrugDescriptionCollection descriptions = new DrugDescriptionCollection();
		descriptions.descriptions = new ArrayList<DrugDescription>();

		for (String name : OpenFDAUtil.DRUG_NAMES) {
			DrugDescription description = null;
			try {
				description = this.client.searchDescription(name);
			}
			catch (Exception e) {
				logger.info("Unable to get description for drug [" + name + "]");
			}
			if (description != null) {
				descriptions.descriptions.add(description);
				logger.info("Added description for search term [" + name + "]");
			}
			Thread.sleep(1000); //avoids rate limiting
		}

		ResponseMapper mapper = new ResponseMapper();
		mapper.marshalObject(descriptions, new File("src/test/resources/json/openFDADrugDescriptions.json"));
	}

	/**
	 * Searches drug recalls and marshals them into a file.
	 * The resulting json file can then be placed in the claire-web app 
	 * so the UI can serve up this data.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void executeSearchRecallEvents() throws Exception {
		String startDateStr = "2014-01-01 000000";
		String endDateStr = "2014-06-30 235959";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		Date startDate = dateFormat.parse(startDateStr);
		Date endDate = dateFormat.parse(endDateStr);

		ChartCollection chartCollection = new ChartCollection();
		chartCollection.charts = new ArrayList<Chart>();

		for (String name : OpenFDAUtil.DRUG_NAMES) {

			try {
				Chart response = this.client.searchRecallEvents(name, "report_date", startDate, endDate);
				chartCollection.charts.add(response);
				logger.info("Added chart for search term [" + name + "]");
			} catch (ClientProtocolException e) {
				logger.info("No recalls found for [" + name + "], message [" + e.getMessage() + "].");
			}
			Thread.sleep(1000); //avoids rate limiting
		}

		ResponseMapper mapper = new ResponseMapper();
		mapper.marshalObject(chartCollection, new File("src/test/resources/json/openFDADrugRecalls.json"));
	}

	@After
	public void teardown() throws IOException {
		this.client.close();
	}
}
