package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.AuthMethod;
import com.telesign.util.TeleSignRequest;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class TeleSignRequestTest {
	public static String CUSTOMER_ID;
	public static String SECRET_KEY;
	public static String PHONE_NUMBER;
	public static String CONNECT_TIMEOUT;
	public static String READ_TIMEOUT;
	public static int readTimeout;
	public static int connectTimeout;
	public static boolean timeouts = false;	
	public static String HTTPS_PROTOCOL;
	public static boolean isHttpsProtocolSet = false;
	public static String RESOURCE_DIR = "src/test/resources/";
	
	@BeforeClass
    public static void setUp() throws IOException {
		Properties props = new Properties();
		try {
		props.load(new FileInputStream(RESOURCE_DIR + "test.properties"));
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
	public void requestCreation() {
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", HTTPS_PROTOCOL);
		else 
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout, HTTPS_PROTOCOL);
		assertNotNull(tr);
		
	}
	
	@Test(expected=IOException.class)
	public void malformedUrl() throws IOException {
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "");
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "", connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "", HTTPS_PROTOCOL);
		else
			tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "", connectTimeout, readTimeout, HTTPS_PROTOCOL);
		
		assertNotNull(tr);
		tr.executeRequest();
	}
	
	@Test
	public void addParameterTest() {
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", HTTPS_PROTOCOL);
		else
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout, HTTPS_PROTOCOL);
		
		assertNull(tr.getAllParams().get("code"));
		tr.addParam("code", "001");
		assertTrue(tr.getAllParams().get("code").equals("001"));
	}
	
	@Test
	public void addHeaderTest() {
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", HTTPS_PROTOCOL);
		else
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key", connectTimeout, readTimeout, HTTPS_PROTOCOL);
		
		assertNull(tr.getAllHeaders().get("Authorization"));
		tr.addHeader("Authorization", "fake");
		assertTrue(tr.getAllHeaders().get("Authorization").equals("fake"));
		
		assertNull(tr.getTsHeaders().get("X-TS-Date"));
		assertNull(tr.getAllHeaders().get("X-TS-Date"));
		
		tr.addHeader("X-TS-Date", "2012-03-13");		
		
		assertTrue(tr.getTsHeaders().get("X-TS-Date").equals("2012-03-13"));
		assertTrue(tr.getAllHeaders().get("X-TS-Date").equals("2012-03-13"));
		
		try {
			String json = tr.executeRequest();
			assertNotNull(json);
		} catch (IOException e) {
			fail("IOException through " + e.getMessage());
		}
	}
	
	@Test
	public void shaMethodTest() throws IOException {
		String result = null;
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, HTTPS_PROTOCOL);
		else
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout, HTTPS_PROTOCOL);
		
		tr.setSigningMethod(AuthMethod.SHA256);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
	}
	
	@Test
	public void nonceTest() throws IOException {
		String nonce = "myUniqueNonce" + System.currentTimeMillis();
		String result = null;
		TeleSignRequest tr;
		if(!timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
		else if(timeouts && !isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout);
		else if(!timeouts && isHttpsProtocolSet)
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, HTTPS_PROTOCOL);
		else
			tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY, connectTimeout, readTimeout, HTTPS_PROTOCOL);
		
		tr.setNonce(nonce);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors[0].code == -30012);
	}

}
