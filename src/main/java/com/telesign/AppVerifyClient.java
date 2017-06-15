package com.telesign;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * AppVerify is a secure, lightweight SDK that integrates a frictionless user verification process into existing
 * native mobile applications.
 **/
public class AppVerifyClient extends RestClient {

    private static final String APPVERIFY_STATUS_RESOURCE = "/v1/mobile/verification/status/%s";

    public AppVerifyClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    public AppVerifyClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    public AppVerifyClient(String customerId,
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
     * Retrieves the verification result for an AppVerify transaction by externalId. To ensure a secure verification
     * flow you must check the status using TeleSign's servers on your backend. Do not rely on the SDK alone to
     * indicate a successful verification.
     * <p>
     * See https://developer.telesign.com/docs/app-verify-android-sdk-self#section-obtaining-verification-status or
     * https://developer.telesign.com/docs/app-verify-ios-sdk-self#section-obtaining-verification-status for detailed
     * API documentation.
     */
    public TelesignResponse status(String externalId, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.get(String.format(APPVERIFY_STATUS_RESOURCE, externalId), params);
    }
}
