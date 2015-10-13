/**
 * @version		1.0
 * @copyright		Copyright © 2013, TeleSign Corporation.
 * @license		http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author		J. Weatherford
 * @maintainer		Humberto Morales
 * @repository		https://github.com/TeleSign/java_telesign
 * @support		support@telesign.com
 */
package com.telesign.verify.response;

import com.google.gson.Gson;

/** The <strong>VerifyResponse</strong> class encapsulates all of the information returned from a call to either the <strong>Verify SMS</strong> web service, or to the <strong>Verify Call</strong>  web service. */
public class VerifyResponse {
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;

	/** An object containing details about the request status. */
	public Status status;

	/** An object that describes the verification status. */
	public Verify verify;
	
    private final Gson gson = new Gson();
	
	/** An array of property-value pairs, that contain information on error conditions that might have resulted from the Request. */
	public static class Error
	{
	    	/**  A 1 to 5-digit error code (possibly negative) that indicates the type of error that occurred. When no error occurs, the default value 0 is returned. */
		public int code;
		
		/** A string that describes the type of error that occurred. If no error occurs, this parameter is empty. */
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

	/** An object that describes the verification status. */
	public static class Verify {
	    
		/** Indicates whether the verification code entered matches that which was sent. Possible values are VALID, INVALID, or UNKNOWN. When the code entered matches the code sent, the response will be VALID. When the code entered does not match the code sent, code_state will be INVALID. */
		public String code_state;
		
		/**  Always set to an empty string. */
		public String code_entered;
	}

	/**
	 * Converts a <strong>VerifyResponse</strong> object to its equivalent JSON format.  
	 * @see java.lang.Object#toString()
	 * @return A string containing the response from the TeleSign server, in JSON format.  
	 */
	@Override
	public String toString() {
		return gson.toJson(this);
	}
}
