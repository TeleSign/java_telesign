package com.telesign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telesign.telebureau.Telebureau;
import com.telesign.telebureau.Telebureau.TelebureauBuilder;
import com.telesign.telebureau.response.TelebureauResponse;
import com.telesign.testUtil.TestUtil;

public class TelebureauTest {
	String referenceId;

	@BeforeClass
	public static void setUp() throws IOException {
		TestUtil.initProperties();
		if (TestUtil.runTests)
			TestUtil.startServer();
		else
			TestUtil.testUrl = "https://rest.telesign.com";
	}

	@AfterClass
	public static void close() {
		if (TestUtil.runTests)
			TestUtil.stopServer();
	}

	private Telebureau initParams() {
		if (TestUtil.CUSTOMER_ID.isEmpty() || TestUtil.SECRET_KEY.isEmpty()) {
			fail("TestUtil.CUSTOMER_ID and TestUtil.SECRET_KEY must be set to pass this test");
		}

		TelebureauBuilder builder = Telebureau.initTelebureau(
				TestUtil.CUSTOMER_ID, TestUtil.SECRET_KEY);
		builder.ciphers(TestUtil.CIPHERS)
				.connectTimeout(TestUtil.connectTimeout)
				.readTimeout(TestUtil.readTimeout)
				.httpsProtocol(TestUtil.HTTPS_PROTOCOL).url(TestUtil.testUrl);
		builder.extra(TestUtil.EXTRA_MAP)
				.originatingIp(TestUtil.ORIGINATING_IP)
				.sessionId(TestUtil.SESSION_ID).ucid(TestUtil.UCID);
		builder.fraud_type(TestUtil.FRAUD_TYPE)
				.occurred_at(TestUtil.OCCURRED_AT);
		builder.verified_by(TestUtil.VERIFIED_BY)
				.verified_at(TestUtil.VERIFIED_AT)
				.discovered_at(TestUtil.DISCOVERED_AT)
				.fraud_ip(TestUtil.FRAUD_IP).impact_type(TestUtil.IMPACT_TYPE)
				.impact(TestUtil.IMPACT);
		Telebureau telebureau = builder.create();

		return telebureau;
	}
	
	@Test
	public void createEvent() {
		Telebureau telebureau = initParams();

		TelebureauResponse response = telebureau
				.createEvent(TestUtil.PHONE_NUMBER);
		System.out.println(response.toString());
		referenceId = response.reference_id;
		assertNotNull(response);
		assertTrue(response.errors.length == 0);
		assertTrue(response.status.code == 1400 || response.status.code == 1401);
	}

	@Test
	public void retrieve() {
		Telebureau telebureau = initParams();

		TelebureauResponse response = telebureau.retrieve(referenceId);
		assertNotNull(response);
		assertTrue(response.errors.length == 0);
		assertTrue(response.status.code == 1400);
	}

	@Test
	public void delete() {
		Telebureau telebureau = initParams();

		TelebureauResponse response = telebureau.delete(referenceId);
		assertNotNull(response);
		assertTrue(response.errors.length == 0);
		assertTrue(response.status.code == 1400);
	}
}
