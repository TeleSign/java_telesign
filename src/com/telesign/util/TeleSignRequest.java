/**
 * @version     1.0
 * @copyright   Copyright © 2013, TeleSign Corporation.
 * @license     http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author      John Weatherford
 * @maintainer  Humberto Morales
 * @repository  https://github.com/TeleSign/java_telesign
 * @support     support@telesign.com
 */
package com.telesign.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;

/** The TeleSignRequest class is an abstraction for creating and sending HTTP 1.1 REST Requests for TeleSign web services. */
public class TeleSignRequest {
	/** A boolean value that indicates the Request Method. <em>True</em> for <strong>POST</strong>, and <em>False</em> for <strong>GET</strong>. */
	private boolean post;

	/** The web service's Base URI. For TeleSign web services, this is <em>https://rest.telesign.com/</em>. */
	private final String base;

	/** The name that identifies the network resource. Each of the TeleSign web services is identified by its resource specifier. */
	private final String resource;

	/** Your TeleSign Customer ID. This represents your TeleSign account number. */
	private final String customer_id;

	/** Your TeleSign Secret Shared Key (available from the TeleSign Client Portal). */
	private final String secret_key;

	/** The set of HTTP 1.1 Request header field/value pairs. */
	private TreeMap<String, String> headers;

	/** The set of <em>TeleSign-specific</em> Request header field/value pairs. */
	private TreeMap<String, String> ts_headers;

	/** The set of HTTP 1.1 Request parameter attribute/value pairs. */
	private HashMap<String, String> params;

	/** The <em>absolute form</em> of the Resource URI */
	private URL url;

	/** The persistent session connection for the Request. */
	private HttpURLConnection connection;

	/** The contents of the entity body in the POST Request. */
	private String body = "";

	/** A boolean value that indicates whether the <em>TeleSign-specific</em> <strong>Date</strong> Request header field is used. */
	private boolean ts_date = false;

	/** An AuthMethod enumeration value  that specifies the strength of the encryption method to use when signing the Authentication header. */
	private AuthMethod auth = AuthMethod.SHA1;

	/** Setting default Integer value that specifies the HttpConnection connect timeout value. Having default value as 30000 **/
    private int connectTimeout = 30000;

    /** Setting default Integer value that specifies the HttpConnection read timeout value. Having default value as 30000 **/
    private int readTimeout = 30000;
    
    private String httpsProtocol = "TLSv1.2";


	/**
	 * The TeleSitgnRequest class constructor. A TeleSitgnRequest object
	 * contains all of the information required to call any/all of the TeleSign
	 * web services.
	 *
	 * @param base
	 *			[Required] A string representing the Base URI. For TeleSign
	 *			web services, this is https://rest.telesign.com/.
	 * @param resource
	 *			[Required] A string representing the name of the network
	 *			resource. Each of the TeleSign web services is identified by
	 *			its resource specifier.
	 * @param method
	 *			[Required] A string representing the method to be performed on
	 *			the resource. For TeleSign web services, this is either GET or
	 *			POST.
	 * @param customer_id
	 *			[Required] A string representing your TeleSign Customer ID.
	 *			This represents your TeleSign account number.
	 * @param secret_key
	 *			[Required] A string representing your TeleSign Secret Shared
	 *			Key (available from the TeleSign Client Portal).
	 */
	public TeleSignRequest(String base, String resource, String method, String customer_id, String secret_key) {

		this.base = base;
		this.resource = resource;
		this.customer_id = customer_id;
		this.secret_key = secret_key;

		post = (method.toLowerCase().equals("post"));

		ts_headers = new TreeMap<String, String>();
		headers = new TreeMap<String, String>();
		params = new HashMap<String, String>();
	}

