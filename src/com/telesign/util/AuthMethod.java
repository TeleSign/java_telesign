package com.telesign.util;

/**
 * An enumeration of values that represent cryptographic hash functions
 * available for calculating an Hash-based Message Authentication Code (HMAC)
 * used in building the Signature part of the Authorization Request Header. Each
 * enumeration member corresponds to its cryptographic strength.
 */
public enum AuthMethod {

	SHA1("HmacSHA1", "hmac-sha1"), SHA256("HmacSHA256", "hmac-sha256");

	/**
	 * The AuthMethod constructor.
	 * 
	 * @param value
	 *            [Required] A string containing the name of the cryptographic hash function.
	 * @param ts_value
	 *            [Required] A string containing the <em>TeleSign-specific</em> version of the name of the cryptographic hash function.
	 */
	AuthMethod(String value, String ts_value) {
		this.value = value;
		this.ts_value = ts_value;
	}

	/**
	 * Gets the name of the cryptographic hash function.
	 * 
	 * @return A string containing the name of the hash function.
	 */
	public String value() {
		return value;
	}

	/**
	 * Gets the <em>TeleSign-specific</em> version of the name of the cryptographic hash function.
	 * 
	 * @return A string containing the name of the hash function.
	 */
	public String tsValue() {
		return ts_value;
	}

	private final String value;
	private final String ts_value;
}
