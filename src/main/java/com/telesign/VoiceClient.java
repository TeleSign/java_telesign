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

    public VoiceClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    public VoiceClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    public VoiceClient(String customerId,
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
     * Send a voice call to the target phone_number.
     * <p>
     * See https://developer.telesign.com/docs/voice-api for detailed API documentation.
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
     */
    public TelesignResponse status(String referenceId, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.get(String.format(VOICE_STATUS_RESOURCE, referenceId), params);
    }
}
