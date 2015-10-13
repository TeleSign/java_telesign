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

/** The <strong>PhoneIdLiveResponse</strong> class encapsulates all of the information returned from a call to the <strong>PhoneID Live</strong> web service.  */
public class PhoneIdLiveResponse {
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;

	/** An object that contains details about the phone type. */
	public PhoneType phone_type;

	/** A String containing the Signature, exactly as it was sent in the Request. Unless the request contained an invalid Signature, this contains an empty string.*/
	public String signature_string;

	/** An object containing details about the request status. */
	public Status status;

	/** An object containing details about the numbering attributes of the specified phone number. */
	public Numbering numbering;

	/** An object containing geographical location information associated with the phone number. */
	public Location location;
	
	/** An object containing information about the company that provides telecommunications services for the phone number. */
	public Carrier carrier;
	
	private final Gson gson = new Gson();
	
	/**
	 * A JSON object containing details about a specified phone number�s state of operation.
	 * <strong>Note:</strong> In the case where PhoneID Live is not supported for the specified phone number, the server returns a <strong>Live</strong> object that is <em>null</em>. 
	 */
	public Live live;
	
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
	    	
	    	/** An object containing details about the original phone number passed to TeleSign�s PhoneID API. */
		public OriginalNumber original;
		
		/** An object containing details about how the phone number was cleansed. Phone Cleansing corrects common formatting issues in submitted phone numbers. */
		public CleansingNumber cleansing;
		
	    	/** An object containing details about the original phone number passed to TeleSign�s PhoneID API. */
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

	/**
	 * An object containing geographical location information associated with the phone number.
	 * <strong>Note:</strong> <emphasis>Location</emphasis> refers to the place where the phone is registered, <emphasis>not</emphasis> to where it actually is at the time of this request. 
	 */
	public static class Location {
	    
	    	/** A string specifying the name of the County (or Parish) associated with the phone number (U.S. only). */
		public String county;
		
		/** A string specifying the name of the city associated with the phone number. */
		public String city;
		
		/** The 2-letter State Code of the state (province, district, or territory) associated with the phone number (North America only). */
		public String state;
		
		/** The 5-digit United States Postal Service ZIP Code associated with the phone number (U.S. only). */
		public String zip;
		
		/** An object containing details about the country associated with the phone number. */
		public Country country;
		
		/** An object containing details about the Time Zone associated with the phone number. */
		public TimeZone time_zone;
		
		/** An object containing details about the geographical coordinates of the location where the phone number is registered. */
		public Coordinates coordinates;
		
		/** A 4-digit string indicating the Primary Metropolitan Statistical Area (PMSA) Code for the location associated with the phone number (U.S. only). PMSA Codes are governed by the US Census Bureau. */
		public String metro_code;
		
		/** An object containing details about the country associated with the phone number. */
		public static class Country {
		    
		    	/** The ISO 3166-1 2-letter Country Code associated with the phone number. */
			public String iso2;
			
			/** The ISO 3166-1 3-letter Country Code associated with the phone number */
			public String iso3;
			
			/** The Country Name associated with phone number. */
			public String name;
		}

		/** An object containing details about the Time Zone associated with the phone number. */
		public static class TimeZone {
		    
		    	/** For U.S. domestic phone numbers, this parameter returns the UTC offset associated with the phone number. */
			public String utc_offset_min;
			
			/** A string identifying the Time Zone Name (TZ) associated with the phone number (U.S. only). For example: "America/Los_Angeles". */
			public String name;
			
			/** For international phone numbers, this parameter returns the maximum UTC offset for the country associated with the phone number. For U.S. domestic phone numbers, this parameter returns the same result as utc_offset_min. */
			public String utc_offset_max;
		}

		/** An object containing details about the geographical coordinates of the location where the phone number is registered. */
		public static class Coordinates {
		    
		    	/** A value indicating the number of degrees of latitude of the location associated with the phone number, expressed in seven decimal digits, with five decimal places. For example, 34.18264. */
			public double latitude;
			
			/** A value indicating the number of degrees of longitude of the location associated with the phone number, expressed in eight decimal digits, with five decimal places For example, -118.30840. */
			public double longitude;
		}
	}

	/**  An object containing information about the company that provides telecommunications services for the phone number. */
	public static class Carrier {

		/** The string specifying the name of the carrier.  For example: "Verizon". */
		public String name;
	}

	/**
	 * A JSON object containing details about a specified phone number�s state of operation.
	 * <strong>Note:</strong> In the case where PhoneID Live is not supported for the specified phone number, the server returns a <strong>Live</strong> object that is <em>null</em>. 
	 */
	public static class Live {
	    
	    	/** A string indicating the current status of the subscriber�s phone number. */
		public String subscriber_status;
		
		/** A string indicating the current status of the phone equipment. */
		public String device_status;
		
		/** A string indicating whether the mobile device is currently roaming outside of the contracted service coverage area. */
		public String roaming;
		
		/** The country in which the mobile device is roaming. */
		public String roaming_country;
	}

	/**
	 * Converts a <strong>PhoneIdLiveResponse</strong> object to its equivalent JSON format, using the {@link com.google.gson.Gson} class library.
	 * @see java.lang.Object#toString()
	 * @return A string containing the response from the TeleSign server, in JSON format.  
	 * @see com.google.gson.Gson 
	 */
	@Override
	public String toString() {
		return gson.toJson(this);
	}
}