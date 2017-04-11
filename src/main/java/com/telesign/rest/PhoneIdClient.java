package com.telesign.rest;

import java.util.Map;

/**
 * A set of APIs that deliver deep phone number data attributes that help optimize the end user
 * verification process and evaluate risk.
 */
public class PhoneIdClient extends RestClient {

    private static final String PHONEID_RESOURCE = "/v1/phoneid/%s";

    public PhoneIdClient(String customerId, String secretKey) throws TelesignException {
        super(customerId, secretKey);
    }

    /**
     * The PhoneID API provides a cleansed phone number, phone type, and telecom carrier information to determine the
     * best communication method - SMS or voice.
     * <p>
     * See https://developer.telesign.com/docs/phoneid-api for detailed API documentation.
     */
    public TelesignResponse phoneid(String phoneNumber, Map<String, String> params) throws TelesignException {

        return this.post(String.format(PHONEID_RESOURCE, phoneNumber), params);
    }
}
