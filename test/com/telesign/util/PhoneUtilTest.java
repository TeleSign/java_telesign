package com.telesign.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhoneUtilTest {

	@Test
	public void PhoneFormatTest() {
		String phone1 = "0013105551234";
		String phone2 = "013105551234";
		String phone3 = "13105551234";
		String phone4 = "3105551234";
		String phone5 = "05551234";
		
		assertTrue(PhoneUtil.formatTo11Digits(phone1).equals("13105551234"));
		assertTrue(PhoneUtil.formatTo11Digits(phone2).equals("13105551234"));
		assertTrue(PhoneUtil.formatTo11Digits(phone3).equals("13105551234"));
		assertTrue(PhoneUtil.formatTo11Digits(phone4).equals("13105551234"));
		assertNull(PhoneUtil.formatTo11Digits(phone5));
		
	}
	
	@Test
	public void PhoneFormatNonNumber() {
		String notPhone = "JUNK";
		assertNull(PhoneUtil.formatTo11Digits(notPhone));
		
	}

}
