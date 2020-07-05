package com.telesign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import okhttp3.*;
import okio.Buffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The TeleSign RestClient is a generic HTTP REST client that can be extended to
 * make requests against any of TeleSign's REST API endpoints.
 * <p>
 * See https://developer.telesign.com for detailed API documentation.
 */
public class RestClient {

	private static final String userAgent = String.format("TeleSignSDK/java-%s Java/%s OkHttp/%s", BuildConfig.VERSION,
			System.getProperty("java.version"), OkHttp.VERSION);

	public static final String URL_FORM_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String JSON_CONTENT_TYPE = "application/json";

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
	 * TeleSign RestClient useful for making generic RESTful requests against our
	 * API.
	 *
	 * @param customerId
	 *            Your customer_id string associated with your account.
	 * @param apiKey
	 *            Your api_key string associated with your account.
	 * @param restEndpoint
	 *            (optional) Override the default restEndpoint to target another
	 *            endpoint.
	 * @param connectTimeout
	 *            (optional) connectTimeout passed into OkHttp.
	 * @param readTimeout
	 *            (optional) readTimeout passed into OkHttp.
	 * @param writeTimeout
	 *            (optional) writeTimeout passed into OkHttp.
	 * @param proxy
	 *            (optional) proxy passed into OkHttp.
	 * @param proxyUsername
	 *            (optional) proxyUserName used to create an Authenticator passed
	 *            into OkHttp.
	 * @param proxyPassword
	 *            (optional) proxyPassword used to create an Authenticator passed
	 *            into OkHttp.
	 */
	public RestClient(String customerId, String apiKey, String restEndpoint, Integer connectTimeout,
			Integer readTimeout, Integer writeTimeout, Proxy proxy, final String proxyUsername,
			final String proxyPassword) {

		this.customerId = customerId;
		this.apiKey = apiKey;

		if (restEndpoint == null) {
			this.restEndpoint = "https://rest-api.telesign.com";
		} else {
			this.restEndpoint = restEndpoint;
		}

		if (connectTimeout == null) {
			connectTimeout = 10;
		}

		if (readTimeout == null) {
			readTimeout = 10;
		}

		if (writeTimeout == null) {
			writeTimeout = 10;
		}

		OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
				.connectTimeout(connectTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS)
				.writeTimeout(writeTimeout, TimeUnit.SECONDS);

		if (proxy != null) {
			okHttpClientBuilder.proxy(proxy);

			if (proxyUsername != null && proxyPassword != null) {

				Authenticator proxyAuthenticator = new Authenticator() {
					public Request authenticate(Route route, Response response) throws IOException {
						String credential = Credentials.basic(proxyUsername, proxyPassword);
						return response.request().newBuilder().header("Proxy-Authorization", credential).build();
					}
				};

				okHttpClientBuilder.proxyAuthenticator(proxyAuthenticator);
			}
		}

		this.client = okHttpClientBuilder.build();
	}

