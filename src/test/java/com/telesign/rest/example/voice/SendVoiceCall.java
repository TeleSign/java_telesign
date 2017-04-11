package com.telesign.rest.example.voice;

import com.telesign.rest.RestClient;
import com.telesign.rest.VoiceClient;

public class SendVoiceCall {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String message = "You're scheduled for a dentist appointment at 2:30PM.";
        String messageType = "ARN";

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, null);
        } catch (RestClient.TelesignException e) {
            e.printStackTrace();
        }
    }
}