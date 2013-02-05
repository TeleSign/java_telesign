package com.telesign.response;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.telesign.phoneid.response.PhoneIdContactResponse;
import com.telesign.phoneid.response.PhoneIdLiveResponse;
import com.telesign.phoneid.response.PhoneIdScoreResponse;
import com.telesign.phoneid.response.PhoneIdStandardResponse;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class ResponseTest {
	
	@Test
	public void GsonEnabled() {
		
		Gson gson = new Gson();
		assertTrue(gson != null);
	}

	@Test
	public void ValidResponse() {
		String valid_json = "{\r\n  \"reference_id\": \"F0123456789ABCDEF0123456789ABCDE\",\r\n  \"resource_uri\": null,\r\n  \"sub_resource\": \"standard\",\r\n  \"errors\": [],\r\n  \"phone_type\":   {\r\n    \"code\": \"1\",\r\n    \"description\": \"FIXED_LINE\"\r\n  },\r\n  \"status\":   {\r\n    \"updated_on\": \"2012-10-03T14:51:28.709526Z\",\r\n    \"code\": 300,\r\n    \"description\": \"Transaction successfully completed\"\r\n  },\r\n  \"numbering\":   {\r\n    \"original\":   {\r\n      \"phone_number\": \"5555551234\",\r\n      \"complete_phone_number\": \"15555551234\",\r\n      \"country_code\": \"1\"\r\n    },\r\n    \"cleansing\":   {\r\n      \"sms\":   {\r\n        \"phone_number\": \"5555551234\",\r\n        \"min_length\": 10,\r\n        \"cleansed_code\": 100,\r\n        \"max_length\": 10,\r\n        \"country_code\": \"1\"\r\n      },\r\n      \"call\":   {\r\n        \"phone_number\": \"5555551234\",\r\n        \"min_length\": 10,\r\n        \"cleansed_code\": 100,\r\n        \"max_length\": 10,\r\n        \"country_code\": \"1\"\r\n      }\r\n    }\r\n  },\r\n  \"location\":   {\r\n    \"county\": \"Los Angeles County\",\r\n    \"city\": \"Los Angeles\",\r\n    \"state\": \"CA\",\r\n    \"zip\": \"90066\",\r\n    \"country\":   {\r\n      \"iso3\": \"\",\r\n      \"iso2\": \"US\",\r\n      \"name\": \"United States\"\r\n    },\r\n    \"time_zone\":   {\r\n      \"utc_offset_min\": \"-8\",\r\n      \"name\": \"America/Los_Angeles\",\r\n      \"utc_offset_max\": \"-8\"\r\n    },\r\n    \"coordinates\":   {\r\n      \"latitude\": 33.99791,\r\n      \"longitude\": -118.42302\r\n    },\r\n    \"metro_code\": \"4480\"\r\n  }\r\n}";
		
		Gson gson = new Gson();
		PhoneIdStandardResponse valid_response = gson.fromJson(valid_json, PhoneIdStandardResponse.class);
		
		assertTrue(valid_response.location.city.equals("Los Angeles"));
		
	}	
	
	@Test
	public void InvalidResponse() {
		String invalid_auth_json = "{\r\n  \"status\":    {\r\n    \"updated_on\": \"2012-10-03T14:51:28.709526Z\",\r\n    \"code\": 501,\r\n    \"description\": \"Not authorized\"\r\n  },\r\n  \"signature_string\": \"GET\\n\\nTue, 01 May 2012 10:09:16 -0700\\n\r\n                       x-ts-nonce:dff0f33c-7b52-4b6a-a556-23e32ca11fe1\\n\r\n                       /v1/phoneid/standard/15555551234\",\r\n  \"errors\": [   {\r\n    \"code\": -30006,\r\n    \"description\": \"Invalid Signature.\"\r\n  }]\r\n}";
		
		Gson gson = new Gson();
		PhoneIdStandardResponse invalid_response = gson.fromJson(invalid_auth_json, PhoneIdStandardResponse.class);
		
		assertTrue(invalid_response.errors[0].description.equals("Invalid Signature."));
		
	}
	
	@Test
	public void StanardResponseTest() {
		String valid_json = "{\"reference_id\":\"013CA90BA4E5030BE4D449F000000C80\",\"sub_resource\":\"standard\",\"errors\":[],\"phone_type\":{\"code\":2,\"description\":\"MOBILE\"},\"status\":{\"updated_on\":\"2013-02-05T06:29:38.311333Z\",\"code\":300,\"description\":\"Transaction successfully completed\"},\"numbering\":{\"original\":{\"phone_number\":\"8582259543\",\"complete_phone_number\":\"18582259543\",\"country_code\":\"1\"},\"cleansing\":{\"sms\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100},\"call\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100}}},\"location\":{\"county\":\"San Diego\",\"city\":\"San Diego\",\"state\":\"CA\",\"zip\":\"92126\",\"country\":{\"iso2\":\"US\",\"iso3\":\"USA\",\"name\":\"United States\"},\"time_zone\":{\"utc_offset_min\":\"-8\",\"name\":\"America/Los_Angeles\",\"utc_offset_max\":\"-8\"},\"coordinates\":{\"latitude\":32.91585,\"longitude\":-117.13308},\"metro_code\":\"7320\"}}";
		Gson gson = new Gson();
		PhoneIdStandardResponse valid_response = gson.fromJson(valid_json, PhoneIdStandardResponse.class);
		assertTrue(valid_response.reference_id.equals("013CA90BA4E5030BE4D449F000000C80"));
		assertTrue(valid_response.toString().equals(valid_json));
		
	}
	@Test
	public void ContactResponseTest() {
		String valid_json = "{\"reference_id\":\"013CA90BA5DF030BE4D449F000000C81\",\"sub_resource\":\"contact\",\"errors\":[{\"code\":-60001,\"description\":\"PhoneID Contact Data Not Found\"}],\"phone_type\":{\"code\":2,\"description\":\"MOBILE\"},\"status\":{\"updated_on\":\"2013-02-05T06:29:41.604126Z\",\"code\":301,\"description\":\"Transaction partially completed\"},\"numbering\":{\"original\":{\"phone_number\":\"8582259543\",\"complete_phone_number\":\"18582259543\",\"country_code\":\"1\"},\"cleansing\":{\"sms\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100},\"call\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100}}},\"location\":{\"county\":\"San Diego\",\"city\":\"San Diego\",\"state\":\"CA\",\"zip\":\"92126\",\"country\":{\"iso2\":\"US\",\"iso3\":\"USA\",\"name\":\"United States\"},\"time_zone\":{\"utc_offset_min\":\"-8\",\"name\":\"America/Los_Angeles\",\"utc_offset_max\":\"-8\"},\"coordinates\":{\"latitude\":32.91585,\"longitude\":-117.13308},\"metro_code\":\"7320\"}}";
		Gson gson = new Gson();
		PhoneIdContactResponse valid_response = gson.fromJson(valid_json, PhoneIdContactResponse.class);
		assertTrue(valid_response.reference_id.equals("013CA90BA5DF030BE4D449F000000C81"));
		assertTrue(valid_response.toString().equals(valid_json));
	}
	@Test
	public void ScoreResponseTest() {
		String valid_json = "{\"reference_id\":\"013CA90BB661030BE4D449F000000C82\",\"sub_resource\":\"score\",\"errors\":[],\"status\":{\"updated_on\":\"2013-02-05T06:29:42.801614Z\",\"code\":300,\"description\":\"Transaction successfully completed\"},\"numbering\":{\"original\":{\"phone_number\":\"8582259543\",\"complete_phone_number\":\"18582259543\",\"country_code\":\"1\"},\"cleansing\":{\"sms\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100},\"call\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100}}},\"risk\":{\"level\":\"medium-low\",\"score\":300,\"recommendation\":\"allow\"}}";
		Gson gson = new Gson();
		PhoneIdScoreResponse valid_response = gson.fromJson(valid_json, PhoneIdScoreResponse.class);
		assertTrue(valid_response.reference_id.equals("013CA90BB661030BE4D449F000000C82"));
		assertTrue(valid_response.toString().equals(valid_json));
	}
	@Test
	public void LiveResponseTest() {
		String valid_json = "{\"reference_id\":\"013CA90BB7CF030BE4D449F000000C83\",\"sub_resource\":\"live\",\"errors\":[],\"phone_type\":{\"code\":2,\"description\":\"MOBILE\"},\"status\":{\"updated_on\":\"2013-02-05T06:29:43.932432Z\",\"code\":300,\"description\":\"Transaction successfully completed\"},\"numbering\":{\"original\":{\"phone_number\":\"8582259543\",\"complete_phone_number\":\"18582259543\",\"country_code\":\"1\"},\"cleansing\":{\"sms\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100},\"call\":{\"phone_number\":\"8582259543\",\"country_code\":\"1\",\"min_length\":10,\"max_length\":10,\"cleansed_code\":100}}},\"location\":{\"county\":\"San Diego\",\"city\":\"San Diego\",\"state\":\"CA\",\"zip\":\"92126\",\"country\":{\"iso2\":\"US\",\"iso3\":\"USA\",\"name\":\"United States\"},\"time_zone\":{\"utc_offset_min\":\"-8\",\"name\":\"America/Los_Angeles\",\"utc_offset_max\":\"-8\"},\"coordinates\":{\"latitude\":32.91585,\"longitude\":-117.13308},\"metro_code\":\"7320\"},\"live\":{\"subscriber_status\":\"ACTIVE\",\"device_status\":\"UNAVAILABLE\",\"roaming\":\"UNAVAILABLE\"}}";
		Gson gson = new Gson();
		PhoneIdLiveResponse valid_response = gson.fromJson(valid_json, PhoneIdLiveResponse.class);
		assertTrue(valid_response.reference_id.equals("013CA90BB7CF030BE4D449F000000C83"));
		assertTrue(valid_response.toString().equals(valid_json));
	}

}
