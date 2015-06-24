package com._42six.claire.client.topsy.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopsyResults {
	
	public TopsyRequest request;
	public TopsyResponse response;

	public TopsyResults() {
	}
	
	public static class TopsyRequest {
		
		public Parameters parameters;
		
		public static class Parameters {
			
			public String q;
		}
	}

	public static class TopsyResponse {

		public TopsyResult[] list;
		public int total;

		public TopsyResponse () {
		}

		public static class TopsyResult {

			public String content;
			public long firstpost_date;
			
			public TopsyResult() {
			}
		}
	}

}
