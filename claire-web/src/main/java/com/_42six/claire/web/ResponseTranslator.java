package com._42six.claire.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math3.stat.regression.SimpleRegression;
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
import com._42six.claire.commons.model.DrugRankings;
import com._42six.claire.commons.model.FDAStats;
import com._42six.claire.commons.model.Trend;
import com._42six.claire.commons.model.TwitterStats;
import com._42six.claire.openfda.util.OpenFDAUtil;
import com._42six.claire.web.model.SortableDrug;

/**
 * As service to both translate POJOs to response objects, 
 * and a lookup table for reference data.
 */
public class ResponseTranslator {

	private static ResponseTranslator instance;

	private final Calendar minDate ;
	private final Calendar maxDate;

	private final Map<String, TwitterDrugDetail> twitterDetailMap;
	private final Map<String, Chart> openFDADrugDatesMap;
	private final Map<String, DrugDescription> openFDADrugDescriptionMap;
	private final Map<String, Chart> openFDADrugRecallsMap;
	private final Map<String, DrugRankings> drugRankMap;

	private SortedSet<Trend> positiveTrendList;
	private SortedSet<Trend> adverseEventTrendList;

	private Map<String, Trend> twitterTrendMap;


	public ResponseTranslator(
			File twitterDetailsFile,
			File openFDADrugAdverseFile,
			File openFDADrugDescriptionFile,
			File openFDADrugRecallsFile
			) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException, ParseException {
		this(
				new FileInputStream(twitterDetailsFile),
				new FileInputStream(openFDADrugAdverseFile),
				new FileInputStream(openFDADrugDescriptionFile),
				new FileInputStream(openFDADrugRecallsFile)
				);
	}

