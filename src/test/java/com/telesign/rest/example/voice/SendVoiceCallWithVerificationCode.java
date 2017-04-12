package com.telesign.rest.example.voice;

import com.telesign.rest.RestClient;
import com.telesign.rest.Util;
import com.telesign.rest.VoiceClient;

public class SendVoiceCallWithVerificationCode {

    public static void main(String[] args) {

        String customerId = "customer_id";
        String secretKey = "secret_key";

        String phoneNumber = "13107705278";
        String verifyCode = Util.randomWithNDigits(5);
        String verifyCodeWithCommas = String.join(", ", verifyCode.split(""));
        String message = String.format("Hello, your code is %s. Once again, your code is %s. Goodbye.", verifyCodeWithCommas, verifyCodeWithCommas);
        String messageType = "OTP";

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, secretKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}