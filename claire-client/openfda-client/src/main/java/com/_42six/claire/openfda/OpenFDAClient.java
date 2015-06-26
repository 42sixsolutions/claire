package com._42six.claire.openfda;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com._42six.claire.client.commons.response.Chart;
import com._42six.claire.client.commons.response.DataPoint;
import com._42six.claire.client.commons.response.DrugDescriptionCollection.DrugDescription;
import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.http.HttpClient;
import com._42six.claire.openfda.model.OpenFDACountByDayResponse;
import com._42six.claire.openfda.model.OpenFDACountByDayResponse.Result;
import com._42six.claire.openfda.model.OpenFDALabelResponse;
import com._42six.claire.openfda.util.OpenFDADateAdapter;

/**
 * A client for interacting with the OpenFDA web api
 */
public class OpenFDAClient extends HttpClient {

	private static final String SCHEME = "https";
	private static final String HOST = "api.fda.gov";
	private static final String PATH_DRUG_ADVERSE = "/drug/event.json";
	private static final String PATH_DRUG_LABEL = "/drug/label.json";
	private static final String PATH_DRUG_RECALL = "/drug/enforcement.json";

	private final String apiKey;
	private ResponseMapper mapper;
	private OpenFDADateAdapter dateAdapter;

	public OpenFDAClient(String apiKey) throws ClientProtocolException, IOException {
		super();
		this.apiKey = apiKey;
		this.mapper = new ResponseMapper();
		this.dateAdapter = new OpenFDADateAdapter();
	}

	/**
	 * Build a URI to query adverse events
	 * 
	 * @param drugName
	 * @param countField
	 * @return 
	 * @throws URISyntaxException
	 */
	private URI buildUriAdverseEvents(String drugName, String countField) throws URISyntaxException {
		URI uri = new URIBuilder()
		.setScheme(SCHEME)
		.setHost(HOST)
		.setPath(PATH_DRUG_ADVERSE)
		.setParameter("search", "patient.drug.medicinalproduct:" + drugName)
		.setParameter("count", countField)
		.setParameter("api_key", this.apiKey)
		.build();

		return uri;
	}

	/**
	 * Build a URI to query drug labels
	 * 
	 * @param drugName
	 * @return
	 * @throws URISyntaxException
	 */
	private URI buildUriLabel(String drugName) throws URISyntaxException {
		URI uri = new URIBuilder()
		.setScheme(SCHEME)
		.setHost(HOST)
		.setPath(PATH_DRUG_LABEL)
		.setParameter("search", "openfda.brand_name:" + drugName)
		.setParameter("api_key", this.apiKey)
		.build();

		return uri;
	}

	/**
	 * Build a URI to query recall events
	 * 
	 * @param drugName
	 * @param countField
	 * @return 
	 * @throws URISyntaxException
	 */
	private URI buildUriRecallEvents(String drugName, String countField) throws URISyntaxException {
		URI uri = new URIBuilder()
		.setScheme(SCHEME)
		.setHost(HOST)
		.setPath(PATH_DRUG_RECALL)
		.setParameter("search", "product_description:" + drugName)
		.setParameter("count", countField)
		.setParameter("api_key", this.apiKey)
		.build();

		return uri;
	}

	/**
	 * Search drug descriptions
	 * 
	 * @param drugName
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public DrugDescription searchDescription(String drugName) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		OpenFDALabelResponse label = searchLabels(drugName);
		return toDrugDescription(drugName, label);
	}

	private OpenFDALabelResponse searchLabels(String drugName) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		HttpGet get = new HttpGet(buildUriLabel(drugName));
		String response = this.execute(get);
		return this.mapper.unmarshalString(response, OpenFDALabelResponse.class);
	}

	private OpenFDACountByDayResponse searchAdverseEvents(String drugName, String countField) throws ClientProtocolException, IOException, URISyntaxException {
		HttpGet get = new HttpGet(buildUriAdverseEvents(drugName, countField));
		String response = this.execute(get);
		return this.mapper.unmarshalString(response, OpenFDACountByDayResponse.class);
	}

	/**
	 * Search adverse events
	 * 
	 * @param drugName
	 * @param countField
	 * @param startDate
	 * @param endDate
	 * @return Object representing number of events per day for a drug
	 * @throws Exception
	 */
	public Chart searchAdverseEvents(String drugName, String countField, Date startDate, Date endDate) throws Exception {
		OpenFDACountByDayResponse response = searchAdverseEvents(drugName, countField);
		return toChart(drugName, response, startDate, endDate);
	}

	private OpenFDACountByDayResponse searchRecallEvents(String drugName, String countField) throws ClientProtocolException, IOException, URISyntaxException {
		HttpGet get = new HttpGet(buildUriRecallEvents(drugName, countField));
		String response = this.execute(get);
		return this.mapper.unmarshalString(response, OpenFDACountByDayResponse.class);
	}

	/**
	 * Search recall events
	 * 
	 * @param drugName
	 * @param countField
	 * @param startDate
	 * @param endDate
	 * @return Object representing number of recalls per day for a drug
	 * @throws Exception
	 */
	public Chart searchRecallEvents(String drugName, String countField, Date startDate, Date endDate) throws Exception {
		OpenFDACountByDayResponse response = searchRecallEvents(drugName, countField);
		return toChart(drugName, response, startDate, endDate);
	}

	/**
	 * Convert an OpenFDALabelResponse to a DrugDescription
	 * 
	 * @param drugName
	 * @param label
	 * @return
	 */
	private DrugDescription toDrugDescription(String drugName, OpenFDALabelResponse label) {
		DrugDescription description = new DrugDescription();

		description.name = drugName;
		if (label.results != null && label.results.length > 0) {
			if (label.results[0].description != null && label.results[0].description.length > 0) {
				description.description = label.results[0].description[0];
			}
			if (label.results[0].pharmacodynamics != null && label.results[0].pharmacodynamics.length > 0) {
				description.pharmacodynamics = label.results[0].pharmacodynamics[0];
			}

			if (label.results[0].openfda != null && label.results[0].openfda.generic_name != null && label.results[0].openfda.generic_name.length > 0) {
				description.genericName = label.results[0].openfda.generic_name[0];
			}
		}

		return description;
	}

	/**
	 * Convert an OpenFDACountByDayResponse to a Chart object
	 * 
	 * @param name
	 * @param countByDay
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	private Chart toChart(String name, OpenFDACountByDayResponse countByDay, Date startDate, Date endDate) throws Exception {
		Chart chart = new Chart();
		chart.setName(name);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		TreeSet<DataPoint> set = new TreeSet<DataPoint>();
		chart.setPoints(set);
		for (Result result : countByDay.results) {
			Date date = this.dateAdapter.unmarshal(result.time);
			cal.setTime(date);
			if (cal.getTimeInMillis() == startCal.getTimeInMillis()
					|| cal.getTimeInMillis() == endCal.getTimeInMillis() 
					|| (cal.after(startCal) && cal.before(endCal))) {
				DataPoint point = new DataPoint();
				point.setDate(dateFormat.parse(result.time));
				point.setCount(result.count);
				set.add(point);
			}
		}
		return chart;
	}
}
