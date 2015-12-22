/**
 * @version     1.0
 * @copyright   Copyright © 2013, TeleSign Corporation.
 * @license     http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author      John Weatherford
 * @maintainer  Humberto Morales
 * @repository  https://github.com/TeleSign/java_telesign
 * @support     support@telesign.com
 */
package com.telesign.phoneid;

import com.google.gson.Gson;
import com.telesign.GlobalConstant;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.TeleSignRequest;
import java.io.IOException;

/**
 * The PhoneId class abstracts your interactions with the
 * <em>TeleSign PhoneID web service</em>. A PhoneId object encapsulates your
 * credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class PhoneId {

	private final String customer_id;
	private final String secret_key;
	private int connectTimeout = 30000;
	private int readTimeout = 30000;
	
	private final Gson gson = new Gson();

	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customer_id
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secret_key
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 */
	public PhoneId(String customer_id, String secret_key) {

		this.customer_id = customer_id;
		this.secret_key = secret_key;
	}
	
	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customer_id
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secret_key
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 * @param connectTimeout 
	 * 			[Required] A integer representing connection timeout value while connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout value while reading response returned from Telesign api.
	 */
	public PhoneId(String customer_id, String secret_key, int connectTimeout, int readTimeout) {

		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	/**
	 * Returns information about a specified phone number�s type, numbering
	 * structure, cleansing details, and location details.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdStandardResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdStandardResponse standard(String phone_number) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/standard/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdStandardResponse response = gson.fromJson(result,
				PhoneIdStandardResponse.class);

		return response;
	}

	/**
	 * Returns risk information about a specified phone number, including a
	 * real-time risk score, threat level, and recommendation for action.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdScoreResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdScoreResponse score(String phone_number, String ucid) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/score/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);
			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdScoreResponse response = gson.fromJson(result,
				PhoneIdScoreResponse.class);

		return response;
	}

	/**
	 * Returns contact details for a specified phone number�s subscriber. This
	 * includes the subscriber's First Name, Last Name, Street Address, City,
	 * State (or Province), Country, and ZIP (Postal) Code.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdContactResponse contact(String phone_number, String ucid) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/contact/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdContactResponse response = gson.fromJson(result,
				PhoneIdContactResponse.class);

		return response;
	}

	/**
	 * Returns information about a specified phone number�s
	 * <em>state of operation</em>. You can use it to find out if:
	 * <ul>
	 * <li>the line is in service,</li>
	 * <li>the number is reachable,</li>
	 * <li>the mobile phone is roaming, and if so, in which country.</li>
	 * </ul>
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdLiveResponse live(String phone_number, String ucid) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/live/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdLiveResponse response = gson.fromJson(result,
				PhoneIdLiveResponse.class);

		return response;
	}
		
	/**
	 * Returns information about a specified phone number�s type, numbering
	 * structure, cleansing details, and location details.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param originating_ip [Optional] Your end users IP Address. This value must be in the format defined by IETF in the 
	 * 								   Internet-Draft document titled Textual Representation of IPv4 and IPv6 Addresses. Ex: originating_ip=192.168.123.456.
	 * 								   Set it to null if not sending originating ip.
	 * @param session_id	[Optional] Your end users session id. Set it to "null" if not sending session id.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdStandardResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdStandardResponse standard(String phone_number, String originating_ip, String session_id) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/standard/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdStandardResponse response = gson.fromJson(result,
				PhoneIdStandardResponse.class);

		return response;
	}

	/**
	 * Returns risk information about a specified phone number, including a
	 * real-time risk score, threat level, and recommendation for action.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip [Optional] Your end users IP Address. This value must be in the format defined by IETF in the 
	 * 								   Internet-Draft document titled Textual Representation of IPv4 and IPv6 Addresses. Ex: originating_ip=192.168.123.456.
	 * 								   Set it to null if not sending originating ip.
	 * @param session_id	[Optional] Your end users session id. Set it to "null" if not sending session id.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdScoreResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdScoreResponse score(String phone_number, String ucid, String originating_ip, String session_id) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/score/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdScoreResponse response = gson.fromJson(result,
				PhoneIdScoreResponse.class);

		return response;
	}

	/**
	 * Returns contact details for a specified phone number�s subscriber. This
	 * includes the subscriber's First Name, Last Name, Street Address, City,
	 * State (or Province), Country, and ZIP (Postal) Code.
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip [Optional] Your end users IP Address. This value must be in the format defined by IETF in the 
	 * 								   Internet-Draft document titled Textual Representation of IPv4 and IPv6 Addresses. Ex: originating_ip=192.168.123.456.
	 * 								   Set it to null if not sending originating ip.
	 * @param session_id	[Optional] Your end users session id. Set it to "null" if not sending session id.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdContactResponse contact(String phone_number, String ucid, String originating_ip, String session_id) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/contact/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);
			
			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdContactResponse response = gson.fromJson(result,
				PhoneIdContactResponse.class);

		return response;
	}

	/**
	 * Returns information about a specified phone number�s
	 * <em>state of operation</em>. You can use it to find out if:
	 * <ul>
	 * <li>the line is in service,</li>
	 * <li>the number is reachable,</li>
	 * <li>the mobile phone is roaming, and if so, in which country.</li>
	 * </ul>
	 * 
	 * @param phone_number
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip [Optional] Your end users IP Address. This value must be in the format defined by IETF in the 
	 * 								   Internet-Draft document titled Textual Representation of IPv4 and IPv6 Addresses. Ex: originating_ip=192.168.123.456.
	 * 								   Set it to null if not sending originating ip.
	 * @param session_id	[Optional] Your end users session id. Set it to "null" if not sending session id.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdLiveResponse live(String phone_number, String ucid, String originating_ip, String session_id) {

		String result = null;

		try {

			TeleSignRequest tr = new TeleSignRequest(
					GlobalConstant.REST_TELESIGN_URL, "/v1/phoneid/live/"
							+ phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout);
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid API: "
					+ e.getMessage());
		}

		PhoneIdLiveResponse response = gson.fromJson(result,
				PhoneIdLiveResponse.class);

		return response;
	}
}
