package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhoneIdClientTest extends TestCase {

    private MockWebServer mockServer;

    private String customerId;
    private String apiKey;

    private SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");


    public void setUp() throws Exception {
        super.setUp();

        this.customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        this.apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        this.mockServer = new MockWebServer();
        this.mockServer.start();
    }

    public void tearDown() throws Exception {
        super.tearDown();

        this.mockServer.shutdown();
    }

    public void testPhoneIdWithParamsAndAddons() throws Exception {

        HashMap<String, Object> params = new HashMap<String, Object>() {{
            put("originating_ip", "127.0.0.1");
            put("account_lifecycle_event", "create");
            put("addons", new HashMap<String, Object>() {{
                put("contact", new HashMap<>());
                put("contact_plus", new HashMap<String, String>() {{
                    put("billing_postal_code", "90210");
                }});
            }});
        }};

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        PhoneIdClient client = new PhoneIdClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.phoneid("18005555555", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/phoneid/18005555555", request.getPath());
        assertEquals("body is not as expected", "{\"addons\":{\"contact_plus\":{\"billing_postal_code\":\"90210\"},\"contact\":{}},\"originating_ip\":\"127.0.0.1\",\"account_lifecycle_event\":\"create\"}",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/json",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testPhoneId() throws Exception {

        HashMap<String, Object> params = new HashMap<String, Object>();

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        PhoneIdClient client = new PhoneIdClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.phoneid("18005555555", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/phoneid/18005555555", request.getPath());
        assertEquals("body is not as expected", "{}",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/json",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

}


