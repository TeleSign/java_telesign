package com.telesign;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import com.google.gson.Gson;

/**
 * A set of APIs that deliver deep phone number data attributes that help optimize the end user
 * verification process and evaluate risk.
 */
public class PhoneIdClient extends RestClient {

    private static final String PHONEID_RESOURCE = "/v1/phoneid/%s";
    
    public static final MediaType JSON = MediaType.parse("application/json"); // ; charset=utf-8 

    public PhoneIdClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    public PhoneIdClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    public PhoneIdClient(String customerId,
                         String apiKey,
                         String restEndpoint,
                         Integer connectTimeout,
                         Integer readTimeout,
                         Integer writeTimeout,
                         Proxy proxy,
                         final String proxyUsername,
                         final String proxyPassword) {
        super(customerId, apiKey, restEndpoint, connectTimeout, readTimeout, writeTimeout, proxy, proxyUsername, proxyPassword);
    }

    /**
     * The PhoneID API provides a cleansed phone number, phone type, and telecom carrier information to determine the
     * best communication method - SMS or voice.
     * <p>
     * See https://developer.te
     * lesign.com/docs/phoneid-api for detailed API documentation.
     */
    public TelesignResponse phoneid(String phoneNumber, Map<String, ? extends Object> params) throws IOException, GeneralSecurityException {

        return this.post(String.format(PHONEID_RESOURCE, phoneNumber), params, JSON_CONTENT_TYPE);
    }

    @Override
    public RequestBody createRequestBody(Map<String, ? extends Object> params, String contentType) throws UnsupportedEncodingException, IOException {
        if (contentType.equals(JSON_CONTENT_TYPE)) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            HashMap<String, Object> paramsCopy = new HashMap<>(params);

            if (paramsCopy.containsKey("addons")) {
                jsonObject.add("addons", gson.toJsonTree(
                        paramsCopy.remove("addons"),
                        new TypeToken<HashMap<String, HashMap<String, String>>>() {
                        }.getType()));
            }

            JsonObject paramsObject = gson.toJsonTree(params, new TypeToken<HashMap<String, String>>() {
            }.getType()).getAsJsonObject();
            for (String key : paramsCopy.keySet()) {
                jsonObject.add(key, paramsObject.get(key));
            }

            String jsonString = jsonObject.toString();
            return params != null ? RequestBody.create(com.telesign.PhoneIdClient.JSON, jsonString.getBytes()) : RequestBody.create(com.telesign.PhoneIdClient.JSON, "");
        } else {
            return super.createRequestBody(params, contentType);
        }
   }
}
