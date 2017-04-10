package com.telesign.rest;

import java.util.Map;

public class PhoneIdClient extends RestClient {

	private static final String PHONEID_RESOURCE = "/v1/phoneid/%s";

	public PhoneIdClient(String customerId, String secretKey) {
		super(customerId, secretKey);
	}

	public TelesignResponse phoneid(String phoneNumber, Map<String, String> params) {

		return this.post(String.format(PHONEID_RESOURCE, phoneNumber), params);
	}
}
