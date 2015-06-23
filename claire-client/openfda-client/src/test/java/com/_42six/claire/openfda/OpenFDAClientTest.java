package com._42six.claire.openfda;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class OpenFDAClientTest {

	private OpenFDAClient client;
	
    public OpenFDAClientTest() {
    }

    @Before
    public void setup() throws ClientProtocolException, IOException {
    	this.client = new OpenFDAClient();
    }
    
    @Ignore
    @Test
    public void testClient() throws Exception {
    	System.out.println(this.client.search());
    }
    
    @After
    public void teardown() throws IOException {
    	this.client.close();
    }
}
