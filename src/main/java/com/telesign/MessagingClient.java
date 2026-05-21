package com.telesign;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * TeleSign's Messaging API allows you to easily send SMS messages. You can send alerts, reminders, and notifications,
 * or you can send verification messages containing one-time passcodes (OTP).
 */
public class MessagingClient extends RestClient {

    private static final String MESSAGING_RESOURCE = "/v1/messaging";
    private static final String MESSAGING_STATUS_RESOURCE = "/v1/messaging/%s";

    /**
     * Constructor for MessagingClient.
     * @param customerId
     *          Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
     */
    public MessagingClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    /**
     * Constructor for MessagingClient.
     * @param customerId
     *          Your customer_id string associated with your account.
      * @param apiKey
      *        Your api_key string associated with your account.
      * @param restEndpoint
      *         (optional) Override the default restEndpoint to target another endpoint.
     */
    public MessagingClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    /**
     * Constructor for MessagingClient.
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
    public MessagingClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
	 * Telesign Messaging is an omnichannel engagement product that allows you to enrich your messages 
     * with the advanced features provided by various channels. 
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
    public MessagingClient(String customerId,
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
     * Send a message to the target phone_number.
     * <p>
     * See https://developer.telesign.com/docs/messaging-api for detailed API documentation.
     * 
     * @param phoneNumber
     *            The phone number to send the message to.
     * @param message
     *            The message to send.
     * @param messageType
     *            The type of the message.
     * @param params
     *            Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse message(String phoneNumber, String message, String messageType, Map<String, String> params) throws IOException, GeneralSecurityException {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("phone_number", phoneNumber);
        params.put("message", message);
        params.put("message_type", messageType);

        return this.post(MESSAGING_RESOURCE, params);
    }

    /**
     * Retrieves the current status of the message.
     * <p>
     * See https://developer.telesign.com/docs/messaging-api for detailed API documentation.
     * 
     * @param referenceId
     *            The reference_id of the message for which to retrieve status.
     * @param params
     *            Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse status(String referenceId, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.get(String.format(MESSAGING_STATUS_RESOURCE, referenceId), params);
    }
}
