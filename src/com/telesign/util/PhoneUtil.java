package com.telesign.util;

/**
 *	Copyright (c) TeleSign Corporation 2012.
 *	License: MIT
 *	Support email address "support@telesign.com"
 *	Author: jweatherford
 */
public class PhoneUtil {

	/**
	 * helper method to validate numbers to 11 digits
	 * 
	 * @param phoneNumber 
	 * @return the phone number validated to 11 digits
	 */
	public static String formatTo11Digits(String phoneNumber){
	    if(phoneNumber == null) return null;
	    
	    //clean the phone number of anything but digits
	    String cleanPhone = phoneNumber.replaceAll("[^\\d]", "");
	    
	    try {
	        Long.parseLong(cleanPhone);
	    } catch(NumberFormatException nfe) {
	        return null;
	    }
	    
	    switch(cleanPhone.length()) {
	    case 0:
	    case 1:
	    case 2: 
	    case 3:
	    case 4:
	    case 5:
	    case 6:
	    case 7:
	    case 8:
	    case 9:
	        return null;
	    case 10:
	        return "1" + cleanPhone;
	    case 11:
	        return cleanPhone;
	    case 12: 
	        //if the phone number is 12 digits and starts with 11 or 01, treat it as country code and omit
	        //example 113102669619
	        if(cleanPhone.charAt(0) == '0') {
	            return cleanPhone.substring(1);
	        } 
	        return null;
	    case 13:
	        if(cleanPhone.startsWith("00")){
	            return cleanPhone.substring(2);
	        }
	        return null;
	    default:
	        return null;
	    }
	}

}
