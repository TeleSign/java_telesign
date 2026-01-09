package com.telesign;

import java.net.Proxy;

/**
 * AppVerify is a secure, lightweight SDK that integrates a frictionless user verification process into existing
 * native mobile applications.
 **/
public class AppVerifyClient extends RestClient {

    public AppVerifyClient(String customerId, String apiKey) {
        super(customerId, apiKey);
    }

    public AppVerifyClient(String customerId, String apiKey, String restEndpoint) {
        super(customerId, apiKey, restEndpoint);
    }

    public AppVerifyClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {
        super(customerId, apiKey, restEndpoint, source, sdkVersionOrigin, sdkVersionDependency);
    }

    public AppVerifyClient(String customerId,
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

}
