/**
 * @version		1.0
 * @copyright		Copyright © 2013, TeleSign Corporation.
 * @license		http://opensource.org/licenses/mit-license.php The MIT License (MIT).
 * @author		J. Weatherford
 * @maintainer		Humberto Morales
 * @repository		https://github.com/TeleSign/java_telesign
 * @support		support@telesign.com
 */
package com.telesign.util;

/**
 * The PhoneUtil class contains a mentod for normalizing phone numbers.
 */
public class PhoneUtil
{
	/**
	 * This mentod is a helper method for cleansing phone numbers. 
	 * Specifically, it ensures that all phone number conform to a standard 11 digit format.
	 * For example, (310) 555-1212 becomes 13105551212.
	 * @param phoneNumber	[Required] A string that contains the user's phone number, in its original form. 
	 * @return					The cleansed form of the phone number.
	 */
	public static String formatTo11Digits(String phoneNumber)
	{
		if(phoneNumber == null) return null;

		// Remove non-numeric characters (leaving digits 0..9). 
		String cleanPhone = phoneNumber.replaceAll("[^\\d]", "");

		// Assert that the characters in the cleaned phone number are all numeric.
		try
		{
			Long.parseLong(cleanPhone);		// Convert the string representation of the cleaned phone number to a long value. 
		}
		catch(NumberFormatException nfe)
		{
			return null;
		}

		switch(cleanPhone.length())
		{
			case 0:	case 1:	case 2:	case 3:	case 4:
			case 5:	case 6:	case 7:	case 8:	case 9:
				return null;
			case 10:						// Missing the dialing code for North America (+1).
				return "1" + cleanPhone;
			case 11:
				return cleanPhone;
			case 12: 
				// If the phone number is 12 digits long, and begins with either "11" or "01", then treat it as country code and omit.
				// For example 113102669619
				if(cleanPhone.charAt(0) == '0')
				{
					return cleanPhone.substring(1);		// Remove the leading zero.
				} 
				return null;
			case 13:
				if(cleanPhone.startsWith("00"))
				{
					return cleanPhone.substring(2);		// Remove the leading two zeros.
				}
				return null;
			default:
				return null;
		}
	}
}
