package com.telesign;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * TeleSign's Voice API allows you to easily send voice messages. You can send alerts, reminders, and notifications,
 * or you can send verification messages containing time-based, one-time passcodes (TOTP).
 */
public class VoiceClient extends RestClient {

    private static final String VOICE_RESOURCE = "/v1/voice";
    private static final String VOICE_STATUS_RESOURCE = "/v1/voice/%s";

    /**
     * Constructor for VoiceClient.
     * @param customerId
     *         Your customer_id string associated with your account.
     * @param apiKey
     *        Your api_key string associated with your account.
     */
    public VoiceClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    /**
     * Constructor for VoiceClient.
     * @param customerId
     *        Your customer_id string associated with your account.
     * @param apiKey
     *        Your api_key string associated with your account.
     * @param restEndpoint
     *         (optional) Override the default restEndpoint to target another endpoint.
     */
    public VoiceClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    /**
     * Constructor for VoiceClient.
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
    public VoiceClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    /**
	 * Use Telesign Voice to build application-to-person, person-to-application, and person-to-person voice
     *  calling into web and mobile applications.
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
    public VoiceClient(String customerId,
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
     * Send a voice call to the target phone_number.
     * <p>
     * See https://developer.telesign.com/docs/voice-api for detailed API documentation.
     * 
     * @param phoneNumber
     *           The phone number to send the voice call to.
     * @param message
     *          The message to send in the voice call.
     * @param messageType
     *         The type of the message.
     * @param params
     *         Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse call(String phoneNumber, String message, String messageType, Map<String, String> params) throws IOException, GeneralSecurityException {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("phone_number", phoneNumber);
        params.put("message", message);
        params.put("message_type", messageType);

        return this.post(VOICE_RESOURCE, params);
    }

    /**
     * Retrieves the current status of the voice call.
     * <p>
     * See https://developer.telesign.com/docs/voice-api for detailed API documentation.
     * 
     * @param referenceId
     *            The reference_id of the voice call.
     * @param params
     *            Additional parameters for the request.
     * @return The TelesignResponse for the request.
     * @throws IOException if the HTTP request fails.
     * @throws GeneralSecurityException if there is a security exception.
     */
    public TelesignResponse status(String referenceId, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.get(String.format(VOICE_STATUS_RESOURCE, referenceId), params);
    }
}
