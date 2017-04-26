package com.telesign.example.phoneid;

import com.telesign.PhoneIdClient;
import com.telesign.RestClient;

public class CheckPhoneTypeToBlockVoip {

    public static void main(String[] args) {

        String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String phoneNumber = "phone_number";
        String phoneTypeVoip = "5";

        try {

            PhoneIdClient phoneIdClient = new PhoneIdClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = phoneIdClient.phoneid(phoneNumber, null);

            if (telesignResponse.ok) {
                if (telesignResponse.json.getAsJsonObject("phone_type").get("code").getAsString().equals(phoneTypeVoip)) {
                    System.out.println(String.format("Phone number %s is a VOIP phone.", phoneNumber));
                } else {
                    System.out.println(String.format("Phone number %s is not a VOIP phone.", phoneNumber));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}