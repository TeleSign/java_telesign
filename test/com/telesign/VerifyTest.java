package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(VerifyTest.class);
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
    public void verifyError() {
        Verify ver = new Verify("Junk", "Fake");
        VerifyResponse ret = ver.call("13102224444");
        assertNotNull(ret);
        assertTrue(ret.errors[0].code == -30000);
    }

    @Test
    public void verifyRequestCall() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.call(PHONE_NUMBER);
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);
    }

    @Test
    public void verifyRequestCallWithLanguage() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US");
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);
    }

    @Test
    public void verifyRequestCallWithAllParams() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.call(PHONE_NUMBER, "en-US", "12345", "keypress", 1, "1234", true);
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);
    }

    @Test
    public void verifyRequestSMS() {


        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.sms(PHONE_NUMBER);
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);

    }

    @Test
    public void verifyRequestSMSWithLanguage() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US");
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);

    }

    @Test
    public void verifyRequestSMSAllParams() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.sms(PHONE_NUMBER, "en-US", "12345", "Thanks! Custom code template pass! Code: $$CODE$$");
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);

    }

    @Test
    public void verifyRequestCallwithResult() {
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
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
        if (CUSTOMER_ID.isEmpty() || SECRET_KEY.isEmpty() || PHONE_NUMBER.isEmpty()) {
            fail("CUSTOMER_ID, SECRET_KEY and PHONE_NUMBER must be set to pass this test");
        }

        Verify ver = new Verify(CUSTOMER_ID, SECRET_KEY);
        VerifyResponse ret = ver.sms(PHONE_NUMBER);
        assertNotNull(ret);
        assertTrue(ret.errors.length == 0);

        String reference_id = ret.reference_id;

        VerifyResponse ret2 = ver.status(reference_id);
        assertNotNull(ret2);
        assertTrue(ret2.errors.length == 0);
    }
}
