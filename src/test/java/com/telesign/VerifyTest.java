package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telesign.testUtil.TestUtil;
import com.telesign.verify.Verify;
import com.telesign.verify.response.VerifyResponse;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class VerifyTest {
	
	@BeforeClass
    public static void setUp() throws IOException {
		TestUtil.initProperties();
		TestUtil.startServer();
	}
	
	@AfterClass
	public static void stopServer(){
		TestUtil.stopServer();
	}
	
	
	@Test
	public void verifyError() {
		Verify ver;
		if(!TestUtil.timeouts)
			ver = new Verify("Junk" , "Fake");
		else 
			ver = new Verify("Junk" , "Fake", TestUtil.connectTimeout, TestUtil.readTimeout);
		
		VerifyResponse ret = ver.call("13102224444");
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}

	private Verify initVerifyParams() {
		if(TestUtil.CUSTOMER_ID.isEmpty() || TestUtil.SECRET_KEY.isEmpty() || TestUtil.PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		
		if(!TestUtil.timeouts && !TestUtil.isHttpsProtocolSet)
			ver = new Verify(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY);
		else if(TestUtil.timeouts && !TestUtil.isHttpsProtocolSet)
			ver = new Verify(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY, TestUtil.connectTimeout, TestUtil.readTimeout);
		else if(!TestUtil.timeouts && TestUtil.isHttpsProtocolSet)
			ver = new Verify(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY, TestUtil.HTTPS_PROTOCOL);
		else
			ver = new Verify(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY, TestUtil.connectTimeout, TestUtil.readTimeout, TestUtil.HTTPS_PROTOCOL);
		
		return ver;
	}
	
	@Test
	public void verifyRequestCall() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER); System.out.println(ret.toString());
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithLanguage() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithCallForwardAction() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER, "en-US", TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID, TestUtil.CALL_FORWARD_ACTION);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyReqCallNoOptionalParams() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER, "en-US", "54321", "keypress", 1, "1234", true); 
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestCallWithAllParams() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER, "en-US", "12345", "keypress", 1, "1234", true, TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID, TestUtil.CALL_FORWARD_ACTION);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSMS() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER); 
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSWithLanguage() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER, "en-US");
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	@Test
	public void verifyRequestSMSAllParams() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER, "en-US", "12345", "Thanks! Custom code template pass! Code: $$CODE$$", TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestCallwithResult() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
		
	}
	
	@Test
	public void verifyRequestSMSwithResult() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
	}

	@Test
	public void verifyRequestSMSwithVerifyCode() {
		Verify ver = initVerifyParams();
		
		String verify_code = "12345";
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER, null, verify_code, null, TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		
		String reference_id = ret.reference_id;
		
		VerifyResponse ret2 = ver.status(reference_id, verify_code, TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret2);
		assertTrue(ret2.errors.length == 0);
		assertTrue(ret2.verify.code_state.equals("VALID"));
	}
	
	@Test
	public void verifyRequestPush(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.push(TestUtil.PHONE_NUMBER,TestUtil.BUNDLE_ID);		
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestPushWithAllParams(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.push(TestUtil.PHONE_NUMBER, TestUtil.PUSH_NOTIFICATION_TYPE, TestUtil.PUSH_NOTIFICATION_VALUE, TestUtil.BUNDLE_ID, "Verify request push", TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSoftToken(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.softToken(TestUtil.PHONE_NUMBER, TestUtil.SOFT_TOKEN_ID, "571591", TestUtil.BUNDLE_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestSoftTokenWithAllParams(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.softToken(TestUtil.PHONE_NUMBER, TestUtil.SOFT_TOKEN_ID, "928417", TestUtil.BUNDLE_ID, TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);		
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void vertifyRequestRegistration(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.registration(TestUtil.PHONE_NUMBER, TestUtil.BUNDLE_ID);System.out.println(ret.toString());
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void verifyRequestRegistrationWithAllParams(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.registration(TestUtil.PHONE_NUMBER, TestUtil.BUNDLE_ID, TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void smartVerify(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.smartVerify(TestUtil.PHONE_NUMBER,"BACS", TestUtil.CALLER_ID, "en-US", null, TestUtil.SMART_VERIFY_PREFERENCE, TestUtil.SMART_VERIFY_IGNORE_RISK);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void smartVerifyWithAllParams(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.smartVerify(TestUtil.PHONE_NUMBER,"BACS", TestUtil.CALLER_ID, "en-US", null, TestUtil.SMART_VERIFY_PREFERENCE, TestUtil.SMART_VERIFY_IGNORE_RISK , TestUtil.ORIGINATING_IP, TestUtil.SESSION_ID);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
}
