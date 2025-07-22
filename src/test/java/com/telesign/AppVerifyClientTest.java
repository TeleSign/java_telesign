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
                "",
                null,
                null,
                null);
        assertNotNull(client);
    }
}


