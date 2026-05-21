package com.telesign;

import java.net.Proxy;

/**
 * AppVerify is a secure, lightweight SDK that integrates a frictionless user verification process into existing
 * native mobile applications.
 **/
public class AppVerifyClient extends RestClient {

    /**
     * Constructor for AppVerifyClient.
     * @param customerId
     *            Your customer_id string associated with your account.
     * @param apiKey
     *           Your api_key string associated with your account.
     */
    public AppVerifyClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    /**
     * Constructor for AppVerifyClient.
      * @param customerId
      *         Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      * @param restEndpoint
      *         (optional) Override the default restEndpoint to target another endpoint.
     */
    public AppVerifyClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    /**
     * Constructor for AppVerifyClient.
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
    public AppVerifyClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
	 * The Telesign App Verify API allows you to easily integrate frictionless phone number verification
     * on iOS and Android using flash calls.
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
    public AppVerifyClient(String customerId,
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

}
