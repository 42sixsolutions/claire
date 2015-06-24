package com._42six.claire.services.translator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.topsy.model.TopsyResults;
import com._42six.claire.client.topsy.model.TopsyResults.TopsyResponse.TopsyResult;
import com._42six.claire.openfda.util.OpenFDAUtil;
import com._42six.claire.services.analytics.SentimentAnalysisService;

public class TwitterTranslator {

	private SentimentAnalysisService analysisService;
	private ResponseMapper mapper;

	public TwitterTranslator() {
		this.analysisService = new SentimentAnalysisService();
		this.mapper = new ResponseMapper();
	}

	public void translate(File[] inputFiles) throws Exception {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (File f : inputFiles) {
			TopsyResults results = this.mapper.unmarshalFile(f, TopsyResults.class);
			if (OpenFDAUtil.DRUG_NAMES_SET.contains(results.request.parameters.q)) {
				for (TopsyResult result : results.response.list) {
					String sentiment = this.analysisService.getAnalysis(result.content);
					if (!map.containsKey(sentiment)) {
						map.put(sentiment, 0);
					}
					map.put(sentiment, map.get(sentiment) + 1);
				}
				//System.out.println(map);
			}
		}
	}
}
