package com.telesign.verify;

import java.io.IOException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.telesign.util.TeleSignRequest;
import com.telesign.verify.response.VerifyResponse;


/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class Verify {
	
	/**
	 * Implementation of the TeleSign Verify api 
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/verify.html">https://portal.telesign.com/docs/content/verify.html</a>
	 * 
	 * 
	 * Simple constructor for setting the customer id and secret_key
	 * @param customer_id the TeleSign customer id. 
	 * @param secret_key the TeleSign secret key 
	 */
	public Verify(String customer_id, String secret_key) {
		this.customer_id = customer_id;
		this.secret_key = secret_key;
	}
	
	/**
	 * Convenience method an overload of {@link com.telesign.verify.Verify#sms(String, String, String, String)}
	 * 
	 * @param phone_number the phone number to send the sms message
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse sms(String phone_number) {
		return sms(phone_number, null, null, null);
	}
	
	/**
	 * Convenience method an overload of {@link com.telesign.verify.Verify#sms(String, String, String, String)}
	 * 
	 * @param phone_number the phone number to send the sms message
	 * @param language The String representation of the language to send the sms message
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse sms(String phone_number, String language) {
		return sms(phone_number, language, null, null);
	}
	
	/**
	 * Make a Verify SMS request to TeleSign's API. This method allows 
	 * the language, verification code and template of the message to be set. 
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/verify-sms.html">https://portal.telesign.com/docs/content/verify-sms.html</a>
	 * 
	 * @param phone_number the phone number to send the sms message
	 * @param language The String representation of the language to send the sms message
	 * @param verify_code The code to send via sms. Set to null to let Telesign generate the code
	 * @param template The template of the message that is being sent. Set to null for default, otherwise must include $$CODE$$ as a variable placeholder
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse sms(String phone_number, String language, String verify_code, String template) {
		String result = null;
		try {
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/sms", "POST", customer_id, secret_key);
			
			String body = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8");;
			
			if(language != null) {
				body += "&language=" + URLEncoder.encode(language, "UTF-8");;
			}
			
			if(verify_code != null) {
				body += "&verify_code=" + URLEncoder.encode(verify_code, "UTF-8");;
			}
			
			if(template != null) {
				body += "&template=" + URLEncoder.encode(template, "UTF-8");;
			}
			
			tr.setPostBody(body);
			
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
		
	}
	
	/**
	 * Convenience method for {@link com.telesign.verify.Verify#call(String, String, String, String, int, String, boolean)}
	 * 
	 * @param phone_number Required, the phone number of the person to dial
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse call(String phone_number) {
		return call(phone_number, null, null, null, 0, null, true);
	}
	
	/**
	 * Convenience method for {@link com.telesign.verify.Verify#call(String, String, String, String, int, String, boolean)}
	 * 
	 * @param phone_number Required, th phone number of the person to dial
	 * @param language optional can be null. The language code of the call 
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse call(String phone_number, String language) {
		return call(phone_number, language, null, null, 0, null, true);
	}
	
	/**
	 * Make a Verify API call to TeleSigns phone service. Calling this method 
	 * results in an automated phone call made to the given phone_number. The language
	 * is specified as a string. Extensions and delays are programmable using the
	 * extension_type and extension template. 
	 * 
	 * <p>
	 * <a href="https://portal.telesign.com/docs/content/verify-call.html">https://portal.telesign.com/docs/content/verify-call.html</a>
	 * 
	 * @param phone_number Required, the phone number of the person to dial
	 * @param language optional can be null. The language code of the call 
	 * @param verify_code optional. if null, verify code will be generated by telesign
	 * @param verify_method optional. Only available option is currently null (dictated code) or "keypress"
	 * @param extension_type optional. If 0, no extension. if 1, DTMF extension. If 2, voice extension
	 * @param extension_template optional. If null not used. Otherwise the extension to reach 
	 * @param redial optional. Default true
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 *         
	 * 
	 */
	public VerifyResponse call(String phone_number , String language, String verify_code, String verify_method, int extension_type, String extension_template, boolean redial) {
		
		String result = null;
		try {
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/call", "POST", customer_id, secret_key);
			
			String body = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8");;
			
			if(language != null) {
				body += "&language=" + URLEncoder.encode(language, "UTF-8");;
			}
			
			if(verify_code != null) {
				body += "&verify_code=" + URLEncoder.encode(verify_code, "UTF-8");;
			}
			
			if(verify_method != null && verify_method.equalsIgnoreCase("keypress")) {
				body += "&verify_method=" + URLEncoder.encode(verify_method, "UTF-8");;
			}
			if(extension_type > 0 && extension_type < 3) {
				body += "&extension_type=" + URLEncoder.encode(Integer.toString(extension_type), "UTF-8");;
			}
			if(extension_template != null) {
				body += "&extension_template=" + URLEncoder.encode(extension_template, "UTF-8");;
			}
			if(!redial) { //only set this if it is false
				body += "&redial=" + URLEncoder.encode(Boolean.toString(redial), "UTF-8");;
			}
			
			
			tr.setPostBody(body);
			
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
	
	/**
	 * Return the results of the post referenced by the reference_id. After a verify 
	 * SMS or Call has been made, the status of that verification request can be retrieved
	 * with this method. 
	 * 
	 * @param resource_id the id returned from either requestsSMS or requestCall
	 * @return {@link com.telesign.verify.response.VerifyResponse VerifyResponse} The fully formed
	 *         response object repersentation of the JSON reply
	 */
	public VerifyResponse status(String resource_id) {
		String result = null;
		try {
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/" + resource_id, "GET", customer_id, secret_key);
			result = tr.executeRequest();
		} catch (IOException e) {
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
	
	
	private final String customer_id;
	private final String secret_key;
}
