package com.telesign.verify.response;

import com.google.gson.Gson;

/**
 * Google GSON mapped class to represent a TeleSign JSON response to a 
 * Verify.sms(), Verify.call() and Verify.status() requests
 * 
 * 
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class VerifyResponse {
	public String reference_id;
	public String resource_uri;
	public String sub_resource;
	public Error[] errors;
	public Status status;
	public Verify verify;
	
	public static class Error {
		public int code;
		public String description;
	}
	
	public static class Status {
		public String updated_on;
		public int code;
		public String description;
	}
	
	public static class Verify {
		public String code_state;
		public String code_entered;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
