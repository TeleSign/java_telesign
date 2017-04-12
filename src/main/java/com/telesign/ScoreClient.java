package com.telesign;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * Score provides risk information about a specified phone number.
 */
public class ScoreClient extends RestClient {

    private static final String SCORE_RESOURCE = "/v1/score/%s";

    public ScoreClient(String customerId, String secretKey) {

        super(customerId, secretKey);
    }

    /**
     * Score is an API that delivers reputation scoring based on phone number intelligence, traffic patterns, machine
     * learning, and a global data consortium.
     * <p>
     * See https://developer.telesign.com/docs/rest_api-phoneid-score for detailed API documentation.
     */
    public TelesignResponse score(String phoneNumber, String accountLifecycleEvent, Map<String, String> params) throws IOException, GeneralSecurityException {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put("account_lifecycle_event", accountLifecycleEvent);

        return this.post(String.format(SCORE_RESOURCE, phoneNumber), params);
    }
}
