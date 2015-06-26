package com._42six.claire.services.translator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.commons.response.TwitterDrugDetail;
import com._42six.claire.client.commons.response.TwitterDrugDetail.EventsByDate;
import com._42six.claire.client.commons.response.TwitterDrugDetailCollection;
import com._42six.claire.client.topsy.model.TopsyResults;
import com._42six.claire.client.topsy.model.TopsyResults.TopsyResponse.TopsyResult;
import com._42six.claire.openfda.util.OpenFDAUtil;
import com._42six.claire.services.analytics.SentimentAnalysisService;
import com._42six.claire.services.analytics.SentimentAnalysisService.SentimentScore;

/**
 * Service to translate twitter json data into a POJO.
 */
public class TwitterTranslator {

	private static final Logger logger = LoggerFactory.getLogger(TwitterTranslator.class);

	private SentimentAnalysisService analysisService;
	private ResponseMapper mapper;

	public TwitterTranslator() {
		this.analysisService = new SentimentAnalysisService();
		this.mapper = new ResponseMapper();
	}
	
	/**
	 * Create a collection of TwitterDrugDetail objects
	 * 
	 * @param drugMap
	 * @return
	 */
	public TwitterDrugDetailCollection toCollection(Map<String, Map<Date, EventsByDate>> drugMap) {
		TwitterDrugDetailCollection collection = new TwitterDrugDetailCollection();
		
		for (String drugName : drugMap.keySet()) {
			TwitterDrugDetail detail = new TwitterDrugDetail();
			detail.setDrugName(drugName);
			Map<Date, EventsByDate> dateMap = drugMap.get(drugName);
			detail.getEvents().addAll(dateMap.values());
			collection.getDrugs().add(detail);
			for (EventsByDate events : detail.getEvents()) {
				detail.setTotalTweets(detail.getTotalTweets() + events.getNegativeCount() + events.getNeutralCount() + events.getPositiveCount());
			}
		}
		
		return collection;
	}

	/**
	 * Convert a list of twitter json files to a TwitterDrugDetailColleciton
	 * 
	 * @param inputFiles
	 * @return
	 * @throws Exception
	 */
	public TwitterDrugDetailCollection translate(File[] inputFiles) throws Exception {

		Calendar calendar = Calendar.getInstance();

		Map<String, Map<Date, EventsByDate>> drugMap = new HashMap<String, Map<Date, EventsByDate>>();
		for (File f : inputFiles) {
			TopsyResults results = this.mapper.unmarshalFile(f, TopsyResults.class);
			if (OpenFDAUtil.DRUG_NAMES_SET.contains(results.request.parameters.q)) {

				if (!drugMap.containsKey(results.request.parameters.q)) {
					drugMap.put(results.request.parameters.q, new HashMap<Date, EventsByDate>());
				}
				Map<Date, EventsByDate> dateMap = drugMap.get(results.request.parameters.q);

				for (TopsyResult result : results.response.list) {

					SentimentScore sentiment = this.analysisService.getAnalysis(result.content);

					if (sentiment != null) {

						calendar.setTime(new Date(result.firstpost_date * 1000));
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						Date date = calendar.getTime();
						if (!dateMap.containsKey(date)) {
							Date thisDate = new Date(calendar.getTimeInMillis());
							EventsByDate events = new EventsByDate();
							events.setDate(thisDate);
							dateMap.put(thisDate, events);
						}

						EventsByDate events = dateMap.get(date);
						
						switch (sentiment) {
				        case NEGATIVE:
				        	events.setNegativeCount(events.getNegativeCount() + 1);
				            break;
				        case NEUTRAL:
				        	events.setNeutralCount(events.getNeutralCount() + 1);
				            break;
				        case POSITIVE:
				        	events.setPositiveCount(events.getPositiveCount() + 1);
				        	break;
						}
					}
				}
				logger.info("Scored [" + results.response.list.length + "] tweets for term [" + results.request.parameters.q + "].");
			}
		}
		
		return toCollection(drugMap);
	}
}
