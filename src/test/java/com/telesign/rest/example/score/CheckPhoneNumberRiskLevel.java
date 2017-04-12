package com.telesign.rest.example.score;

import com.telesign.rest.RestClient;
import com.telesign.rest.ScoreClient;

public class CheckPhoneNumberRiskLevel {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String accountLifecycleEvent = "create";

        try {
            ScoreClient scoreClient = new ScoreClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = scoreClient.score(phoneNumber, accountLifecycleEvent, null);

            if (telesignResponse.ok) {
                System.out.println(String.format("Phone number %s has a '%s' risk level and the recommendation is to '%s' the transaction.",
                        phoneNumber,
                        telesignResponse.json.getAsJsonObject("risk").get("level").getAsString(),
                        telesignResponse.json.getAsJsonObject("risk").get("recommendation").getAsString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}