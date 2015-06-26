package com._42six.claire.services.analytics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com._42six.claire.services.analytics.SentimentAnalysisService.SentimentScore;

/**
 * Test sentiment analysis scoring
 */
public class SentimentTest {

    SentimentAnalysisService service;

    @Before
    public void setUp() throws Exception {
        service = new SentimentAnalysisService();
    }

    @Test
    public void testAnalysis() throws Exception {
        SentimentScore sentiment = service.getAnalysis("This is the best movie I have ever seen.\n" +
                "Those who find ugly meanings in beautiful things are corrupt without being charming.\n" +
                "There are slow and repetitive parts, but it has just enough spice to keep it interesting.");

        Assert.assertEquals(SentimentScore.POSITIVE, sentiment);
    }

    @Test
    public void testAnalysisTree() throws Exception {

    	SentimentScore sentiment = service.getAnalysis("This is the worst movie I have ever seen.");

        Assert.assertEquals(SentimentScore.NEGATIVE, sentiment);
    }

    @Test
    public void testAnalysisAll() throws Exception {
    	SentimentScore sentiment = service.getAnalysis(null);

        Assert.assertEquals(null, sentiment);
    }
}
