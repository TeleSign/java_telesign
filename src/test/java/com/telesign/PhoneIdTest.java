package com.telesign;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telesign.phoneid.PhoneId;
import com.telesign.phoneid.PhoneId.PhoneIdBuilder;
import com.telesign.phoneid.response.PhoneIdCallForwardResponse;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdNumberDeactivationResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdSimSwapCheckResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.testUtil.TestUtil;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class PhoneIdTest {	
	
	@BeforeClass
    public static void setUp() throws IOException {
		TestUtil.initProperties();
		if(TestUtil.runTests)
			TestUtil.startServer();
		else
			TestUtil.testUrl = "https://rest.telesign.com";
	}

	@AfterClass
	public static void close(){
		if(TestUtil.runTests)
			TestUtil.stopServer();
	}
	
	@Test
	public void phoneIdError() {
		PhoneIdBuilder pidb = PhoneId.initPhoneId("Junk" , "Fake");
		pidb.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).httpsProtocol(TestUtil.HTTPS_PROTOCOL);		
		
		PhoneId pid = pidb.create();
		
		PhoneIdStandardResponse ret = pid.standard("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
		
		PhoneIdScoreResponse ret2 = pid.score("13102224444");
		assertNotNull(ret2);
		assertTrue(ret2.errors[0].code == -30000);
		
		PhoneIdContactResponse ret3 = pid.contact("13102224444");
		assertNotNull(ret3);
		assertTrue(ret3.errors[0].code == -30000);
		
		PhoneIdLiveResponse ret4 = pid.live("13102224444");
		assertNotNull(ret4);
		assertTrue(ret4.errors[0].code == -30000);
	}
	
	private PhoneId initPhoneIdParams() {
		if(TestUtil.CUSTOMER_ID.isEmpty() || TestUtil.SECRET_KEY.isEmpty()) {
			fail("TestUtil.CUSTOMER_ID and TestUtil.SECRET_KEY must be set to pass this test");
		}
		
		PhoneIdBuilder pidb = PhoneId.initPhoneId(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY);
		pidb.extra(TestUtil.EXTRA_MAP).ciphers(TestUtil.CIPHERS);
		pidb.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).httpsProtocol(TestUtil.HTTPS_PROTOCOL).url(TestUtil.testUrl);
		pidb.originatingIp(TestUtil.ORIGINATING_IP).sessionId(TestUtil.SESSION_ID);
		
		PhoneId pid = pidb.create();
				
		return pid;
	}
	
	@Test
	public void phoneIdStandard() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdStandardResponse ret = pid.standard("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);
		
	}
	
	@Test
	public void phoneIdScore() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdScoreResponse ret = pid.score(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);
		assertTrue(ret.risk.level.length() > 0);
		
	}
	
	@Test
	public void phoneIdContact() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdContactResponse ret = pid.contact("13105551234");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301); //for this fake number we expect a partially completed request
	}
	
	@Test
	public void phoneIdLiveDummy() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdLiveResponse ret = pid.live("13105551234");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301);  //for this fake number we expect a partially completed request
	}
	
	@Test
	public void phoneIdLiveReal() {		
		if(TestUtil.PHONE_NUMBER.isEmpty()) {
			fail("For this test we require a valid phone number to test against");
		}
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdLiveResponse ret2 = pid.live(TestUtil.PHONE_NUMBER);
		
		assertTrue(ret2.status.code == 300); 
		assertNotNull(ret2.live);
		assertTrue(!ret2.live.subscriber_status.isEmpty());
	}
	
	@Test
	public void responseToString() {
		if(TestUtil.PHONE_NUMBER.isEmpty()) {
			fail("For this test we require a valid phone number to test against");
		}
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdStandardResponse ret1 = pid.standard(TestUtil.PHONE_NUMBER);
		PhoneIdContactResponse ret2 = pid.contact(TestUtil.PHONE_NUMBER);
		PhoneIdScoreResponse ret3 = pid.score(TestUtil.PHONE_NUMBER);
		PhoneIdLiveResponse ret4 = pid.live(TestUtil.PHONE_NUMBER);
		
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
	
	@Test
	public void phoneIdSimSwap() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdSimSwapCheckResponse ret = pid.simSwap(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);System.out.println(ret.toString());
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 2204);
	}
	
	@Test
	public void phoneIdCallForward() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdCallForwardResponse ret = pid.callForward(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);System.out.println(ret.toString());
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);		
	}
	
	@Test
	public void phoneIdNoDeactivation() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdNumberDeactivationResponse ret = pid.deactivation(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 2300);		
	}	

}
