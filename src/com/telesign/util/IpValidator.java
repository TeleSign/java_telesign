package com.telesign.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/** This class provides methods to validate a IP address.**/
public class IpValidator {
	private static Pattern IPV4_PATTERN;
	private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";	

	static {
		try {
			IPV4_PATTERN = Pattern.compile(ipv4Pattern,	Pattern.CASE_INSENSITIVE);			
		} catch (PatternSyntaxException e) {

		}
	}

	/**
	 * Determine if the given string is a valid IPv4 or IPv6 address. 
	 * 
	 * @param ipAddress
	 *            A string that is to be examined to verify whether or not it
	 *            could be a valid IP address.
	 * @return <code>true</code> if the string is a value that is a valid IP
	 *         address, <code>false</code> otherwise.
	 */
	public static boolean isValidIpAddress(final String ipAddress) {
		if (null == ipAddress || ipAddress.isEmpty()) {
			return false;
		}	
		return isValidIpv4Address(ipAddress) || isValidIpv6Address(ipAddress);
	}
	
	/**
	 * Determines whether the string is a valid IPv4 address.
	 *  
	 * @param ipv4Address A string to be examined for a valid ipv4 address.
	 * @return <code>true</code> if the string is a value that is a valid IPv4
	 *         address, <code>false</code> otherwise.
	 **/
	private static boolean isValidIpv4Address(String ipv4Address) {
		Matcher ipv4 = IpValidator.IPV4_PATTERN.matcher(ipv4Address);
		return ipv4.matches();		
	}
	
	/**
	 * Determines whether the string is a valid IPv6 address.</br>
	 * Logic for validation based on Apache commons InetAddressValidator 
	 * 
	 * @param ipv6Address A string to be examined for a valid ipv6 address.
	 * @return <code>true</code> if the string is a value that is a valid IPv6
	 *         address, <code>false</code> otherwise.
	 **/
	private static boolean isValidIpv6Address(String ipv6Address) {
		boolean containsCompressedZeroes = ipv6Address.indexOf("::") > -1;														
																			
		if (containsCompressedZeroes
				&& (ipv6Address.indexOf("::") != ipv6Address
						.lastIndexOf("::"))) {
			return false;
		}
		if ((ipv6Address.startsWith(":") && !ipv6Address.startsWith("::"))
				|| (ipv6Address.endsWith(":") && !ipv6Address.endsWith("::"))) {
			return false;
		}
		Object[] octets = ipv6Address.split(":");
		if (containsCompressedZeroes) {
			List octetList = new ArrayList(Arrays.asList(octets));
			if (ipv6Address.endsWith("::")) {
				// String.split() drops ending empty segments
				octetList.add("");
			} else if (ipv6Address.startsWith("::") && !octetList.isEmpty()) {
				octetList.remove(0);
			}
			octets = octetList.toArray();
		}
		if (octets.length > 8) {
			return false;
		}
		int validOctets = 0;
		int emptyOctets = 0;
		for (int index = 0; index < octets.length; index++) {
			String octet = (String) octets[index];
			if (octet.length() == 0) {
				emptyOctets++;
				if (emptyOctets > 1) {
					return false;
				}
			} else {
				emptyOctets = 0;
				if (octet.indexOf(".") > -1) {
					if (!ipv6Address.endsWith(octet)) {
						return false;
					}
					if (index > octets.length - 1 || index > 6) {
						// IPV4 occupies last two octets
						return false;
					}
					if (!isValidIpv4Address(octet)) {
						return false;
					}
					validOctets += 2;
					continue;
				}
				if (octet.length() > 4) {
					return false;
				}
				int octetInt = 0;
				try {
					octetInt = Integer.valueOf(octet, 16).intValue();
				} catch (NumberFormatException e) {
					return false;
				}
				if (octetInt < 0 || octetInt > 0xffff) {
					return false;
				}
			}
			validOctets++;
		}
		if (validOctets < 8 && !containsCompressedZeroes) {
			return false;
		}
		return true;
	}

}