	/**
	 * Get a singleton instance
	 * 
	 * @param twitterDetailsStream
	 * @param openFDADrugAdverseStream
	 * @param openFDADrugDescriptionsStream
	 * @param openFDADrugRecallsStream
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static synchronized ResponseTranslator getInstance(
			InputStream twitterDetailsStream,
			InputStream openFDADrugAdverseStream,
			InputStream openFDADrugDescriptionsStream,
			InputStream openFDADrugRecallsStream
			)  throws JsonParseException, JsonMappingException, IOException, ParseException {
		if(instance == null) {
			instance = new ResponseTranslator(
					twitterDetailsStream, 
					openFDADrugAdverseStream, 
					openFDADrugDescriptionsStream, 
					openFDADrugRecallsStream);
		}
		return instance;
	}

	public ResponseTranslator(
			InputStream twitterDetailsStream,
			InputStream openFDADrugAdverseStream,
			InputStream openFDADrugDescriptionsStream,
			InputStream openFDADrugRecallsStream
			) throws JsonParseException, JsonMappingException, IOException, ParseException {

		this.minDate = Calendar.getInstance();
		this.minDate.setTime(new SimpleDateFormat("yyyyMMdd").parse("20140101"));
		this.maxDate = Calendar.getInstance();
		this.maxDate.setTime(new SimpleDateFormat("yyyyMMdd").parse("20140630"));

		ResponseMapper mapper =  new ResponseMapper();

		ChartCollection adverseCharts = mapper.unmarshalStream(openFDADrugAdverseStream, ChartCollection.class);
		this.openFDADrugDatesMap = createOpenFDADatesMap(adverseCharts);

		TwitterDrugDetailCollection twitterDrugDetails = mapper.unmarshalStream(twitterDetailsStream, TwitterDrugDetailCollection.class);
		this.twitterDetailMap = createTwitterDrugDetailsMap(twitterDrugDetails);

		DrugDescriptionCollection descriptions = mapper.unmarshalStream(openFDADrugDescriptionsStream, DrugDescriptionCollection.class);
		this.openFDADrugDescriptionMap = createOpenFDADrugDescriptionMap(descriptions);

		ChartCollection recallCharts = mapper.unmarshalStream(openFDADrugRecallsStream, ChartCollection.class);
		this.openFDADrugRecallsMap = createOpenFDADatesMap(recallCharts);

		this.drugRankMap = createDrugRankMap();
		createTrends();

	}

	/**
	 * Create a chart lookup for drug names.
	 * 
	 * @param charts
	 * @return
	 */
	private Map<String, Chart> createOpenFDADatesMap(ChartCollection charts) {
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

	/**
	 * Create a drug description lookup map.
	 * 
	 * @param descriptions
	 * @return
	 */
	private Map<String, DrugDescription> createOpenFDADrugDescriptionMap(DrugDescriptionCollection descriptions) {
		Map<String, DrugDescription> map = new HashMap<String, DrugDescription>();

		for (DrugDescription desc : descriptions.descriptions) {
			map.put(desc.name.toLowerCase(), desc);
		}
		return map;
	}

	/**
	 * Create a TwitterDrugDetail lookup map for drug names.
	 * 
	 * @param twitterDrugDetails
	 * @return
	 */
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

	/**
	 * Get a list of all available drugs.
	 */
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

	/**
	 * Get a chart for a given drugName.
	 * 
	 * @param drugName
	 * @param peakPercent - the percent of increase above the average for a day to be used for the "tweet spike"
	 * @return
	 */
	public ChartDetail getChart(String drugName, Integer peakPercent) {
		if (drugName == null ||
				(!this.twitterDetailMap.containsKey(drugName.toLowerCase()) &&
						!this.openFDADrugDatesMap.containsKey(drugName.toLowerCase()))
				) {
			return null;
		}
		ChartDetail responseDetail = new ChartDetail();

		/* set tweet points */

		List<ChartDetailDataPoint> positiveList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setPositiveTweets(positiveList);
		List<ChartDetailDataPoint> negativeList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setNegativeTweets(negativeList);
		List<ChartDetailDataPoint> neutralList = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setUnknownTweets(neutralList);
		List<ChartDetailDataPoint> positiveTweetSpikes = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setPositiveTweetSpikes(positiveTweetSpikes);
		List<ChartDetailDataPoint> negativeTweetSpikes = new ArrayList<ChartDetailDataPoint>();
		responseDetail.setNegativeTweetSpikes(negativeTweetSpikes);

		TwitterDrugDetail sourceDetail = this.twitterDetailMap.get(drugName.toLowerCase());

		if (sourceDetail != null) {
			int totalTweets = 0;
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

				totalTweets += event.getPositiveCount() + event.getNegativeCount() + event.getNeutralCount();
			}

			if (totalTweets > 0) {

				double averageTweetsPerDay = (double)totalTweets / sourceDetail.getEvents().size();
				double threshold = (((double)peakPercent / 100) + 1) * averageTweetsPerDay;
				for (EventsByDate event : sourceDetail.getEvents()) {

					ChartDetailDataPoint positivePoint = createPointFromThreshold(event.getDate(), event.getPositiveCount(), threshold);
					if (positivePoint != null) {
						positiveTweetSpikes.add(positivePoint);
					}

					ChartDetailDataPoint negativePoint = createPointFromThreshold(event.getDate(), event.getNegativeCount(), threshold);
					if (negativePoint != null) {
						negativeTweetSpikes.add(negativePoint);
					}
				}
			}
		}

		/* set adverse event points */
		Chart adverseEvents = this.openFDADrugDatesMap.get(drugName.toLowerCase());
		List<ChartDetailDataPoint> adverseList = createChartDetail(adverseEvents, true);
		responseDetail.setAdverseEvents(adverseList);

		/* set recall event points */
		Chart recallEvents = this.openFDADrugRecallsMap.get(drugName.toLowerCase());
		List<ChartDetailDataPoint> recallList = createChartDetail(recallEvents, true);
		responseDetail.setRecalls(recallList);

		return responseDetail;
	}

	/**
	 * Create a point if the count is greater than the threshold
	 * 
	 * @param date
	 * @param count
	 * @param threshold
	 * @return
	 */
	private ChartDetailDataPoint createPointFromThreshold(Date date, int count, final double threshold) {
		if (count > threshold) {
			ChartDetailDataPoint point = new ChartDetailDataPoint();
			point.setDate(date);
			point.setPercentMax(count);
			return point;
		}
		return null;
	}

	/**
	 * Create a list of ChartDetailDataPoints
	 * 
	 * @param chart
	 * @param useCount
	 * @return
	 */
	private List<ChartDetailDataPoint> createChartDetail(Chart chart, boolean useCount) {
		List<ChartDetailDataPoint> chartList = new ArrayList<ChartDetailDataPoint>();

		if (chart != null && chart.getPoints() != null && !chart.getPoints().isEmpty()) {
			int max = 0;
			for (DataPoint sourcePoint : chart.getPoints()) {
				if (sourcePoint.getCount() > max) {
					max = sourcePoint.getCount();
				}
			}
			for (DataPoint sourcePoint : chart.getPoints()) {
				ChartDetailDataPoint outputPoint = new ChartDetailDataPoint();
				chartList.add(outputPoint);
				outputPoint.setDate(sourcePoint.getDate());
				if (useCount) {
					outputPoint.setPercentMax(sourcePoint.getCount());
				}
				else {
					outputPoint.setPercentMax(max == 0 ? 0 : (double)(100 * sourcePoint.getCount()) / max);
				}
			}
		}

		return chartList;
	}

