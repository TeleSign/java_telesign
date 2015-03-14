package com.telesign.util;

/**
 * Created by visweshwarganesh on 5/6/14.
 */

/**
 * This would be the default delivery methods
 */
public enum DeliveryMethod {


    SMS("sms"),
    VOICE("call");


    private String deliveryMethod;


    DeliveryMethod(String text) {
        this.deliveryMethod = text;
    }


    public String toString() {
        return this.deliveryMethod;
    }
}
