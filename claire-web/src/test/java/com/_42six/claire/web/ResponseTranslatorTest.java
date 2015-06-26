package com._42six.claire.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com._42six.claire.commons.model.DrugRankings;
import com._42six.claire.commons.model.Trend;
import com._42six.claire.commons.model.TwitterStats;
import com._42six.claire.openfda.util.OpenFDAUtil;

/**
 * Tests the ResponseTranslator
 */
public class ResponseTranslatorTest {

	private static ResponseTranslator responseTranslator;

	@BeforeClass
	public static void setup() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException, ParseException {
		responseTranslator = new ResponseTranslator(
				new File("src/main/webapp/WEB-INF/classes/json/twitterDetails.json"),
				new File("src/main/webapp/WEB-INF/classes/json/openFDADrugDates.json"),
				new File("src/main/webapp/WEB-INF/classes/json/openFDADrugDescriptions.json"),
				new File("src/main/webapp/WEB-INF/classes/json/openFDADrugRecalls.json")
				);
	}

	@Test
	public void testDrugRankMap() throws JsonParseException, JsonMappingException, IOException {
		for (String name : OpenFDAUtil.DRUG_NAMES_SET) {
			DrugRankings ranking = responseTranslator.getDrugRank(name);
			Assert.assertNotNull(ranking);
			Assert.assertTrue(ranking.getAdverseEventsRank() > 0);
			Assert.assertTrue(ranking.getNegativeTweetsRank() > 0);
			Assert.assertTrue(ranking.getPositiveTweetsRank() > 0);
			Assert.assertTrue(ranking.getRecallsRank() > 0);
			Assert.assertTrue(ranking.getUnknownTweetsRank() > 0);
		}
	}
	
	@Test
	public void testTwitterStats() {
		TwitterStats stats = responseTranslator.getTwitterStats("lipitor");
		Assert.assertTrue(stats.getPercentNegative() > 0);
	}
	
	@Test
	public void testPositiveTwitterTrend() {
		List<Trend> trendList = responseTranslator.getPositiveTwitterTrends();
		for (Trend trend : trendList) {
			Assert.assertNotNull(trend.getBrandName());
		}
		Assert.assertTrue(trendList.size() > 1);
		Assert.assertTrue(trendList.get(0).getSlope() > trendList.get(trendList.size() - 1).getSlope());
	}
	
	@Test
	public void testNegativeTwitterTrend() {
		List<Trend> trendList = responseTranslator.getNegativeTwitterTrends();
		for (Trend trend : trendList) {
			Assert.assertNotNull(trend.getBrandName());
		}
		Assert.assertTrue(trendList.size() > 1);
		Assert.assertTrue(trendList.get(0).getSlope() < trendList.get(trendList.size() - 1).getSlope());
	}
	
	@Test
	public void testAdverseEventTrend() {
		List<Trend> trendList = responseTranslator.getAdverseEventsTrends();
		for (Trend trend : trendList) {
			Assert.assertNotNull(trend.getBrandName());
		}
		Assert.assertTrue(trendList.size() > 1);
		Assert.assertTrue(trendList.get(0).getSlope() > trendList.get(trendList.size() - 1).getSlope());
	}
	
	@Test
	public void testChart() {
		responseTranslator.getChart("lipitor",100);
	}
	
	@Test
	public void testTwitterTrend() {
		responseTranslator.getOverallTwitterTrend("lipitor");
	}
}
