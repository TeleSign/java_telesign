package com.telesign;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import com.google.gson.Gson;;

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

        return this.post(String.format(PHONEID_RESOURCE, phoneNumber), params);
   }
   
   @Override 
   public RequestBody createRequestBody(Map<String, ? extends Object> params) throws UnsupportedEncodingException {
	   String jsonString = (new Gson()).toJson(params);
	   return params != null ? RequestBody.create(PhoneIdClient.JSON, jsonString.getBytes()) : RequestBody.create(PhoneIdClient.JSON, "");      
   }
   
   @Override
   protected String getContentType() {
	   return "application/json";
   }
}
