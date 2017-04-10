package com.telesign.rest;

import java.util.HashMap;
import java.util.Map;

public class MessagingClient extends RestClient {

    private static final String MESSAGING_RESOURCE = "/v1/messaging";
    private static final String MESSAGING_STATUS_RESOURCE = "/v1/messaging/%s";

    public MessagingClient(String customerId, String secretKey) {
        super(customerId, secretKey);
    }

    public TelesignResponse message(String phoneNumber, String message, String messageType, Map<String, String> params) {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("phone_number", phoneNumber);
        params.put("message", message);
        params.put("message_type", messageType);

        return this.post(MESSAGING_RESOURCE, params);
    }

    public TelesignResponse status(String referenceId, Map<String, String> params) {

        return this.get(String.format(MESSAGING_STATUS_RESOURCE, referenceId), params);
    }
}