	/**
	 * The TeleSitgnRequest class constructor. A TeleSitgnRequest object
	 * contains all of the information required to call any/all of the TeleSign
	 * web services.
	 *
	 * @param base
	 *			[Required] A string representing the Base URI. For TeleSign
	 *			web services, this is https://rest.telesign.com/.
	 * @param resource
	 *			[Required] A string representing the name of the network
	 *			resource. Each of the TeleSign web services is identified by
	 *			its resource specifier.
	 * @param method
	 *			[Required] A string representing the method to be performed on
	 *			the resource. For TeleSign web services, this is either GET or
	 *			POST.
	 * @param customer_id
	 *			[Required] A string representing your TeleSign Customer ID.
	 *			This represents your TeleSign account number.
	 * @param secret_key
	 *			[Required] A string representing your TeleSign Secret Shared
	 *			Key (available from the TeleSign Client Portal).
	 * @param connectTimeout
	 * 			[Required] A integer representing connection timeout
	 *			connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout
	 *			while reading response returned from Telesign api.
	 */
	public TeleSignRequest(String base, String resource, String method, String customer_id, String secret_key, int connectTimeout, int readTimeout) {

		this.base = base;
		this.resource = resource;
		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;

		post = (method.toLowerCase().equals("post"));

		ts_headers = new TreeMap<String, String>();
		headers = new TreeMap<String, String>();
		params = new HashMap<String, String>();
	}
	
	/**
	 * The TeleSitgnRequest class constructor. A TeleSitgnRequest object
	 * contains all of the information required to call any/all of the TeleSign
	 * web services.
	 *
	 * @param base
	 *			[Required] A string representing the Base URI. For TeleSign
	 *			web services, this is https://rest.telesign.com/.
	 * @param resource
	 *			[Required] A string representing the name of the network
	 *			resource. Each of the TeleSign web services is identified by
	 *			its resource specifier.
	 * @param method
	 *			[Required] A string representing the method to be performed on
	 *			the resource. For TeleSign web services, this is either GET or
	 *			POST.
	 * @param customer_id
	 *			[Required] A string representing your TeleSign Customer ID.
	 *			This represents your TeleSign account number.
	 * @param secret_key
	 *			[Required] A string representing your TeleSign Secret Shared
	 *			Key (available from the TeleSign Client Portal).
	 * @param httpsProtocol 
	 * 			[Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	public TeleSignRequest(String base, String resource, String method, String customer_id, String secret_key, String httpsProtocol) {

		this.base = base;
		this.resource = resource;
		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.httpsProtocol = httpsProtocol;

		post = (method.toLowerCase().equals("post"));

		ts_headers = new TreeMap<String, String>();
		headers = new TreeMap<String, String>();
		params = new HashMap<String, String>();
	}
	/**
	 * The TeleSitgnRequest class constructor. A TeleSitgnRequest object
	 * contains all of the information required to call any/all of the TeleSign
	 * web services.
	 *
	 * @param base
	 *			[Required] A string representing the Base URI. For TeleSign
	 *			web services, this is https://rest.telesign.com/.
	 * @param resource
	 *			[Required] A string representing the name of the network
	 *			resource. Each of the TeleSign web services is identified by
	 *			its resource specifier.
	 * @param method
	 *			[Required] A string representing the method to be performed on
	 *			the resource. For TeleSign web services, this is either GET or
	 *			POST.
	 * @param customer_id
	 *			[Required] A string representing your TeleSign Customer ID.
	 *			This represents your TeleSign account number.
	 * @param secret_key
	 *			[Required] A string representing your TeleSign Secret Shared
	 *			Key (available from the TeleSign Client Portal).
	 * @param connectTimeout
	 * 			[Required] A integer representing connection timeout
	 *			connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout
	 *			while reading response returned from Telesign api.
	 * @param httpsProtocol 
	 * 			[Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	public TeleSignRequest(String base, String resource, String method, String customer_id, String secret_key, int connectTimeout, int readTimeout, String httpsProtocol) {

		this.base = base;
		this.resource = resource;
		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.httpsProtocol = httpsProtocol;

		post = (method.toLowerCase().equals("post"));

		ts_headers = new TreeMap<String, String>();
		headers = new TreeMap<String, String>();
		params = new HashMap<String, String>();
	}

	/**
	 * Adds an HTTP 1.1 request header field/value pair to the set of request
	 * headers.
	 *
	 * @param key
	 *			[Required] A string representing a Request header field
	 *			<em>name</em>.
	 * @param value
	 *			[Required] A string representing a Request header field
	 *			<em>value</em>.
	 */
	public void addHeader(String key, String value) {

		// Check to see if this request header field is a "TeleSign-specific" header field.
		if ((key.length() > 4) && (key.toLowerCase().substring(0, 5).equals("x-ts-"))) {

			// If using the TeleSign-specific date header, then use a blank line for the standard Date Request header.
			if (key.toLowerCase().equals("x-ts-date")) {

				ts_date = true;
			}

			ts_headers.put(key, value);
		}

		headers.put(key, value);
	}

