package com.telesign;

import junit.framework.TestCase;
import okhttp3.RequestBody;
import okio.Buffer;

import java.io.IOException;
import java.util.HashMap;

public class JsonCreateRequestBodyTest extends TestCase {

    private void makeRequestBodyAndAssert(HashMap<String, ? extends Object> params, String expectedJson) {
        PhoneIdClient phoneIdClient = new PhoneIdClient("", "");
        Buffer actualJsonBuffer = new Buffer();
        try {
            RequestBody rb = phoneIdClient.createRequestBody(params, RestClient.JSON_CONTENT_TYPE);
            rb.writeTo(actualJsonBuffer);
        } catch (IOException exc) {
            fail(exc.getMessage());
        }
        assertEquals(expectedJson, actualJsonBuffer.readUtf8());
    }

    public void testEmptyParams() {
        makeRequestBodyAndAssert(new HashMap<String, Object>(), "{}");
    }

    public void testNoAddonsParams() {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("originating_ip", "127.0.0.1");
            put("bogus", "param");
        }};
        makeRequestBodyAndAssert(params, "{\"bogus\":\"param\",\"originating_ip\":\"127.0.0.1\"}");
    }

    public void testAddonParamOnly() {
        HashMap<String, Object> params = new HashMap<String, Object>() {{
            put("addons", new HashMap<String, Object>() {{
                put("contact", new HashMap<>());
                put("contact_plus", new HashMap<String, String>(){{
                    put("billing_postal_code", "90210");
                }});
                put("contact_match", new HashMap<String, String>() {{
                    put("first_name", "Bob");
                    put("last_name", "Smith");
                    put("address", "12345 Some St");
                    put("city", "Los Angeles");
                    put("postal_code", "90210");
                    put("state", "CA");
                    put("country", "USA");
                }});
                put("current_location", new HashMap<>());
                put("current_location_plus", new HashMap<>());
                put("device_info", new HashMap<>());
                put("number_deactivation", new HashMap<>());
                put("subscriber_status", new HashMap<>());
            }});
        }};
        makeRequestBodyAndAssert(params, "{\"addons\":{\"contact_plus\":{\"billing_postal_code\":\"90210\"},\"device_info\":{},\"current_location_plus\":{},\"contact\":{},\"number_deactivation\":{},\"subscriber_status\":{},\"contact_match\":{\"country\":\"USA\",\"address\":\"12345 Some St\",\"city\":\"Los Angeles\",\"last_name\":\"Smith\",\"state\":\"CA\",\"postal_code\":\"90210\",\"first_name\":\"Bob\"},\"current_location\":{}}}");
    }
    public void testAddonAndRegularParams() {
        HashMap<String, Object> params = new HashMap<String, Object>() {{
            put("originating_ip", "127.0.0.1");
            put("account_lifecycle_event", "create");
            put("addons", new HashMap<String, Object>() {{
                put("contact", new HashMap<>());
                put("contact_plus", new HashMap<String, String>(){{
                    put("billing_postal_code", "90210");
                }});
                put("contact_match", new HashMap<String, String>() {{
                    put("first_name", "Bob");
                    put("last_name", "Smith");
                    put("address", "12345 Some St");
                    put("city", "Los Angeles");
                    put("postal_code", "90210");
                    put("state", "CA");
                    put("country", "USA");
                }});
                put("current_location", new HashMap<>());
                put("current_location_plus", new HashMap<>());
                put("device_info", new HashMap<>());
                put("number_deactivation", new HashMap<>());
                put("subscriber_status", new HashMap<>());
            }});
        }};
        makeRequestBodyAndAssert(params, "{\"addons\":{\"contact_plus\":{\"billing_postal_code\":\"90210\"},\"device_info\":{},\"current_location_plus\":{},\"contact\":{},\"number_deactivation\":{},\"subscriber_status\":{},\"contact_match\":{\"country\":\"USA\",\"address\":\"12345 Some St\",\"city\":\"Los Angeles\",\"last_name\":\"Smith\",\"state\":\"CA\",\"postal_code\":\"90210\",\"first_name\":\"Bob\"},\"current_location\":{}},\"originating_ip\":\"127.0.0.1\",\"account_lifecycle_event\":\"create\"}");
    }
}
