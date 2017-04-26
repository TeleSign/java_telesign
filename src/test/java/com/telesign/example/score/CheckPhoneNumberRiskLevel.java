package com.telesign.example.score;

import com.telesign.RestClient;
import com.telesign.ScoreClient;

public class CheckPhoneNumberRiskLevel {

    public static void main(String[] args) {

        String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String phoneNumber = "phone_number";
        String accountLifecycleEvent = "create";

        try {
            ScoreClient scoreClient = new ScoreClient(customerId, apiKey);
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