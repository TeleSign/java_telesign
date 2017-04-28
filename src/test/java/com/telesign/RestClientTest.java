package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    public void testRestClientConstructors() {

        RestClient client = new RestClient(this.customerId, this.apiKey);
    }

    public void testGenerateTelesignHeadersWithPost() throws GeneralSecurityException {

        String methodName = "POST";
        String dateRfc2616 = "Wed, 14 Dec 2016 18:20:12 GMT";
        String nonce = "A1592C6F-E384-4CDB-BC42-C3AB970369E9";
        String resource = "/v1/resource";
        String bodyParamsUrlEncoded = "test=param";

        String expectedAuthorizationHeader = "TSA FFFFFFFF-EEEE-DDDD-1234-AB1234567890:" +
                "2xVlmbrxLjYrrPun3G3WMNG6Jon4yKcTeOoK9DjXJ/Q=";

        Map<String, String> actualHeaders = RestClient.generateTelesignHeaders(this.customerId,
                this.apiKey,
                methodName,
                resource,
                bodyParamsUrlEncoded,
                dateRfc2616,
                nonce,
                "unitTest");

        assertEquals(expectedAuthorizationHeader, actualHeaders.get("Authorization"));
    }

    public void testGenerateTelesignHeadersUnicodeContent() throws GeneralSecurityException {

        String methodName = "POST";
        String dateRfc2616 = "Wed, 14 Dec 2016 18:20:12 GMT";
        String nonce = "A1592C6F-E384-4CDB-BC42-C3AB970369E9";
        String resource = "/v1/resource";
        String bodyParamsUrlEncoded = "test=%CF%BF";

        String expectedAuthorizationHeader = "TSA FFFFFFFF-EEEE-DDDD-1234-AB1234567890:" +
                "h8d4I0RTxErbxYXuzCOtNqb/f0w3Ck8e5SEkGNj01+8=";

        Map<String, String> actualHeaders = RestClient.generateTelesignHeaders(this.customerId,
                this.apiKey,
                methodName,
                resource,
                bodyParamsUrlEncoded,
                dateRfc2616,
                nonce,
                "unitTest");

        assertEquals(expectedAuthorizationHeader, actualHeaders.get("Authorization"));
    }

    public void testGenerateTelesignHeadersWithGet() throws GeneralSecurityException {

        String methodName = "GET";
        String dateRfc2616 = "Wed, 14 Dec 2016 18:20:12 GMT";
        String nonce = "A1592C6F-E384-4CDB-BC42-C3AB970369E9";
        String resource = "/v1/resource";

        String expectedAuthorizationHeader = "TSA FFFFFFFF-EEEE-DDDD-1234-AB1234567890:" +
                "aUm7I+9GKl3ww7PNeeJntCT0iS7b+EmRKEE4LnRzChQ=";

        Map<String, String> actualHeaders = RestClient.generateTelesignHeaders(this.customerId,
                this.apiKey,
                methodName,
                resource,
                "",
                dateRfc2616,
                nonce,
                "unitTest");

        assertEquals(expectedAuthorizationHeader, actualHeaders.get("Authorization"));
    }

    public void testGenerateTelesignHeadersDefaultValues() throws GeneralSecurityException {

        String methodName = "GET";
        String resource = "/v1/resource";

        String expectedAuthorizationHeader = "TSA FFFFFFFF-EEEE-DDDD-1234-AB1234567890:" +
                "aUm7I+9GKl3ww7PNeeJntCT0iS7b+EmRKEE4LnRzChQ=";

        Map<String, String> actualHeaders = RestClient.generateTelesignHeaders(this.customerId,
                this.apiKey,
                methodName,
                resource,
                "",
                null,
                null,
                null);

        try {
            UUID uuid = UUID.fromString(actualHeaders.get("x-ts-nonce"));
        } catch (IllegalArgumentException e) {
            fail("x-ts-nonce header is not a valid UUID");
        }

        try {
            this.rfc2616.parse(actualHeaders.get("Date"));
        } catch (ParseException e) {
            fail("Date header is not valid rfc2616 format");
        }
    }

    public void testRestClientPost() throws Exception {

        String test_resource = "/test/resource";
        Map<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.post(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

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
        Map<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.get(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

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
        Map<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.put(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

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
        Map<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.delete(test_resource, test_params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

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

    public void testRestClientProxyAuthentication() throws Exception {

        String test_resource = "/test/resource";
        Map<String, String> test_params = new HashMap<>();
        test_params.put("test", "123_\u03ff_test");

        this.mockServer.enqueue(new MockResponse().setBody("{}").setResponseCode(407));
        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        RestClient client = new RestClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""),
                1,
                1,
                1,
                this.mockServer.toProxyAddress(),
                "proxyUsername",
                "proxyPassword");

        client.get(test_resource, test_params);

        assertEquals(2, this.mockServer.getRequestCount());

        RecordedRequest initialRequest = this.mockServer.takeRequest(1, TimeUnit.SECONDS);
        RecordedRequest proxyAuthorizationRequest = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertNull(initialRequest.getHeader("Proxy-Authorization"));
        assertNotNull(proxyAuthorizationRequest.getHeader("Proxy-Authorization"));
    }
}
