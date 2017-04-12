package com.telesign.rest.example.voice;

import com.telesign.rest.RestClient;
import com.telesign.rest.VoiceClient;

import java.util.HashMap;

public class SendVoiceCallFrench {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "13107705278";
        String message = "N'oubliez pas d'appeler votre mère pour son anniversaire demain.";
        String messageType = "ARN";

        HashMap<String, String> params = new HashMap<>();
        params.put("voice", "f-FR-fr");

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}