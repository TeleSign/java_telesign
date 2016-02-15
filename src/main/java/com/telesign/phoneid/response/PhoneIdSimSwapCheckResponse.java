package com.telesign.phoneid.response;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Error;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Numbering;
import com.telesign.phoneid.response.PhoneIdStandardResponse.Status;

public class PhoneIdSimSwapCheckResponse {
	/** A String containing a <em>reference identifier</em> that uniquely identifies the Request message that initiated this Response. */
	public String reference_id;
	
	/** A String containing the URI for accesses the PhoneID resource. */
	public String resource_uri;

	/** An array of {@link Error} objects. Each Error object contains information about an error condition that might have resulted from the Request. */	
	public Error[] errors;
	
	/** A String containing the name of the subresource that was accessed. For example, "standard". */
	public String sub_resource;
	
	/** 
	 * The start and end times for which the SIM Swap Check occurred for the given phone number. 
	 * Status codes returned, other than 2200, yield sim_swap block with the value null; however, if there are error(s), then the sim_swap block is absent.
	 * */
	public SimSwap sim_swap;	

	/** A String containing the Signature, exactly as it was sent in the Request. Unless the request contained an invalid Signature, this contains an empty string.*/
	public String signature_string;
		
	/** An object containing details about the request status. */
	public Status status;
	
	/** An object containing details about the numbering attributes of the specified phone number. */
	public Numbering numbering;
	
	public static class SimSwap{
		
		/** Earliest possible time of SIM swap.	 */
		public String sim_swap_window_start;
		
		/** Latest possible time of SIM swap.	 */
		public String sim_swap_window_end;
	}
	
	private final transient Gson gson = new Gson();
	
	/**
	 * Converts a <strong>PhoneIdSimSwapCheckResponse</strong> object to its equivalent JSON format, using the {@link com.google.gson.Gson} class library.
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
