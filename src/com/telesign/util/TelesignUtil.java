package com.telesign.util;

/**
 * Created by visweshwarganesh on 5/6/14.
 */
public class TelesignUtil {

    /**
     * Helper method for is Null or Empty
     */
    public static final boolean isNullOrEmpty(String value) {
        if (value == null || value == "") {
            return true;
        } else {
            return false;
        }
    }
}
