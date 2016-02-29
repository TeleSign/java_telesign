package com.telesign.phoneid.response;

import com.telesign.phoneid.response.PhoneIdStandardResponse.Error;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Status;

public class PhoneIdNumberDeactivationResponse {
	
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	public NumberDeactivation numberDeactivation;
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;
	
	/** An object containing details about the request status. */
	public Status status;
		
	public static class NumberDeactivation{
		
		/**
		 * The number for which the deactivation check is being performed.
		 */
		public String number;
		/**
		 * The timestamp when number deactivation began storing records for the carrier.
		 */
		public String tracking_since;
		
		/**
		 * The timestamp when the phone number was seen and/or reported as deactivated from the data stream provider.
		 */
		public String last_deactivated;
	}
}
