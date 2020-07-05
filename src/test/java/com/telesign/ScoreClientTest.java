package com.telesign;

import junit.framework.TestCase;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ScoreClientTest extends TestCase {

    private MockWebServer mockServer;

    private String customerId;
    private String apiKey;

    private SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");


    public void setUp() throws Exception {
        super.setUp();

        customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

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
                "");
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
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.score("18005555555", "create", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/score/18005555555", request.getPath());
        assertEquals("body is not as expected", "originating_ip=127.0.0.1&account_lifecycle_event=create",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testScore() throws Exception {

        HashMap<String, Object> params = new HashMap<String, Object>();

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        ScoreClient client = new ScoreClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.score("18005555555", "create", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/score/18005555555", request.getPath());
        assertEquals("body is not as expected", "account_lifecycle_event=create",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

}


