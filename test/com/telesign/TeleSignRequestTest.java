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
        SECRET_KEY = props.getProperty("test.secretkey");
        PHONE_NUMBER = props.getProperty("test.phonenumber");

        boolean pass = true;

        if (CUSTOMER_ID == null || CUSTOMER_ID.isEmpty()) {
            System.out.println("CUSTOMER_ID is not set. Please set the \"test.customerid\" property in the properties file");
            pass = false;
        }

        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            System.out.println("SECRET_KEY is not set. Please set the \"test.secretkey\" property in the properties file");
            pass = false;
        }
        if (PHONE_NUMBER == null || PHONE_NUMBER.isEmpty()) {
            System.out.println("PHONE_NUMBER is not set. Please set the \"test.phonenumber\" property in the properties file");
            pass = false;
        }

        if (!pass) {
            fail("Configuration file not setup correctly!");
        }
    }

    @Test
    public void requestCreation() {
        TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
        assertNotNull(tr);

    }

    @Test(expected = IOException.class)
    public void malformedUrl() throws IOException {
        TeleSignRequest tr = new TeleSignRequest(":junk/rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "");
        assertNotNull(tr);
        tr.executeRequest();
    }

    @Test
    public void addParameterTest() {
        TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");
        assertNull(tr.getAllParams().get("code"));
        tr.addParam("code", "001");
        assertTrue(tr.getAllParams().get("code").equals("001"));
    }

    @Test
    public void addHeaderTest() {
        TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/15551234567", "GET", "customer_id", "secret_key");

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

/*
    @Test
	public void shaMethodTest() throws IOException {
		String result = null;
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
		tr.setSigningMethod(AuthMethod.SHA256);
		
		assertNotNull(tr);
		
		result = tr.executeRequest();
		
		assertNotNull(result);
	
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		assertTrue(response.errors.length == 0);
	}
*/


    @Test
    public void shaMethodTestPhoneIdNotEnabled() throws IOException {
        String result = null;
        TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
        tr.setSigningMethod(AuthMethod.SHA256);

        assertNotNull(tr);

        result = tr.executeRequest();

        assertNotNull(result);

        Gson gson = new Gson();
        PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);

        assertNotNull(response);
        assertTrue(response.errors.length == 1);
        assertTrue(response.status.code == 501);
        assertTrue(response.errors[0].code == -20002);
    }
/*	@Test
    public void nonceTest() throws IOException {
		String nonce = "myUniqueNonce" + System.currentTimeMillis();
		String result = null;
		TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
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
	}*/

    @Test
    public void nonceTestWithPhoneIdNotEnabled() throws IOException {
        String nonce = "myUniqueNonce" + System.currentTimeMillis();
        String result = null;
        TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + PHONE_NUMBER, "GET", CUSTOMER_ID, SECRET_KEY);
        tr.setNonce(nonce);

        assertNotNull(tr);

        result = tr.executeRequest();

        assertNotNull(result);

        Gson gson = new Gson();
        PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);


        assertNotNull(response);
        assertTrue(response.errors.length == 1);
        assertTrue(response.status.code == 501);
        assertTrue(response.errors[0].code == -20002);

        result = tr.executeRequest();

        assertNotNull(result);

        response = gson.fromJson(result, PhoneIdStandardResponse.class);


        assertNotNull(response);
        assertTrue(response.errors[0].code == -30012);
    }

}
