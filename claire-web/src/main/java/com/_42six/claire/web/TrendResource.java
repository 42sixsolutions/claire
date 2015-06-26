package com._42six.claire.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com._42six.claire.commons.model.Trend;

/**
 * Class to handle the Trends REST API endpoints
 */
@Path("trends")
@Singleton
public class TrendResource extends CommonResource {
	
	public TrendResource() throws JsonParseException, JsonMappingException, IOException, ParseException {
		super();
	}
	
    @GET
    @Path("/positive")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getPositiveTwitterTrends() {
    	return responseTranslator.getPositiveTwitterTrends();
    }

    @GET
    @Path("/negative")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getNegativeTwitterTrends() {
    	return responseTranslator.getNegativeTwitterTrends();
    }

    @GET
    @Path("/adverse")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getAdverseEventTrends() {
    	return responseTranslator.getAdverseEventsTrends();
    }
}
