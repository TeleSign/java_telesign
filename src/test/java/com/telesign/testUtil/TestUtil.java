package com.telesign.testUtil;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class TestUtil {
	public static String CUSTOMER_ID;
	public static String SECRET_KEY;
	public static String PHONE_NUMBER;
	public static String CONNECT_TIMEOUT;
	public static String READ_TIMEOUT;
	public static int readTimeout;
	public static int connectTimeout;
	public static boolean timeouts = false;
	public static String ORIGINATING_IP;
	public static String SESSION_ID;
	public static String HTTPS_PROTOCOL;
	public static boolean isHttpsProtocolSet = false;
	public static String RESOURCE_DIR = "src/test/resources/";
	// Verify specific properties
	public static String CALLER_ID;	
	public static String SMART_VERIFY_PREFERENCE;
	public static String SMART_VERIFY_IGNORE_RISK;
	public static String PUSH_NOTIFICATION_TYPE;
	public static String PUSH_NOTIFICATION_VALUE;
	public static String SOFT_TOKEN_ID;
	public static String CALL_FORWARD_ACTION;
	public static String BUNDLE_ID;
	
	// Verify api url path
	private static final String V1_VERIFY       = "/v1/verify/";
	private static final String V1_VERIFY_SMS   = "/v1/verify/sms";
	private static final String V1_VERIFY_CALL  = "/v1/verify/call"; 
	private static final String V1_VERIFY_SMART = "/v1/verify/smart";
	
	private static final String V2_VERIFY_PUSH         = "/v2/verify/push";
	private static final String V2_VERIFY_TOKEN        = "/v2/verify/soft_token";
	private static final String V2_VERIFY_REGISTRATION = "/v2/verify/registration/";
	// PhoneID url path
	private static final String V1_PHONEID_STANDARD = "/v1/phoneid/standard/";
	private static final String V1_PHONEID_SCORE    = "/v1/phoneid/score/";
	private static final String V1_PHONEID_CONTACT  = "/v1/phoneid/contact/";
	private static final String V1_PHONEID_LIVE     = "/v1/phoneid/live/";
	
	public static void initProperties() throws IOException {
		Properties props = new Properties();
		try {
		props.load(new FileInputStream(TestUtil.RESOURCE_DIR + "test.properties"));
		} catch (FileNotFoundException fne) {
			fail("Please create a \"test.properties\" file at the root project directory " +
					"and include your telesign customerid, secretkey and your phone number. " +
					"If you need assistance, please contact telesign support at support@telesign.com");
		}
		
		CUSTOMER_ID = props.getProperty("test.customerid");
		SECRET_KEY =  props.getProperty("test.secretkey");
		PHONE_NUMBER = props.getProperty("test.phonenumber");
		CONNECT_TIMEOUT =  props.getProperty("test.connecttimeout");
		READ_TIMEOUT =  props.getProperty("test.readtimeout");
		ORIGINATING_IP = props.getProperty("test.originating_ip");
		SESSION_ID = props.getProperty("test.session_id");
		HTTPS_PROTOCOL = props.getProperty("test.httpsprotocol");
		
		// Verify api specific
		SMART_VERIFY_PREFERENCE=props.getProperty("test.smart_verify_preference");
		SMART_VERIFY_IGNORE_RISK=props.getProperty("test.smart_verify_ignore_risk");
		PUSH_NOTIFICATION_TYPE=props.getProperty("test.push_notification_type");
		PUSH_NOTIFICATION_VALUE=props.getProperty("test.push_notification_value");
		SOFT_TOKEN_ID = props.getProperty("test.soft_token_id");
		CALL_FORWARD_ACTION = props.getProperty("test.call_forward_action");
		CALLER_ID = props.getProperty("test.caller_id");
		BUNDLE_ID = props.getProperty("test.bundle_id");
		
		
		boolean pass = true; 
		
		if(TestUtil.CUSTOMER_ID == null || TestUtil.CUSTOMER_ID.isEmpty()) {
			System.out.println("TestUtil.CUSTOMER_ID is not set. Please set the \"test.customerid\" property in the properties file");
			pass = false;
		}
		
		if(TestUtil.SECRET_KEY == null || TestUtil.SECRET_KEY.isEmpty()) {
			System.out.println("TestUtil.SECRET_KEY is not set. Please set the \"test.secretkey\" property in the properties file");
			pass = false;
		}
		if(TestUtil.PHONE_NUMBER == null || TestUtil.PHONE_NUMBER.isEmpty()) {
			System.out.println("PHONE_NUMBER is not set. Please set the \"test.phonenumber\" property in the properties file");
			pass = false;
		}
		
		if(TestUtil.CONNECT_TIMEOUT == null || TestUtil.CONNECT_TIMEOUT.isEmpty() || TestUtil.READ_TIMEOUT == null || TestUtil.READ_TIMEOUT.isEmpty()) {
			System.out.println("Either of CONNECT_TIMEOUT or READ_TIMEOUT is not set. Please set the \"test.connecttimeout\" & \"test.readtimeout\" property in the properties file. " +
					"Or default connect & read timeout values would be used");
			pass = true;
		} else {
			TestUtil.connectTimeout = Integer.parseInt(TestUtil.CONNECT_TIMEOUT);
			TestUtil.readTimeout = Integer.parseInt(TestUtil.READ_TIMEOUT);
			TestUtil.timeouts = true;
			pass = true;
		}		

		if(TestUtil.ORIGINATING_IP == null || TestUtil.ORIGINATING_IP.isEmpty()) {
			System.out.println("ORIGINATING_IP not set. Please set the \"test.originating_ip\" property in the properties file");
			pass = true;
		}
		
		if(TestUtil.SESSION_ID == null || TestUtil.SESSION_ID.isEmpty()) {
			System.out.println("SESSION_ID not set. Please set the \"test.session_id\" property in the properties file");
			pass = true;
		}
		
		if(null == TestUtil.HTTPS_PROTOCOL || TestUtil.HTTPS_PROTOCOL.isEmpty()) {
			System.out.println("HTTPS_PROTOCOL is not set. Please set the \"test.httpsprotocol\" property in the properties file"
					+ ", or default value of TLSv1.2 would be used");
			pass = true;
		} else {
			TestUtil.isHttpsProtocolSet = true;
			pass = true;
		}
		
		// Verify Api test specific
		if(null == SMART_VERIFY_PREFERENCE || SMART_VERIFY_PREFERENCE.isEmpty()) {
			System.out.println("SMART_VERIFY_PREFERENCE not set. Please set the \"test.smart_verify_preference\" property in the properties file");
			pass = true;
		}
		
		if(null == SMART_VERIFY_IGNORE_RISK || SESSION_ID.isEmpty()) {
			System.out.println("SMART_VERIFY_IGNORE_RISK not set. Please set the \"test.smart_verify_ignore_risk\" property in the properties file");
			pass = true;
		}		
		
		if(null == CALL_FORWARD_ACTION || CALL_FORWARD_ACTION.isEmpty()) {
			System.out.println("CALL_FORWARD_ACTION not set. Please set the \"test.call_forward_action\" property in the properties file");
			pass = true;
		}
		
		if(null == CALLER_ID || CALLER_ID.isEmpty()) {
			System.out.println("CALLER_ID not set. Please set the \"test.caller_id\" property in the properties file");
			pass = true;
		}
		
		if(null == PUSH_NOTIFICATION_TYPE || PUSH_NOTIFICATION_TYPE.isEmpty()) {
			System.out.println("PUSH_NOTIFICATION_TYPE not set. Please set the \"test.push_notification_type\" property in the properties file");
			pass = true;
		}
		
		if(null == PUSH_NOTIFICATION_VALUE || PUSH_NOTIFICATION_VALUE.isEmpty()) {
			System.out.println("PUSH_NOTIFICATION_VALUE not set. Please set the \"test.push_notification_value\" property in the properties file");
			pass = true;
		}
		
		if(null == SOFT_TOKEN_ID || SOFT_TOKEN_ID.isEmpty()) {
			System.out.println("SOFT_TOKEN_ID not set. Please set the \"test.soft_token_id\" property in the properties file");
			pass = true;
		}
		
		if(null == CALL_FORWARD_ACTION || CALL_FORWARD_ACTION.isEmpty()) {
			System.out.println("CALL_FORWARD_ACTION not set. Please set the \"test.call_forward_action\" property in the properties file");
			pass = true;
		}

		if(null == BUNDLE_ID || BUNDLE_ID.isEmpty()) {
			System.out.println("BUNDLE_ID not set. Please set the \"test.bundle_id\" property in the properties file");
			pass = true;
		}	
		
		if(!pass) {
			fail("Configuration file not setup correctly!");
		}
		//startServer();
	}
	
	public static void startServer(){
		try {
			TelesignTestServer.create();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	public static void stopServer(){
		TelesignTestServer.stopServer();
	}
	
	public static  final Map <String, String> path;
	static{
		path = new HashMap<String, String>();
		
		path.put ("/v1/verify/sms", "V1_VERIFY_SMS");
		path.put ("/v1/verify/call","V1_VERIFY_CALL"); 
		path.put ("/v1/verify/smart","V1_VERIFY_SMART");
		
		path.put ( "/v2/verify/push", "V2_VERIFY_PUSH");
		path.put ("/v2/verify/soft_token","V2_VERIFY_TOKEN");
		path.put ("/v2/verify/registration/","V2_VERIFY_REGISTRATION");
		// PhoneID url path
		path.put ("/v1/phoneid/standard/", "V1_PHONEID_STANDARD");
		path.put ("/v1/phoneid/score/","V1_PHONEID_SCORE");
		path.put ("/v1/phoneid/contact/","V1_PHONEID_CONTACT");
		path.put ("/v1/phoneid/live/","V1_PHONEID_LIVE");
	} 
	public static enum Path{
		V1_VERIFY ,
		V1_VERIFY_SMS ,
		V1_VERIFY_CALL, 
		V1_VERIFY_SMART,
		
		V2_VERIFY_PUSH ,
		V2_VERIFY_TOKEN ,
		V2_VERIFY_REGISTRATION ,
		// PhoneID url path
		V1_PHONEID_STANDARD ,
		V1_PHONEID_SCORE,
		V1_PHONEID_CONTACT,
		V1_PHONEID_LIVE;	
	}
}
