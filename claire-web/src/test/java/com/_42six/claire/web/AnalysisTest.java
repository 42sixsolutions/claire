package com._42six.claire.web;

import com._42six.claire.web.analysis.SentimentAnalysis;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jlee on 6/22/15.
 */
public class AnalysisTest {

    @Test
    public void testAnalysis() throws Exception {
        String sentiment = SentimentAnalysis.getAnalysis("This is the best movie I have ever seen.\n" +
                "Those who find ugly meanings in beautiful things are corrupt without being charming.\n" +
                "There are slow and repetitive parts, but it has just enough spice to keep it interesting.");

         Assert.assertEquals("Positive", sentiment);
    }

    @Test
    public void testAnalysisTree() throws Exception {

        String sentiment = SentimentAnalysis.getAnalysis("This is the worst movie I have ever seen.");

        Assert.assertEquals("Negative", sentiment);
    }

    @Test
    public void testAnalysisAll() throws Exception {
        String sentiment = SentimentAnalysis.getAnalysis(null);

        Assert.assertEquals(null, sentiment);
    }
}