	static byte[] parseBase64(String encoded) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < encoded.length(); i++) {
			char ch = encoded.charAt(i);
			if (
					(ch >= 'A' && ch <= 'Z') ||
							(ch >= 'a' && ch <= 'z') ||
							(ch >= '0' && ch <= '9') ||
							(ch == '+') ||
							(ch == '/')
			) {
				sb.append(ch);
			}
		}
		return Base64.getDecoder().decode(sb.toString());
	}

	static String encodeBase64(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	/**
	 * A simple HTTP Response object to abstract the underlying OkHttp library
	 * response.
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
	 * Creates the canonicalized stringToSign and generates the HMAC signature. This
	 * is used to authenticate requests against the TeleSign REST API.
	 * <p>
	 * See https://developer.telesign.com/docs/authentication for detailed API
	 * documentation.
	 *
	 * @param customerId
	 *            Your account customer_id.
	 * @param apiKey
	 *            Your account api_key.
	 * @param methodName
	 *            The HTTP method name of the request as a upper case string, should
	 *            be one of 'POST', 'GET', 'PUT' or 'DELETE'.
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param requestParams
	 *            URL encoded HTTP body to perform the HTTP request with.
	 * @param dateRfc2616
	 *            (optional) The date and time of the request formatted in rfc 2616.
	 * @param nonce
	 *            (optional) A unique cryptographic nonce for the request.
	 * @param userAgent
	 *            (optional) User Agent associated with the request.
	 * 
	 * @param contentType
	 *            (optional) will be sent only if post or put method is requested
	 * @return Map of HTTP headers to be applied to the request.
	 */
	public static Map<String, String> generateTelesignHeaders(String customerId, String apiKey, 
			String methodName, String resource, String requestParams,
			String dateRfc2616, String nonce, String userAgent, String contentType) throws NoSuchAlgorithmException, InvalidKeyException {

		if (dateRfc2616 == null) {
			SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			rfc2616.setTimeZone(TimeZone.getTimeZone("GMT"));
			dateRfc2616 = rfc2616.format(new Date());
		}

		if (nonce == null) {
			nonce = UUID.randomUUID().toString();
		}
		
		if ((!methodName.equals("POST") & !methodName.equals("PUT")) || contentType == null) {
			contentType = "";
		}

		String authMethod = "HMAC-SHA256";

		StringBuilder stringToSignBuilder = new StringBuilder();

		stringToSignBuilder.append(String.format("%s", methodName));

		stringToSignBuilder.append(String.format("\n%s", contentType));

		stringToSignBuilder.append(String.format("\n%s", dateRfc2616));

		stringToSignBuilder.append(String.format("\nx-ts-auth-method:%s", authMethod));

		stringToSignBuilder.append(String.format("\nx-ts-nonce:%s", nonce));

		if (!contentType.isEmpty() && !requestParams.isEmpty()) {
			stringToSignBuilder.append(String.format("\n%s", requestParams));
		}

		stringToSignBuilder.append(String.format("\n%s", resource));

		String stringToSign = stringToSignBuilder.toString();

		String signature;
		Mac sha256HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKeySpec = new SecretKeySpec(parseBase64(apiKey), "HmacSHA256");
		sha256HMAC.init(secretKeySpec);
		signature = encodeBase64(sha256HMAC.doFinal(stringToSign.getBytes()));

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
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the POST request with.
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse post(String resource, Map<String, ? extends Object> params)
			throws IOException, GeneralSecurityException {
		return this.execute("POST", resource, params, URL_FORM_ENCODED_CONTENT_TYPE);
	}

	/**
	 * Generic TeleSign REST API POST handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the POST request with.
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse post(String resource, Map<String, ? extends Object> params, String contentType)
			throws IOException, GeneralSecurityException {

		return this.execute("POST", resource, params, contentType);
	}

	/**
	 * Generic TeleSign REST API GET handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the GET request with.
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse get(String resource, Map<String, String> params)
			throws IOException, GeneralSecurityException {

		return this.execute("GET", resource, params);
	}

	/**
	 * Generic TeleSign REST API PUT handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the PUT request with.
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse put(String resource, Map<String, String> params)
			throws IOException, GeneralSecurityException {

		return this.execute("PUT", resource, params, URL_FORM_ENCODED_CONTENT_TYPE);
	}

	/**
	 * Generic TeleSign REST API DELETE handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the DELETE request with.
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse delete(String resource, Map<String, String> params)
			throws IOException, GeneralSecurityException {

		return this.execute("DELETE", resource, params);
	}

	/**
	 * Generic TeleSign REST API request handler.
	 *
	 * @param params
	 *            Params to perform the request with.
	 * @return The RequestBody for the request.
	 * @throws IOException
	 */

	public RequestBody createRequestBody(Map<String, ? extends Object> params, String contentType) throws IOException {
		FormBody.Builder formBodyBuilder = new FormBody.Builder();
		for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
			formBodyBuilder.add(entry.getKey(), (String) entry.getValue());
		}
		FormBody formBody = formBodyBuilder.build();

		return formBody;

	}

	/**
	 * Generic TeleSign method for request execution,
	 *
	 * @param methodName
	 * @param resource
	 * @param params
	 * @return The TelesignResponse for the request
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private TelesignResponse execute(String methodName, String resource, Map<String, ? extends Object> params)
			throws IOException, GeneralSecurityException {
		return execute(methodName, resource, params, "");
	}

	/**
	 * Generic TeleSign method for request execution,
	 * 
	 * @param methodName
	 * @param resource
	 * @param params
	 * @return The TelesignResponse for the request
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private TelesignResponse execute(String methodName, String resource, Map<String, ? extends Object> params, String contentType)
			throws IOException, GeneralSecurityException {

		if (params == null) {
			params = new HashMap<>();
		}

		HttpUrl httpUrl = HttpUrl.parse(String.format("%s%s", this.restEndpoint, resource));

		RequestBody requestBody = null;
		String requestParams = "";
		if (methodName.equals("POST") || methodName.equals("PUT")) {
			requestBody = this.createRequestBody(params, contentType);
			if (requestBody != null) {
				Buffer buffer = new Buffer();
				requestBody.writeTo(buffer);
				requestParams = buffer.readUtf8();
			}
		} else {
			HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
			for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
				httpUrlBuilder.addQueryParameter(entry.getKey(), (String) entry.getValue());
			}
			httpUrl = httpUrlBuilder.build();
		}

		Map<String, String> headers = RestClient.generateTelesignHeaders(this.customerId, this.apiKey,
				methodName, resource, requestParams, null, null, RestClient.userAgent, contentType);

		Request.Builder requestBuilder = new Request.Builder().url(httpUrl).method(methodName, requestBody);
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
