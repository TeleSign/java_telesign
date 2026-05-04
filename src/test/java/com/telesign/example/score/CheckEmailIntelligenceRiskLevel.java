package com.telesign.example.score;

import com.google.gson.JsonObject;
import com.telesign.RestClient;
import com.telesign.ScoreClient;

public class CheckEmailIntelligenceRiskLevel {

    public static void main(String[] args) {

        String customerId = "ABC1DE23-A12B-1234-56AB-AB12345678900";
        String apiKey = "ABC12345yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String emailAddress = "support@vero-finto.com";
        String accountLifecycleEvent = "create";

        try {
            ScoreClient scoreClient = new ScoreClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = scoreClient.emailIntelligence(emailAddress, accountLifecycleEvent, null);

            if (telesignResponse.ok) {
                JsonObject intelligenceDetails = telesignResponse.json.getAsJsonObject("intelligence_details");
                JsonObject risk = intelligenceDetails.getAsJsonObject("risk");

                System.out.printf("Email address %s intelligence report:%n",
                        emailAddress);
                System.out.printf("  Risk Level: %s%n",
                        risk.get("level").getAsString());
                System.out.printf("  Risk Score: %d%n",
                        risk.get("score").getAsInt());
                System.out.printf("  Recommendation: %s%n",
                        risk.get("recommendation").getAsString());
            } 
            else {
                System.out.println("Request failed with status: " + telesignResponse.statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
