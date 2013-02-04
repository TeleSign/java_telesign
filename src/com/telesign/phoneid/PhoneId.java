package com.telesign.phoneid;

import java.io.IOException;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.PhoneUtil;
import com.telesign.util.TeleSignRequest;



/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class PhoneId {
	
	/**
	 * Implementation of the TeleSign PhoneId api 
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/phoneid.html">https://portal.telesign.com/docs/content/phoneid.html</a>
	 * 
	 * Simple constructor for setting the customer id and secret_key
	 * @param customer_id the TeleSign customer id. 
	 * @param secret_key the TeleSign secret key 
	 */
	public PhoneId(String customer_id, String secret_key) {
		this.customer_id = customer_id;
		this.secret_key = secret_key;
	}
	
	/**
	 * Make a phoneid standard request to Telesign's public API. Requires the 
	 * phone number. The method creates a @see TeleSignRequest for the standard
	 * api, signs it using the standard SHA1 hash, and performs a GET request.
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/phoneid-standard.html">https://portal.telesign.com/docs/content/phoneid-live.html</a>
	 * 
	 * @param identifier a US phone number to make the standard request against.
	 * @return {@link com.telesign.phoneid.response.PhoneIdStandardResponse PhoneIdStandardResponse} The fully formed response object. If an error occured, the 
	 *         error array of the response will be set
	 */
	public PhoneIdStandardResponse standard(String identifier) {
		String result = null;
		try {
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + clean_phone, "GET", customer_id, secret_key);
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		return response;
	}
	
	/**
	 * Make a phoneid score request to TeleSign's public API. Requires the 
	 * phone number and a string representing the use case code. The method 
	 * creates a {@link com.telesign.util.TeleSignRequest TeleSignRequest} for the score api, signs it using 
	 * the standard SHA1 hash, and performs a GET request.
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/phoneid-score.html">https://portal.telesign.com/docs/content/phoneid-live.html</a>
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes>https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes</a>
	 * 
	 * @param identifier a US phone number to make the standard request against.
	 * @param ucid a TeleSign Use Case Code 
	 * @return {@link com.telesign.phoneid.response.PhoneIdScoreResponse PhoneIdScoreResponse} The fully formed response object. If an error occured, the 
	 *                                 error array of the response will be set
	 */
	public PhoneIdScoreResponse score(String identifier, String ucid) {
		String result = null;
		try {
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/score/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);	
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdScoreResponse response = gson.fromJson(result, PhoneIdScoreResponse.class);
		
		return response;
	}
	
	/**
	 * Make a phoneid contact request to TeleSign's public API. Requires the 
	 * phone number and a string representing the use case code. The method 
	 * creates a @see TeleSignRequest for the score api, signs it using 
	 * the standard SHA1 hash, and performs a GET request.
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/phoneid-contact.html">https://portal.telesign.com/docs/content/phoneid-live.html</a>
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes>https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes</a>
	 * 
	 * @param identifier a US phone number to make the standard request against.
	 * @param ucid a TeleSign Use Case Code 
	 * @return {@link com.telesign.phoneid.response.PhoneIdContactResponse PhoneIdContactResponse} The fully formed response object. If an error occured, the 
	 *                                 error array of the response will be set
	 */
	public PhoneIdContactResponse contact(String identifier, String ucid) {
		String result = null;
		try {
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/contact/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);	
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdContactResponse response = gson.fromJson(result, PhoneIdContactResponse.class);
		
		return response;
	}
	
	/**
	 * Make a phoneid live request to TeleSign's public API. Requires the 
	 * phone number and a string representing the use case code. The method 
	 * creates a @see TeleSignRequest for the score api, signs it using 
	 * the standard SHA1 hash, and performs a GET request.
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/phoneid-live.html">https://portal.telesign.com/docs/content/phoneid-live.html</a>
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes>https://portal.telesign.com/docs/content/xt/xt-use-case-codes.html#xref-use-case-codes</a>
	 * 
	 * @param identifier a US phone number to make the standard request against.
	 * @param ucid a TeleSign Use Case Code 
	 * @return {@link com.telesign.phoneid.response.PhoneIdLiveResponse PhoneIdLiveResponse} The fully formed response object. If an error occured, the 
	 *                                 error array of the response will be set
	 */
	public PhoneIdLiveResponse live(String identifier, String ucid) {
		String result = null;
		try {
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/live/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);	
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdLiveResponse response = gson.fromJson(result, PhoneIdLiveResponse.class);
		
		return response;
	}
	
	
	
	private final String customer_id;
    private final String secret_key;
}
