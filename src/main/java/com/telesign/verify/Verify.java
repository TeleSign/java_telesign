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

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.telesign.exception.TelesignAPIException;
import com.telesign.util.IpValidator;
import com.telesign.util.TeleSignRequest;
import com.telesign.util.TeleSignRequest.RequestBuilder;
import com.telesign.verify.response.VerifyResponse;

/**
 *  The Verify class abstracts your interactions with the <em>TeleSign Verify web service</em>.
 *  A Verify object encapsulates your credentials (your TeleSign <em>Customer ID</em> and <em>Secret Key</em>).
 */
public class Verify {

	private final String customerId;
	private final String secretKey;
	private int connectTimeout;
	private int readTimeout;
	private String httpsProtocol;
	private String ciphers;
	private String url; // Not used yet
	private String mobileUrl;
	
	private String language, template, verifyCode;
	private String sessionId, originatingIp;
	private Map<String, String> extra;
	// Verify call specific
	private String verifyMethod, extensionTemplate;
	private int extensionType;
	private String callForwardAction, ttsMessage, smsMessage, pushMessage, ucid;
	private boolean redial;
	// for smartverify
	private String callerId, preference, ignoreRisk;
	// for softtoken
	private String softTokenId, bundleId;
	// for push API
	private String notificationType, notificationValue;

	private static final String API_BASE_URL   = "https://rest.telesign.com";
	private static final String API_MOBILE_URL = "https://rest-mobile.telesign.com";
	
	private static final String V1_VERIFY       = "/v1/verify/";
	private static final String V1_VERIFY_SMS   = "/v1/verify/sms";
	private static final String V1_VERIFY_CALL  = "/v1/verify/call"; 
	private static final String V1_VERIFY_SMART = "/v1/verify/smart";
	
	private static final String V2_VERIFY_PUSH         = "/v2/verify/push";
	private static final String V2_VERIFY_TOKEN        = "/v2/verify/soft_token";
	private static final String V2_VERIFY_REGISTRATION = "/v2/verify/registration/";
	
	private final Gson gson = new Gson();
	

	public Verify(VerifyBuilder builder) {
		this.customerId = builder.customerId;
		this.secretKey = builder.secretKey;
		this.connectTimeout = builder.connectTimeout;
		this.readTimeout = builder.readTimeout;
		this.httpsProtocol = builder.httpsProtocol;
		this.ciphers = builder.ciphers;
		this.url = builder.url;
		this.mobileUrl = builder.mobileUrl;
		this.language = builder.language;
		this.originatingIp = builder.originatingIp;
		this.sessionId = builder.sessionId;
		this.template = builder.template; 
		this.verifyCode = builder.verifyCode;
		this.verifyMethod = builder.verifyMethod;
		this.extensionType = builder.extensionType;
		this.extensionTemplate = builder.extensionTemplate;
		this.callForwardAction = builder.callForwardAction;
		this.ttsMessage = builder.ttsMessage;
		this.smsMessage = builder.smsMessage;
		this.pushMessage = builder.pushMessage;  
		this.redial = builder.redial;
		this.callerId = builder.callerId;
		this.preference = builder.preference;
		this.ignoreRisk = builder.ignoreRisk;
		this.ucid = builder.ucid;
		this.softTokenId = builder.softTokenId; 
		this.bundleId = builder.bundleId;
		this.notificationType = builder.notificationType;
		this.notificationValue = builder.notificationValue;
		this.extra = builder.extra;
	}
	
