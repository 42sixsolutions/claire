package com._42six.claire.web;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com._42six.claire.client.commons.response.Chart;
import com._42six.claire.client.commons.response.ChartCollection;
import com._42six.claire.client.commons.response.DataPoint;
import com._42six.claire.client.commons.response.DrugDescriptionCollection;
import com._42six.claire.client.commons.response.DrugDescriptionCollection.DrugDescription;
import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.commons.response.TwitterDrugDetail;
import com._42six.claire.client.commons.response.TwitterDrugDetail.EventsByDate;
import com._42six.claire.client.commons.response.TwitterDrugDetailCollection;
import com._42six.claire.commons.model.ChartDetail;
import com._42six.claire.commons.model.ChartDetail.ChartDetailDataPoint;
import com._42six.claire.commons.model.Drug;
import com._42six.claire.commons.model.FDAStats;
import com._42six.claire.commons.model.TwitterStats;
import com._42six.claire.openfda.util.OpenFDAUtil;

public class ResponseTranslator {

	private final Calendar minDate ;
	private final Calendar maxDate;

	private final Map<String, TwitterDrugDetail> twitterDetailMap;
	private final Map<String, Chart> openFDADrugDatesMap;
	private final Map<String, DrugDescription> openFDADrugDescriptionMap;

	public ResponseTranslator(
			InputStream twitterDetailsStream,
			InputStream openFDADrugDatesStream,
			InputStream openFDADrugDescriptionsStream
			) throws JsonParseException, JsonMappingException, IOException, ParseException {

		this.minDate = Calendar.getInstance();
		this.minDate.setTime(new SimpleDateFormat("yyyyMMdd").parse("20140101"));
		this.maxDate = Calendar.getInstance();
		this.maxDate.setTime(new SimpleDateFormat("yyyyMMdd").parse("20140630"));

		ResponseMapper mapper =  new ResponseMapper();

		ChartCollection charts = mapper.unmarshalStream(openFDADrugDatesStream, ChartCollection.class);
		this.openFDADrugDatesMap = createOpenFDADrugDatesMap(charts);

		TwitterDrugDetailCollection twitterDrugDetails = mapper.unmarshalStream(twitterDetailsStream, TwitterDrugDetailCollection.class);
		this.twitterDetailMap = createTwitterDrugDetailsMap(twitterDrugDetails);

		DrugDescriptionCollection descriptions = mapper.unmarshalStream(openFDADrugDescriptionsStream, DrugDescriptionCollection.class);
		this.openFDADrugDescriptionMap = createOpenFDADrugDescriptionMap(descriptions);

	}

	private Map<String, Chart> createOpenFDADrugDatesMap(ChartCollection charts) {
		Map<String, Chart> map = new HashMap<String, Chart>();
		for (Chart chart : charts.charts) {
			map.put(chart.getName().toLowerCase(), chart);

			Iterator<DataPoint> pointIterator = chart.getPoints().iterator();
			while (pointIterator.hasNext()) {
				Calendar eventCal = Calendar.getInstance();
				eventCal.setTime(pointIterator.next().getDate());

				// remove events that aren't in the time span
				if (eventCal.before(minDate) || eventCal.after(maxDate)) {
					pointIterator.remove();
				}
			}
		}
		return Collections.unmodifiableMap(map);
	}

	private Map<String, DrugDescription> createOpenFDADrugDescriptionMap(DrugDescriptionCollection descriptions) {
		Map<String, DrugDescription> map = new HashMap<String, DrugDescription>();

		for (DrugDescription desc : descriptions.descriptions) {
			map.put(desc.name.toLowerCase(), desc);
		}
		return map;
	}

