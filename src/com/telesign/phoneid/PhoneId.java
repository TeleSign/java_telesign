/**
 * @version		1.0
 * @copyright		Copyright © 2013, TeleSign Corporation.
 * @license		http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author		J. Weatherford
 * @maintainer		Humberto Morales
 * @repository		https://github.com/TeleSign/java_telesign
 * @support		support@telesign.com
 */
package com.telesign.phoneid;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.PhoneUtil;
import com.telesign.util.TeleSignRequest;
import java.io.IOException;

/**
 *  The PhoneId class abstracts your interactions with the <em>TeleSign PhoneID web service</em>.
 *  A PhoneId object encapsulates your credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class PhoneId
{
	/**
	 * The PhoneId class constructor.
	 * Once you instantiate a PhoneId object, you can use it to make instance calls to <em>PhoneID Standard</em>, <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and <em>PhoneID Live</em>.
	 * @param customer_id	[Required] A string representing your TeleSign Customer ID. This represents your TeleSign account number.
	 * @param secret_key	[Required] A string representing your TeleSign Secret Key (available from the TeleSign Client Portal).
	 */
    	public PhoneId(String customer_id, String secret_key)
	{
	    this.customer_id = customer_id;
	    this.secret_key = secret_key;
	}
	
	/**
	 * Returns information about a specified phone number’s type, numbering structure, cleansing details, and location details. 
	 * @param identifier	[Required] A string representing the phone number you want information about.
	 * @return		A {@link com.telesign.phoneid.response.PhoneIdStandardResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public PhoneIdStandardResponse standard(String identifier)
	{
		String result = null;
		
		try
		{
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/standard/" + clean_phone, "GET", customer_id, secret_key);
			result = tr.executeRequest();
		}
		catch (IOException e)
		{
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdStandardResponse response = gson.fromJson(result, PhoneIdStandardResponse.class);
		
		return response;
	}
	
	/**
	 * Returns risk information about a specified phone number, including a real-time risk score, threat level, and recommendation for action. 
	 * @param identifier	[Required] A string representing the phone number you want information about.
	 * @param ucid		[Required] A string specifying one of the Use Case Codes.
	 * @return		A {@link com.telesign.phoneid.response.PhoneIdScoreResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public PhoneIdScoreResponse score(String identifier, String ucid)
	{
		String result = null;
		
		try
		{
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/score/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);	
			result = tr.executeRequest();
		}
		catch (IOException e)
		{
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdScoreResponse response = gson.fromJson(result, PhoneIdScoreResponse.class);
		
		return response;
	}
	
	/**
	 * Returns contact details for a specified phone number’s subscriber. This includes the subscriber's First Name, Last Name, Street Address, City, State (or Province), Country, and ZIP (Postal) Code.
	 * @param identifier	[Required] A string representing the phone number you want information about.
	 * @param ucid		[Required] A string specifying one of the Use Case Codes.
	 * @return		A {@link com.telesign.phoneid.response.PhoneIdContactResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public PhoneIdContactResponse contact(String identifier, String ucid)
	{
		String result = null;
		
		try
		{
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/contact/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);
			
			result = tr.executeRequest();			
		}
		catch (IOException e)
		{
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdContactResponse response = gson.fromJson(result, PhoneIdContactResponse.class);
		
		return response;
	}
	
	/**
	 *  Returns information about a specified phone number’s <em>state of operation</em>.
	 *  You can use it to find out 
	 *  if the line is in service, 
	 *  if the number is reachable, 
	 *  if the mobile phone is roaming, and if so, in which country.
	 * @param identifier	[Required] A string representing the phone number you want information about.
	 * @param ucid		[Required] A string specifying one of the Use Case Codes.
	 * @return		A {@link com.telesign.phoneid.response.PhoneIdContactResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public PhoneIdLiveResponse live(String identifier, String ucid)
	{
		String result = null;
		
		try
		{
			String clean_phone = PhoneUtil.formatTo11Digits(identifier);
			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/phoneid/live/" + clean_phone, "GET", customer_id, secret_key);
			tr.addParam("ucid", ucid);	
			result = tr.executeRequest();
		}
		catch (IOException e)
		{
			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		PhoneIdLiveResponse response = gson.fromJson(result, PhoneIdLiveResponse.class);
		
		return response;
	}
	
	private final String customer_id;
    private final String secret_key;
}
