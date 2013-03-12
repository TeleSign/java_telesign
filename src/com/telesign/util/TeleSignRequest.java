/**
f * @version		1.0
 * @copyright		Copyright © 2013, TeleSign Corporation.
 * @license		http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author		J. Weatherford
 * @maintainer		Humberto Morales
 * @repository		https://github.com/TeleSign/java_telesign
 * @support		support@telesign.com
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
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * The TeleSignRequest class is an abstraction for creating and sending HTTP 1.1
 * REST Requests for TeleSign web services.
 */
public class TeleSignRequest {
    /**
     * A boolean value indicates the Request Method. <em>True</em> for
     * <strong>POST</strong>, and <em>False</em> for <strong>GET</strong>.
     */
    private boolean post;

    /**
     * The Base URI. For TeleSign web services, this is
     * https://rest.telesign.com/.
     */
    private final String base;

    /**
     * The name of the network resource. Each of the TeleSign web services is
     * identified by its resource specifier.
     */
    private final String resource;

    /** Your TeleSign Customer ID. This represents your TeleSign account number. */
    private final String customer_id;

    /**
     * Your TeleSign Secret Shared Key (available from the TeleSign Client
     * Portal).
     */
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

    /**
     * A boolean value that indicates whether the <em>TeleSign-specific</em>
     * <strong>Date</strong> Request header field is used.
     */
    private boolean ts_date = false;

    /**
     * The TeleSitgnRequest class constructor. A TeleSitgnRequest object
     * contains all of the information required to call any/all of the TeleSign
     * web services.
     * 
     * @param base
     *            [Required] A string representing the Base URI. For TeleSign
     *            web services, this is https://rest.telesign.com/.
     * @param resource
     *            [Required] A string representing the name of the network
     *            resource. Each of the TeleSign web services is identified by
     *            its resource specifier.
     * @param method
     *            [Required] A string representing the method to be performed on
     *            the resource. For TeleSign web services, this is either GET or
     *            POST.
     * @param customer_id
     *            [Required] A string representing your TeleSign Customer ID.
     *            This represents your TeleSign account number.
     * @param secret_key
     *            [Required] A string representing your TeleSign Secret Shared
     *            Key (available from the TeleSign Client Portal).
     */
    public TeleSignRequest(String base, String resource, String method,
	    String customer_id, String secret_key) {
	post = (method.toLowerCase().equals("post"));

	this.base = base;
	this.resource = resource;
	this.customer_id = customer_id;
	this.secret_key = secret_key;

	ts_headers = new TreeMap<String, String>();
	headers = new TreeMap<String, String>();
	params = new HashMap<String, String>();
    }

    /**
     * Adds an HTTP 1.1 Request header field/value pair to the set of Request
     * Headers.
     * 
     * @param key
     *            [Required] A string representing a Request header field
     *            <em>name</em>.
     * @param value
     *            [Required] A string representing a Request header field
     *            <em>value</em>.
     */
    public void addHeader(String key, String value) {
	// Check to see if this request header field is a "TeleSign-specific"
	// header field.
	if ((key.length() > 4)
		&& (key.toLowerCase().substring(0, 5).equals("x-ts-"))) {
	    // If using the TeleSign-specific date header, then use a blank line
	    // for the standard Date Request header.
	    if (key.toLowerCase().equals("x-ts-date"))
		ts_date = true;

	    ts_headers.put(key, value);
	}

	headers.put(key, value);
    }

    /**
     * Adds an HTTP 1.1 Request parameter attribute/value pair to the set of
     * Request Parameters.
     * 
     * @param key
     *            [Required] A string representing a Request parameter attribute
     *            <em>name</em>.
     * @param value
     *            [Required] A string representing a Request parameter attribute
     *            <em>value</em>.
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
     *            [Required] A string that contains the contents of the POST
     *            Request's entity body.
     */
    public void setPostBody(String post_body) {
	body = post_body;
    }

    /**
     * Gets the entity body of an HTTP 1.1 POST Request message. Used for calls
     * to <strong>Verify.call</strong> and <strong>Verify.sms</strong>.
     * 
     * @return A string that contains the contents of the POST Request's entity
     *         body.
     */
    public String getPostBody() {
	return body;
    }

    /**
     * Returns the set of <em>TeleSign-specific</em> Request header fields.
     * 
     * @return A sorted key/value mapping that contains the TeleSign-specific
     *         Request header fields.
     */
    public Map<String, String> getTsHeaders() {
	return ts_headers;
    }

