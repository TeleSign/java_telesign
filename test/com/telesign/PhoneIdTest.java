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

	public final String CUSTOMER_ID = "CUSTOMER_ID_GOES_HERE";
	public final String SECRET_KEY = "SECRET_KEY_GOES_HERE";
	public final String PHONE_NUMBER = "3105551212";	
	
	@Test
	public void phoneIdError() {
		PhoneId pid = new PhoneId("Junk" , "Fake");
		PhoneIdStandardResponse ret = pid.standard("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
		
		PhoneIdScoreResponse ret2 = pid.score("13102224444", "BACS");
		assertNotNull(ret2);
		assertTrue(ret2.errors[0].code == -30000);
		
		PhoneIdContactResponse ret3 = pid.contact("13102224444", "BACS");
		assertNotNull(ret3);
		assertTrue(ret3.errors[0].code == -30000);
		
		PhoneIdLiveResponse ret4 = pid.live("13102224444", "BACS");
		assertNotNull(ret4);
		assertTrue(ret4.errors[0].code == -30000);
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
	
	@Test
	public void responseToString() {
		
		PhoneId pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		PhoneIdStandardResponse ret1 = pid.standard(PHONE_NUMBER);
		PhoneIdContactResponse ret2 = pid.contact(PHONE_NUMBER , "BACS");
		PhoneIdScoreResponse ret3 = pid.score(PHONE_NUMBER , "BACS");
		PhoneIdLiveResponse ret4 = pid.live(PHONE_NUMBER , "BACS");
		
		//all the successful responses should contain a json formatted reference_id at the start
		String json1 = ret1.toString();
		System.out.println(json1);
		assertTrue(json1.contains("{\"reference_id\":"));
		String json2 = ret2.toString();
		System.out.println(json2);
		assertTrue(json2.contains("{\"reference_id\":"));
		String json3 = ret3.toString();
		System.out.println(json3);
		assertTrue(json3.contains("{\"reference_id\":"));
		String json4 = ret4.toString();
		System.out.println(json4);
		assertTrue(json4.contains("{\"reference_id\":"));
		
	}
	
	
	
	
}
