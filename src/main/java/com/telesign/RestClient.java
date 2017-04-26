package com.telesign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.internal.Version;
import okio.Buffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The TeleSign RestClient is a generic HTTP REST client that can be extended to make requests against any of
 * TeleSign's REST API endpoints.
 * <p>
 * See https://developer.telesign.com for detailed API documentation.
 */
public class RestClient {

    private static final String userAgent = String.format("TeleSignSDK/java-%s Java/%s %s",
            BuildConfig.VERSION,
            System.getProperty("java.version"),
            Version.userAgent());

    private String customerId;
    private String apiKey;
    private String restEndpoint;
    private OkHttpClient client;

    public RestClient(String customerId, String apiKey) {

        this(customerId, apiKey, null, null, null, null, null, null, null);
    }

    public RestClient(String customerId, String apiKey, String restEndpoint) {

        this(customerId, apiKey, restEndpoint, null, null, null, null, null, null);
    }

    /**
     * TeleSign RestClient useful for making generic RESTful requests against our API.
     *
     * @param customerId     Your customer_id string associated with your account.
     * @param apiKey      Your api_key string associated with your account.
     * @param restEndpoint        (optional) Override the default restEndpoint to target another endpoint.
     * @param connectTimeout (optional) connectTimeout passed into OkHttp.
     * @param readTimeout    (optional) readTimeout passed into OkHttp.
     * @param writeTimeout   (optional) writeTimeout passed into OkHttp.
     * @param proxy          (optional) proxy passed into OkHttp.
     * @param proxyUsername  (optional) proxyUserName used to create an Authenticator passed into OkHttp.
     * @param proxyPassword  (optional) proxyPassword used to create an Authenticator passed into OkHttp.
     */
    public RestClient(String customerId,
                      String apiKey,
                      String restEndpoint,
                      Long connectTimeout,
                      Long readTimeout,
                      Long writeTimeout,
                      Proxy proxy,
                      final String proxyUsername,
                      final String proxyPassword) {

        this.customerId = customerId;
        this.apiKey = apiKey;

        if (restEndpoint == null) {
            this.restEndpoint = "https://rest-api.telesign.com";
        } else {
            this.restEndpoint = restEndpoint;
        }

        if (connectTimeout == null) {
            connectTimeout = 10L;
        }

        if (readTimeout == null) {
            readTimeout = 10L;
        }

        if (writeTimeout == null) {
            writeTimeout = 10L;
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS);

        if (proxy != null) {
            okHttpClientBuilder.proxy(proxy);

            if (proxyUsername != null && proxyPassword != null) {

                Authenticator proxyAuthenticator = new Authenticator() {
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(proxyUsername, proxyPassword);
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

    /**
     * A simple HTTP Response object to abstract the underlying OkHttp library response.
     */
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
                } catch (JsonParseException | IllegalStateException e) {
                    this.json = new JsonObject();
                }

            } catch (IOException e) {
                this.body = "";
                this.json = new JsonObject();
            }
        }
    }

