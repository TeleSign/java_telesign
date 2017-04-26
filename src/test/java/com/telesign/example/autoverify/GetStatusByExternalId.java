package com.telesign.example.autoverify;

import com.telesign.AutoVerifyClient;
import com.telesign.RestClient;

public class GetStatusByExternalId {

    public static void main(String[] args) {

        String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String externalId = "external_id";

        try {
            AutoVerifyClient autoverifyClient = new AutoVerifyClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = autoverifyClient.status(externalId, null);

            if (telesignResponse.ok) {
                System.out.println(String.format("AutoVerify transaction with external_id %s has status code %s and status description %s.",
                        externalId,
                        telesignResponse.json.getAsJsonObject("status").get("code").getAsString(),
                        telesignResponse.json.getAsJsonObject("status").get("description").getAsString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}