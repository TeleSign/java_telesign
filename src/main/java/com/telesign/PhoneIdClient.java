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
    
    /**
     * ; charset=utf-8 
     */
    public static final MediaType JSON = MediaType.parse("application/json");
    
    /**
     * Constructor for PhoneIdClient.
     * @param customerId
     *          Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      */
    public PhoneIdClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    /**
     * Constructor for PhoneIdClient.
     * @param customerId
     *         Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      * @param restEndpoint
      *         (optional) Override the default restEndpoint to target another endpoint.
     */
    public PhoneIdClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    /**
     * Constructor for PhoneIdClient.
     * @param customerId
     *          Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      * @param restEndpoint
      *         (optional) Override the default restEndpoint to target another endpoint.
      * @param source
      *         (optional) source string to be added to the User-Agent header of the request, should be the name of the originating SDK.
      * @param sdkVersionOrigin
      *         (optional) sdkVersionOrigin string to be added to the User-Agent header of the request, should be the version of the originating SDK.
      * @param sdkVersionDependency
      *         (optional) sdkVersionDependency string to be added to the User-Agent header of the request, should be the version of the dependency SDK.
     */
    public PhoneIdClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
	 * Telesign Phone ID allows you to get detailed and actionable global phone number and subscriber data
     *  intelligence to strengthen authentications, evaluate fraud risks, and enhance the user experience.
	 *
	 * @param customerId
	 *            Your customer_id string associated with your account.
	 * @param apiKey
	 *            Your api_key string associated with your account.
	 * @param restEndpoint
	 *            (optional) Override the default restEndpoint to target another
	 *            endpoint.
	 * @param connectTimeout
	 *            (optional) connectTimeout passed into OkHttp.
	 * @param readTimeout
	 *            (optional) readTimeout passed into OkHttp.
	 * @param writeTimeout
	 *            (optional) writeTimeout passed into OkHttp.
	 * @param proxy
	 *            (optional) proxy passed into OkHttp.
	 * @param proxyUsername
	 *            (optional) proxyUserName used to create an Authenticator passed
	 *            into OkHttp.
	 * @param proxyPassword
	 *            (optional) proxyPassword used to create an Authenticator passed
	 *            into OkHttp.
	 * @param source
	 *            (optional) source string to be added to the User-Agent header of
	 *            the request, should be the name of the originating SDK.
	 * @param sdkVersionOrigin
	 *            (optional) sdkVersionOrigin string to be added to the User-Agent header of
	 *            the request, should be the version of the originating SDK.
	 * @param sdkVersionDependency
	 *            (optional) sdkVersionDependency string to be added to the User-Agent header of
	 *            the request, should be the version of the dependency SDK.
	 */
    public PhoneIdClient(String customerId,
                         String apiKey,
                         String restEndpoint,
                         Integer connectTimeout,
                         Integer readTimeout,
                         Integer writeTimeout,
                         Proxy proxy,
                         final String proxyUsername,
                         final String proxyPassword,
                         final String source,
                         final String sdkVersionOrigin,
                         final String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, connectTimeout, readTimeout, writeTimeout, proxy, proxyUsername, proxyPassword, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
     * The PhoneID API provides a cleansed phone number, phone type, and telecom carrier information to determine the
     * best communication method - SMS or voice.
     * <p>
     * See https://developer.telesign.com/docs/phoneid-api for detailed API documentation.
     * 
     * @param phoneNumber
     *            The phone number to query.
     * @param params
     *            Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse phoneid(String phoneNumber, Map<String, ? extends Object> params) throws IOException, GeneralSecurityException {

        return this.post(String.format(PHONEID_RESOURCE, phoneNumber), params, JSON_CONTENT_TYPE);
    }

    /**
	 * Generic TeleSign REST API request handler.
	 *
	 * @param params
	 *            Params to perform the request with.
	 * @param contentType
	 * 		  Application/json, www-url ....
	 * @return The RequestBody for the request.
	 * @throws UnsupportedEncodingException if the encoding is not supported.
     * @throws IOException if something goes wrong during request body creation.
	 */
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
