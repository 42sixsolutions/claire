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

import com._42six.claire.client.commons.response.ChartArrayResponse;
import com._42six.claire.client.commons.response.ChartArrayResponseCollection;
import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.openfda.util.OpenFDAUtil;

/**
 * Unit test for simple App.
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

	@Test
	public void testMarshal() throws JsonParseException, JsonMappingException, IOException {
		ResponseMapper mapper = new ResponseMapper();
		ChartArrayResponseCollection chart = mapper.unmarshalChartResponse(new File("src/main/resources/json/openFDADrugDates.json"));
		
		Assert.assertNotNull(chart.charts);
		Assert.assertEquals(78, chart.charts.size());
		for (ChartArrayResponse c : chart.charts) {
			Assert.assertNotNull(c.name);
			Assert.assertNotNull(c.chartArray);
		}	
	}
	
	@Ignore
	@Test
	public void testClient() throws Exception {
		String startDateStr = "2014-01-01 000000";
		String endDateStr = "2014-06-30 235959";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		Date startDate = dateFormat.parse(startDateStr);
		Date endDate = dateFormat.parse(endDateStr);

		ChartArrayResponseCollection chartCollection = new ChartArrayResponseCollection();
		chartCollection.charts = new ArrayList<ChartArrayResponse>();
		
		for (String name : OpenFDAUtil.DRUG_NAMES) {

			ChartArrayResponse response = this.client.search(name, "receivedate", startDate, endDate);
			chartCollection.charts.add(response);
			logger.info("Added chart for search term [" + name + "]");
			Thread.sleep(1000); //avoids rate limiting
		}
		
		ResponseMapper mapper = new ResponseMapper();
		mapper.marshalChartResponse(chartCollection, new File("src/main/resources/json/openFDADrugDates.json"));
	}

	@After
	public void teardown() throws IOException {
		this.client.close();
	}
}
