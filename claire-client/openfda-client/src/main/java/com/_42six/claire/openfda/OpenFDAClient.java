package com._42six.claire.openfda;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com._42six.claire.client.http.HttpClient;

public class OpenFDAClient extends HttpClient {

	private static final String ENDPOINT_DRUG = "https://api.fda.gov/drug/event.json";

	public OpenFDAClient() throws ClientProtocolException, IOException {
		super();
	}

	public String search() throws ClientProtocolException, IOException {
		return this.execute(ENDPOINT_DRUG);
	}

}
