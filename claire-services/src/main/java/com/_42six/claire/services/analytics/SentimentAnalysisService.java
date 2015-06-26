package com._42six.claire.services.analytics;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

/**
 * Service to score sentences based on sentiment.
 */
public class SentimentAnalysisService {

    private StanfordCoreNLP pipeline;

    public enum Sentiment {
        VERY_POSITIVE("Very positive", 2),
        POSITIVE("Positive", 1),
        NEUTRAL("Neutral", 0),
        NEGATIVE("Negative", -1),
        VERY_NEGATIVE("Very negative", -2);

        private String name;
        private int value;

        Sentiment(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }
    }
    
    public enum SentimentScore {
    	POSITIVE,
    	NEGATIVE,
    	NEUTRAL
    };

    public SentimentAnalysisService() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Get SentimentScore for a given paragraph or sentence.
     * 
     * @param text
     * @return
     * @throws Exception
     */
    public SentimentScore getAnalysis(String text) throws Exception {

        if (text != null) {
            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            int sentimentValue = 0;

            String sentiment;
            for (CoreMap sentence : sentences) {
                sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);

                if (sentiment.equalsIgnoreCase(Sentiment.VERY_POSITIVE.getName())) {
                    sentimentValue += Sentiment.VERY_POSITIVE.value;
                } else if (sentiment.equalsIgnoreCase(Sentiment.POSITIVE.getName())) {
                    sentimentValue += Sentiment.POSITIVE.value;
                } else if (sentiment.equalsIgnoreCase(Sentiment.NEGATIVE.getName())) {
                    sentimentValue += Sentiment.NEGATIVE.value;
                } else if (sentiment.equalsIgnoreCase(Sentiment.VERY_NEGATIVE.getName())) {
                    sentimentValue += Sentiment.VERY_NEGATIVE.value;
                }
            }

            SentimentScore finalSentiment = null;

            if (sentimentValue > 0) {
                finalSentiment = SentimentScore.POSITIVE;
            } else if (sentimentValue == 0) {
                finalSentiment = SentimentScore.NEUTRAL;
            } else if (sentimentValue < 0) {
                finalSentiment = SentimentScore.NEGATIVE;
            }

            return finalSentiment;
        }

        return null;
    }
}