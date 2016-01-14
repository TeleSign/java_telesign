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
public class PhoneIdTest {
	public static String CUSTOMER_ID;
	public static String SECRET_KEY;
	public static String PHONE_NUMBER;
	public static String CONNECT_TIMEOUT;
	public static String READ_TIMEOUT;
	public static int readTimeout;
	public static int connectTimeout;
	public static boolean timeouts = false;
	public static String ORIGINATING_IP;
	public static String SESSION_ID;
	public static String HTTPS_PROTOCOL;
	public static boolean isHttpsProtocolSet = false;
	
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
		CONNECT_TIMEOUT =  props.getProperty("test.connecttimeout");
		READ_TIMEOUT =  props.getProperty("test.readtimeout");
		ORIGINATING_IP = props.getProperty("test.originating_ip");
		SESSION_ID = props.getProperty("test.session_id");
		HTTPS_PROTOCOL = props.getProperty("test.httpsprotocol");
		
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
		
		if(CONNECT_TIMEOUT == null || CONNECT_TIMEOUT.isEmpty() || READ_TIMEOUT == null || READ_TIMEOUT.isEmpty()) {
			System.out.println("Either of CONNECT_TIMEOUT or READ_TIMEOUT is not set. Please set the \"test.connecttimeout\" & \"test.readtimeout\" property in the properties file. " +
					"Or default connect & read timeout values would be used");
			pass = true;
		} else {
			connectTimeout = Integer.parseInt(CONNECT_TIMEOUT);
			readTimeout = Integer.parseInt(READ_TIMEOUT);
			timeouts = true;
			pass = true;
		}		

		if(ORIGINATING_IP == null || ORIGINATING_IP.isEmpty()) {
			System.out.println("ORIGINATING_IP not set. Please set the \"test.originating_ip\" property in the properties file");
			pass = true;
		}
		
		if(SESSION_ID == null || SESSION_ID.isEmpty()) {
			System.out.println("SESSION_ID not set. Please set the \"test.session_id\" property in the properties file");
			pass = true;
		}
		
		if(null == HTTPS_PROTOCOL || HTTPS_PROTOCOL.isEmpty()) {
			System.out.println("HTTPS_PROTOCOL is not set. Please set the \"test.httpsprotocol\" property in the properties file"
					+ ", or default value of TLSv1.2 would be used");
			pass = true;
		} else {
			isHttpsProtocolSet = true;
			pass = true;
		}
		
		if(!pass) {
			fail("Configuration file not setup correctly!");
		}
	}
	
	@Test
	public void phoneIdError() {
		PhoneId pid;
		if(!timeouts)
			pid = new PhoneId("Junk" , "Fake");
		else 
			pid = new PhoneId("Junk" , "Fake", connectTimeout, readTimeout);
		
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
	
	private PhoneId initPhoneIdParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty()) {
			fail("CUSTOMER_ID and SECRET_KEY must be set to pass this test");
		}
		
		PhoneId pid;
		
		if(!timeouts && !isHttpsProtocolSet)
			pid = new PhoneId(CUSTOMER_ID, SECRET_KEY);
		else if(timeouts && !isHttpsProtocolSet)
			pid = new PhoneId(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			pid = new PhoneId(CUSTOMER_ID, SECRET_KEY, HTTPS_PROTOCOL);
		else
			pid = new PhoneId(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout, HTTPS_PROTOCOL);
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
		
		PhoneIdScoreResponse ret = pid.score("13105551234", "BACS");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		assertTrue(ret.status.code == 300);
		assertTrue(ret.risk.level.length() > 0);
		
	}
	
	@Test
	public void phoneIdContact() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdContactResponse ret = pid.contact("13105551234", "BACS");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301); //for this fake number we expect a partially completed request
	}
	
	@Test
	public void phoneIdLiveDummy() {
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdLiveResponse ret = pid.live("13105551234", "BACS");
		assertNotNull(ret);
		
		assertTrue(ret.status.code == 301);  //for this fake number we expect a partially completed request
	}
	
	@Test
	public void phoneIdLiveReal() {		
		if(PHONE_NUMBER.isEmpty()) {
			fail("For this test we require a valid phone number to test against");
		}
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdLiveResponse ret2 = pid.live(PHONE_NUMBER , "BACS");
		
		assertTrue(ret2.status.code == 300); 
		assertNotNull(ret2.live);
		assertTrue(!ret2.live.subscriber_status.isEmpty());
	}
	
	@Test
	public void responseToString() {
		if(PHONE_NUMBER.isEmpty()) {
			fail("For this test we require a valid phone number to test against");
		}
		PhoneId pid = initPhoneIdParams();
		
		PhoneIdStandardResponse ret1 = pid.standard(PHONE_NUMBER, ORIGINATING_IP, SESSION_ID);
		PhoneIdContactResponse ret2 = pid.contact(PHONE_NUMBER , "BACS", ORIGINATING_IP, SESSION_ID);
		PhoneIdScoreResponse ret3 = pid.score(PHONE_NUMBER , "BACS", ORIGINATING_IP, SESSION_ID);
		PhoneIdLiveResponse ret4 = pid.live(PHONE_NUMBER , "BACS", ORIGINATING_IP, SESSION_ID);
		
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
