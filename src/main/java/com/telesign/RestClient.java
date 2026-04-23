package com.telesign;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.Buffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The TeleSign RestClient is a generic HTTP REST client that can be extended to
 * make requests against any of TeleSign's REST API endpoints.
 * <p>
 * See https://developer.telesign.com for detailed API documentation.
 */
public class RestClient {

	private static final String SDK_VERSION = RestClient.class.getPackage().getImplementationVersion();

	public static final String URL_FORM_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String AUTH_BASIC = "Basic";

	protected String customerId;
	protected String apiKey;
	private String restEndpoint;
	private OkHttpClient client;
	private String userAgent;

	public RestClient(String customerId, String apiKey) {

		this(customerId, apiKey, null, null, null, null, null, null, null, null, null, null);
	}

	public RestClient(String customerId, String apiKey, String restEndpoint) {

		this(customerId, apiKey, restEndpoint, null, null, null, null, null, null, null, null, null);
	}

	public RestClient(String customerId, String apiKey, String restEndpoint, String source, String sdkVersionOrigin, String sdkVersionDependency) {

		this(customerId, apiKey, restEndpoint, null, null, null, null, null, null, source, sdkVersionOrigin, sdkVersionDependency);
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
			final String proxyPassword, final String source, final String sdkVersionOrigin, final String sdkVersionDependency) {

		this.customerId = customerId;
		this.apiKey = apiKey;

		this.userAgent = String.format("TeleSignSDK/java Java/%s OkHttp/%s OriginatingSDK/%s SDKVersion/%s",
				System.getProperty("java.version"), OkHttp.VERSION, (source == null ? "java_telesign" : source), (sdkVersionOrigin == null ? SDK_VERSION : sdkVersionOrigin));

		if (!Objects.equals(source, "java_telesign") && sdkVersionDependency != null) {
            this.userAgent += String.format(" DependencySDKVersion/%s", sdkVersionDependency);
        }

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
			String dateRfc2616, String nonce, String userAgent, String contentType, String authMethod) throws NoSuchAlgorithmException, InvalidKeyException {

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

		String authorization = "";
		Map<String, String> headers = new HashMap<>();

		if (Objects.equals(authMethod, "Basic")) {

			String credentials = customerId + ":" + apiKey;

			String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

			authorization = authMethod + " " + encodedCredentials;
		} else {

			StringBuilder stringToSignBuilder = new StringBuilder();

			stringToSignBuilder.append(String.format("%s", methodName));

			stringToSignBuilder.append(String.format("\n%s", contentType));

			stringToSignBuilder.append(String.format("\n%s", dateRfc2616));

			stringToSignBuilder.append(String.format("\nx-ts-auth-method:%s", "HMAC-SHA256"));

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

			authorization = String.format("TSA %s:%s", customerId, signature);
			headers.put("Date", dateRfc2616);
			headers.put("Content-Type", contentType);
			headers.put("x-ts-auth-method", authMethod);
			headers.put("x-ts-nonce", nonce);
		}

		headers.put("Authorization", authorization);

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
		return this.execute("POST", resource, params, URL_FORM_ENCODED_CONTENT_TYPE, null);
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

		return this.execute("POST", resource, params, contentType, null);
	}

	/**
	 * Generic TeleSign REST API POST handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the POST request with.
	 * @param contentType
	 *            Appication/json, www-url ....
	 * @param authMethod
	 *            Basic, Diggest ...
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse post(String resource, Map<String, ? extends Object> params, String contentType, String authMethod)
			throws IOException, GeneralSecurityException {

		return this.execute("POST", resource, params, contentType, authMethod);
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

		return this.execute("PUT", resource, params, URL_FORM_ENCODED_CONTENT_TYPE, null);
	}

	/**
	 * Generic TeleSign REST API PATCH handler.
	 *
	 * @param resource
	 *            The partial resource URI to perform the request against.
	 * @param params
	 *            Params to perform the PATCH request with.
	 * @param contentType
	 *            Appication/json, www-url ....
	 * @param authMethod
	 *            Basic, Digest ...
	 * @return The TelesignResponse for the request.
	 */
	public TelesignResponse patch(String resource, Map<String, String> params, String contentType, String authMethod)
			throws IOException, GeneralSecurityException {

		return this.execute("PATCH", resource, params, contentType, authMethod);
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
		if (Objects.equals(contentType, "application/json")) {
			Gson gson = new Gson();
			String json = gson.toJson(params);
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, json.getBytes());
			return body;
		} else {
			FormBody.Builder formBodyBuilder = new FormBody.Builder();
			for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
				formBodyBuilder.add(entry.getKey(), (String) entry.getValue());
			}
			FormBody formBody = formBodyBuilder.build();

			return formBody;
		}
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
		return execute(methodName, resource, params, "", null);
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
	private TelesignResponse execute(String methodName, String resource, Map<String, ? extends Object> params, String contentType, String authMethod)
			throws IOException, GeneralSecurityException {

		if (authMethod == null) {
			authMethod = "HMAC-SHA256";
		}

		if (params == null) {
			params = new HashMap<>();
		}

		HttpUrl httpUrl = HttpUrl.parse(String.format("%s%s", this.restEndpoint, resource));

		RequestBody requestBody = null;
		String requestParams = "";
		if (methodName.equals("POST") || methodName.equals("PUT") || methodName.equals("PATCH")) {
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
				methodName, resource, requestParams, null, null, this.userAgent, contentType, authMethod);

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
