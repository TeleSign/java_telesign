package com.telesign.rest;

import java.util.HashMap;
import java.util.Map;

public class VoiceClient extends RestClient {

    private static final String VOICE_RESOURCE = "/v1/voice";
    private static final String VOICE_STATUS_RESOURCE = "/v1/voice/%s";

    public VoiceClient(String customerId, String secretKey) {

        super(customerId, secretKey);
    }

    public TelesignResponse call(String phoneNumber, String message, String messageType, Map<String, String> params) {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("phone_number", phoneNumber);
        params.put("message", message);
        params.put("message_type", messageType);

        return this.post(VOICE_RESOURCE, params);
    }

    public TelesignResponse status(String referenceId, Map<String, String> params) {

        return this.get(String.format(VOICE_STATUS_RESOURCE, referenceId), params);
    }
}
