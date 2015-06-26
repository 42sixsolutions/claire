package com._42six.claire.openfda.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Count by day response
 */
@XmlRootElement
public class OpenFDACountByDayResponse {

	public Result[] results;
	
	public static class Result {
		public String time;
		public int count;
	}
}
