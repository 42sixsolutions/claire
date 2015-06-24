package com._42six.claire.client.topsy.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopsyResults {

	public TopsyResponse response;

	public TopsyResults() {
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