	/**
	 * Adds an HTTP 1.1 Request parameter attribute/value pair to the set of
	 * Request Parameters.
	 *
	 * @param key
	 *			[Required] A string representing a Request parameter attribute
	 *			<em>name</em>.
	 * @param value
	 *			[Required] A string representing a Request parameter attribute
	 *			<em>value</em>.
	 */
	public void addParam(String key, String value) {

		params.put(key, value);
	}

	/**
	 * Sets the entity body of an HTTP 1.1 POST Request message. Contains the
	 * Request parameters for <strong>Verify.call</strong> and
	 * <strong>Verify.sms</strong>.
	 *
	 * @param post_body
	 *			[Required] A string that contains the contents of the POST
	 *			Request's entity body.
	 */
	public void setPostBody(String post_body) {

		body = post_body;
	}

	/**
	 * Gets the entity body of an HTTP 1.1 POST Request message. Used for calls
	 * to <strong>Verify.call</strong> and <strong>Verify.sms</strong>.
	 *
	 * @return A string that contains the contents of the POST Request's entity
	 *		 body.
	 */
	public String getPostBody() {

		return body;
	}

	/**
	 * Returns the set of <em>TeleSign-specific</em> Request header fields.
	 *
	 * @return A sorted key/value mapping that contains the TeleSign-specific
	 *		 Request header fields.
	 */
	public Map<String, String> getTsHeaders() {

		return ts_headers;
	}

	/**
	 * Returns the set of both the standard <u>and</u> the
	 * <em>TeleSign-specific</em> Request header fields.
	 *
	 * @return A sorted key/value mapping that contains all of the Request header fields.
	 */
	public Map<String, String> getAllHeaders() {

		TreeMap<String, String> tmp = new TreeMap<String, String>();

		tmp.putAll(headers);
		tmp.putAll(ts_headers);

		return tmp;
	}

	/**
	 * Returns the set of POST Request parameters.
	 *
	 * @return A sorted key/value mapping that contains all of the POST Request parameters.
	 */
	public Map<String, String> getAllParams() {

		return params;
	}

	/**
	 * Creates and sends the REST request.
	 *
	 * @return A string containing the TeleSign web server's Response.
	 * @throws IOException
	 *			 A {@link java.security.SignatureException} signals that an
	 *			 error occurred while attempting to sign the Request.
	 */
	public String executeRequest() throws IOException {

		String signingString = getSigningString(customer_id);
		String signature;
		String url_output = "";

		// Create the absolute form of the resource URI, and place it in a string buffer.
		StringBuffer full_url = new StringBuffer(base).append(resource);

		if (params.size() > 0) {

			full_url.append("?");
			int i = 0;

			for (String key : params.keySet()) {

				if (++i != 0) {

					full_url.append("&");
				}

				full_url.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
			}
		}

		url = new URL(full_url.toString());

		// Create the Signature using the formula: Signature = Base64(HMAC-SHA( YourTeleSignAPIKey, UTF-8-Encoding-Of( StringToSign )).
		try {

			signature = encode(signingString, secret_key);
		}
		catch (SignatureException e) {

			System.err.println("Error signing request " + e.getMessage());

			return null;
		}

		String auth_header = "TSA " + customer_id + ":" + signature;

		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(connectTimeout);
		connection.setReadTimeout(readTimeout);
		connection.setRequestProperty("Authorization", auth_header);
		setTLSProtocol();
		
		if (post) {

			connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
		}

		for (String key : ts_headers.keySet()){

			connection.setRequestProperty(key, ts_headers.get(key));
		}


		for (String key : headers.keySet()) {

			connection.setRequestProperty(key, headers.get(key));
		}

		if (post) {

			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

			wr.writeBytes(body);
			wr.flush();
			wr.close();
		}

		int response = connection.getResponseCode();

		BufferedReader in;

		try {

			InputStream isr = (response == 200) ? connection.getInputStream() : connection.getErrorStream();
			in = new BufferedReader(new InputStreamReader(isr));
			String urlReturn;

			while ((urlReturn = in.readLine()) != null) {

				url_output += urlReturn;
			}

			in.close();
		}
		catch (IOException e) {
			System.err.println("IOException while reading from input stream " + e.getMessage());
			throw new RuntimeException(e);
		}

		return url_output;
	}