    /**
     * Returns the set of both the standard <u>and</u> the
     * <em>TeleSign-specific</em> Request header fields.
     * 
     * @return A sorted key/value mapping that contains all of the Request
     *         header fields.
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
     * @return A sorted key/value mapping that contains all of the POST Request
     *         parameters.
     */
    public Map<String, String> getAllParams() {
	return params;
    }

    /**
     * Creates and sends the REST request.
     * 
     * @return A string containing the TeleSign web server's Response.
     * @throws IOException
     *             A {@link java.security.SignatureException} signals that an
     *             error occurred while attempting to sign the Request.
     */
    public String executeRequest() throws IOException {
	String signingString = getSigningString(customer_id);
	String signature;
	String url_output = "";

	// Create the absolute form of the resource URI, and place it in a
	// string buffer.
	StringBuffer full_url = new StringBuffer(base).append(resource);

	if (params.size() > 0) {
	    full_url.append("?");
	    int i = 0; // The first parameter is always prefixed with a question
		       // mark.

	    for (String key : params.keySet()) {
		if (++i != 0) // Additonal parameters are always prefixed with
			      // an ampersands.
		    full_url.append("&");

		full_url.append(URLEncoder.encode(key, "UTF-8")).append("=")
			.append(URLEncoder.encode(params.get(key), "UTF-8"));
	    }
	}

	/*
	 * Convert the URL string into a URL object, to use later for creating a
	 * persistent connection for the session.
	 */
	url = new URL(full_url.toString());

	/*
	 * Create the Signature using the formula: Signature = Base64(
	 * HMAC-SHA1( YourTeleSignAPIKey, UTF-8-Encoding-Of( StringToSign ) )
	 */
	try {
	    signature = hmacsha1(signingString, secret_key);
	} catch (SignatureException e) {
	    System.err.println("Error signing request " + e.getMessage());

	    return null;
	}

	/*
	 * Create the Authorization header using the following formula:
	 * Authorization = "TSA" + " " + Customer ID + ":" + Signature.
	 */
	String auth_header = "TSA" + customer_id + ":" + signature;

	/*
	 * Create an HTTP Connection object to open a persistent connection to
	 * the URL, and begin a new session for the Request/Response
	 * transaction.
	 */
	connection = (HttpURLConnection) url.openConnection();
	connection.setConnectTimeout(30000);

	// Send the Request message headers to the web resource.
	connection.setRequestProperty("Authorization", auth_header); // Send the
								     // Authorization
								     // header.

	if (post) // This Request message contains an enitiy body, so
	    connection.setRequestProperty("Content-Length",
		    Integer.toString(body.length())); // Send the size of the
						      // message payload as
						      // the value of the
						      // Content-Length header.
	for (String key : ts_headers.keySet())
	    connection.setRequestProperty(key, ts_headers.get(key)); // Add the
								     // TeleSign-specific
								     // Request
								     // headers.

	for (String key : headers.keySet())
	    connection.setRequestProperty(key, headers.get(key)); // Add the
								  // generl
								  // Request
								  // headers.

	if (post) // This Request message contains an enitiy body, so
	{ // use the connection for outputting (writing a stream of)
	  // information,
	    connection.setDoOutput(true); // as opposed to false, for inputting.

	    DataOutputStream wr = new DataOutputStream(
		    connection.getOutputStream());

	    wr.writeBytes(body); // Write the entity body for the POST Request.
	    wr.flush();
	    wr.close();
	}

	// Sending Request now complete.

	// Retrieve the Status Code from the web server. The Status-Code is a
	// 3-digit integer that indicates the result
	int response = connection.getResponseCode(); // of the web server's
						     // attempt to understand
						     // and satisfy your
						     // request.

	// Retrieve the contents of the Response message from the TeleSign
	// server.
	BufferedReader in;

	try {
	    // Extract the the contents of the Response message with a stream
	    // object.
	    InputStream isr = (response == 200) ? connection.getInputStream()
		    : connection.getErrorStream(); // When successful, the value
						   // is "200".

	    in = new BufferedReader(new InputStreamReader(isr)); // Read the
								 // input stream
								 // into a
								 // buffer.

	    String urlReturn; // Transfer the contents of the buffer to string
			      // variable.

	    while ((urlReturn = in.readLine()) != null)
		// The response headers are separated using the new line
		// character ("\n").
		url_output += urlReturn; // Information returned in the response
					 // body is structured in JSON format.

	    in.close();
	} catch (IOException e) {
	    System.err.println("IOException while reading from input stream "
		    + e.getMessage());
	}

	return url_output;
    }

