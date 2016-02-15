package com.telesign.phoneid.response;

public class PhoneIdCallForwardResponse {
	public PhoneIdStandardResponse standardResponse;
	/**
	 * An object containing the call forwarding information associated with the phone number.
	 */
	public CallForward call_forwarding;

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

}
