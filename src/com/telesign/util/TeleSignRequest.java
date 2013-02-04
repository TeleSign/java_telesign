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
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class TeleSignRequest {
	
	
	/**
	 * The TeleSignRequest is the base for which all the TeleSign API requests.
	 * This class handles both GET and POST requests and the different signing
	 * methods for each. If specialized access or parameters are needed for the 
	 * API over the defaults given in the @see PhoneId class or @see Verify class 
	 * a custom request can be made using this class.
	 * 
	 * @param base the base url of the request. This does not include parameters or the method
	 *             of the call. Usually this is https://rest.telesign.com
	 * @param resource the resource called off the base url. This is the extension off the base
	 *                 of the url. Example "/v1/phoneid/standard/1234567890"
	 * @param method the method of the request. one of GET or POST
	 * @param customer_id the TeleSign provided customer_id
	 * @param secret_key the TeleSign provided secret_key
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
	 * Add a header to this TeleSign Request. If the header starts with
	 * X-TS- it is added to a special list of TeleSign headers. This
	 * also checks for the presence of X-TS-Date to correctly set the
	 * date for the request whether the Specialize TeleSign Date header
	 * is set or if it should be generated at request time
	 * 
	 * @param key the name of the header
	 * @param value the value to set for the heaader.
	 */
	public void addHeader(String key, String value) {
		if((key.length() > 4) && (key.toLowerCase().substring(0, 5).equals("x-ts-"))) {
			
			//mark the date if it is found to not use normal date params
			if(key.toLowerCase().equals("x-ts-date")) {
				ts_date = true;
			}
			ts_headers.put(key, value);
		}
		headers.put(key, value);
	}
	
	/**
	 * Add a parameter to the query String. Holds the parameters in a map
	 * until it is time to sign them 
	 * 
	 * @param key the name of the parameter
	 * @param value the value of the parameter
	 */
	public void addParam(String key, String value) {
		params.put(key, value);
	}
	
	
	/**
	 * Set the body of the request for POST requests
	 * 
	 * @param post_body
	 */
	public void setPostBody(String post_body) {
		body = post_body;
	}
	
	/**
	 * Get the POST body for this request
	 * 
	 * @return the string value of the POST request
	 */
	public String getPostBody() {
		return body;
	}
	
	/**
	 * Return a sorted list of all the headers. 
	 * 
	 * @return a sorted map of all the headers for this request
	 */
	public Map<String, String> getAllHeaders() {
		TreeMap<String, String> tmp = new TreeMap<String, String>();
		tmp.putAll(headers);
		tmp.putAll(ts_headers);
		return tmp;
	}
	
	/**
	 * Return the TeleSign specific Headers set for this request
	 * 
	 * @return the map of the TeleSign specific headers. This map returns a sorted order
	 */
	public Map<String, String> getTsHeaders() {
		return ts_headers;
	}
	
	/**
	 * Return all the Query parameters set on this request. 
	 * 
	 * @return a map containing all the query parameters set for this request
	 */
	public Map<String, String> getAllParams() {
		return params;
	}
	
	/**
	 * Sign and make the TeleSign Request to the url that was set on this request.
	 * The signature is then calculated based on the headers, post parameters and 
	 * resource as described below.
	 * 
	 *	Authorization = "TSA" + " " + Customer ID + ":" + Signature
	 *
	 *	Signature = Base64( HMAC-SHA1( YourTeleSignAPIKey, UTF-8-Encoding-Of( StringToSign ) ) )
	 *
	 *	StringToSign = HTTP-method + "\n" +
	 *	   Content-Type + "\n" +
	 *	   Date + "\n" +
	 *	   CanonicalizedTsHeaders +
	 *	   CanonicalizedPOSTVariables +
	 *	   CanonicalizedResource
	 *
	 *
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/rest-auth.html">https://portal.telesign.com/docs/content/rest-auth.html</a>
	 * 
	 * @return A string containing the JSON string response that was returned from the html request
	 * @throws IOException
	 */
	public String executeRequest() throws IOException {
		String signingString = getSigningString(customer_id); 
		String signature;
		String url_output = "";
		
		/* construct the full url */
		StringBuffer full_url = new StringBuffer(base).append(resource);
		if(params.size() > 0) {
			full_url.append("?");
			int i =0; 
			for(String key : params.keySet()) {
				if(++i != 0) {  //don't add the ampersand on the first iteration
					full_url.append("&"); 
				}
				
				full_url.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
			}
		}
		
		url = new URL(full_url.toString());
		connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(30000);  
		
		
		try {
			signature = hmacsha1(signingString, secret_key);
		} catch (SignatureException e) {
			System.err.println("Error signing request " + e.getMessage());
			return null;
		}
		
		String auth_header = "TSA " + customer_id + ":" + signature; 
		
		connection.setRequestProperty("Authorization", auth_header);
		
		if(post) {
			connection.setRequestProperty("Content-Length", Integer.toString(body.length()));
		}
		
		for(String key : ts_headers.keySet()) {
			connection.setRequestProperty(key, ts_headers.get(key));
		}
		
		for(String key : headers.keySet()) {
			connection.setRequestProperty(key, headers.get(key));
		}
		
		if(post) {
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
		    wr.writeBytes(body);
		    wr.flush();
		    wr.close();
		}
		
		int response = connection.getResponseCode();
		
		BufferedReader in;
		try {
			InputStream  isr = (response == 200) ? connection.getInputStream() : connection.getErrorStream();
			in = new BufferedReader(new InputStreamReader(isr));
			String urlReturn;
	
			while ((urlReturn = in.readLine()) != null) {
			    url_output += urlReturn;
			}
			in.close();
		} catch (IOException e) {
			System.err.println("IOException while reading from input stream " + e.getMessage());
		}
		
		
		return url_output;
	}
	
	/**
	 * Only run when the request is executed
	 * 
	 * @param customer_id
	 * @return the string to sign for the request
	 */
	private String getSigningString(String customer_id) {
		String stringToSign = post ? "POST\n": "GET\n";
		String content_type = post ? "application/x-www-form-urlencoded\n" : "\n";
		
		/* set the content type */
		stringToSign += content_type;
		
		/* Handle the Date */
		if(ts_date) {
			stringToSign += "\n";
		} else {
			
			Date now = new Date();
			//Tue, 10 Jan 2012 19:36:42 +0000
			SimpleDateFormat rfc2616 = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss ZZZZ");
			String date = rfc2616.format(now);
			addHeader("Date", date);
			stringToSign += date + "\n";
		}
		
		//canonize the ts_headers 
		Set<String> orderedKeys = ts_headers.keySet();
		for(String key : orderedKeys) {
			stringToSign += key.toLowerCase() + ":" + ts_headers.get(key) + "\n";
		}
		
		if(post) {
			stringToSign += body + "\n";
		}
		
		stringToSign += resource;
		
		return stringToSign;
	}
	
	/**
	 * Perform a hmac sha1 hash of the passed string using the passed salt
	 * @param data The information to hash
	 * @param key the salt of the hash
	 * @return a hashed and base64 encoded representation of the data and the salt
	 * @throws java.security.SignatureException
	 */
	private String hmacsha1(String data, String key)  throws java.security.SignatureException {
		String HMAC_SHA1_ALGORITHM = "HmacSHA1";
		String result;
		
		byte[] decoded_key = Base64.decodeBase64(key);
		
		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(decoded_key, HMAC_SHA1_ALGORITHM);
			
			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			
			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());
			
			// base64-encode the hmac
			result =  new String(Base64.encodeBase64(rawHmac));
			
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		
		return result;
	}
	
	private TreeMap<String, String> ts_headers;
	private TreeMap<String, String> headers;
	
	private HashMap<String, String> params;

	private URL url;
	private HttpURLConnection connection;
	private boolean post;
	private String body = "";
	
	private final String base;
	private final String resource;
	private final String customer_id;
	private final String secret_key;
	
	private boolean ts_date = false;
}
