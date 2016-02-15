package com.telesign.phoneid.response;

public class PhoneIdNumberDeactivationResponse {
	public PhoneIdStandardResponse standardResponse;
	public NumberDeactivation numberDeactivation;
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
