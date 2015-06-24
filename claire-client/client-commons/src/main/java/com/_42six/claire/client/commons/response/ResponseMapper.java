package com._42six.claire.client.commons.response;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class ResponseMapper {

	private ObjectMapper mapper;

	public ResponseMapper() {
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public void marshalChartResponse(ChartCollection chart, File file) throws JsonParseException, JsonMappingException, IOException {
		this.mapper.writeValue(file, chart);
	}
	
	public ChartCollection unmarshalChartResponse(File file) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(file, ChartCollection.class);
	}

}
