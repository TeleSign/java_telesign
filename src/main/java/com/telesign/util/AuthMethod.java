package com.telesign.util;

public enum AuthMethod {
	SHA1 ("HmacSHA1", "hmac-sha1"),
    SHA256 ("HmacSHA256", "hmac-sha256");
	
	AuthMethod(String value, String ts_value) {
		this.value = value;
		this.ts_value = ts_value;
	}
	
	public String value() { 
		return value;
	}
	
	public String tsValue() { 
		return ts_value;
	}
	
	
	private final String value;
	private final String ts_value;
}
