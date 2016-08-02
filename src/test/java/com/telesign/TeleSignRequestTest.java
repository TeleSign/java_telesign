package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.testUtil.TestUtil;
import com.telesign.util.AuthMethod;
import com.telesign.util.TeleSignRequest;
import com.telesign.util.TeleSignRequest.RequestBuilder;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class TeleSignRequestTest {
	
	public static RequestBuilder requestBuilder;
	
	@BeforeClass
    public static void setUp() throws IOException {		
		TestUtil.initProperties();
		if(TestUtil.runTests)
			TestUtil.startServer();
		else
			TestUtil.testUrl = "https://rest.telesign.com";
		requestBuilder = initRequestParams();
	}
	
	@AfterClass
	public static void stopServer(){
		if(TestUtil.runTests)
			TestUtil.stopServer();
	}
	
	public static RequestBuilder initRequestParams(){
		if(TestUtil.CUSTOMER_ID.isEmpty() || TestUtil.SECRET_KEY.isEmpty() || TestUtil.PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		return TeleSignRequest.init(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY);
		
	}

	@Test
	public void requestCreation() {	
		requestBuilder.baseUrl(TestUtil.testUrl).subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();
		
		assertNotNull(tr);
		
	}
	
	@Test(expected=IOException.class)
	public void malformedUrl() throws IOException {
		requestBuilder.baseUrl(":junk/rest.telesign.com").subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();
				
		assertNotNull(tr);
		tr.executeRequest();
	}
	
	@Test
	public void addParameterTest() {
		requestBuilder.baseUrl(TestUtil.testUrl).subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();
				
		assertNull(tr.getAllParams().get("code"));
		tr.addParam("code", "001");
		assertTrue(tr.getAllParams().get("code").equals("001"));
	}
	
	@Test
	public void addHeaderTest() {
		requestBuilder.baseUrl(TestUtil.testUrl).subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();
		
		assertNull(tr.getAllHeaders().get("Authorization"));
		tr.addHeader("Authorization", "fake");
		assertTrue(tr.getAllHeaders().get("Authorization").equals("fake"));
		
		assertNull(tr.getTsHeaders().get("X-TS-Date"));
		assertNull(tr.getAllHeaders().get("X-TS-Date"));
		
		tr.addHeader("X-TS-Date", "2012-03-13");		
		
		assertTrue(tr.getTsHeaders().get("X-TS-Date").equals("2012-03-13"));
		assertTrue(tr.getAllHeaders().get("X-TS-Date").equals("2012-03-13"));
		
		try {
			String json = tr.executeRequest();
			assertNotNull(json);
		} catch (IOException e) {
			fail("IOException through " + e.getMessage());
		}
	}
	
	@Test
	public void shaMethodTest() throws IOException {
		requestBuilder.baseUrl(TestUtil.testUrl).subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();
		String result = null;
				
		tr.setSigningMethod(AuthMethod.SHA256);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
	}
	
	@Test
	public void nonceTest() throws IOException {
		requestBuilder.baseUrl(TestUtil.testUrl).subResource("/v1/phoneid/standard/15551234567").httpMethod("GET").httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);		
		TeleSignRequest tr = requestBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).create();		
		String nonce = "myUniqueNonce" + System.currentTimeMillis();
		String result = null;
		
		tr.setNonce(nonce);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
		
		if(!TestUtil.runTests){
			response = gson.fromJson(result, PhoneIdStandardResponse.class);
			assertTrue(response.errors[0].code == -30012);
		}
	}

}