	public static class VerifyBuilder{
		private final String customerId;
		private final String secretKey;
		private int connectTimeout;
		private int readTimeout;
		private String httpsProtocol;
		private String ciphers;		
		private String url = "https://rest.telesign.com";
		private String mobileUrl = "https://rest-mobile.telesign.com";
		private String sessionId, originatingIp; 
		private String language, template, verifyCode;
		private String verifyMethod, extensionTemplate;
		private int extensionType;
		private String callForwardAction, ttsMessage, smsMessage, pushMessage,  ucid;
		private boolean redial = true;
		private Map<String, String> extra;
		private String callerId, preference, ignoreRisk;
		private String softTokenId, bundleId;
		private String notificationType, notificationValue;
		/**
		 * The VerifyBuilder class constructor.
		 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
		 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
		 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
		 */
		public VerifyBuilder(String customerId, String secretKey){
			this.customerId = customerId;
			this.secretKey = secretKey;
		}
		/** 
		 * Connection timeout is the timeout in making the initial connection; i.e. completing the TCP connection handshake.
		 * @param connectTimeout int value representing connection timeout
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder connectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}
		/** 
		 * The read timeout is the timeout on waiting to read data. 
		 * Specifically, if the server fails to send a byte [timeout] seconds after the last byte, a read timeout error will be raised.
		 * @param readTimeout int value representing connection read timeout
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder readTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}
		/** 
		 * @param httpsProtocol The httpsProtocol you want to use.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder httpsProtocol(String httpsProtocol) {
			this.httpsProtocol = httpsProtocol;
			return this;
		}
		/** 
		 * Comma separated string containing list of ciphers, Ex: ciphers = 
		 * "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_GCM_SHA256,
		 * TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_128_CBC_SHA256,
		 * TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_DSS_WITH_AES_128_CBC_SHA256"
		 * @param ciphers [optional] Please set the ciphers that you want to use. 
		 * @return
		 */
		public VerifyBuilder ciphers(String ciphers){
			this.ciphers = ciphers;
			return this;
		}
		/**
		 * Set TeleSign REST api url 
		 * @param url
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder url(String url){
			this.url = url;
			return this;
		}
		/**
		 * Set TeleSign REST api mobileUrl 
		 * @param mobileUrl
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder mobileUrl(String mobileUrl){
			this.mobileUrl = mobileUrl;
			return this;
		}
		/** 
		 * @param sessionId [Optional] Your end users session id. Set it to "null" if not sending session id.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder sessionId(String sessionId){
			this.sessionId = sessionId;
			return this;
		}
		/** 
		 * @param originatingIp [Optional] Your end users IP Address. This value must be in the format 
		 * defined by IETF in the Internet-Draft document titled Textual Representation of IPv4 and IPv6 
		 * Addresses. Ex: originatingIp=192.168.123.456. Set it to null if not sending originating ip. 		 * 
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder originatingIp(String originatingIp){
			this.originatingIp = originatingIp;
			return this;
		}
		/** 
		 * @param language [Optional] A string containing the IETF language tag. For example, "fr-CA". Set this value to "null" to use English (the default).
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder language(String language){
			this.language = language;
			return this;
		}
		/** 
		 * @param template [Optional] A string containing a text message to override the 
		 * predefined text message template. Your text message must incorporate a $$CODE$$ 
		 * placeholder to integrate the verify_code field. Set this value to null (the default) to use the predefined template.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder template(String template){
			this.template = template;
			return this;
		}
		/** 
		 * @param verifyCode [Optional] A string containing the verification code that you want to send to the end user. 
		 * When you set this value to "null", TeleSign automatically generates the verification code (the default behavior). 
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder verifyCode(String verifyCode){
			this.verifyCode = verifyCode;
			return this;
		}
		/** 
		 * @param verifyMethod [Optional]	A string containing the input method you want the end user to use when returning the verification 
		 * code. Use a value of "keypress" when you want the user to use their phone to dial the code. Set this value to null 
		 * when you want the user to enter the code into your web application (the default).
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder verifyMethod(String verifyMethod){
			this.verifyMethod = verifyMethod;
			return this;
		}		
		/** 
		 * @param extensionType [Optional]	An Integer value representing the type of response to use when dialing into a Private 
		 * Branch Exchange (PBX). Use a value of 1 to have TeleSign use Dual-Tone Multi-Frequency (DTMF) tones to dail the user's 
		 * extension. Use a value of 2 to have TeleSign use voice automation to request the user's extension. Use a value of 0 
		 * (the default) when the user isn't behind a PBX.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder extensionType(int extensionType){
			this.extensionType = extensionType;
			return this;
		}		
		/** 
		 * @param extensionTemplate [Optional]	A numerical string specifying the user's PBX extension number. 
		 * Since this value is used in the call string, you can include one second pauses by adding commas before 
		 * the extension number.  Set this value to null (the default) if not used.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder extensionTemplate(String extensionTemplate){
			this.extensionTemplate = extensionTemplate;
			return this;
		}
		/** 
		 * @param callForwardAction [Optional]	A string containing call forward action parameter. block or allow
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder callForwardAction(String callForwardAction){
			this.callForwardAction = callForwardAction;
			return this;
		} 
		/** 
		 * @param ttsMessage [Optional] The message to display to the end user, in the body of the notification.
		 * If you don't include this parameter, then TeleSign automatically supplies the default message.
		 * [Example] ttsMessage=Enter the code displayed on our web site.[Default] is <i>null</i>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder ttsMessage(String ttsMessage){
			this.ttsMessage = ttsMessage;
			return this;
		}
		/** 
		 * @param smsMessage [Optional] The message to display to the end user, in the body of the notification.
		 * If you don't include this parameter, then TeleSign automatically supplies the default message.
		 * [Example] smsMessage=Enter the code displayed on our web site.[Default] is <i>null</i>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder smsMessage(String smsMessage){
			this.smsMessage = smsMessage;
			return this;
		}
		/** 
		 * @param pushMessage [Optional] The message to display to the end user, in the body of the notification.
		 * If you don't include this parameter, then TeleSign automatically supplies the default message.
		 * [Example] pushMessage=Enter the code displayed on our web site.[Default] is <i>null</i>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder pushMessage(String pushMessage){
			this.pushMessage = pushMessage;
			return this;
		}
		
		/** 
		 * @param redial [Optional]	A boolean value that enables/disables redialing. 
		 * Set this value to "true" (the default) when you want TeleSign to re-attempt the call 
		 * after a failed attempt. Set this value to "false" when you don't.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder redial(boolean redial){
			return this;
		}
		/**
		 * @param callerId [Optional] End user's caller ID if available. Used for Verify SMS and Verify 
		 * Call transactions, but is ignored for Verify Push transactions.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder callerId(String callerId){
			this.callerId = callerId;
			return this;
		}
		/** 
		 * @param preference [Optional] Allows customers to override the Smart Verify method selection. 
		 * Customers can specify either "call", "sms" or "push" to be the recommended method to attempt.
		 * Since not all methods are supported on all devices, TeleSign may ignore the selected override method, 
		 * in order to provide the method that is most appropriate, in which case Telesign selects the 
		 * method in the order of "push", "sms", and "call".
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder preference(String preference){
			this.preference = preference;
			return this;
		}
		/**
		 * @param ignoreRisk [Optional] If set to "true", allows customers to bypass blocking the request 
		 * if the score is above the threshold value configured in the customer account.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder ignoreRisk(String ignoreRisk){
			this.ignoreRisk = ignoreRisk;
			return this;
		}		
		/** 
		 * @param ucid [Required] A string the specifies one of the 
		 * <a href="http://docs.telesign.com/rest/content/xt/xt-use-case-codes.html#xref-use-case-codes">Use Case Codes</a>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder ucid(String ucid){
			this.ucid = ucid;
			return this;
		}
		/** 
		 * @param extra The Extra parameter you would like to send to Telesign to take advantage of unreleased features.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder extra(Map<String, String> extra){
			this.extra = extra;
			return this;
		}
		public VerifyBuilder softTokenId(String softTokenId){
			this.softTokenId = softTokenId;
			return this;
		}
		/** 
		 * @param bundleId [Required] Specifies a custom banner and icon for the TeleSign 
		 * AuthID application to use for this notification.This allows you to brand your 
		 * notifications with your corporate logo and/or your service-specific branding.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder bundleId(String bundleId){
			this.bundleId = bundleId;
			return this;
		}
		/** 
		 * @param notificationType [Optional] Indicates the security measure to use for 
		 * transaction authorization. Valid values are <i>SIMPLE</i> and <i>CODE</i>.The default value is <i>SIMPLE</i>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder notificationType(String notificationType){
			this.notificationType = notificationType;
			return this;
		}
		/** 
		 * @param notificationValue [Optional] Applies when notification_type=CODE.You normally 
		 * leave this parameter empty, and accept the default behavior in which TeleSign automatically 
		 * generates a six-digit value for you,and sends it to you in our response message. 
		 * If you'd prefer to use your own verification code, you can override the default 
		 * behavior by setting a numeric value for this parameter. Values must be between 
		 * six and eight digits long. [Default] is <i>null</i>.
		 * @return VerifyBuilder A {@link com.telesign.verify.Verify.VerifyBuilder} object.
		 */
		public VerifyBuilder notificationValue(String notificationValue){
			this.notificationValue = notificationValue;
			return this;
		}
		/** 
		 * @return Verify A {@link com.telesign.verify.Verify} object.
		 */
		public Verify create(){
			Verify verifyObj = new Verify(this);
			return verifyObj;
		}
		
	}
	
