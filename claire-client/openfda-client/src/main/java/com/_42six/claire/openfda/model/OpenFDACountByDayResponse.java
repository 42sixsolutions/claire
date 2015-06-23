package com._42six.claire.openfda.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OpenFDACountByDayResponse {

	public Result[] results;
	
	public static class Result {
		public String time;
		public int count;
	}
}
