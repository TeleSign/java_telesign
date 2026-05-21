package com.telesign;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * Score provides risk information about a specified phone number or email address.
 */
public class ScoreClient extends RestClient {

    private static final String INTELLIGENCE_SCORE_RESOURCE = "/intelligence/phone";
    private static final String EMAIL_INTELLIGENCE_RESOURCE = "/intelligence/email";

    private static final String DETECT_REST_ENDPOINT = "https://detect.telesign.com";

    /**
     * Constructor for ScoreClient.
     * @param customerId
     *         Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      */
    public ScoreClient(String customerId, String apiKey) {
        super(customerId, apiKey, DETECT_REST_ENDPOINT);
    }
    
    /**
     * Constructor for ScoreClient.
     * @param customerId
     *         Your customer_id string associated with your account.
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
    public ScoreClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
     * Constructor for ScoreClient.
     * @param customerId
     *         Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      * @param restEndpoint
      *         (optional) Override the default restEndpoint to target another endpoint.
     */
    public ScoreClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    /**
	 * Telesign Intelligence helps with identifying potentially fraudulent activity by analyzing the transaction risk 
     * associated with a phone number or email address.
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
    public ScoreClient(String customerId,
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
     *Obtain a risk recommendation for this phone number, as well as other relevant information using Telesign Cloud API.
     * <p>
     * See https://developer.telesign.com/enterprise/reference/submitphonenumberforintelligencecloud for detailed API documentation.
     * 
     * @param phoneNumber
     *            The phone number to query.
     * @param accountLifecycleEvent
     *          The attempted action associated with this phone number for which you want to evaluate its risk.
     * @param params
     *           Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse score(String phoneNumber, String accountLifecycleEvent, Map<String, String> params) throws IOException, GeneralSecurityException {
        if (params == null) {
            params = new HashMap<>();
        }
        
        params.put("phone_number", phoneNumber);
        params.put("account_lifecycle_event", accountLifecycleEvent);

        return this.post(INTELLIGENCE_SCORE_RESOURCE, params);
    }

    /**
     * Obtain a risk recommendation for this email address, as well as other relevant information using Telesign Cloud API.
     * <p>
     * See https://developer.telesign.com/enterprise/reference/submitemailaddressforemailintelligence for detailed API documentation.
     * 
     * @param emailAddress
     *            The email address to query.
     * @param accountLifecycleEvent
     *          The attempted action associated with this email address for which you want to evaluate its risk.
     * @param params
     *           Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse emailIntelligence(String emailAddress, String accountLifecycleEvent, Map<String, String> params) throws GeneralSecurityException, IOException {
        if (params == null) {
            params = new HashMap<>();
        }
        
        params.put("email_address", emailAddress);
        params.put("account_lifecycle_event", accountLifecycleEvent);

        return this.post(EMAIL_INTELLIGENCE_RESOURCE, params);
    }
}
