package com.telesign.example.messaging;

import com.telesign.MessagingClient;
import com.telesign.RestClient;
import com.telesign.Util;

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