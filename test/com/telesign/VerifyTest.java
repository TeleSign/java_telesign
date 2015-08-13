package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
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
		
		if(!pass) {
			fail("Configuration file not setup correctly!");
		}
	}
	
	
	@Test
	public void verifyError() {
		Verify ver;
		if(!timeouts)
			ver = new Verify("Junk" , "Fake");
		else 
			ver = new Verify("Junk" , "Fake", connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}
	
	@Test
	public void verifyRequestCall() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithLanguage() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithAllParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", "12345", "keypress", 1, "1234", true, ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSMS() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.sms(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSWithLanguage() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	@Test
	public void verifyRequestSMSAllParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US", "12345", "Thanks! Custom code template pass! Code: $$CODE$$", ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestCallwithResult() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
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
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.sms(PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
	}

	@Test
	public void verifyRequestSMSwithVerifyCode() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		String verify_code = "12345";
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.sms(PHONE_NUMBER, null, verify_code, null, ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id, verify_code, ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
		assertTrue(ret2.verify.code_state.equals("VALID"));
	}
}
