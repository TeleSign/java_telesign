/**
 * @version     1.0
 * @copyright   Copyright © 2013, TeleSign Corporation.
 * @license     http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author      John Weatherford
 * @maintainer  Humberto Morales
 * @repository  https://github.com/TeleSign/java_telesign
 * @support     support@telesign.com
 */
package com.telesign.verify;

import com.google.gson.Gson;
import com.telesign.util.TeleSignRequest;
import com.telesign.verify.response.VerifyResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 *  The Verify class abstracts your interactions with the <em>TeleSign Verify web service</em>.
 *  A Verify object encapsulates your credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class Verify {

	private final String customer_id;
	private final String secret_key;

	/**
	 * The Verify class constructor.
	 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
	 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
	 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
	 */
	public Verify(String customer_id, String secret_key) {

		this.customer_id = customer_id;
		this.secret_key = secret_key;
	}

	/**
	 * Delivers a verification code to the end user by sending it in a text message.
	 * This is the simplest of the three overloads of this method. This overload takes the only required paramter�the end user's phone number. 
	 * @param phone_number	[Required] A string containing the user's phone number.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse sms(String phone_number) {

		return sms(phone_number, null, null, null);
	}

	/**
	 * Delivers a verification code to the end user by sending it in a text message.
	 * Use this overload when the user's native written language is not the default language (English). You specify the user's language in the <em>language</em> parameter.
	 * @param phone_number	[Required]	A string containing the user�s phone number.
	 * @param language		[Optional]	A string containing the IETF language tag. For example, "fr-CA". Set this value to "null" to use English (the default). This value is used in applying predefined text message templates.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse sms(String phone_number, String language) {

		return sms(phone_number, language, null, null);
	}

	/**
	 * Delivers a verification code to the end user by sending it in a text message.
	 * Use this overload when:
	 * <ul>
	 * 	<li>the end user's native written language is not the default language (English), or</li>
	 * 	<li>when you want to send the user a verification code that you create, or</li>
	 * 	<li>when you want to apply a custom text message template.</li>
	 * </ul>
	 * 
	 * @param phone_number	[Required]	A string containing the user�s phone number.
	 * @param language		[Optional]	A string containing the IETF language tag. For example, "fr-CA". Set this value to "null" to use English (the default). This value is used in applying predefined text message templates.
	 * @param verify_code	[Optional]	A string containing the verification code that you want to send to the end user. When you set this value to "null", TeleSign automatically generates the verification code (the default behavior).
	 * @param template		[Optional]	A string containing a text message to override the predefined text message template. Your text message must incorporate a $$CODE$$ placeholder to integrate the verify_code field. Set this value to null (the default) to use the predefined template.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse sms(String phone_number, String language, String verify_code, String template) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/sms", "POST", customer_id, secret_key);
			String body = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8");
			
			if(language != null) {

				body += "&language=" + URLEncoder.encode(language, "UTF-8");
			}
			
			if(verify_code != null) {

				body += "&verify_code=" + URLEncoder.encode(verify_code, "UTF-8");
			}
			
			if(template != null) {

				body += "&template=" + URLEncoder.encode(template, "UTF-8");
			}
			
			tr.setPostBody(body);
			
			result = tr.executeRequest();
		}
		catch (IOException e) {

			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
	
	/**
	 * Delivers a verification code to the end user with a phone call. When the user answers their phone, the TeleSign server plays an automated voice message that contains the code.
	 * This is the simplest of the three overloads of this method. This overload takes the only required parameter�the end user's phone number.
	 * @param phone_number	[Required]	A string containing the user�s phone number.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse call(String phone_number) {

		return call(phone_number, null, null, null, 0, null, true);
	}
	
	/**
	 * Delivers a verification code to the end user with a phone call. When the user answers their phone, the TeleSign server plays an automated voice message that contains the code.
	 * Use this overload when the user's native spoken language is not the default language (English). You specify the user's language in the <em>language</em> parameter.
	 * @param phone_number	[Required] A string containing the user�s phone number.
	 * @param language		[Optional]	A string containing the IETF language tag. For example, "fr-CA". Set this value to "null" to use English (the default).
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse call(String phone_number, String language) {

		return call(phone_number, language, null, null, 0, null, true);
	}
	
	/**
	 * Delivers a verification code to the end user - with a phone call. When the user answers their phone, the TeleSign server plays an automated voice message that contains the code.
	 * Use this overload when:
	 * <ul>
	 * 	<li>the end user's spoken language is not the default language (English), or</li>
	 * 	<li>when you want to send them a verification code that you create, or </li>
	 * 	<li>when you need to specify a method for handling automated interactions with a PBX.</li>
	 * </ul>
	 * 
	 * @param phone_number			[Required]	A string containing the user�s phone number.
	 * @param language				[Optional]	A string containing the IETF language tag. For example, "fr-CA". Set this value to "null" to use English (the default).
	 * @param verify_code			[Optional]	A string containing the verification code that you want to send to the end user. When you set this value to "null", TeleSign automatically generates the verification code (the default behavior).
	 * @param verify_method			[Optional]	A string containing the input method you want the end user to use when returning the verification code. Use a value of "keypress" when you want the user to use their phone to dial the code. Set this value to null when you want the user to enter the code into your web aplication (the default). 
	 * @param extension_type		[Optional]	An Integer value representing the type of response to use when dialing into a Private Branch Exchange (PBX). Use a value of 1 to have TeleSign use Dual-Tone Multi-Frequency (DTMF) tones to dail the user's extension. Use a value of 2 to have TeleSign use voice automation to request the user's extension. Use a value of 0 (the default) when the user isn't behind a PBX. 
	 * @param extension_template	[Optional]	A numerical string specifying the user's PBX extension number. Since this value is used in the call string, you can include one second pauses by adding commas before the extension number.  Set this value to null (the default) if not used. 
	 * @param redial				[Optional]	A boolean value that enables/disables redialing. Set this value to "true" (the default) when you want TeleSign to re-attempt the call after a failed attempt. Set this value to "false" when you don't.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse call(String phone_number , String language, String verify_code, String verify_method, int extension_type, String extension_template, boolean redial) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/call", "POST", customer_id, secret_key);
			String body = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8");
			
			if(language != null) {

				body += "&language=" + URLEncoder.encode(language, "UTF-8");
			}
			
			if(verify_code != null) {

				body += "&verify_code=" + URLEncoder.encode(verify_code, "UTF-8");
			}
			
			if(verify_method != null && verify_method.equalsIgnoreCase("keypress")) {

				body += "&verify_method=" + URLEncoder.encode(verify_method, "UTF-8");
			}

			if(extension_type > 0 && extension_type < 3) {

				body += "&extension_type=" + URLEncoder.encode(Integer.toString(extension_type), "UTF-8");
			}
			if(extension_template != null) {

				body += "&extension_template=" + URLEncoder.encode(extension_template, "UTF-8");
			}

			if(!redial) {

				body += "&redial=" + URLEncoder.encode(Boolean.toString(redial), "UTF-8");
			}
			
			tr.setPostBody(body);
			result = tr.executeRequest();
		}
		catch (IOException e) {

			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
	
	/**
	 * Requests the verification result from TeleSign.
	 * After sending an end user a verification code, wait a minute or two to allow them to receive it and then respond, and then call this method to find out if the end user passed the code challenge.
	 * This method takes only one parameter�the ID of this particular web service transaction.
	 * @param resource_id	[Required]	The string returned in the Response Message that TeleSign sends upon receipt of your HTTP 1.1 Request Message - for either {@link com.telesign.verify#sms()} or {@link com.telesign.verify#call()}.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse status(String resource_id) {

		String result = null;
		
		try {

			TeleSignRequest tr = new TeleSignRequest("https://rest.telesign.com", "/v1/verify/" + resource_id, "GET", customer_id, secret_key);
			result = tr.executeRequest();
		}
		catch (IOException e) {

			System.err.println("IOException while executing phoneid API: " + e.getMessage());
		}
		
		Gson gson = new Gson();
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
}
