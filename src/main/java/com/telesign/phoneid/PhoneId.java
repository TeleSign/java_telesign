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
import com.telesign.phoneid.response.PhoneIdCallForwardResponse;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdNumberDeactivationResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdSimSwapCheckResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.TeleSignRequest;
import com.telesign.util.TeleSignRequest.RequestBuilder;

import java.io.IOException;

/**
 * The PhoneId class abstracts your interactions with the
 * <em>TeleSign PhoneID web service</em>. A PhoneId object encapsulates your
 * credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class PhoneId {

	private final String customer_id;
	private final String secret_key;
	private int connectTimeout;
	private int readTimeout;
	private String httpsProtocol;
	
	private String API_BASE_URL = "https://rest.telesign.com";
	private static final String BASE_URI_REST_ND_TELESIGN = "https://rest-nd.telesign.com";
	
	private static final String V1_PHONEID_STANDARD = "/v1/phoneid/standard/";
	private static final String V1_PHONEID_SCORE    = "/v1/phoneid/score/";
	private static final String V1_PHONEID_CONTACT  = "/v1/phoneid/contact/";
	private static final String V1_PHONEID_LIVE     = "/v1/phoneid/live/";	

	private static final String V1_PHONEID_SIM_SWAP_CHECK = "/v1/phoneid/sim_swap/check/";
	private static final String V1_PHONEID_CALL_FORWARD = "/v1/phoneid/call_forward/";
	private static final String V1_PHONEID_NUMBER_DEACTIVATION = "/v1/phoneid/number_deactivation/";
	
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
	 * @param httpsProtocol 
	 * 			  [Optional] Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2           
	 */
	public PhoneId(String customer_id, String secret_key, String httpsProtocol) {

		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.httpsProtocol = httpsProtocol;
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
	 * @param httpsProtocol [Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	public PhoneId(String customer_id, String secret_key, int connectTimeout, int readTimeout, String httpsProtocol) {

		this.customer_id = customer_id;
		this.secret_key = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.httpsProtocol = httpsProtocol;
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

		return standard(phone_number, null, null);
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

		return score(phone_number, ucid, null, null);
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

		return contact(phone_number, ucid, null, null);
	}

	/**
	 * Returns information about a specified phone number
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

		return live(phone_number, ucid, null, null);
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

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_STANDARD + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_STANDARD + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid standard API: "
					+ e.getMessage());
			throw new RuntimeException(e);
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

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_SCORE + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_SCORE + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid score API: "
					+ e.getMessage());
			throw new RuntimeException(e);
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

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_CONTACT + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_CONTACT + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);
			
			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid contact API: "
					+ e.getMessage());
			throw new RuntimeException(e);
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

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_LIVE + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_LIVE + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid live API: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		PhoneIdLiveResponse response = gson.fromJson(result,
				PhoneIdLiveResponse.class);

		return response;
	}

	/**
	 * @param phone_number
	 * @param ucid				[Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip	[Optional] The end users IP Address. This value must be in the format of IPv4 and IPv6 Addresses.
	 * @param session_id		[Optional] A free-form string (up to 64 ASCII characters) that indicates an ID (either raw or hashed) 
	 * 										that is unique to the users session with the customer. 
	 * 										The session_id links multiple calls and other TeleSign services that are associated with the current session.
	 * @return PhoneIdSimSwapCheckResponse
	 */
	public PhoneIdSimSwapCheckResponse simSwap(String phone_number, String ucid, String originating_ip, String session_id) {
		String result = null;

		try {

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_SIM_SWAP_CHECK + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_SIM_SWAP_CHECK + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid sim-swap API: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		PhoneIdSimSwapCheckResponse response = gson.fromJson(result,
				PhoneIdSimSwapCheckResponse.class);

		return response;
	}
	
	/**
	 * The PhoneID Call Forward web service provides call forwarding information for the specified mobile number.
	 * You can use it to find:
	 * <ul><li>Call Forwarding status</li><li>Forwarded to number</li></ul>
	 * @param phone_number
	 * @param ucid				[Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip	[Optional] The end users IP Address. This value must be in the format of IPv4 and IPv6 Addresses.
	 * @param session_id		[Optional] A free-form string (up to 64 ASCII characters) that indicates an ID (either raw or hashed) 
	 * 										that is unique to the users session with the customer. 
	 * 										The session_id links multiple calls and other TeleSign services that are associated with the current session.
	 * @return PhoneIdCallForwardResponse
	 */
	public PhoneIdCallForwardResponse callForward(String phone_number, String ucid, String originating_ip, String session_id) {
		String result = null;

		try {

			//TeleSignRequest tr = new TeleSignRequest(API_BASE_URL, V1_PHONEID_CALL_FORWARD + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(API_BASE_URL).subResource(V1_PHONEID_CALL_FORWARD + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid call forward API: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		PhoneIdCallForwardResponse response = gson.fromJson(result,
				PhoneIdCallForwardResponse.class);

		return response;
	}
	
	/**
	 * The <em>PhoneID Number Deactivation</em> web service provides status information for phone numbers that have been deactivated 
	 * due to various reasons. Reasons include, but are not limited to, contract termination, SIM card expiration, and so on.
	 * @param phone_number		[Required] A string representing the phone number you want information about.
	 * @param ucid				[Required] A string specifying one of the Use Case Codes.
	 * @param originating_ip	[Optional] The end users IP Address. This value must be in the format of IPv4 and IPv6 Addresses.
	 * @param session_id		[Optional] A free-form string (up to 64 ASCII characters) that indicates an ID (either raw or hashed) 
	 * 										that is unique to the users session with the customer. 
	 * 										The session_id links multiple calls and other TeleSign services that are associated with the current session.
	 * @return PhoneIdCallForwardResponse
	 */
	public PhoneIdNumberDeactivationResponse deactivation(String phone_number, String ucid, String originating_ip, String session_id) {
		String result = null;

		try {

			//TeleSignRequest tr = new TeleSignRequest(BASE_URI_REST_ND_TELESIGN, V1_PHONEID_NUMBER_DEACTIVATION + phone_number, "GET", customer_id, secret_key, connectTimeout, readTimeout, httpsProtocol);
			
			TeleSignRequest tr = new RequestBuilder(customer_id, secret_key).baseUrl(BASE_URI_REST_ND_TELESIGN).subResource(V1_PHONEID_NUMBER_DEACTIVATION + phone_number).
					httpsProtocol(httpsProtocol).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			tr.addParam("ucid", ucid);

			if(originating_ip != null) {

				tr.addParam("originating_ip", originating_ip);
			}
			
			if(session_id != null) {

				tr.addParam("session_id", session_id);
			}

			result = tr.executeRequest();
		} catch (IOException e) {

			System.err.println("IOException while executing phoneid number deactivation API: "
					+ e.getMessage());
			throw new RuntimeException(e);
		}

		PhoneIdNumberDeactivationResponse response = gson.fromJson(result,
				PhoneIdNumberDeactivationResponse.class);

		return response;
	}
	
}
