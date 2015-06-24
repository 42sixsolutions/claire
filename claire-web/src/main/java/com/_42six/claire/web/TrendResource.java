package com._42six.claire.web;

import com._42six.claire.commons.model.Trend;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Class to handle the Trends REST API endpoints
 */
@Path("trends")
public class TrendResource {
    @GET
    @Path("/positive")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getPositiveTwitterTrends() {
        return Lists.newArrayList(
                new Trend().setBrandName("Plavix").setPercentPositive(99).setPercentNegative(0),
                new Trend().setBrandName("Zocor").setPercentPositive(98).setPercentNegative(2)
        );
    }

    @GET
    @Path("/negative")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getNegativeTwitterTrends() {
        return Lists.newArrayList(
                new Trend().setBrandName("Lipitor").setPercentPositive(99).setPercentNegative(0),
                new Trend().setBrandName("Nexium").setPercentPositive(98).setPercentNegative(2)
        );
    }

    @GET
    @Path("/adverse")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Trend> getAdverseEventTrends() {
        return Lists.newArrayList(
                new Trend().setBrandName("Amoxil").setCount(5676),
                new Trend().setBrandName("Synthroid").setCount(4565)
        );
    }
}
