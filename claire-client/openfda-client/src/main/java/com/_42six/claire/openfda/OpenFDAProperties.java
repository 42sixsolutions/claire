package com._42six.claire.openfda;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OpenFDAProperties extends Properties {
	
	private static final long serialVersionUID = 3671260118582543074L;
	
	public static final String FIELD_API_KEY = "openfda.api.key";

	public OpenFDAProperties(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		this.load(input);
	}

}