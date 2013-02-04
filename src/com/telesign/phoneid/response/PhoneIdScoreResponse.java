package com.telesign.phoneid.response;

import com.google.gson.Gson;


/**
 * Google GSON mapped class to represent a TeleSign JSON response to a 
 * PhoneId.score() request
 * 
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class PhoneIdScoreResponse {
	public String reference_id;
	public String resource_uri;
	public String sub_resource;
	public Error[] errors;
	public String signature_string;
	public Status status;
	public Numbering numbering;
	public Risk risk;
	
	public static class Error {
		public int code;
		public String description;
	}
	
	public static class PhoneType {
		public int code;
		public String description;
	}
	
	public static class Status {
		public String updated_on;
		public int code;
		public String description;
	}
	
	public static class Numbering {
		public OriginalNumber original;
		public CleansingNumber cleansing;
		
		public static class OriginalNumber {
			public String phone_number;
			public String complete_phone_number;
			public String country_code;
		}
	
		public static class CleansingNumber {
			public Number sms;
			public Number call;
			public static class Number {
				public String phone_number;
				public String country_code;
				public int min_length;
				public int max_length;
				public int cleansed_code;
			}
		}
	}
	
	public static class Risk {
		public String level;
		public int score;
		public String recommendation;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}