	private Map<String, TwitterDrugDetail> createTwitterDrugDetailsMap(
			TwitterDrugDetailCollection twitterDrugDetails
			) {
		Map<String, TwitterDrugDetail> map = new HashMap<String, TwitterDrugDetail>();
		for (TwitterDrugDetail detail : twitterDrugDetails.getDrugs()) {
			map.put(detail.getDrugName().toLowerCase(), detail);

			Iterator<EventsByDate> dateIterator = detail.getEvents().iterator();
			while (dateIterator.hasNext()) {
				Calendar eventCal = Calendar.getInstance();
				eventCal.setTime(dateIterator.next().getDate());

				// remove events that aren't in the time span
				if (eventCal.before(minDate) || eventCal.after(maxDate)) {
					dateIterator.remove();
				}
			}
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
		if (drugName == null ||
				(!this.twitterDetailMap.containsKey(drugName.toLowerCase()) &&
						!this.openFDADrugDatesMap.containsKey(drugName.toLowerCase()))
				) {
			return null; //FIXME: throw 404
		}
		ChartDetail responseDetail = new ChartDetail();

		/* set tweet points */

		List<ChartDetailDataPoint> positiveList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setPositiveTweets(positiveList);
		List<ChartDetailDataPoint> negativeList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setNegativeTweets(negativeList);
		List<ChartDetailDataPoint> neutralList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setUnknownTweets(neutralList);

		TwitterDrugDetail sourceDetail = this.twitterDetailMap.get(drugName.toLowerCase());

		if (sourceDetail != null) {
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

				// use count instead of percent
				/*
				int total = event.getNegativeCount() + event.getNeutralCount() + event.getPositiveCount();
				positivePoint.setPercentMax(total == 0 ? 0 : (100 * event.getPositiveCount()) / total);
				negativePoint.setPercentMax(total == 0 ? 0 : (100 * event.getNegativeCount()) / total);
				neutralPoint.setPercentMax(total == 0 ? 0 : (100 * event.getNeutralCount()) / total);
				 */
				positivePoint.setPercentMax(event.getPositiveCount());
				negativePoint.setPercentMax(event.getNegativeCount());
				neutralPoint.setPercentMax(event.getNeutralCount());
			}
		}

		/* set adverse event points */

		List<ChartDetailDataPoint> adverseList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setAdverseEvents(adverseList);

		Chart adverseEvents = this.openFDADrugDatesMap.get(drugName.toLowerCase());

		if (adverseEvents != null && adverseEvents.getPoints() != null && !adverseEvents.getPoints().isEmpty()) {
			int max = 0;
			for (DataPoint sourcePoint : adverseEvents.getPoints()) {
				if (sourcePoint.getCount() > max) {
					max = sourcePoint.getCount();
				}
			}
			for (DataPoint sourcePoint : adverseEvents.getPoints()) {
				ChartDetailDataPoint outputPoint = new ChartDetailDataPoint();
				adverseList.add(outputPoint);
				outputPoint.setDate(sourcePoint.getDate());
				outputPoint.setPercentMax(max == 0 ? 0 : (100 * sourcePoint.getCount()) / max);
			}
		}

		/* set recall event points */
		List<ChartDetailDataPoint> recallList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setRecalls(recallList);
		//TODO: 

		return responseDetail;
	}

	public FDAStats getFDAStats(String drugName) {

		if (drugName == null || !this.openFDADrugDatesMap.containsKey(drugName.toLowerCase())
				) {
			return null; //FIXME: throw 404
		}

		int totalAdverseEvents = 0;
		Chart chart = this.openFDADrugDatesMap.get(drugName.toLowerCase());
		for (DataPoint point : chart.getPoints()) {
			totalAdverseEvents += point.getCount();
		}
		//TODO: calculate total recalls
		int totalRecalls = 0;

		return new FDAStats().setTotalAdverseEvents(totalAdverseEvents).setTotalRecalls(totalRecalls);
	}

	public TwitterStats getTwitterStats(String drugName) {

		if (drugName == null || !this.twitterDetailMap.containsKey(drugName.toLowerCase())
				) {
			return null; //FIXME: throw 404
		}

		int totalPositive = 0;
		int totalNegative = 0;
		int totalNeutral = 0;

		TwitterDrugDetail detail = this.twitterDetailMap.get(drugName.toLowerCase());
		if (detail.getEvents() != null) {
			for (EventsByDate events : detail.getEvents()) {
				totalNegative += events.getNegativeCount();
				totalPositive += events.getPositiveCount();
				totalNeutral += events.getNeutralCount();
			}
		}

		int total = totalPositive + totalNegative + totalNeutral;

		return new TwitterStats()
		.setPercentPositive(total == 0 ? 0 : (100 * totalPositive) / total)
		.setPercentNegative(total == 0 ? 0 : (100 * totalNegative) / total)
		.setPercentUnknown(total == 0 ? 0 : (100 * totalNeutral) / total)
		.setTotalTweets(total);
	}

	public Drug getDrugDetails(String drugName) {
		if (drugName == null || drugName.trim().isEmpty() || !this.openFDADrugDescriptionMap.containsKey(drugName.toLowerCase())
				) {
			return null; //FIXME: throw 404
		}

		DrugDescription description = this.openFDADrugDescriptionMap.get(drugName.toLowerCase());

		return new Drug()
		.setBrandName(Character.toUpperCase(description.name.charAt(0)) + description.name.substring(1))
		.setGenericName(description.genericName)
		.setDescription(description.description)
		.setPharmacodynamics(description.pharmacodynamics);
	}
}
