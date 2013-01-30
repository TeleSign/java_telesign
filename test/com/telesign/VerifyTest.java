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

	public final String CUSTOMER_ID = "";
	public final String SECRET_KEY = "";
	public final String PHONE_NUMBER = "";
	
	
	@Test
	public void verifyError() {
		Verify ver = new Verify("Junk" , "Fake");
		VerifyResponse ret = ver.requestCall("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}
	
	@Test
	public void verifyRequestCall() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.requestCall(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSMS() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.requestSMS(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestCallwithResult() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.requestCall(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.getCallResults(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSwithResult() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		VerifyResponse ret = ver.requestSMS(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.getSMSResults(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
	}
}
