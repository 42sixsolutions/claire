package com._42six.claire.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.commons.response.TwitterDrugDetail;
import com._42six.claire.client.commons.response.TwitterDrugDetail.EventsByDate;
import com._42six.claire.client.commons.response.TwitterDrugDetailCollection;
import com._42six.claire.commons.model.ChartDetail;
import com._42six.claire.commons.model.ChartDetail.ChartDetailDataPoint;
import com._42six.claire.commons.model.Drug;
import com._42six.claire.openfda.util.OpenFDAUtil;

public class ResponseTranslator {
	
	private final Map<String, TwitterDrugDetail> twitterDetailMap;
	
	public ResponseTranslator(InputStream twitterDetailsStream) throws JsonParseException, JsonMappingException, IOException {
		ResponseMapper mapper =  new ResponseMapper();
		TwitterDrugDetailCollection twitterDrugDetails = mapper.unmarshalStream(twitterDetailsStream, TwitterDrugDetailCollection.class);
		this.twitterDetailMap = createTwitterDrugDetailsMap(twitterDrugDetails);
	}
	
	private Map<String, TwitterDrugDetail> createTwitterDrugDetailsMap(TwitterDrugDetailCollection twitterDrugDetails) {
		Map<String, TwitterDrugDetail> map = new HashMap<String, TwitterDrugDetail>();
		for (TwitterDrugDetail detail : twitterDrugDetails.getDrugs()) {
			map.put(detail.getDrugName().toLowerCase(), detail);
		}
		return Collections.unmodifiableMap(map);
	}

	public List<Drug> getDrugs() {
		List<Drug> drugList = new ArrayList<Drug>();
		for (String drugName : OpenFDAUtil.DRUG_NAMES_SET) {
			Drug drug = new Drug();
			drugList.add(drug);
			
			//capitalize first letter
			drug.setBrandName(Character.toUpperCase(drugName.charAt(0)) + drugName.substring(1));
		}
		return drugList;
	}
	
	public ChartDetail getChart(String drugName) {
		if (drugName == null || !this.twitterDetailMap.containsKey(drugName.toLowerCase())) {
			return null; //FIXME: throw 404
		}
		ChartDetail responseDetail = new ChartDetail();

		List<ChartDetailDataPoint> positiveList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setPositiveTweets(positiveList);
		List<ChartDetailDataPoint> negativeList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setNegativeTweets(negativeList);
		List<ChartDetailDataPoint> neutralList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setUnknownTweets(neutralList);
		
		TwitterDrugDetail sourceDetail = this.twitterDetailMap.get(drugName.toLowerCase());
		
		for (EventsByDate event : sourceDetail.getEvents()) {
			ChartDetailDataPoint positivePoint = new ChartDetailDataPoint();
			ChartDetailDataPoint negativePoint = new ChartDetailDataPoint();
			ChartDetailDataPoint neutralPoint = new ChartDetailDataPoint();
			
			positiveList.add(positivePoint);
			negativeList.add(negativePoint);
			neutralList.add(neutralPoint);
			
			positivePoint.setDate(event.getDate());
			negativePoint.setDate(event.getDate());
			neutralPoint.setDate(event.getDate());
			
			int total = event.getNegativeCount() + event.getNeutralCount() + event.getPositiveCount();
			positivePoint.setPercentMax(total == 0 ? 0 : (100 * event.getPositiveCount()) / total);
			negativePoint.setPercentMax(total == 0 ? 0 : (100 * event.getNegativeCount()) / total);
			neutralPoint.setPercentMax(total == 0 ? 0 : (100 * event.getNeutralCount()) / total);
		}
		
		return responseDetail;
	}
}
