package com.telesign.example.phoneid;

import com.telesign.PhoneIdClient;
import com.telesign.RestClient;

public class Cleansing {

    public static void main(String[] args) {

      	String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
	    String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String extraDigit = "0";
        String phoneNumber = "phone_number";
        String incorrectPhoneNumber = String.format("%s%s", phoneNumber, extraDigit);

        try {
            PhoneIdClient phoneIdClient = new PhoneIdClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = phoneIdClient.phoneid(incorrectPhoneNumber, null);

            if (telesignResponse.ok) {
                System.out.println(String.format("Cleansed phone number has country code %s and phone number is %s.",
                        telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("cleansing").getAsJsonObject("call").get("country_code").getAsString(),
                        telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("cleansing").getAsJsonObject("call").get("phone_number").getAsString()));

                System.out.println(String.format("Original phone number was %s.",
                        telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("original").get("complete_phone_number").getAsString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
