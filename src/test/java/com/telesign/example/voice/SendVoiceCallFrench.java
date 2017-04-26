package com.telesign.example.voice;

import com.telesign.RestClient;
import com.telesign.VoiceClient;

import java.util.HashMap;

public class SendVoiceCallFrench {

    public static void main(String[] args) {

        String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String phoneNumber = "phone_number";
        String message = "N'oubliez pas d'appeler votre mère pour son anniversaire demain.";
        String messageType = "ARN";

        HashMap<String, String> params = new HashMap<>();
        params.put("voice", "f-FR-fr");

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}