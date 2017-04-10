package com.telesign.rest.example.phoneid;

import com.telesign.rest.PhoneIdClient;
import com.telesign.rest.RestClient;

public class Cleansing {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String extraDigit = "0";
        String phoneNumber = "phone_number";
        String incorrectPhoneNumber = String.format("%s%s", phoneNumber, extraDigit);

        PhoneIdClient phoneIdClient = new PhoneIdClient(customerId, secretKey);
        RestClient.TelesignResponse telesignResponse = phoneIdClient.phoneid(incorrectPhoneNumber, null);

        if (telesignResponse.ok) {
            System.out.println(String.format("Cleansed phone number has country code %s and phone number is %s.",
                    telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("cleansing").getAsJsonObject("call").get("country_code").getAsString(),
                    telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("cleansing").getAsJsonObject("call").get("phone_number").getAsString()));

            System.out.println(String.format("Original phone number was %s.",
                    telesignResponse.json.getAsJsonObject("numbering").getAsJsonObject("original").get("complete_phone_number").getAsString()));
        }
    }
}