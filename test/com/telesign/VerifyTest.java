package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.telesign.verify.Verify;
import com.telesign.verify.response.VerifyResponse;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class VerifyTest {

	public final String CUSTOMER_ID = "440813A2-1F7E-11E1-B760-000000000000";
	public final String SECRET_KEY = "eiWUKl5jc3wfwI5w3xFma5kp8MrYArj66Z4+JkvhgUubhRCuymfEOWrKLQZXFoiG+3GXYzLJP5s5IGyXpIeP1w==";
	public final String PHONE_NUMBER = "8582259543";
	
	
	@Test
	public void verifyError() {
		Verify ver = new Verify("Junk" , "Fake");
		VerifyResponse ret = ver.call("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}
	
	@Test
	public void verifyRequestCall() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.call(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithLanguage() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithAllParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", "12345", "keypress", 1, "1234", true);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSMS() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.sms(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSWithLanguage() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	@Test
	public void verifyRequestSMSAllParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US", "12345", "Thanks! Custom code template pass! Code: $$CODE$$");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestCallwithResult() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.call(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSwithResult() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.sms(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
	}
}
