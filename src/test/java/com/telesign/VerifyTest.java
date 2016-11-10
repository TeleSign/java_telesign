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
import com.telesign.verify.Verify.VerifyBuilder;
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
		if(TestUtil.runTests)
			TestUtil.startServer();
		else {
			TestUtil.testUrl = "https://rest.telesign.com";
			TestUtil.mobileTestUrl = "https://rest-mobile.telesign.com";
			}
	}
	
	@AfterClass
	public static void stopServer(){
		if(TestUtil.runTests)
			TestUtil.stopServer();
	}
	
	
	@Test
	public void verifyError() {
		Verify ver;
		
		VerifyBuilder verifyBuilder = Verify.init("Junk" , "Fake");
		verifyBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout);
		ver = verifyBuilder.url(TestUtil.testUrl).create();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors[0].code == -30000);
	}

	private Verify initVerifyParams() {
		if(TestUtil.CUSTOMER_ID.isEmpty() || TestUtil.SECRET_KEY.isEmpty() || TestUtil.PHONE_NUMBER.isEmpty()) {
			fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
		}
		
		Verify ver;
		
		VerifyBuilder verifyBuilder = Verify.init(TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY);
		verifyBuilder.language(TestUtil.LANGUAGE).ucid(TestUtil.UCID).verifyCode(TestUtil.VERIFY_CODE).template(TestUtil.TEMPLATE);
		verifyBuilder.connectTimeout(TestUtil.connectTimeout).readTimeout(TestUtil.readTimeout).httpsProtocol(TestUtil.HTTPS_PROTOCOL).ciphers(TestUtil.CIPHERS);
		verifyBuilder.callForwardAction(TestUtil.CALL_FORWARD_ACTION).redial(true).verifyMethod(TestUtil.VERIFY_METHOD);
		verifyBuilder.extensionTemplate(TestUtil.EXTENSION_TEMPLATE).extensionType(Integer.parseInt(TestUtil.EXTENSION_TYPE));
		verifyBuilder.ttsMessage(TestUtil.TTS_MESSAGE).pushMessage(TestUtil.PUSH_MESSAGE).smsMessage(TestUtil.SMS_MESSAGE);
		verifyBuilder.callerId(TestUtil.CALLER_ID).ignoreRisk(TestUtil.SMART_VERIFY_IGNORE_RISK).preference(TestUtil.SMART_VERIFY_PREFERENCE);
		verifyBuilder.extra(TestUtil.EXTRA_MAP);
		ver = verifyBuilder.url(TestUtil.testUrl).mobileUrl(TestUtil.mobileTestUrl).create();
		
		return ver;
	}
	
	@Test
	public void verifyRequestCall() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER);
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
	public void verifyRequestCallwithResult() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.call(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		if(!TestUtil.runTests){			
			String reference_id = ret.reference_id;
			
			VerifyResponse ret2 = ver.status(reference_id);
			assertNotNull(ret2);
			assertTrue(ret2.errors.length == 0);
		}
		
	}
	
	@Test
	public void verifyRequestSMSwithResult() {
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.sms(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
		if(!TestUtil.runTests){	
			String reference_id = ret.reference_id;
			
			VerifyResponse ret2 = ver.status(reference_id);
			assertNotNull(ret2);
			assertTrue(ret2.errors.length == 0);
		}
	}	
	
	@Test
	public void verifyRequestPush(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.push(TestUtil.PHONE_NUMBER);		
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}	
	
	@Test
	public void verifyRequestSoftToken(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.softToken(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}
	
	@Test
	public void vertifyRequestRegistration(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.registration(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}	
	
	@Test
	public void smartVerify(){
		Verify ver = initVerifyParams();
		
		VerifyResponse ret = ver.smartVerify(TestUtil.PHONE_NUMBER);
		assertNotNull(ret);
		assertTrue(ret.errors.length == 0);
	}	
}
