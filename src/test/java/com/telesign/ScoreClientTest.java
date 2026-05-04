package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ScoreClientTest extends TestCase {

    private MockWebServer mockServer;

    private String customerId;
    private String apiKey;

    public void setUp() throws Exception {
        super.setUp();

        customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        apiKey = "ABC12345yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        mockServer = new MockWebServer();
        mockServer.start();
    }

    public void tearDown() throws Exception {
        super.tearDown();

        this.mockServer.shutdown();
    }

    public void testScoreClientConstructorMinimal() throws Exception {
        ScoreClient client = new ScoreClient(customerId, apiKey);
        assertNotNull(client);
    }

    public void testScoreClientConstructorFull() throws Exception {
        ScoreClient client = new ScoreClient(customerId,
                apiKey,
                "rest endpoint",
                0,
                0,
                0,
                null,
                "",
                "",
                null,
                null,
                null);
        assertNotNull(client);
    }

    public void testScoreWithParams() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("originating_ip", "127.0.0.1");
            put("account_lifecycle_event", "create");
        }};

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        ScoreClient client = new ScoreClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""), null, null, null);

        client.score("11234567890", "create", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/intelligence/phone", request.getPath());

        String body = request.getBody().readUtf8();
        assertTrue("body does not contain phone_number", body.contains("phone_number=11234567890"));
        assertTrue("body does not contain originating_ip", body.contains("originating_ip=127.0.0.1"));
        assertTrue("body does not contain account_lifecycle_event", body.contains("account_lifecycle_event=create"));

        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testScore() throws Exception {
        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        ScoreClient client = new ScoreClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""), null, null, null);

        client.score("18005555555", "create", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/intelligence/phone", request.getPath());

        String body = request.getBody().readUtf8();
        assertTrue("body does not contain phone_number", body.contains("phone_number=18005555555"));
        assertTrue("body contains account_lifecycle_event", body.contains("account_lifecycle_event=create"));

        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testEmailIntelligenceWithParams() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("originating_ip", "127.0.0.1");
        }};

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        ScoreClient client = new ScoreClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""), null, null, null);

        client.emailIntelligence("support@vero-finto.com", "sign-in", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/intelligence/email", request.getPath());

        String body = request.getBody().readUtf8();
        assertTrue("body does not contain email_address", body.contains("email_address=support%40vero-finto.com"));
        assertTrue("body does not contain originating_ip", body.contains("originating_ip=127.0.0.1"));
        assertTrue("body does not contain account_lifecycle_event", body.contains("account_lifecycle_event=sign-in"));

        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testEmailIntelligence() throws Exception {
        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        ScoreClient client = new ScoreClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""), null, null, null);

        client.emailIntelligence("support@vero-finto.com", "create", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/intelligence/email", request.getPath());

        String body = request.getBody().readUtf8();
        assertTrue("body does not contain email_address", body.contains("email_address=support%40vero-finto.com"));
        assertTrue("body contains account_lifecycle_event", body.contains("account_lifecycle_event=create"));

        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }
}