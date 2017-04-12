package com.telesign.example.autoverify;

import com.telesign.AutoVerifyClient;
import com.telesign.RestClient;

public class GetStatusByExternalId {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String externalId = "external_id";

        try {
            AutoVerifyClient autoverifyClient = new AutoVerifyClient(customerId, secretKey);
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