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
	public PhoneId(String customer_id, String secret_key) {
		this.customer_id = customer_id;
		this.secret_key = secret_key;
	}
	
	/**
	 * Make a phoneid request to Telesign's API. Requires the phone number, customer_id
	 * and secret_key
	 * 
	 * @param identifier - the phone number to use
	 * @return
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
