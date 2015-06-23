package com._42six.claire.openfda;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com._42six.claire.openfda.model.OpenFDACountByDayResponse;

public class OpenFDAResultsReader {

	private ObjectMapper mapper;
	
	public OpenFDAResultsReader() {
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public OpenFDACountByDayResponse unmarshalCountByDay(String webServiceResponse) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(webServiceResponse, OpenFDACountByDayResponse.class);
	}
	
	public OpenFDACountByDayResponse unmarshalCountByDay(File file) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(file, OpenFDACountByDayResponse.class);
	}
}
