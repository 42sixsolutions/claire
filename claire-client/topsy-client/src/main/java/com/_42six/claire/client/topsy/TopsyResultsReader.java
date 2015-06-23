package com._42six.claire.client.topsy;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com._42six.claire.client.topsy.model.TopsyResults;

public class TopsyResultsReader {
	
	private ObjectMapper mapper;
	
	public TopsyResultsReader() {
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public TopsyResults unmarshal(String webServiceResponse) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(webServiceResponse, TopsyResults.class);
	}
	
	public TopsyResults unmarshal(File file) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(file, TopsyResults.class);
	}
}
