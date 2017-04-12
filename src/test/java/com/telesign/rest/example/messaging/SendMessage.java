package com.telesign.rest.example.messaging;

import com.telesign.rest.MessagingClient;
import com.telesign.rest.RestClient;

public class SendMessage {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String message = "You're scheduled for a dentist appointment at 2:30PM.";
        String messageType = "ARN";

        try {
            MessagingClient messagingClient = new MessagingClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}