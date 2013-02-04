package com.telesign;

import static org.junit.Assert.*;

import org.junit.Test;

import com.telesign.phoneid.PhoneId;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class PhoneIdTest {

	public final String CUSTOMER_ID = "440813A2-1F7E-11E1-B760-000000000000";
	public final String SECRET_KEY = "eiWUKl5jc3wfwI5w3xFma5kp8MrYArj66Z4+JkvhgUubhRCuymfEOWrKLQZXFoiG+3GXYzLJP5s5IGyXpIeP1w==";
	public final String PHONE_NUMBER = "8582259543";
	
	
	@Test
	public void phoneIdError() {
		PhoneId pid = new PhoneId("Junk" , "Fake");
		PhoneIdStandardResponse ret = pid.standard("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}
	
	@Test
	public void phoneIdStandard() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty()) {
			fail("CUSTOMER_ID and SECRET_KEY must be set to pass this test");
		}
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdStandardResponse ret = pid.standard("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);
		
	}
	
	@Test
	public void phoneIdScore() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty()) {
			fail("CUSTOMER_ID and SECRET_KEY must be set to pass this test");
		}
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdScoreResponse ret = pid.score("13105551234", "BACS");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);
		assertTrue(ret.risk.level.length() > 0);
		
	}
	
	@Test
	public void phoneIdContact() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty()) {
			fail("CUSTOMER_ID and SECRET_KEY must be set to pass this test");
		}
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdContactResponse ret = pid.contact("13105551234", "BACS");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301); //for this fake number we expect a partially completed request
	}
	
	@Test
	public void phoneIdLiveDummy() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty()) {
			fail("CUSTOMER_ID and SECRET_KEY must be set to pass this test");
		}
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdLiveResponse ret = pid.live("13105551234", "BACS");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301);  //for this fake number we expect a partially completed request
	}
		
	@Test
	public void phoneIdLiveReal() {
		
		if(PHONE_NUMBER.isEmpty()) {
			fail("For this test we require a valid phone number to test against");
		}
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdLiveResponse ret2 = pid.live(PHONE_NUMBER , "BACS");
		
		assertTrue(ret2.status.code == 300); 
		assertNotNull(ret2.live);
		assertTrue(!ret2.live.subscriber_status.isEmpty());
	}
	
	
	
	
}