	/**
	 * Get FDAStats for a drug name.
	 * 
	 * @param drugName
	 * @return
	 */
	public FDAStats getFDAStats(String drugName) {

		if (drugName == null) {
			return null;
		}

		int totalAdverseEvents = 0;
		Chart adverseChart = this.openFDADrugDatesMap.get(drugName.toLowerCase());
		if (adverseChart != null) {
			for (DataPoint point : adverseChart.getPoints()) {
				totalAdverseEvents += point.getCount();
			}
		}

		int totalRecalls = 0;
		Chart recallChart = this.openFDADrugRecallsMap.get(drugName.toLowerCase());
		if (recallChart != null) {
			for (DataPoint point : recallChart.getPoints()) {
				totalRecalls += point.getCount();
			}
		}

		return new FDAStats().setTotalAdverseEvents(totalAdverseEvents).setTotalRecalls(totalRecalls);
	}

	/**
	 * Get twitter statistics for a drug name.
	 * 
	 * @param drugName
	 * @return
	 */
	public TwitterStats getTwitterStats(String drugName) {

		if (drugName == null) {
			return null;
		}

		int totalPositive = 0;
		int totalNegative = 0;
		int totalNeutral = 0;

		TwitterDrugDetail detail = this.twitterDetailMap.get(drugName.toLowerCase());
		if (detail != null && detail.getEvents() != null) {
			for (EventsByDate events : detail.getEvents()) {
				totalNegative += events.getNegativeCount();
				totalPositive += events.getPositiveCount();
				totalNeutral += events.getNeutralCount();
			}
		}

		int total = totalPositive + totalNegative + totalNeutral;

		return new TwitterStats()
		.setPercentPositive(total == 0 ? 0 : (double)(100 * totalPositive) / total)
		.setPercentNegative(total == 0 ? 0 : (double)(100 * totalNegative) / total)
		.setPercentUnknown(total == 0 ? 0 : (double)(100 * totalNeutral) / total)
		.setTotalTweets(total);
	}

	/**
	 * Get drug details for a drug name
	 * 
	 * @param drugName
	 * @return
	 */
	public Drug getDrugDetails(String drugName) {
		if (drugName == null || drugName.trim().isEmpty() || !this.openFDADrugDescriptionMap.containsKey(drugName.toLowerCase())
				) {
			return null;
		}

		DrugDescription description = this.openFDADrugDescriptionMap.get(drugName.toLowerCase());

		return new Drug()
		.setBrandName(Character.toUpperCase(description.name.charAt(0)) + description.name.substring(1))
		.setGenericName(description.genericName)
		.setDescription(description.description)
		.setPharmacodynamics(description.pharmacodynamics);
	}

	/**
	 * Get a drugs rank
	 * 
	 * @param drugName
	 * @return
	 */
	public DrugRankings getDrugRank(String drugName) {
		return drugName == null ? null : this.drugRankMap.get(drugName.toLowerCase());
	}

	/**
	 * Create a DrugRankings lookup map for drug names
	 * 
	 * @return
	 */
	private Map<String, DrugRankings> createDrugRankMap() {

		Map<String, DrugRankings> map = new HashMap<String, DrugRankings>();

		SortedSet<SortableDrug> adverseSet = new TreeSet<SortableDrug>();
		SortedSet<SortableDrug> negativeSet = new TreeSet<SortableDrug>();
		SortedSet<SortableDrug> neutralSet = new TreeSet<SortableDrug>();
		SortedSet<SortableDrug> positiveSet = new TreeSet<SortableDrug>();
		SortedSet<SortableDrug> recallSet = new TreeSet<SortableDrug>();


		for (String drugName : OpenFDAUtil.DRUG_NAMES_SET) {

			String drugLower = drugName.toLowerCase();
			map.put(drugLower, new DrugRankings());

			FDAStats fdaStats = getFDAStats(drugLower);
			adverseSet.add(new SortableDrug(drugLower, (double)fdaStats.getTotalAdverseEvents()));
			recallSet.add(new SortableDrug(drugLower, (double)fdaStats.getTotalRecalls()));

			TwitterStats twitterStats = getTwitterStats(drugLower);

			positiveSet.add(new SortableDrug(drugLower, twitterStats.getPercentPositive()));
			negativeSet.add(new SortableDrug(drugLower, twitterStats.getPercentNegative()));
			neutralSet.add(new SortableDrug(drugLower, twitterStats.getPercentUnknown()));
		}

		int i = 0;
		for (SortableDrug drug : adverseSet) {
			++i;
			map.get(drug.getName()).setAdverseEventsRank(i);
		}
		i = 0;
		for (SortableDrug drug : recallSet) {
			++i;
			map.get(drug.getName()).setRecallsRank(i);
		}
		i = 0;
		for (SortableDrug drug : positiveSet) {
			++i;
			map.get(drug.getName()).setPositiveTweetsRank(i);
		}
		i = 0;
		for (SortableDrug drug : neutralSet) {
			++i;
			map.get(drug.getName()).setUnknownTweetsRank(i);
		}
		i = 0;
		for (SortableDrug drug : negativeSet) {
			++i;
			map.get(drug.getName()).setNegativeTweetsRank(i);
		}

		return Collections.unmodifiableMap(map);
	}

