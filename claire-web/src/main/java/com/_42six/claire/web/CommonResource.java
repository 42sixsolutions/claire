package com._42six.claire.web;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Common resource to be used for sharing the ResponseTranslator
 */
public abstract class CommonResource {

	protected ResponseTranslator responseTranslator;
	
	public CommonResource() throws JsonParseException, JsonMappingException, IOException, ParseException {
		InputStream twitterDetailsStream = this.getClass().getClassLoader().getResourceAsStream("/json/twitterDetails.json");
		InputStream openFDADrugDatesStream = this.getClass().getClassLoader().getResourceAsStream("/json/openFDADrugDates.json");
		InputStream openFDADrugDescriptionsStream = this.getClass().getClassLoader().getResourceAsStream("/json/openFDADrugDescriptions.json");
		InputStream openFDADrugRecallsStream = this.getClass().getClassLoader().getResourceAsStream("/json/openFDADrugRecalls.json");
		this.responseTranslator = ResponseTranslator.getInstance(
				twitterDetailsStream, 
				openFDADrugDatesStream, 
				openFDADrugDescriptionsStream, 
				openFDADrugRecallsStream);
	}
}
