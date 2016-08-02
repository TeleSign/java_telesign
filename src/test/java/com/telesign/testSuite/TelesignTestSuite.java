package com.telesign.testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.telesign.PhoneIdTest;
import com.telesign.TeleSignRequestTest;
import com.telesign.VerifyTest;
import com.telesign.response.ResponseTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = { TeleSignRequestTest.class, 
		VerifyTest.class, 
		PhoneIdTest.class, 
		ResponseTest.class })
public class TelesignTestSuite {

}
