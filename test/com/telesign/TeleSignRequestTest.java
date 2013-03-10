package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.AuthMethod;
import com.telesign.util.TeleSignRequest;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class TeleSignRequestTest {
	
	public final String CUSTOMER_ID = "440813A2-1F7E-11E1-B760-000000000000";
	public final String SECRET_KEY = "eiWUKl5jc3wfwI5w3xFma5kp8MrYArj66Z4+JkvhgUubhRCuymfEOWrKLQZXFoiG+3GXYzLJP5s5IGyXpIeP1w==";
	public final String PHONE_NUMBER = "3105551234";

	@Test
	public void requestCreation() {
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		assertNotNull(tr);
		
	}
	
	@Test(expected=IOException.class)
	public void malformedUrl() throws IOException {
		TeleSignRequest tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "");
		assertNotNull(tr);
		tr.executeRequest();
	}
	
	@Test
	public void addParameterTest() {
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		assertNull(tr.getAllParams().get("code"));
		tr.addParam("code", "001");
		assertTrue(tr.getAllParams().get("code").equals("001"));
	}
	
	@Test
	public void addHeaderTest() {
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		
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
		String result = null;
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
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
		String nonce = "myUniqueNonce" + System.currentTimeMillis();
		String result = null;
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
		tr.setNonce(nonce);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors[0].code == -30012);
	}

}