	/**
	 * The Verify class constructor.
	 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
	 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
	 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
	 */
	@Deprecated
	public Verify(String customer_id, String secret_key) {

		this.customerId = customer_id;
		this.secretKey = secret_key;
	}
	
	/**
	 * The Verify class constructor.
	 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
	 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
	 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
	 * @param httpsProtocol [Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	@Deprecated
	public Verify(String customer_id, String secret_key, String httpsProtocol) {

		this.customerId = customer_id;
		this.secretKey = secret_key;
		this.httpsProtocol = httpsProtocol;
	}
	
	/**
	 * The Verify class constructor.
	 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
	 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
	 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
	 * @param connectTimeout 
	 * 			[Required] A integer representing connection timeout value while connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout value while reading response returned from Telesign api.
	 */
	@Deprecated
	public Verify(String customer_id, String secret_key, int connectTimeout, int readTimeout) {

		this.customerId = customer_id;
		this.secretKey = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	/**
	 * The Verify class constructor.
	 * Once you instantiate a Verify object, you can use it to make instance calls to <em>Verify SMS</em> and <em>Verify Call</em>.
	 * @param customer_id	[Required]	A string containing your TeleSign Customer ID (your TeleSign account number).
	 * @param secret_key	[Required]	A string containing your TeleSign Secret Key (a bese64-encoded string valu, available from the TeleSign Client Portal).
	 * @param connectTimeout 
	 * 			[Required] A integer representing connection timeout value while connecting to Telesign api.
	 * @param readTimeout
	 * 			[Required] A integer representing read timeout value while reading response returned from Telesign api.
	 * @param httpsProtocol [Optional]	Specify the protocol version to use. ex: TLSv1.1, TLSv1.2. default is TLSv1.2
	 */
	@Deprecated
	public Verify(String customer_id, String secret_key, int connectTimeout, int readTimeout, String httpsProtocol) {

		this.customerId = customer_id;
		this.secretKey = secret_key;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.httpsProtocol = httpsProtocol;
	}

	/**
	 * Delivers a verification code to the end user by sending it in a text message.
	 * Use this overload when:
	 * <ul>
	 * 	<li>the end user's native written language is not the default language (English), or</li>
	 * 	<li>when you want to send the user a verification code that you create, or</li>
	 * 	<li>when you want to apply a custom text message template.</li>
	 * </ul>
	 * Additionally following parameters are applicable @param language, @param verifyCode, @param template, @param ucid, @param extra, @param originatingId, @param sessionId
	 * @param phoneNo	[Required]	A string containing the user's phone number.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse sms(String phoneNo) {

		String result = null;

		try {			
			
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_VERIFY_SMS).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("POST").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");
			
			if(language != null) {

				body += "&language=" + URLEncoder.encode(language, "UTF-8");
			}
			
			if(verifyCode != null) {

				body += "&verify_code=" + URLEncoder.encode(verifyCode, "UTF-8");
			}
			
			if(template != null) {

				body += "&template=" + URLEncoder.encode(template, "UTF-8");
			}
			
			if(originatingIp != null && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip=" + URLEncoder.encode(originatingIp, "UTF-8");
			}
			
			if(sessionId != null && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}
			
			if(null != ucid && !ucid.isEmpty()) {

				body += "&ucid=" + URLEncoder.encode(ucid, "UTF-8");
			}			

			if(null != extra)
				extraParams(tr);
			
			tr.setPostBody(body);
			
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing Verify SMS API", e);			
		}
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}	
	
	/**
	 * Delivers a verification code to the end user - with a phone call. 
	 * When the user answers their phone, the TeleSign server plays an automated voice message that contains the code.
	 * Use this when:
	 * <ul>
	 * 	<li>the end user's spoken language is not the default language (English), or</li>
	 * 	<li>when you want to send them a verification code that you create, or </li>
	 * 	<li>when you need to specify a method for handling automated interactions with a PBX.</li>
	 * </ul>
	 * Additionally following parameters are applicable: @param language, @param verifyCode, @param verifyMethod, @param extensionType, @param extensionTemplate, @param redial, @param callForwardAction
	 * @param phoneNo			[Required]	A string containing the user's phone number.	 
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse call(String phoneNo) {

		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_VERIFY_CALL).
					httpsProtocol(httpsProtocol).httpMethod("POST").ciphers(ciphers).connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");
			
			if(language != null) {

				body += "&language=" + URLEncoder.encode(language, "UTF-8");
			}
			
			if(verifyCode != null) {

				body += "&verify_code=" + URLEncoder.encode(verifyCode, "UTF-8");
			}
			
			if(verifyMethod != null && verifyMethod.equalsIgnoreCase("keypress")) {

				body += "&verify_method=" + URLEncoder.encode(verifyMethod, "UTF-8");
			}

			if(extensionType > 0 && extensionType < 3) {

				body += "&extension_type=" + URLEncoder.encode(Integer.toString(extensionType), "UTF-8");
			}
			if(extensionTemplate != null) {

				body += "&extension_template=" + URLEncoder.encode(extensionTemplate, "UTF-8");
			}
			if(originatingIp != null && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip=" + URLEncoder.encode(originatingIp, "UTF-8");
			}
			if(sessionId != null && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}
			if(!redial) {

				body += "&redial=" + URLEncoder.encode(Boolean.toString(redial), "UTF-8");
			}
			
			if (null != callForwardAction) {				
				if ("block".equalsIgnoreCase(callForwardAction)) {

					body += "&call_forward_action=" + URLEncoder.encode("Block", "UTF-8");

				} else if ("flag".equalsIgnoreCase(callForwardAction)) {

					body += "&call_forward_action=" + URLEncoder.encode("Flag", "UTF-8");

				}
			}
			if(ttsMessage != null && !ttsMessage.isEmpty())
				body += "&tts_message=" + URLEncoder.encode(ttsMessage, "UTF-8");
			
			if(null != ucid) {

				body += "&ucid=" + URLEncoder.encode(ucid, "UTF-8");
			}

			if(null != extra)
				extraParams(tr);
			
			tr.setPostBody(body);
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing verify call API", e);			
		}
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
	
	/**
	 * Requests the verification result from TeleSign.
	 * After sending an end user a verification code, wait a minute or two to 
	 * allow them to receive it and then respond, and then call this method to find out if the end user passed the code challenge.
	 * 
	 * Additionally following parameters are applicable: @param originatingId, @param sessionId, @param extra
	 * @param resource_id	[Required]	The string returned in the Response Message that TeleSign sends upon receipt of your HTTP 1.1 Request Message - for either {@link com.telesign.verify#sms()} or {@link com.telesign.verify#call()}. 
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse status(String resource_id) {

		String result = null;
		
		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_VERIFY + resource_id).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();

			if (verifyCode != null)
				tr.addParam("verify_code", verifyCode);
			
			if(originatingIp != null && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null && !sessionId.isEmpty()) {

				tr.addParam("session_id", sessionId);
			}

			if(null != extra)
				extraParams(tr);
			
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing verify status API", e);			
		}
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}	

	/**
	 * The TeleSign Mobile Device Registration web service allows you to query the current state of the Push Verify application registration.
	 * Additionally following parameters are applicable: @param extra, @param originatingId, @param sessionId
	 * @param phoneNo 		   [Required] Your end user's phone number, including the country code.	 * 
	 * @return  A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse registration(String phoneNo){
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(mobileUrl).subResource(V2_VERIFY_REGISTRATION + phoneNo).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("GET").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			
			if(null != bundleId && !bundleId.isEmpty()) {

				tr.addParam("bundle_id", bundleId);
			}

			if(originatingIp != null && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				tr.addParam("originating_ip", originatingIp);
			}
			
			if(sessionId != null && !sessionId.isEmpty()) {

				tr.addParam("session_id", sessionId);
			}

			if(null != extra)
				extraParams(tr);
			
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing verify registration API", e);
		}
			
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}
		
	/**
	 * Calls the specified phone number, and using speech synthesis, speaks the verification code to the user.
	 * Additionally following parameters are applicable: @param ucid, @param callerId, @param language, @param verifyCode, @param preference, ignoreRisk, ttsMessage, pushMessage, smsMessage, originatingIp, sessionId, extra	 
	 * @param phoneNo [Required] Your end user's phone number, including the country code.	 	 
	 * @return	A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse smartVerify(String phoneNo){
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(url).subResource(V1_VERIFY_SMART).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("POST").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");

			if(null != ucid) {

				body += "&ucid=" + URLEncoder.encode(ucid, "UTF-8");
			}
			
			if(null != callerId) {

				body += "&caller_id=" + URLEncoder.encode(callerId, "UTF-8");
			}
			
			if(null != language) {

				body += "&language=" + URLEncoder.encode(language, "UTF-8");
			}
			
			if(null != verifyCode) {

				body += "&verify_code=" + URLEncoder.encode(verifyCode, "UTF-8");
			}
			
			if(null != preference) {

				body += "&preference=" + URLEncoder.encode(preference, "UTF-8");
			}
			
			if(null != ignoreRisk) {

				body += "&ignore_risk=" + URLEncoder.encode(ignoreRisk, "UTF-8");
			}
			
			if(null != ttsMessage && !ttsMessage.isEmpty())
				body += "&tts_message=" + URLEncoder.encode(ttsMessage, "UTF-8");
			
			if(null != pushMessage && !pushMessage.isEmpty())
				body += "&push_message=" + URLEncoder.encode(pushMessage, "UTF-8");
			
			if(null != smsMessage && !smsMessage.isEmpty())
				body += "&sms_message=" + URLEncoder.encode(smsMessage, "UTF-8");
						
			if(null != originatingIp && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip=" + URLEncoder.encode(originatingIp, "UTF-8");
			}
			if(null != sessionId && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}			

			if(null != extra)
				extraParams(tr);
			
			tr.setPostBody(body);			
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing smart verify API", e);
		}	
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}	
	
	/**
	 * The push method sends a push notification containing the verification code to the specified phone number (supported for mobile phones only).
	 * Additionally following parameters are applicable: @param notificationType, @param notificationValue, @param bundleId, @param pushMessage, @param originatingId, @param sessionId
	 * @param phoneNo 		 [Required] The phone number of the mobile device that you want to send push notifications to. 
	 * 								  		The phone number must include its associated country code (1 for North America). 
	 * 								  		For example, phone_number=13105551212. 	 
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse push(String phoneNo){
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(mobileUrl).subResource(V2_VERIFY_PUSH).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("POST").connectTimeout(connectTimeout).readTimeout(readTimeout).create();

			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");			
			
			if(null != ucid) {

				body += "&ucid=" + URLEncoder.encode(ucid, "UTF-8");
			}
			if(null == notificationType || notificationType.isEmpty()){
				
				notificationType = "SIMPLE";
				body += "&notification_type=" + URLEncoder.encode(notificationType, "UTF-8");
				
			} else if("CODE".equalsIgnoreCase(notificationType)) {
				
				body += "&notification_type=" + URLEncoder.encode(notificationType.toUpperCase(), "UTF-8");
				body += "&notification_value=" + URLEncoder.encode(isValidNotificationValue(notificationValue)?notificationValue:null, "UTF-8");
			}						

			if(null != bundleId) {

				body += "&bundle_id=" + URLEncoder.encode(bundleId, "UTF-8");
			}
			
			if(null != pushMessage) {

				body += "&message=" + URLEncoder.encode(pushMessage, "UTF-8");
			}
			
			if(null != originatingIp && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip=" + URLEncoder.encode(originatingIp, "UTF-8");
			}
			if(null != sessionId && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}

			if(null != extra)
				extraParams(tr);
			
			tr.setPostBody(body);
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing Verify push API", e);			
		}		
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
	}	
	
	/**
	 * The TeleSign Mobile Device Soft Token Notification web service allows you to anticipate when your users need to use their soft token to 
	 * generate a time-sensitive one-time passcode. You can use this web service to preemptively send them a push notification that initializes 
	 * their on-device TeleSign AuthID application with the right soft token. When they open the notification, the soft token launches ready for 
	 * them to use.
	 * Additionally following parameters are applicable: @param softTokenId, @param verifyCode, @param bundleId, @param originatingIp, @param sessionId
	 * @param phoneNo   [Required] The phone number for the Verify Soft Token request, including country code. For example, phone_number=13105551212.
	 * @return A {@link com.telesign.verify.response.VerifyResponse} object, which contains the JSON-formatted response body from the TeleSign server.
	 */
	public VerifyResponse softToken(String phoneNo){
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey).baseUrl(mobileUrl).subResource(V2_VERIFY_TOKEN).
					httpsProtocol(httpsProtocol).ciphers(ciphers).httpMethod("POST").connectTimeout(connectTimeout).readTimeout(readTimeout).create();
			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");

			if(null != softTokenId) {

				body += "&soft_token_id=" + URLEncoder.encode(softTokenId, "UTF-8");
			}
			
			if(null != verifyCode) {

				body += "&verify_code=" + URLEncoder.encode(verifyCode, "UTF-8");
			}
			
			if(null != bundleId) {

				body += "&bundle_id=" + URLEncoder.encode(bundleId, "UTF-8");
			}
			
			if(null != originatingIp && !originatingIp.isEmpty() && IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip=" + URLEncoder.encode(originatingIp, "UTF-8");
			}
			
			if(null != sessionId && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}

			if(null != extra)
				extraParams(tr);
			
			tr.setPostBody(body);
			result = tr.executeRequest();
		}
		catch (Exception e) {
			throw new TelesignAPIException("Exception while executing Verify soft token API", e);
			}
		
		VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
		
		return response;
		}
	
	/**
	 * Matches the notification_value for a string having 6-8 digits 
	 * @param notification_value
	 * @return boolean value checking validity for notification_value  
	 */
	private boolean isValidNotificationValue(String notification_value){
		if(null != notification_value){
			final Matcher m = Pattern.compile("\\d{6,8}").matcher(notification_value);		
			return m.matches();
		} else 
			return false;
	}
	
	/**
	 * Parses extra params and adds to TeleSignRequest
	 * @param tr TeleSignRequest
	 */
	private void extraParams(TeleSignRequest tr) {
		for(Map.Entry<String, String> extraParam:extra.entrySet())
			tr.addParam(extraParam.getKey(), extraParam.getValue());
	}
	
	public static VerifyBuilder init(String customerId, String secretKey){
		return new VerifyBuilder(customerId, secretKey);
	}
}
