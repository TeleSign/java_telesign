package com.telesign;

import junit.framework.TestCase;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MessagingClientTest extends TestCase {

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

    public void testMessagingClientConstructorMinimal() throws Exception {
        MessagingClient client = new MessagingClient(customerId, apiKey);
        assertNotNull(client);
    }


    public void testMessagingClientConstructorFull() throws Exception {
        MessagingClient client = new MessagingClient(customerId,
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

    public void testMessagingWithParams() throws Exception {

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("originating_ip", "127.0.0.1");
            put("account_lifecycle_event", "create");
        }};

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        MessagingClient client = new MessagingClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.message("18005555555", "Test Message Content", "MKT", params);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/messaging", request.getPath());
        assertEquals("body is not as expected",
                "originating_ip=127.0.0.1&account_lifecycle_event=create&phone_number=18005555555&" +
                        "message_type=MKT&message=Test%20Message%20Content",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testMessagingNullParams() throws Exception {

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        MessagingClient client = new MessagingClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.message("18005555555", "Test Message Content", "ARN", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "POST", request.getMethod());
        assertEquals("path is not as expected", "/v1/messaging", request.getPath());
        assertEquals("body is not as expected", "phone_number=18005555555&message_type=ARN&message=Test%20Message%20Content",
                request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "application/x-www-form-urlencoded",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }

    public void testMessagingStatus() throws Exception {

        this.mockServer.enqueue(new MockResponse().setBody("{}"));

        MessagingClient client = new MessagingClient(this.customerId,
                this.apiKey,
                this.mockServer.url("").toString().replaceAll("/$", ""));

        client.status("FakeReferenceId", null);

        RecordedRequest request = this.mockServer.takeRequest(1, TimeUnit.SECONDS);

        assertEquals("method is not as expected", "GET", request.getMethod());
        assertEquals("path is not as expected", "/v1/messaging/FakeReferenceId", request.getPath());
        assertEquals("body is not as expected", "", request.getBody().readUtf8());
        assertEquals("Content-Type header is not as expected", "",
                request.getHeader("Content-Type"));
        assertEquals("x-ts-auth-method header is not as expected", "HMAC-SHA256",
                request.getHeader("x-ts-auth-method"));
    }


}


