package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;

public class RestClientTest extends TestCase {

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

    public void testRestClientPost() throws Exception {

        String test_resource = "/test/resource";
        HashMap<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.post(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest();

        assertEquals("POST", request.getMethod());
        assertEquals("/test/resource", request.getPath());
        assertEquals("test=123_%CF%BF_test", request.getBody().readUtf8());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));
        assertEquals("HMAC-SHA256", request.getHeader("x-ts-auth-method"));

        try {
            UUID uuid = UUID.fromString(request.getHeader("x-ts-nonce"));
        } catch (IllegalArgumentException e) {
            fail("x-ts-nonce header is not a valid UUID");
        }

        try {
            this.rfc2616.parse(request.getHeader("Date"));
        } catch (ParseException e) {
            fail("Date header is not valid rfc2616 format");
        }

        assertNotNull(request.getHeader("Authorization"));
    }

    public void testRestClientGet() throws Exception {

        String test_resource = "/test/resource";
        HashMap<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.get(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/test/resource?test=123_%CF%BF_test", request.getPath());
        assertEquals("", request.getBody().readUtf8());
        assertEquals("", request.getHeader("Content-Type"));
        assertEquals("HMAC-SHA256", request.getHeader("x-ts-auth-method"));

        try {
            UUID uuid = UUID.fromString(request.getHeader("x-ts-nonce"));
        } catch (IllegalArgumentException e) {
            fail("x-ts-nonce header is not a valid UUID");
        }

        try {
            this.rfc2616.parse(request.getHeader("Date"));
        } catch (ParseException e) {
            fail("Date header is not valid rfc2616 format");
        }

        assertNotNull(request.getHeader("Authorization"));
    }

    public void testRestClientPut() throws Exception {

        String test_resource = "/test/resource";
        HashMap<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.put(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest();

        assertEquals("PUT", request.getMethod());
        assertEquals("/test/resource", request.getPath());
        assertEquals("test=123_%CF%BF_test", request.getBody().readUtf8());
        assertEquals("application/x-www-form-urlencoded", request.getHeader("Content-Type"));
        assertEquals("HMAC-SHA256", request.getHeader("x-ts-auth-method"));

        try {
            UUID uuid = UUID.fromString(request.getHeader("x-ts-nonce"));
        } catch (IllegalArgumentException e) {
            fail("x-ts-nonce header is not a valid UUID");
        }

        try {
            this.rfc2616.parse(request.getHeader("Date"));
        } catch (ParseException e) {
            fail("Date header is not valid rfc2616 format");
        }

        assertNotNull(request.getHeader("Authorization"));
    }

    public void testRestClientDelete() throws Exception {

        String test_resource = "/test/resource";
        HashMap<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.delete(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest();

        assertEquals("DELETE", request.getMethod());
        assertEquals("/test/resource?test=123_%CF%BF_test", request.getPath());
        assertEquals("", request.getBody().readUtf8());
        assertEquals("", request.getHeader("Content-Type"));
        assertEquals("HMAC-SHA256", request.getHeader("x-ts-auth-method"));

        try {
            UUID uuid = UUID.fromString(request.getHeader("x-ts-nonce"));
        } catch (IllegalArgumentException e) {
            fail("x-ts-nonce header is not a valid UUID");
        }

        try {
            this.rfc2616.parse(request.getHeader("Date"));
        } catch (ParseException e) {
            fail("Date header is not valid rfc2616 format");
        }

        assertNotNull(request.getHeader("Authorization"));
    }


}
