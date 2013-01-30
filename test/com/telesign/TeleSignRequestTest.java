package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.telesign.util.TeleSignRequest;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class TeleSignRequestTest {

	@Test
	public void requestCreation() {
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		assertNotNull(tr);
		
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
	}

}
