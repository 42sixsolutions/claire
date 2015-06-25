package com._42six.claire.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com._42six.claire.commons.model.DrugRankings;
import com._42six.claire.openfda.util.OpenFDAUtil;

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

}
