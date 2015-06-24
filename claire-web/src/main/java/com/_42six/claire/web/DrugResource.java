package com._42six.claire.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com._42six.claire.commons.model.ChartDetail;
import com._42six.claire.commons.model.Drug;
import com._42six.claire.commons.model.DrugRankings;
import com._42six.claire.commons.model.FDAStats;
import com._42six.claire.commons.model.TwitterStats;
import jersey.repackaged.com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Class to handle the Drug REST API endpoints
 */
@Path("drug")
public class DrugResource {
    @GET
    @Path("/detail/{drugName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Drug getDrugDetails(@PathParam("drugName") String drugName) {
        return new Drug().setBrandName("Crestor").setGenericName("ROSUVASTATIN CALCIUM")
                .setDescription("Rosuvastatin (marketed by AstraZeneca as Crestor, In India marketed by (Cipla)as \"rosulip\" is a member of the drug class of statins, used in combination with exercise, diet, and weight-loss to treat high cholesterol and related conditions, and to prevent cardiovascular disease. It was developed by Shionogi. Crestor is the fourth-highest selling drug in the United States, accounting for approx. $5.2 billion in sales in 2013.")
                .setPharmacodynamics("The primary use of rosuvastatin is for the treatment of dyslipidemia.[3] It is recommended to be used only after other measures such as diet, exercise, and weight reduction have not improved cholesterol levels");
    }

    @GET
    @Path("/tweets/{drugName}")
    @Produces(MediaType.APPLICATION_JSON)
    public TwitterStats getTwitterStats(@PathParam("drugName") String drugName) {
        return new TwitterStats().setTotalTweets(10).setPercentNegative(15)
                .setPercentPositive(25).setPercentUnknown(60);
    }

    @GET
    @Path("/fda/{drugName}")
    @Produces(MediaType.APPLICATION_JSON)
    public FDAStats getFDAStats(@PathParam("drugName") String drugName) {
        return new FDAStats().setTotalRecalls(7).setTotalAdverseEvents(1500);
    }

    @GET
    @Path("/ranking/{drugName}")
    @Produces(MediaType.APPLICATION_JSON)
    public DrugRankings getDrugRanks(@PathParam("drugName") String drugName) {
        return new DrugRankings().setPositiveTweetsRank(10).setNegativeTweetsRank(15).setUnknownTweetsRank(20)
                .setAdverseEventsRank(1).setRecallsRank(1);
    }

    @GET
    @Path("/chart/{drugName}")
    @Produces(MediaType.APPLICATION_JSON)
    public ChartDetail getChart(@PathParam("drugName") String drugName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new ChartDetail()
                    .setPositiveTweets(Lists.newArrayList(
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-01")).setPercentMax(25),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-02")).setPercentMax(15),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-03")).setPercentMax(20),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-04")).setPercentMax(100)))
                    .setNegativeTweets(Lists.newArrayList(
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-01")).setPercentMax(50),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-02")).setPercentMax(75),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-03")).setPercentMax(100),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-04")).setPercentMax(25)))
                    .setUnknownTweets(Lists.newArrayList(
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-01")).setPercentMax(100),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-02")).setPercentMax(10),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-03")).setPercentMax(70),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-04")).setPercentMax(95)))
                    .setAdverseEvents(Lists.newArrayList(
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-01")).setPercentMax(15),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-02")).setPercentMax(100),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-03")).setPercentMax(0),
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-04")).setPercentMax(55)))
                    .setRecalls(Lists.newArrayList(
                            new ChartDetail.ChartDetailDataPoint().setDate(formatter.parse("2015-01-02")).setPercentMax(100)));
        } catch (ParseException ex) {
            //Get rid of this catch when we stop using dummy data
            throw new WebApplicationException("Couldn't parse date", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Drug> getDrugs() {
        return Lists.newArrayList(new Drug().setBrandName("enbrel"), new Drug().setBrandName("aspirin"));
    }
}

