package com.telesign.rest;

import java.util.Map;

public class AutoVerifyClient extends RestClient {

    private static final String AUTOVERIFY_STATUS_RESOURCE = "/v1/mobile/verification/status/%s";

    public AutoVerifyClient(String customerId, String secretKey) {
        super(customerId, secretKey);
    }

    public TelesignResponse status(String externalId, Map<String, String> params) {

        return this.get(String.format(AUTOVERIFY_STATUS_RESOURCE, externalId), params);
    }
}
