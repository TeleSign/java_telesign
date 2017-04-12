package com.telesign.rest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * AutoVerify is a secure, lightweight SDK that integrates a frictionless user verification process into existing
 * native mobile applications.
 **/
public class AutoVerifyClient extends RestClient {

    private static final String AUTOVERIFY_STATUS_RESOURCE = "/v1/mobile/verification/status/%s";

    public AutoVerifyClient(String customerId, String secretKey) {
        super(customerId, secretKey);
    }

    /**
     * Retrieves the verification result for an AutoVerify transaction by externalId. To ensure a secure verification
     * flow you must check the status using TeleSign's servers on your backend. Do not rely on the SDK alone to
     * indicate a successful verification.
     * <p>
     * See https://developer.telesign.com/docs/auto-verify-sdk#section-obtaining-verification-status for detailed API
     * documentation.
     */
    public TelesignResponse status(String externalId, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.get(String.format(AUTOVERIFY_STATUS_RESOURCE, externalId), params);
    }
}
