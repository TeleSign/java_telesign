package com.telesign.rest;

import java.util.HashMap;
import java.util.Map;

public class ScoreClient extends RestClient {

    private static final String SCORE_RESOURCE = "/v1/score/%s";

    public ScoreClient(String customerId, String secretKey) {

        super(customerId, secretKey);
    }

    public TelesignResponse score(String phoneNumber, String accountLifecycleEvent, Map<String, String> params) {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("account_lifecycle_event", accountLifecycleEvent);

        return this.post(String.format(SCORE_RESOURCE, phoneNumber), params);
    }
}
