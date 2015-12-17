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
	public static String CALLER_ID; 
	public static String CONNECT_TIMEOUT;
	public static String READ_TIMEOUT;
	public static int readTimeout;
	public static int connectTimeout;
	public static boolean timeouts = false;
	public static String ORIGINATING_IP;
	public static String SESSION_ID;
	public static String SMART_VERIFY_PREFERENCE;
	public static String SMART_VERIFY_IGNORE_RISK;
	public static String PUSH_NOTIFICATION_TYPE;
	public static String PUSH_NOTIFICATION_VALUE;
	public static String SOFT_TOKEN_ID;
	public static String CALL_FORWARD_ACTION;
	public static String BUNDLE_ID;
	
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
		SMART_VERIFY_PREFERENCE=props.getProperty("test.smart_verify_preference");
		SMART_VERIFY_IGNORE_RISK=props.getProperty("test.smart_verify_ignore_risk");
		PUSH_NOTIFICATION_TYPE=props.getProperty("test.push_notification_type");
		PUSH_NOTIFICATION_VALUE=props.getProperty("test.push_notification_value");
		SOFT_TOKEN_ID = props.getProperty("test.soft_token_id");
		CALL_FORWARD_ACTION = props.getProperty("test.call_forward_action");
		CALLER_ID = props.getProperty("test.caller_id");
		BUNDLE_ID = props.getProperty("test.bundle_id");
		
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
		
		if(null == SMART_VERIFY_PREFERENCE || SMART_VERIFY_PREFERENCE.isEmpty()) {
			System.out.println("SMART_VERIFY_PREFERENCE not set. Please set the \"test.smart_verify_preference\" property in the properties file");
			pass = true;
		}
		
		if(null == SMART_VERIFY_IGNORE_RISK || SESSION_ID.isEmpty()) {
			System.out.println("SMART_VERIFY_IGNORE_RISK not set. Please set the \"test.smart_verify_ignore_risk\" property in the properties file");
			pass = true;
		}		
		
		if(null == CALL_FORWARD_ACTION || CALL_FORWARD_ACTION.isEmpty()) {
			System.out.println("CALL_FORWARD_ACTION not set. Please set the \"test.call_forward_action\" property in the properties file");
			pass = true;
		}
		
		if(null == CALLER_ID || CALLER_ID.isEmpty()) {
			System.out.println("CALLER_ID not set. Please set the \"test.caller_id\" property in the properties file");
			pass = true;
		}
		
		if(null == PUSH_NOTIFICATION_TYPE || PUSH_NOTIFICATION_TYPE.isEmpty()) {
			System.out.println("PUSH_NOTIFICATION_TYPE not set. Please set the \"test.push_notification_type\" property in the properties file");
			pass = true;
		}
		
		if(null == PUSH_NOTIFICATION_VALUE || PUSH_NOTIFICATION_VALUE.isEmpty()) {
			System.out.println("PUSH_NOTIFICATION_VALUE not set. Please set the \"test.push_notification_value\" property in the properties file");
			pass = true;
		}
		
		if(null == SOFT_TOKEN_ID || SOFT_TOKEN_ID.isEmpty()) {
			System.out.println("SOFT_TOKEN_ID not set. Please set the \"test.soft_token_id\" property in the properties file");
			pass = true;
		}
		
		if(null == CALL_FORWARD_ACTION || CALL_FORWARD_ACTION.isEmpty()) {
			System.out.println("CALL_FORWARD_ACTION not set. Please set the \"test.call_forward_action\" property in the properties file");
			pass = true;
		}

		if(null == BUNDLE_ID || BUNDLE_ID.isEmpty()) {
			System.out.println("BUNDLE_ID not set. Please set the \"test.bundle_id\" property in the properties file");
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
	public void verifyRequestCallWithCallForwardAction() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", ORIGINATING_IP, SESSION_ID, CALL_FORWARD_ACTION);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyReqCallNoOptionalParams() {
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", "54321", "keypress", 1, "1234", true); 
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
		
		VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", "12345", "keypress", 1, "1234", true, ORIGINATING_IP, SESSION_ID, CALL_FORWARD_ACTION);
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
	
	@Test
	public void verifyRequestPush(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.push(PHONE_NUMBER,BUNDLE_ID);		
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestPushWithAllParams(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.push(PHONE_NUMBER, PUSH_NOTIFICATION_TYPE, PUSH_NOTIFICATION_VALUE, BUNDLE_ID, "Verify request push", ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSoftToken(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.softToken(PHONE_NUMBER, SOFT_TOKEN_ID, "571591", BUNDLE_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSoftTokenWithAllParams(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		VerifyResponse ret = ver.softToken(PHONE_NUMBER, SOFT_TOKEN_ID, "928417", BUNDLE_ID, ORIGINATING_IP, SESSION_ID);
		System.out.println("verifyRequestSoftTokenWithAllParams response: " + ret.toString()); // To Be removed
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void vertifyRequestRegistration(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.registration(PHONE_NUMBER, BUNDLE_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestRegistrationWithAllParams(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.registration(PHONE_NUMBER, BUNDLE_ID, ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void smartVerify(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.smartVerify(PHONE_NUMBER,"BACS", CALLER_ID, "en-US", null, SMART_VERIFY_PREFERENCE, SMART_VERIFY_IGNORE_RISK);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void smartVerifyWithAllParams(){
		if(CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		if(!timeouts)
			ver = new Verify(CUSTOMER_ID, SECRET_KEY);
		else
			ver = new Verify(CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		
		VerifyResponse ret = ver.smartVerify(PHONE_NUMBER,"BACS", CALLER_ID, "en-US", null, SMART_VERIFY_PREFERENCE, SMART_VERIFY_IGNORE_RISK , ORIGINATING_IP, SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
}
