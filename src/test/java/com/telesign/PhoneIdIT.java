package com.telesign;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
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
public class PhoneIdIT {
	public static String CUSTOMER_ID;
	public static String SECRET_KEY;
	public static String PHONE_NUMBER;
	
	
	@BeforeClass
    public static void setUp() throws IOException {
		Properties props = new Properties();
		try {
		props.load(new FileInputStream("test.properties"));
		} catch (FileNotFoundException fne) {
			fail("Please create a \"test.properties\" file at the root project directory " +
					"and include your telesign customerid, secretkey and your phone number. " +
					"If you need assistance, please contact telesign support at support@telesign.com");
		}
		
		CUSTOMER_ID = props.getProperty("test.customerid");
		SECRET_KEY =  props.getProperty("test.secretkey");
		PHONE_NUMBER = props.getProperty("test.phonenumber");
		
		boolean pass = true; 
		
		if(CUSTOMER_ID == null || CUSTOMER_ID.isEmpty()) {
			System.out.println("CUSTOMER_ID is not set. Please set the \"test.customerid\" property in the properties file");
			pass = false;
		}
		
		if(SECRET_KEY == null || SECRET_KEY.isEmpty()) {
			System.out.println("SECRET_KEY is not set. Please set the \"test.secretkey\" property in the properties file");
			pass = false;
		}
		if(PHONE_NUMBER == null || PHONE_NUMBER.isEmpty()) {
			System.out.println("PHONE_NUMBER is not set. Please set the \"test.phonenumber\" property in the properties file");
			pass = false;
		}
		
		if(!pass) {
			fail("Configuration file not setup correctly!");
		}
	}


	
	
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
