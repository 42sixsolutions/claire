package com._42six.claire.openfda;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import com._42six.claire.client.commons.response.Chart;
import com._42six.claire.client.commons.response.Chart.DataPoint;
import com._42six.claire.client.http.HttpClient;
import com._42six.claire.openfda.model.OpenFDACountByDayResponse;
import com._42six.claire.openfda.model.OpenFDACountByDayResponse.Result;
import com._42six.claire.openfda.util.OpenFDADateAdapter;

public class OpenFDAClient extends HttpClient {

	private static final String SCHEME = "https";
	private static final String HOST = "api.fda.gov";
	private static final String PATH = "/drug/event.json";
	
	private final String apiKey;
	private OpenFDAResultsReader reader;
	private OpenFDADateAdapter dateAdapter;
	
	public OpenFDAClient(String apiKey) throws ClientProtocolException, IOException {
		super();
		this.apiKey = apiKey;
		this.reader = new OpenFDAResultsReader();
		this.dateAdapter = new OpenFDADateAdapter();
	}
	
	private URI buildUri(String drugName, String countField) throws URISyntaxException {
		URI uri = new URIBuilder()
        .setScheme(SCHEME)
        .setHost(HOST)
        .setPath(PATH)
        .setParameter("search", "patient.drug.medicinalproduct:" + drugName)
        .setParameter("count", countField)
        .setParameter("api_key", this.apiKey)
        .build();
		
		return uri;
	}

	public OpenFDACountByDayResponse search(String drugName, String countField) throws ClientProtocolException, IOException, URISyntaxException {
		HttpGet get = new HttpGet(buildUri(drugName, countField));
		String response = this.execute(get);
		return this.reader.unmarshalCountByDay(response);
	}
	
	public Chart search(String drugName, String countField, Date startDate, Date endDate) throws Exception {
		OpenFDACountByDayResponse response = search(drugName, countField);
		return toChart(drugName, response, startDate, endDate);
	}
	
	public Chart toChart(String name, OpenFDACountByDayResponse countByDay, Date startDate, Date endDate) throws Exception {
		Chart chart = new Chart();
		chart.name = name;
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		Calendar cal = Calendar.getInstance();
		
		chart.points = new TreeSet<DataPoint>();
		for (Result result : countByDay.results) {
			Date date = this.dateAdapter.unmarshal(result.time);
			cal.setTime(date);
			if (cal.getTimeInMillis() == startCal.getTimeInMillis()
					|| cal.getTimeInMillis() == endCal.getTimeInMillis() 
					|| (cal.after(startCal) && cal.before(endCal))) {

				DataPoint point = new DataPoint();
				point.label = result.time;
				point.count = result.count;
				chart.points.add(point);
			}
		}
		return chart;
	}
}