	/**
	 * Sets the level of encryption to use when signing this request.
	 * Current values are SHA-1 and SHA-256, and represented in
	 * the AuthMethod enumeration.
	 *
	 * @param auth	[Required] One of the AuthMethod enumeration values.
	 */
	public void setSigningMethod(AuthMethod auth) {

		addHeader("x-ts-auth-method", auth.tsValue());
		this.auth = auth;
	}

	/**
	 * Sets a nonce (a numeric value that you consider valid for only fifteen minutes or so) to use for this request.
	 * If use a nonce, you must set its value before each request execution.
	 * This method is considered a <em>convenience method</em> because this data can also be set using the addHeader option.
	 *
	 * @param nonce
	 */
	public void setNonce(String nonce) {
		addHeader("x-ts-nonce", nonce);
	}

	/**
	 * This method is used internally by the {@link com.telesign.util.TeleSignRequest#executeRequest()} method.
	 *
	 * @param customer_id
	 *			[Required] A string representing your TeleSign Customer ID.
	 *			This represents your TeleSign account number.
	 * @return A string representing the signing string used to create a
	 *		 Signature object, for the authenticate the REST request.
	 */
	private String getSigningString(String customer_id) {

		String stringToSign = post ? "POST\n" : "GET\n";
		String content_type = post ? "application/x-www-form-urlencoded\n" : "\n";

		stringToSign += content_type;

		if (ts_date) {

			stringToSign += "\n";
		}
		else {

			Date now = new Date();

			SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZ", Locale.US);
			String date = rfc2616.format(now);
			addHeader("Date", date);

			stringToSign += date + "\n";
		}

		/*
		 * Create the CanonicalizedTsHeaders element. This entails: Converting
		 * header names to lowercase, Sorting the set by header name, Ensuring
		 * that each X-TS- header name appears just once, Unfolding long header
		 * values that span multiple lines, by replacing the folding white space
		 * (newline) with a single space, and Removing white space surrounding
		 * the colon that appears between each header name/value pair.
		 */
		Set<String> orderedKeys = ts_headers.keySet();

		for (String key : orderedKeys) {

			stringToSign += key.toLowerCase() + ":" + ts_headers.get(key) + "\n";
		}

		if (post) {

			stringToSign += body + "\n";
		}

		stringToSign += resource;

		return stringToSign;
	}

	/**
	 * Creates the Signature component for the Authorization header field.
	 * Uses your Secret Key to encode the stringToSign.
	 * This is a <em>helper method</em>, used internally by the {@link TeleSignRequest#executeRequest()} method.
	 *
	 * @param data
	 *			[Required] The stringToSign.
	 * @param key
	 *			[Required] Your TeleSign API Key. Also known as your Secret
	 *			Key, and your Shared Cryptographic Key. It s a bese64-encoded
	 *			string value.
	 * @return A String containing the Base64-encoded hash-based message
	 *		 authentication code.
	 * @throws java.security.SignatureException
	 *			 Failed to generate HMAC. IllegalArgumentException - if
	 *			 algorithm is null or key is null or empty.
	 */
	private String encode(String data, String key)  throws java.security.SignatureException {

		String result;

		byte[] decoded_key = Base64.decodeBase64(key);

		try {
			// Get an hmac_sha key from the raw key bytes.
			SecretKeySpec signingKey = new SecretKeySpec(decoded_key, auth.value());

			// Get an hmac_sha Mac instance, and initialize it with the signing key.
			Mac mac = Mac.getInstance(auth.value());
			mac.init(signingKey);

			// Compute the HMAC on input data bytes.
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// Base64-encode the HMAC.
			result =  new String(Base64.encodeBase64(rawHmac));

		}
		catch (Exception e) {

			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}

		return result;
	}
	
	/**
	 * Set the TLS protocol to TLSv1.2
	 */
	private void setTLSProtocol() {
		SSLContext sslContext;
		try {			
			// setting ssl instance to TLSv1.x
			sslContext = SSLContext.getInstance(httpsProtocol);
			
			// sslContext initialize
			sslContext.init(null,null,new SecureRandom());

			// typecasting ssl with HttpsUrlConnection and setting sslcontext
			((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());			
		} catch (NoSuchAlgorithmException e1) {
			System.err.println("Received No Such Alogorithm Exception " + e1.getMessage());
		}
		catch (KeyManagementException e) {
			System.err.println("Key Management Exception received " + e.getMessage());
		}
	}
}
