package com.telesign.rest.example.messaging;

import com.telesign.rest.MessagingClient;
import com.telesign.rest.RestClient;
import com.telesign.rest.Util;

public class SendMessageWithVerificationCode {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String verifyCode = Util.randomWithNDigits(5);
        String message = String.format("Your code is %s", verifyCode);
        String messageType = "OTP";


        try {
            MessagingClient messagingClient = new MessagingClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}