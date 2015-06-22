package com._42six.claire.client.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * A generic REST client using Apache's HttpCLient
 */
public class HttpClient {

	private CloseableHttpClient httpClient;

	public HttpClient() throws ClientProtocolException, IOException {
		this.httpClient = HttpClients.createDefault();
	}

	public String execute(String endpoint) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(endpoint);
		CloseableHttpResponse response = this.httpClient.execute(httpGet);

		try {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}

		} finally {
			response.close();
		}
	}
	
	public void close() throws IOException {
		this.httpClient.close();
	}
}
