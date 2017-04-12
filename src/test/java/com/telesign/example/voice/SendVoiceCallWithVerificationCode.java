package com.telesign.example.voice;

import com.telesign.RestClient;
import com.telesign.Util;
import com.telesign.VoiceClient;

public class SendVoiceCallWithVerificationCode {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "phone_number";
        String verifyCode = Util.randomWithNDigits(5);
        String message = String.format("Hello, your code is %s. Once again, your code is %s. Goodbye.", verifyCode, verifyCode);
        String messageType = "OTP";

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}