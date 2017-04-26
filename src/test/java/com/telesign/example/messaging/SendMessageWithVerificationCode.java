package com.telesign.example.messaging;

import com.telesign.MessagingClient;
import com.telesign.RestClient;
import com.telesign.Util;

public class SendMessageWithVerificationCode {

    public static void main(String[] args) {

        String customerId = "FFFFFFFF-EEEE-DDDD-1234-AB1234567890";
        String apiKey = "EXAMPLE----TE8sTgg45yusumoN6BYsBVkh+yRJ5czgsnCehZaOYldPJdmFh6NeX8kunZ2zU1YWaUw/0wV6xfw==";

        String phoneNumber = "phone_number";
        String verifyCode = Util.randomWithNDigits(5);
        String message = String.format("Your code is %s", verifyCode);
        String messageType = "OTP";


        try {
            MessagingClient messagingClient = new MessagingClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}