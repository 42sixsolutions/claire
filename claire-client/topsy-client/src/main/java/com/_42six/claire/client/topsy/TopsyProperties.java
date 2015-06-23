package com._42six.claire.client.topsy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TopsyProperties extends Properties {
	
	public static final String FIELD_API_KEY = "topsy.api.key";

	private static final long serialVersionUID = 5880172540776154326L;
	
	public TopsyProperties(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		this.load(input);
	}

}
