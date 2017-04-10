package com.telesign.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.internal.Version;
import okio.Buffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.Proxy;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RestClient {

    private static final String userAgent = String.format("TeleSignSDK/java-%s Java/%s %s",
            BuildConfig.VERSION,
            System.getProperty("java.version"),
            Version.userAgent());

    private String customerId;
    private String secretKey;
    private String apiHost;
    private OkHttpClient client;

    public RestClient(String customerId, String secretKey) {

        this(customerId, secretKey, "https://rest-api.telesign.com", 10, 10, 10, null, null, null);
    }

    public RestClient(String customerId,
                      String secretKey,
                      String apiHost,
                      long connectTimeout,
                      long readTimeout,
                      long writeTimeout,
                      Proxy proxy,
                      String proxyUserName,
                      String proxyPassword) {

        this.customerId = customerId;
        this.secretKey = secretKey;
        this.apiHost = apiHost;

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);

        if (proxy != null) {
            okHttpClientBuilder.proxy(proxy);

            if (proxyUserName != null && proxyPassword != null) {

                Authenticator proxyAuthenticator = new Authenticator() {
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(proxyUserName, proxyPassword);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                };

                okHttpClientBuilder.proxyAuthenticator(proxyAuthenticator);
            }
        }

        this.client = okHttpClientBuilder.build();
    }

    public static class TelesignResponse {

        public int statusCode;
        public Map<String, List<String>> headers;
        public String body;
        public boolean ok;
        public JsonObject json;

        public TelesignResponse(Response okHttpResponse) {

            this.statusCode = okHttpResponse.code();
            this.headers = okHttpResponse.headers().toMultimap();
            this.ok = okHttpResponse.isSuccessful();

            try {
                this.body = okHttpResponse.body().string();

                try {
                    this.json = new JsonParser().parse(body).getAsJsonObject();
                } catch (IllegalStateException e) {
                    this.json = new JsonObject();
                }

            } catch (IOException e) {
                this.body = "";
                this.json = new JsonObject();
            }
        }
    }

    public static class TelesignException extends RuntimeException {

        public TelesignException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Map<String, String> generateTelesignHeaders(String customerId,
                                                              String secretKey,
                                                              String methodName,
                                                              String resource,
                                                              String urlEncodedFields,
                                                              String dateRfc2616,
                                                              String nonce,
                                                              String userAgent) throws RestClient.TelesignException {

        try {
            if (dateRfc2616 == null) {
                dateRfc2616 = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
            }

            if (nonce == null) {
                nonce = UUID.randomUUID().toString();
            }

            String contentType = "";
            if (methodName.equals("POST") || methodName.equals("PUT")) {
                contentType = "application/x-www-form-urlencoded";
            }

            String authMethod = "HMAC-SHA256";

            StringBuilder string_to_sign_builder = new StringBuilder();

            string_to_sign_builder.append(String.format("%s", methodName));

            string_to_sign_builder.append(String.format("\n%s", contentType));

            string_to_sign_builder.append(String.format("\n%s", dateRfc2616));

            string_to_sign_builder.append(String.format("\nx-ts-auth-method:%s", authMethod));

            string_to_sign_builder.append(String.format("\nx-ts-nonce:%s", nonce));

            if (!contentType.isEmpty() && !urlEncodedFields.isEmpty()) {
                string_to_sign_builder.append(String.format("\n%s", urlEncodedFields));
            }

            string_to_sign_builder.append(String.format("\n%s", resource));

            String string_to_sign = string_to_sign_builder.toString();

            String signature;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(string_to_sign.getBytes()));

            String authorization = String.format("TSA %s:%s", customerId, signature);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", authorization);
            headers.put("Date", dateRfc2616);
            headers.put("Content-Type", contentType);
            headers.put("x-ts-auth-method", authMethod);
            headers.put("x-ts-nonce", nonce);

            if (userAgent != null) {
                headers.put("User-Agent", userAgent);
            }

            return headers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TelesignResponse post(String resource, Map<String, String> params) {

        return this.execute("POST", resource, params);
    }

    public TelesignResponse get(String resource, Map<String, String> params) {

        return this.execute("GET", resource, params);
    }

    public TelesignResponse put(String resource, Map<String, String> params) {

        return this.execute("PUT", resource, params);
    }

    public TelesignResponse delete(String resource, Map<String, String> params) {

        return this.execute("DELETE", resource, params);
    }

    private TelesignResponse execute(String methodName, String resource, Map<String, String> params) {

        try {
            if (params == null) {
                params = new HashMap<>();
            }

            String resourceUri = String.format("%s%s", this.apiHost, resource);

            FormBody formBody = null;
            String urlEncodedFields = "";
            if (methodName.equals("POST") || methodName.equals("PUT")) {
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
                formBody = formBuilder.build();

                Buffer buffer = new Buffer();
                formBody.writeTo(buffer);
                urlEncodedFields = buffer.readUtf8();
            }

            Map<String, String> headers = RestClient.generateTelesignHeaders(
                    this.customerId,
                    this.secretKey,
                    methodName,
                    resource,
                    urlEncodedFields,
                    null,
                    null,
                    RestClient.userAgent);

            Request.Builder requestBuilder = new Request.Builder()
                    .url(resourceUri)
                    .method(methodName, formBody);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            Request request = requestBuilder.build();

            TelesignResponse telesignResponse;
            try (Response okhttpResponse = this.client.newCall(request).execute()) {
                telesignResponse = new TelesignResponse(okhttpResponse);
            }

            return telesignResponse;

        } catch (Exception e) {
            throw new RestClient.TelesignException("An error occurred executing the request.", e);
        }
    }
}
