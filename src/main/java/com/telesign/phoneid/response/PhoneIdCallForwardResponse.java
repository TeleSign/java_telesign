package com.telesign.phoneid.response;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Error;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Location;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Numbering;
import com.telesign.phoneid.response.PhoneIdStandardResponse.PhoneType;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Status;

public class PhoneIdCallForwardResponse {
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	/** An object containing details about the request status. */
	public Status status;
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;

	/** An object containing details about the numbering attributes of the specified phone number. */
	public Numbering numbering;

	/** An object containing geographical location information associated with the phone number. */
	public Location location; 
	
	/** An object that contains details about the phone type. */
	public PhoneType phone_type;
	
	/**
	 * An object containing the call forwarding information associated with the phone number.
	 */
	public CallForward call_forwarding;
	
	private final transient Gson gson = new Gson();

	public class CallForward {
		/**
		 * One of the following status codes:
		 * <ul>
		 * <li>UNCONDITIONAL: unconditional call forwarding is enabled on the
		 * phone number.</li>
		 * <li>NOT REACHABLE: call forwarding is enabled since the phone is
		 * either turned off or outside coverage area.</li>
		 * <li>NOT FORWARDED: no call forwarding is enabled on the phone number.
		 * </li>
		 * <li>UNAVAILABLE: call forwarding information is currently unavailable
		 * on the phone number due to carrier or network issues. The information
		 * can become available at a later time.</li>
		 * <li>UNSUPPORTED: call forwarding information is not supported for the
		 * phone number, type of phone (e.g. mobile vs landline), country or
		 * carrier in question. The information in unlikely to become available
		 * at a later time unless the coverage is expanded</li>
		 * </ul>
		 */
		public String forwarding;

		/**
		 * The phone number to which the call has been forwarded to, if
		 * applicable. The phone number is in the E.164 format.
		 */
		public String forwarded_to;
	}
	
	/**
	 * Converts a <strong>PhoneIdScoreResponse</strong> object to its equivalent JSON format.  
	 * @see java.lang.Object#toString()
	 * @return A string containing the response from the TeleSign server, in JSON format, using the {@link com.google.gson.Gson} class library.
	 * @see com.google.gson.Gson 
	 */
	@Override
	public String toString() {
		return gson.toJson(this);
	}

}
