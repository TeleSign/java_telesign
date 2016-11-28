package com.telesign.telebureau.response;

import com.google.gson.Gson;

public class TelebureauResponse {
	private final transient Gson gson = new Gson();
	
	/** An object containing details about the request status. */
	public Status status;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;
	
	/** Contains details about your fraud submission. This will also include any optional parameters specified in the original POST request. */
	public Fraud_event fraud_event;
	
	/** Contains details about your fraud submission. This will also include any optional parameters specified in the original POST request. */
	public static class Fraud_event{
		/** The end user’s complete phone number, which includes its country code. */
		public String phone_number;
	}
	
	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;
	
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
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
	
	/**
	 * Converts a <strong>TelebureauResponse</strong> object to its equivalent JSON format, using the {@link com.google.gson.Gson} class library.
	 * @see java.lang.Object#toString()
	 * @return A string containing the response from the TeleSign server, in JSON format.  
	 * @see com.google.gson.Gson 
	 */
	@Override
	public String toString()
	{
		return gson.toJson(this);
	}
}
