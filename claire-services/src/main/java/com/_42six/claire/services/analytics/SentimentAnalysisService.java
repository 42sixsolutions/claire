package com._42six.claire.services.analytics;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by jlee on 6/22/15.
 */
public class SentimentAnalysisService {

    public static String getAnalysis(String text) throws Exception {

        if (text != null) {
            // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            int positiveCount = 0;
            int negativeCount = 0;

            String sentiment;
            for (CoreMap sentence : sentences) {
                sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);

                if (sentiment.equalsIgnoreCase("very positive")) {
                    positiveCount += 2;
                } else if (sentiment.equalsIgnoreCase("positive")) {
                    positiveCount += 1;
                } else if (sentiment.equalsIgnoreCase("negative")) {
                    negativeCount += 1;
                } else if (sentiment.equalsIgnoreCase("very negative")) {
                    negativeCount += 2;
                }
            }

            int results = positiveCount - negativeCount;
            String finalSentiment = null;

            if (results > 0) {
                finalSentiment = "Positive";
            } else if (results == 0) {
                finalSentiment = "Neutral";
            } else if (results < 0) {
                finalSentiment = "Negative";
            }

            return finalSentiment;
        }

        return null;
    }
}
