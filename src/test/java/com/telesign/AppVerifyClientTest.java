package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AppVerifyClientTest extends TestCase {

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

    public void testAppVerifyClientConstructorMinimal() throws Exception {
        AppVerifyClient client = new AppVerifyClient(customerId, apiKey);
        assertNotNull(client);
    }


    public void testAppVerifyClientConstructorFull() throws Exception {
        AppVerifyClient client = new AppVerifyClient(customerId,
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

    public void testAppVerifyStatus() throws Exception {

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        AppVerifyClient client = new AppVerifyClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.status("FakeExternalId", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "GET", request.getMethod());
        assertEquals("path is not as expected", "/v1/mobile/verification/status/FakeExternalId", request.getPath());
        assertEquals("body is not as expected", "", request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "", request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }


}


