package com._42six.claire.openfda;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import com._42six.claire.client.http.HttpClient;

public class OpenFDAClient extends HttpClient {

	private static final String SCHEME = "https";
	private static final String HOST = "api.fda.gov";
	private static final String PATH = "/drug/event.json";
	
	public OpenFDAClient() throws ClientProtocolException, IOException {
		super();
	}
	
	private URI buildUri() throws URISyntaxException {
		URI uri = new URIBuilder()
        .setScheme(SCHEME)
        .setHost(HOST)
        .setPath(PATH)
        //.setParameter("q", searchString)
        .build();
		
		return uri;
	}

	public String search() throws ClientProtocolException, IOException, URISyntaxException {
		HttpGet get = new HttpGet(buildUri());
		return this.execute(get);
	}

}
