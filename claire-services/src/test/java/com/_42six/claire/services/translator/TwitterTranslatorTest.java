package com._42six.claire.services.translator;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TwitterTranslatorTest {
	
	TwitterTranslator twitterTranslator;
	
	@Before
	public void setup() {
		this.twitterTranslator = new TwitterTranslator();
	}
	
	@Ignore
	@Test
	public void testTwitterTranslator() throws Exception {
		File[] fileList = new File("/tmp/topsy").listFiles();
		twitterTranslator.translate(fileList);
	}

}
