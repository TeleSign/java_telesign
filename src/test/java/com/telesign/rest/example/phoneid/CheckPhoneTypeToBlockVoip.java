package com.telesign.rest.example.phoneid;

import com.telesign.rest.PhoneIdClient;
import com.telesign.rest.RestClient;

public class CheckPhoneTypeToBlockVoip {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String phoneTypeVoip = "5";

        PhoneIdClient phoneIdClient = new PhoneIdClient(customerId, secretKey);
        RestClient.TelesignResponse telesignResponse = phoneIdClient.phoneid(phoneNumber, null);

        if (telesignResponse.ok) {
            if (telesignResponse.json.getAsJsonObject("phone_type").get("code").getAsString().equals(phoneTypeVoip)) {
                System.out.println(String.format("Phone number %s is a VOIP phone.", phoneNumber));
            } else {
                System.out.println(String.format("Phone number %s is not a VOIP phone.", phoneNumber));
            }
        }
    }
}