package com._42six.claire.openfda.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class OpenFDALabelResponse {
	
	public Result[] results;

	public static class Result {
		
		public String[] description;
		public String[] pharmacodynamics;
		public OpenFDA openfda;
		
		public static class OpenFDA {
			
			public String[] generic_name;
		}
	}
}
