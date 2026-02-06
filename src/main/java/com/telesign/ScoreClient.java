package com.telesign;

import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * Score provides risk information about a specified phone number.
 */
public class ScoreClient extends RestClient {

    private static final String INTELLIGENCE_SCORE_RESOURCE = "/intelligence/phone";

    private static final String DETECT_REST_ENDPOINT = "https://detect.telesign.com";

    public ScoreClient(String customerId, String apiKey) {
        super(customerId, apiKey, DETECT_REST_ENDPOINT);
    }

    public ScoreClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    public ScoreClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    public ScoreClient(String customerId,
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
     *Obtain a risk recommendation for this phone number, as well as other relevant information using Telesign Cloud API.
     * <p>
     * See https://developer.telesign.com/enterprise/reference/submitphonenumberforintelligencecloud for detailed API documentation.
     */
    public TelesignResponse score(String phoneNumber, String accountLifecycleEvent, Map<String, String> params) throws IOException, GeneralSecurityException {
        if (params == null) {
            params = new HashMap<>();
        }
        
        params.put("phone_number", phoneNumber);
        params.put("account_lifecycle_event", accountLifecycleEvent);

        return this.post(INTELLIGENCE_SCORE_RESOURCE, params);
    }
}
