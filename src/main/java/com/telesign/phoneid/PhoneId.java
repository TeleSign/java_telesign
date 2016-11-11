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

import java.util.Map;

import com.google.gson.Gson;
import com.telesign.exception.TelesignAPIException;
import com.telesign.phoneid.response.PhoneIdCallForwardResponse;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdNumberDeactivationResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdSimSwapCheckResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;
import com.telesign.util.TeleSignRequest;
import com.telesign.util.TeleSignRequest.RequestBuilder;

/**
 * The PhoneId class abstracts your interactions with the
 * <em>TeleSign PhoneID web service</em>. A PhoneId object encapsulates your
 * credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class PhoneId {

	private final String customerId;
	private final String secretKey;
	private int connectTimeout;
	private int readTimeout;
	private String httpsProtocol;
	private String ciphers;
	//private boolean runTests;
	private String url; // Not used yet
	private Map<String, String> extra; 
	private String sessionId, originatingIp;
	private String ucid;
	
	private static final String API_BASE_URL = "https://rest.telesign.com";	
	
	private static final String V1_PHONEID_STANDARD = "/v1/phoneid/standard/";
	private static final String V1_PHONEID_SCORE    = "/v1/phoneid/score/";
	private static final String V1_PHONEID_CONTACT  = "/v1/phoneid/contact/";
	private static final String V1_PHONEID_LIVE     = "/v1/phoneid/live/";	

	private static final String V1_PHONEID_SIM_SWAP_CHECK = "/v1/phoneid/sim_swap/check/";
	private static final String V1_PHONEID_CALL_FORWARD = "/v1/phoneid/call_forward/";
	private static final String V1_PHONEID_NUMBER_DEACTIVATION = "/v1/phoneid/number_deactivation/";
	
	private final Gson gson = new Gson();
	
	public static class PhoneIdBuilder{
		
		private final String customerId;
		private final String secretKey;
		private int connectTimeout;
		private int readTimeout;		
		private String httpsProtocol;
		private String ciphers;
		private Map<String, String> extra;
		private String url = "https://rest.telesign.com";
		private String sessionId, originatingIp;
		private String ucid;
		
		/**
		 * The PhoneIdBuilder class constructor. Once you instantiate a PhoneId object, you
		 * can use it to make instance calls to <em>PhoneID Standard</em>,
		 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
		 * <em>PhoneID Live</em>.	 
		 * @param customerId [Required] A string representing your TeleSign Customer ID.
		 *            This represents your TeleSign account number.
		 * @param secretKey	 [Required] A string representing your TeleSign Secret Key
		 *            (available from the TeleSign Client Portal).
		 */
		public PhoneIdBuilder(String customerId, String secretKey){
			this.customerId = customerId;
			this.secretKey = secretKey;
		}
		/** 
		 * Comma separated string containing list of ciphers, Ex: 
		 * ciphers = "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_GCM_SHA256,
		 * TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_128_CBC_SHA256,
		 * TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_DSS_WITH_AES_128_CBC_SHA256".
		 * @param ciphers [optional] Please set the ciphers that you want to use. 
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder ciphers(String ciphers){
			this.ciphers = ciphers;
			return this;
		}
		/**
		 * Set TeleSign REST api url 
		 * @param url
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder url(String url){
			this.url = url;
			return this;
		}
		/** 
		 * Connection timeout is the timeout in making the initial connection; i.e. completing the TCP connection handshake.
		 * @param connectTimeout int value representing connection timeout
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder connectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}
		/** 
		 * The read timeout is the timeout on waiting to read data. 
		 * Specifically, if the server fails to send a byte [timeout] seconds after the last byte, a read timeout error will be raised.
		 * @param readTimeout int value representing connection read timeout
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder readTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}
		/** 
		 * @param httpsProtocol The httpsProtocol you want to use.
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder httpsProtocol(String httpsProtocol) {
			this.httpsProtocol = httpsProtocol;
			return this;
		}
		/** 
		 * @param sessionId [Optional] Your end users session id. Set it to "null" if not sending session id.
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder sessionId(String sessionId){
			this.sessionId = sessionId;
			return this;
		}
		/** 
		 * @param originatingIp [Optional] Your end users IP Address. This value must be in the format 
		 * defined by IETF in the Internet-Draft document titled Textual Representation of IPv4 and IPv6 
		 * Addresses. Ex: originatingIp=192.168.123.456. Set it to null if not sending originating ip. 
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder originatingIp(String originatingIp){
			this.originatingIp = originatingIp;
			return this;
		}
		/** 
		 * @param extra The Extra parameter you would like to send to Telesign to take advantage of unreleased features.
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder extra(Map<String, String> extra){
			this.extra = extra;
			return this;
		}
		/** 
		 * @param ucid [Required] A string the specifies one of the 
		 * <a href="http://docs.telesign.com/rest/content/xt/xt-use-case-codes.html#xref-use-case-codes">Use Case Codes</a>.
		 * @return PhoneIdBuilder A {@link com.telesign.phoneid.PhoneId.PhoneIdBuilder} object.
		 */
		public PhoneIdBuilder ucid(String ucid){
			this.ucid = ucid;
			return this;
		}
		/** 
		 * @return PhoneId A {@link com.telesign.phoneid.PhoneId} object.
		 */
		public PhoneId create(){
			PhoneId phoneIdObj = new PhoneId(this); 
			return phoneIdObj;
		}
		
	}

	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customerId
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secretKey
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 */
	@Deprecated
	public PhoneId(String customerId, String secretKey) {

		this.customerId = customerId;
		this.secretKey = secretKey;
	}
	
	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customerId
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secretKey
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 * @param connectTimeout 
	 * 			[Required] A integer representing connection timeout value while connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout value while reading response returned from Telesign api.
	 */
	@Deprecated
	public PhoneId(String customerId, String secretKey, int connectTimeout, int readTimeout) {

		this.customerId = customerId;
		this.secretKey = secretKey;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customerId
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secretKey
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 * @param httpsProtocol 
	 * 			  [Optional] Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2           
	 */
	@Deprecated
	public PhoneId(String customerId, String secretKey, String httpsProtocol) {

		this.customerId = customerId;
		this.secretKey = secretKey;
		this.httpsProtocol = httpsProtocol;
	}
	
	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>.
	 * 
	 * @param customerId
	 *            [Required] A string representing your TeleSign Customer ID.
	 *            This represents your TeleSign account number.
	 * @param secretKey
	 *            [Required] A string representing your TeleSign Secret Key
	 *            (available from the TeleSign Client Portal).
	 * @param connectTimeout 
	 * 			[Required] A integer representing connection timeout value while connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout value while reading response returned from Telesign api.
	 * @param httpsProtocol [Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	@Deprecated
	public PhoneId(String customerId, String secretKey, int connectTimeout, int readTimeout, String httpsProtocol) {

		this.customerId = customerId;
		this.secretKey = secretKey;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.httpsProtocol = httpsProtocol;
	}
	/**
	 * The PhoneId class constructor. Once you instantiate a PhoneId object, you
	 * can use it to make instance calls to <em>PhoneID Standard</em>,
	 * <em>PhoneID Score</em>, <em>PhoneID Contact</em>, and
	 * <em>PhoneID Live</em>, <em>simSwap</>, <em>CallForward</em> & <em>Deactivation</em>.
	 * @param builder
	 */
	public PhoneId(PhoneIdBuilder builder) {
		this.customerId = builder.customerId;
		this.secretKey = builder.secretKey;
		this.connectTimeout = builder.connectTimeout;
		this.httpsProtocol = builder.httpsProtocol;
		this.readTimeout = builder.readTimeout;
		this.url = builder.url;
		this.extra = builder.extra;
		this.originatingIp = builder.originatingIp;
		this.sessionId = builder.sessionId;
		this.ucid = builder.ucid;
		this.ciphers = builder.ciphers;
	}

	/**
	 * Returns information about a specified phone numberï¿½s type, numbering
	 * structure, cleansing details, and location details.
	 * 
	 * @param phoneNo
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @return A {@link com.telesign.phoneid.response.PhoneIdStandardResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdStandardResponse standard(String phoneNo) {

		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).
					subResource(V1_PHONEID_STANDARD + phoneNo).httpsProtocol(httpsProtocol).ciphers(ciphers).
					httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);
			
			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {			
			throw new TelesignAPIException("Exception occured while executing phoneid standard API",e);
		}

		PhoneIdStandardResponse response = gson.fromJson(result,
				PhoneIdStandardResponse.class);

		return response;
	}

	/**
	 * Returns risk information about a specified phone number, including a
	 * real-time risk score, threat level, and recommendation for action.
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdScoreResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdScoreResponse score(String phoneNo) {

		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_SCORE + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);

			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {			
			throw new TelesignAPIException("Exception while executing phoneid score API",e);
		}

		PhoneIdScoreResponse response = gson.fromJson(result,
				PhoneIdScoreResponse.class);

		return response;
	}

	/**
	 * Returns contact details for a specified phone numberï¿½s subscriber. This
	 * includes the subscriber's First Name, Last Name, Street Address, City,
	 * State (or Province), Country, and ZIP (Postal) Code.
	 * 
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @param ucid
	 *            [Required] A string specifying one of the Use Case Codes.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdContactResponse contact(String phoneNo) {

		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_CONTACT + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);
			
			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {			
			throw new TelesignAPIException("Exception while executing phoneid contact API", e);
		}

		PhoneIdContactResponse response = gson.fromJson(result,
				PhoneIdContactResponse.class);

		return response;
	}

	/**
	 * Returns information about a specified phone number
	 * <em>state of operation</em>. You can use it to find out if:
	 * <ul>
	 * <li>the line is in service,</li>
	 * <li>the number is reachable,</li>
	 * <li>the mobile phone is roaming, and if so, in which country.</li>
	 * </ul>
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo
	 *            [Required] A string representing the phone number you want
	 *            information about.
	 * @return A {@link com.telesign.phoneid.response.PhoneIdContactResponse}
	 *         object, which contains the JSON-formatted response body from the
	 *         TeleSign server.
	 */
	public PhoneIdLiveResponse live(String phoneNo) {

		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_LIVE + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);

			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException("Exception while executing phoneid live API", e);
		}

		PhoneIdLiveResponse response = gson.fromJson(result,
				PhoneIdLiveResponse.class);

		return response;
	}

	/**
	 * This service provides data about potential sim_swaps associated with the specified phone number. 
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo 
	 * @return PhoneIdSimSwapCheckResponse
	 */
	public PhoneIdSimSwapCheckResponse simSwap(String phoneNo) {
		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_SIM_SWAP_CHECK + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);

			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException("Exception while executing phoneid sim-swap API", e);
		}

		PhoneIdSimSwapCheckResponse response = gson.fromJson(result,
				PhoneIdSimSwapCheckResponse.class);

		return response;
	}
	
	/**
	 * The PhoneID Call Forward web service provides call forwarding information for the specified mobile number.
	 * You can use it to find:
	 * <ul><li>Call Forwarding status</li><li>Forwarded to number</li></ul>
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo
	 * @return PhoneIdCallForwardResponse
	 */
	public PhoneIdCallForwardResponse callForward(String phoneNo) {
		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_CALL_FORWARD + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);

			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException("Exception while executing phoneid call forward API", e);
		}

		PhoneIdCallForwardResponse response = gson.fromJson(result,
				PhoneIdCallForwardResponse.class);

		return response;
	}
	
	/**
	 * The <em>PhoneID Number Deactivation</em> web service provides status information for phone numbers that have been deactivated 
	 * due to various reasons. Reasons include, but are not limited to, contract termination, SIM card expiration, and so on.
	 * Additionally following parameters are applicable: @param ucid, @param originatingIp, @param sessionId, @param extra
	 * @param phoneNo		[Required] A string representing the phone number you want information about.	 
	 * @return PhoneIdCallForwardResponse
	 */
	public PhoneIdNumberDeactivationResponse deactivation(String phoneNo) {
		String result = null;

		try {
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_PHONEID_NUMBER_DEACTIVATION + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			if(null != ucid)
				tr.addParam("ucid", ucid);
			
			if(null != extra)
				extraParams(tr);

			if(originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException("Exception while executing phoneid number deactivation API", e);
		}

		PhoneIdNumberDeactivationResponse response = gson.fromJson(result,
				PhoneIdNumberDeactivationResponse.class);

		return response;
	}
	
	/**
	 * Parses extra params and adds to TeleSignRequest
	 * @param tr TeleSignRequest
	 */
	private void extraParams(TeleSignRequest tr) {
		for(Map.Entry<String, String> extraParam:extra.entrySet())
			tr.addParam(extraParam.getKey(), extraParam.getValue());
	}

	public static PhoneIdBuilder initPhoneId(String customerId, String secretKey){
		return new PhoneIdBuilder(customerId, secretKey);
	}	
}
