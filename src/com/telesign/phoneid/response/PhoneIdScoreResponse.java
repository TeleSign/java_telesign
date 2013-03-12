/**
 * @version		1.0
 * @copyright		Copyright © 2013, TeleSign Corporation.
 * @license		http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author		J. Weatherford
 * @maintainer		Humberto Morales
 * @repository		https://github.com/TeleSign/java_telesign
 * @support		support@telesign.com
 */
package com.telesign.phoneid.response;

import com.google.gson.Gson;

/** The <strong>PhoneIdScoreResponse</strong> class encapsulates all of the information returned from a call to the <strong>PhoneID Score</strong> web service.  */
public class PhoneIdScoreResponse {
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;

	/** A String containing the Signature, exactly as it was sent in the Request. Unless the request contained an invalid Signature, this contains an empty string.*/
	public String signature_string;

	/** An object containing details about the request status. */
	public Status status;

	/** An object containing details about the numbering attributes of the specified phone number. */
	public Numbering numbering;

	/** An object that describes the risk score for the phone number specified in the request. */
	public Risk risk;
	
	/** An array of property-value pairs, that contain information on error conditions that might have resulted from the Request. */
	public static class Error
	{
	    	/**  A 1 to 5-digit error code (possibly negative) that indicates the type of error that occurred. When no error occurs, the default value 0 is returned. */
		public int code;
		
		/** A string that describes the type of error that occurred. If no error occurs, this parameter is empty. */
		public String description;
	}

	/**  An object containing details about the phone type. */
	public static class PhoneType {
	    
	    	/** One of the <emphasis>Phone Type Codes</emphasis>." */
		public int code;
		
		/**  A description of the phone type. */
		public String description;
	}

	/** An object containing details about the request status. */
	public static class Status {
	    	
	    	/** An ISO 8601 UTC timestamp indicating when the transaction status was updated. */
		public String updated_on;
		
		/**  One of the<emphasis> Transaction Status Codes</emphasis>. */
		public int code;
		
		/** A descriptionm of the transaction status. */
		public String description;
	}

	/** An object containing details about the numbering attributes of the specified phone number. */
	public static class Numbering {
	    	
	    	/** An object containing details about the original phone number passed to TeleSign’s PhoneID API. */
		public OriginalNumber original;
		
		/** An object containing details about how the phone number was cleansed. Phone Cleansing corrects common formatting issues in submitted phone numbers. */
		public CleansingNumber cleansing;
		
	    	/** An object containing details about the original phone number passed to TeleSign’s PhoneID API. */
		public static class OriginalNumber {
		    
		    	/** The Base Phone Number. This is simply the phone number without the Country Dialing Code. */
			public String phone_number;
			
			/** The Base Phone Number prefixed with the Country Dialing Code. This forms the Subresource Identifier part of the PhoneID Contact web service URI. */
			public String complete_phone_number;
			
			/**  A 1, 2, or 3-digit number representing the Country Dialing Code. For example, the Country Dialing Code for both the U.S.A. and Canada is 1, and the Country Dialing Code for the United Kingdom is 44. */
			public String country_code;
		}
	
		/** An object containing details about how the phone number was cleansed. Phone Cleansing corrects common formatting issues in submitted phone numbers. */
		public static class CleansingNumber {
		    
		    	/** An object containing cleansing details about a phone number used for receiving text messages. */	
			public Number sms;
			
			/** An object containing cleansing details about a phone number used for receiving voice calls. */
			public Number call;
			
			/** An object containing cleansing details about a phone number. */
			public static class Number {
			    
			    	/** The Base Phone Number. This is simply the phone number without the Country Dialing Code. */
				public String phone_number;
				
				/**  A 1, 2, or 3-digit number representing the Country Dialing Code. For example, the Country Dialing Code for both the U.S.A. and Canada is 1, and the Country Dialing Code for the United Kingdom is 44. */
				public String country_code;
				
				/**  The minimum number of digits allowed for phone numbers with this particular Country Dialing Code. */
				public int min_length;
				
				/** The maximum number of digits allowed for phone numbers with this particular Country Dialing Code. */
				public int max_length;
				
				/** One of the Phone Number Cleansing Codes describing the cleansing operation TeleSign performed on the phone number. The default value is 100 (No changes were made to the phone number). */
				public int cleansed_code;
			}
		}
	}

	/** An object that describes the risk score for the phone number specified in the request. */
	public static class Risk {
	    
	    	/** A string indicating the severity of the risk. */
		public String level;
		
		/** One of the <strong>Score</strong> values. */
		public int score;
		
		/** A string indicating the action that TeleSign recommends that you take based on the risk score. */
		public String recommendation;
	}

	/**
	 * Converts a <strong>PhoneIdScoreResponse</strong> object to its equivalent JSON format.  
	 * @see java.lang.Object#toString()
	 * @return A string containing the response from the TeleSign server, in JSON format, using the {@link com.google.gson.Gson} class library.
	 * @see com.google.gson.Gson 
	 */
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}