    /**
     * Generates the TeleSign REST API headers used to authenticate requests.
     * <p>
     * Creates the canonicalized stringToSign and generates the HMAC signature. This is used to authenticate requests
     * against the TeleSign REST API.
     * <p>
     * See https://developer.telesign.com/docs/authentication for detailed API documentation.
     *
     * @param customerId       Your account customer_id.
     * @param apiKey        Your account api_key.
     * @param methodName       The HTTP method name of the request as a upper case string, should be one of 'POST', 'GET',
     *                         'PUT' or 'DELETE'.
     * @param resource         The partial resource URI to perform the request against.
     * @param urlEncodedFields URL encoded HTTP body to perform the HTTP request with.
     * @param dateRfc2616      (optional) The date and time of the request formatted in rfc 2616.
     * @param nonce            (optional) A unique cryptographic nonce for the request.
     * @param userAgent        (optional) User Agent associated with the request.
     * @return Map of HTTP headers to be applied to the request.
     */
    public static Map<String, String> generateTelesignHeaders(String customerId,
                                                              String apiKey,
                                                              String methodName,
                                                              String resource,
                                                              String urlEncodedFields,
                                                              String dateRfc2616,
                                                              String nonce,
                                                              String userAgent) throws NoSuchAlgorithmException, InvalidKeyException {

        if (dateRfc2616 == null) {
            SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
            rfc2616.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateRfc2616 = rfc2616.format(new Date());
        }

        if (nonce == null) {
            nonce = UUID.randomUUID().toString();
        }

        String contentType = "";
        if (methodName.equals("POST") || methodName.equals("PUT")) {
            contentType = "application/x-www-form-urlencoded";
        }

        String authMethod = "HMAC-SHA256";

        StringBuilder stringToSignBuilder = new StringBuilder();

        stringToSignBuilder.append(String.format("%s", methodName));

        stringToSignBuilder.append(String.format("\n%s", contentType));

        stringToSignBuilder.append(String.format("\n%s", dateRfc2616));

        stringToSignBuilder.append(String.format("\nx-ts-auth-method:%s", authMethod));

        stringToSignBuilder.append(String.format("\nx-ts-nonce:%s", nonce));

        if (!contentType.isEmpty() && !urlEncodedFields.isEmpty()) {
            stringToSignBuilder.append(String.format("\n%s", urlEncodedFields));
        }

        stringToSignBuilder.append(String.format("\n%s", resource));

        String stringToSign = stringToSignBuilder.toString();

        String signature;
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(DatatypeConverter.parseBase64Binary(apiKey), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);
        signature = DatatypeConverter.printBase64Binary(sha256HMAC.doFinal(stringToSign.getBytes()));

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
    }

    /**
     * Generic TeleSign REST API POST handler.
     *
     * @param resource The partial resource URI to perform the request against.
     * @param params   Body params to perform the POST request with.
     * @return The TelesignResponse for the request.
     */
    public TelesignResponse post(String resource, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.execute("POST", resource, params);
    }

    /**
     * Generic TeleSign REST API GET handler.
     *
     * @param resource The partial resource URI to perform the request against.
     * @param params   Body params to perform the POST request with.
     * @return The TelesignResponse for the request.
     */
    public TelesignResponse get(String resource, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.execute("GET", resource, params);
    }

    /**
     * Generic TeleSign REST API PUT handler.
     *
     * @param resource The partial resource URI to perform the request against.
     * @param params   Body params to perform the POST request with.
     * @return The TelesignResponse for the request.
     */
    public TelesignResponse put(String resource, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.execute("PUT", resource, params);
    }

    /**
     * Generic TeleSign REST API DELETE handler.
     *
     * @param resource The partial resource URI to perform the request against.
     * @param params   Body params to perform the POST request with.
     * @return The TelesignResponse for the request.
     */
    public TelesignResponse delete(String resource, Map<String, String> params) throws IOException, GeneralSecurityException {

        return this.execute("DELETE", resource, params);
    }

    /**
     * Generic TeleSign REST API request handler.
     *
     * @param methodName The HTTP method name, as an upper case string.
     * @param resource   The partial resource URI to perform the request against.
     * @param params     Body params to perform the POST request with.
     * @return The TelesignResponse for the request.
     */
    private TelesignResponse execute(String methodName, String resource, Map<String, String> params) throws IOException, GeneralSecurityException {

        if (params == null) {
            params = new HashMap<>();
        }

        HttpUrl httpUrl = HttpUrl.parse(String.format("%s%s", this.restEndpoint, resource));

        FormBody formBody = null;
        String urlEncodedFields = "";
        if (methodName.equals("POST") || methodName.equals("PUT")) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            formBody = formBodyBuilder.build();

            Buffer buffer = new Buffer();
            formBody.writeTo(buffer);
            urlEncodedFields = buffer.readUtf8();
        } else {
            HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            httpUrl = httpUrlBuilder.build();
        }

        Map<String, String> headers = RestClient.generateTelesignHeaders(
                this.customerId,
                this.apiKey,
                methodName,
                resource,
                urlEncodedFields,
                null,
                null,
                RestClient.userAgent);

        Request.Builder requestBuilder = new Request.Builder()
                .url(httpUrl)
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
    }
}
