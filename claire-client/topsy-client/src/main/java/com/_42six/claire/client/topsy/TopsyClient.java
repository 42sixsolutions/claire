package com._42six.claire.client.topsy;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._42six.claire.client.commons.response.ResponseMapper;
import com._42six.claire.client.http.HttpClient;
import com._42six.claire.client.topsy.model.TopsyResults;

public class TopsyClient extends HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(TopsyClient.class);

	private static final String SCHEME = "https";
	private static final String HOST = "otter.topsy.com";
	private static final String PATH = "/search.js";
	
	private ResponseMapper mapper;
	private final String apiKey;

	public TopsyClient(String apiKey) throws ClientProtocolException, IOException {
		super();
		this.mapper = new ResponseMapper();
		this.apiKey = apiKey;
	}

	private URI buildUri(String searchString, int offset, int perPage, Date startDate, Date endDate) throws URISyntaxException {
		URI uri = new URIBuilder()
		.setScheme(SCHEME)
		.setHost(HOST)
		.setPath(PATH)
		.setParameter("q", searchString)
		.setParameter("type", "tweet")
		.setParameter("allow_lang", "en")
		.setParameter("offset", String.valueOf(offset))
		.setParameter("perpage", String.valueOf(perPage))
		.setParameter("mintime", String.valueOf(startDate.getTime() / 1000))
		.setParameter("maxtime", String.valueOf(endDate.getTime() / 1000))
		.setParameter("apikey", this.apiKey)
		.build();

		return uri;
		//HttpGet httpget = new HttpGet(uri);
	}

	public void search(String searchString, File directory, Date startDate, Date endDate) throws ClientProtocolException, IOException, URISyntaxException, JAXBException, InstantiationException, IllegalAccessException {
		boolean doNext = true;

		final int pageCount = 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");

		for (int offset = 0, i = 0; doNext; offset += 100, ++i) {
			HttpGet httpGet = new HttpGet(buildUri(searchString, offset, pageCount, startDate, endDate));
			String response = this.execute(httpGet);
			TopsyResults results = this.mapper.unmarshalString(response, TopsyResults.class);
			if (results == null || results.response == null || results.response.list == null || results.response.list.length == 0) {
				doNext = false;
				logger.info("Completed search term [" + searchString + "]. Wrote [" + i + "] files.");
			}
			else {
				File outFile = new File(directory, 
						searchString + "-" +
								dateFormat.format(startDate) + "-" +
								dateFormat.format(endDate) + "-" +
								"pagesize" + pageCount + "-" + 
								"offset." + offset +
								".json"
						);
				logger.info("Outputting to file: " + outFile.getAbsolutePath());
				FileUtils.writeStringToFile(outFile, response);
			}
		}
	}

}