    /**
     * Create the <em>string to sign</em>. This is a <em>helper method</em>,
     * used interanlly by the
     * {@link com.telesign.util.TeleSignRequest#executeRequest()} method.
     * 
     * @param customer_id
     *            [Required] A string representing your TeleSign Customer ID.
     *            This represents your TeleSign account number.
     * @return A string representing the signing string used to create a
     *         Signature object, for the authenticate the REST request.
     */
    private String getSigningString(String customer_id) {
	// Create the stringToSign element of the Signature component.
	String stringToSign = post ? "POST\n" : "GET\n";
	String content_type = post ? "application/x-www-form-urlencoded\n"
		: "\n";

	// Concatonate the Request headers to the stringToSign.
	stringToSign += content_type;

	/*
	 * Add the Date header to both the set of Headers, /* and to the
	 * stringToSign (killing two birds with one stone).
	 */
	if (ts_date) // If you use the TeleSign-specific Date header,
	    stringToSign += "\n"; // then the standard Date header isn't
				  // required,
	else // so just insert an empty line into the StringToSign (as a
	     // placeholder).
	{
	    // Create a standard HTTP 1.1 Date header field.
	    Date now = new Date();

	    /*
	     * Convert the Date object to conform to the RFC 2616, Section 3.3.1
	     * format. For example : Tue, 10 Jan 2012 19:36:42 +0000.
	     */
	    SimpleDateFormat rfc2616 = new SimpleDateFormat(
		    "EEE, dd MMM YYYY HH:mm:ss ZZZZ");
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

	for (String key : orderedKeys)
	    // Add each of the X-TS- headers (name:value pairs) to the
	    // stringToSign,
	    stringToSign += key.toLowerCase() + ":" + ts_headers.get(key)
		    + "\n"; // each one separated by a newline.

	// Create the CanonicalizedPOSTVariables element.
	if (post)
	    stringToSign += body + "\n"; // Just concatonate the POST Request
					 // entity body "as-is" to the
					 // stringToSign.

	// Create the CanonicalizedResource element.
	stringToSign += resource; // Just concatonate the resource "as-is" to
				  // the stringToSign.

	return stringToSign;
    }

    /**
     * Creates the Signature component for the Authorization header field. Uses
     * your Secret Key to encode the stringToSign. This is a
     * <em>helper method</em>, used interanlly by the
     * {@link TeleSignRequest#executeRequest()} method.
     * 
     * @param data
     *            [Required] The stringToSign.
     * @param key
     *            [Required] Your TeleSign API Key. Also known as your Secret
     *            Key, and your Shared Cryptographic Key. It s a bese64-encoded
     *            string value.
     * @return A String containing the Base64-encoded hash-based message
     *         authentication code.
     * @throws java.security.SignatureException
     *             Failed to generate HMAC. IllegalArgumentException - if
     *             algorithm is null or key is null or empty.
     */
    private String hmacsha1(String data, String key)
	    throws java.security.SignatureException {
	String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	String result;

	byte[] decoded_key = Base64.decodeBase64(key); // Get the raw key value.

	try {
	    /*
	     * Construct a another secret key from the raw key, one that can be
	     * used with the specified cryptographic hash algorithm.
	     */
	    SecretKeySpec signingKey = new SecretKeySpec(decoded_key,
		    HMAC_SHA1_ALGORITHM);

	    // Create a MAC object, which implements the SHA-1 hash algorithm.
	    Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
	    mac.init(signingKey); // Use the new encryption key to for
				  // encrypting the stringToSign.

	    // Encrypt the stringToSign.
	    byte[] rawHmac = mac.doFinal(data.getBytes()); // This renders the
							   // encrypted for of
							   // the stringToSign
							   // as chunk of binary
							   // data.

	    // Encode the binary data into an ASCII string format - so it can be
	    // sent reliably over the Internet (a medium originally designed for
	    // transporting textual data).
	    result = new String(Base64.encodeBase64(rawHmac));

	} catch (Exception e) {
	    throw new SignatureException("Failed to generate HMAC : "
		    + e.getMessage());
	}

	return result;
    }
}