	/**
	 * Create the trends lists
	 */
	private void createTrends() {
		this.positiveTrendList = new TreeSet<Trend>();
		this.adverseEventTrendList = new TreeSet<Trend>();
		this.twitterTrendMap = new HashMap<String, Trend>();

		for (String drug : OpenFDAUtil.DRUG_NAMES_SET) {
			//calculate twitter slope
			SimpleRegression twitterRegression = new SimpleRegression();
			TwitterDrugDetail detail = this.twitterDetailMap.get(drug);
			long diff = 0;
			boolean firstRun = true;
			Date lastDate = null;
			Date thisDate = null;
			for (EventsByDate event : detail.getEvents()) {
				thisDate = event.getDate();
				
				if (firstRun) {
					lastDate = event.getDate();
				}
				
				//one unit on the x axis is the number of hours between events
				diff += (thisDate.getTime() - lastDate.getTime()) / (1000 * 60 * 60);
				
				twitterRegression.addData(diff, event.getPositiveCount());
				lastDate = event.getDate();
				firstRun = false;
			}
			Trend twitterTrend = new Trend(
					Character.toUpperCase(drug.charAt(0)) + drug.substring(1), 
					twitterRegression.getSlope());
			this.positiveTrendList.add(twitterTrend);
			this.twitterTrendMap.put(drug.toLowerCase(), twitterTrend);

			//calculate adverse event slope
			SimpleRegression adverseEventRegression = new SimpleRegression();
			Chart chart = this.openFDADrugDatesMap.get(drug);
			firstRun = true;
			long l = 0;
			for (DataPoint point : chart.getPoints()) {
				thisDate = point.getDate();
				
				if (firstRun) {
					lastDate = point.getDate();
				}
				
				//one unit on the x axis is the number of hours between events
				l += (thisDate.getTime() - lastDate.getTime()) / (1000 * 60 * 60);
				
				adverseEventRegression.addData(l, point.getCount());
				lastDate = point.getDate();
				firstRun = false;
			}
			this.adverseEventTrendList.add(new Trend(
					Character.toUpperCase(drug.charAt(0)) + drug.substring(1), 
					adverseEventRegression.getSlope()));
		}
	}

	/**
	 * Get the list of twitter positive trending drugs
	 * 
	 * @return
	 */
	public List<Trend> getPositiveTwitterTrends() {
		List<Trend> list = new ArrayList<Trend>(this.positiveTrendList);
		int i = list.size() > 5 ? 5 : list.size();
		return list.subList(0, i);
	}

	/**
	 * Get the list of twitter negative trending drugs
	 * 
	 * @return
	 */
	public List<Trend> getNegativeTwitterTrends() {
		List<Trend> list = new ArrayList<Trend>(this.positiveTrendList);
		Collections.reverse(list);
		int i = list.size() > 5 ? 5 : list.size();
		return list.subList(0, i);
	}

	/**
	 * Get the list of adverse event trending drugs
	 * 
	 * @return
	 */
	public List<Trend> getAdverseEventsTrends() {
		List<Trend> list = new ArrayList<Trend>(this.adverseEventTrendList);
		int i = list.size() > 5 ? 5 : list.size();
		return list.subList(0, i);
	}

	/**
	 * Get the trend score for a drug name
	 * 
	 * @return
	 */
	public Trend getOverallTwitterTrend(String drugName) {
		if (drugName == null) {
			return null;
		}
		return this.twitterTrendMap.get(drugName.toLowerCase());
	}
}
