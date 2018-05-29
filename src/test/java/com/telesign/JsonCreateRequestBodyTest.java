package com.telesign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import okhttp3.RequestBody;
import okio.Buffer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonCreateRequestBodyTest extends TestCase {

    private void makeRequestBodyAndAssert(HashMap<String, ? extends Object> params) {
        PhoneIdClient phoneIdClient = new PhoneIdClient("", "");
        Buffer actualJsonBuffer = new Buffer();
        Gson gson = new Gson();

        try {
            RequestBody rb = phoneIdClient.createRequestBody(params, RestClient.JSON_CONTENT_TYPE);
            rb.writeTo(actualJsonBuffer);
        } catch (IOException exc) {
            fail(exc.getMessage());
        }
        HashMap<String, Object> unserializedJson = gson.fromJson(actualJsonBuffer.readUtf8(),
                new TypeToken<HashMap<String, Object>>() {}.getType());
        assertEquals(params, unserializedJson);
    }

    public void testEmptyParams() {
        makeRequestBodyAndAssert(new HashMap<String, Object>());
    }

    public void testNoAddonsParams() {
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("originating_ip", "127.0.0.1");
            put("bogus", "param");
        }};
        makeRequestBodyAndAssert(params);
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
        makeRequestBodyAndAssert(params);
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
        makeRequestBodyAndAssert(params);
    }
}
