package com.telesign.telebureau;

import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import com.telesign.exception.TelesignAPIException;
import com.telesign.telebureau.response.TelebureauResponse;
import com.telesign.util.IpValidator;
import com.telesign.util.TeleSignRequest;
import com.telesign.util.TeleSignRequest.RequestBuilder;

public class Telebureau {
	// https://developer.telesign.com/docs/telebureau
	// https://github.com/TeleSign/python_telesign/blob/master/telesign/api.py
	private final String customerId;
	private final String secretKey;
	private int connectTimeout;
	private int readTimeout;
	private String httpsProtocol;
	private String ciphers;
	// private boolean runTests;
	private String url; // Not used yet
	private Map<String, String> extra;
	private String sessionId, originatingIp;
	private String ucid;

	private String fraud_type, occurred_at;
	private String verified_by = "None";
	private String verified_at = "None";
	private String discovered_at = "None";
	private String fraud_ip = "None";
	private String impact_type = "None";
	private String impact = "None";
	private boolean addToBlocklist;
	private String blocklistStartDate;
	private String blocklistEndDate;
	// private String timeout = "None";

	private final Gson gson = new Gson();

	private static final String TELEBUREAU_EVENT = "/v1/telebureau/event/";

	public static class TelebureauBuilder {
		private final String customerId;
		private final String secretKey;
		private int connectTimeout;
		private int readTimeout;
		private String httpsProtocol;
		private String ciphers;
		private Map<String, String> extra;
		private String url = "https://rest.telesign.com";
		private String sessionId, originatingIp;
		private String ucid;

		private String fraud_type, occurred_at;
		private String verified_by = "None";
		private String verified_at = "None";
		private String discovered_at = "None";
		private String fraud_ip = "None";
		private String impact_type = "None";
		private String impact = "None";

		private boolean addToBlocklist = false;
		private String blocklistStartDate;
		private String blocklistEndDate;

		// private String timeout = "None";

		/**
		 * The TelebureauBuilder class constructor. Once you instantiate a
		 * Telebureau object, you can use it to make instance calls to
		 * <em>Telebureau create</em>, <em>Telebureau retrieve</em>,
		 * <em>Telebureau delete</em>.
		 * 
		 * @param customerId
		 *            [Required] A string representing your TeleSign Customer
		 *            ID. This represents your TeleSign account number.
		 * @param secretKey
		 *            [Required] A string representing your TeleSign Secret Key
		 *            (available from the TeleSign Client Portal).
		 */
		public TelebureauBuilder(String customerId, String secretKey) {
			this.customerId = customerId;
			this.secretKey = secretKey;
		}

		/**
		 * Comma separated string containing list of ciphers, Ex: ciphers =
		 * "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_128_GCM_SHA25
		 * 6 , TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_128_CBC_SHA256,
		 * TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
		 * ,TLS_DHE_DSS_WITH_AES_128_CBC_SHA256".
		 * 
		 * @param ciphers
		 *            [optional] Please set the ciphers that you want to use.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder ciphers(String ciphers) {
			this.ciphers = ciphers;
			return this;
		}

		/**
		 * Set TeleSign REST api url
		 * 
		 * @param url
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * Connection timeout is the timeout in making the initial connection;
		 * i.e. completing the TCP connection handshake.
		 * 
		 * @param connectTimeout
		 *            int value representing connection timeout
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder connectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}

		/**
		 * The read timeout is the timeout on waiting to read data.
		 * Specifically, if the server fails to send a byte [timeout] seconds
		 * after the last byte, a read timeout error will be raised.
		 * 
		 * @param readTimeout
		 *            int value representing connection read timeout
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder readTimeout(int readTimeout) {
			this.readTimeout = readTimeout;
			return this;
		}

		/**
		 * @param httpsProtocol
		 *            The httpsProtocol you want to use.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder httpsProtocol(String httpsProtocol) {
			this.httpsProtocol = httpsProtocol;
			return this;
		}

		/**
		 * @param sessionId
		 *            [Optional] Your end users session id. Set it to "null" if
		 *            not sending session id.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder sessionId(String sessionId) {
			this.sessionId = sessionId;
			return this;
		}

		/**
		 * @param originatingIp
		 *            [Optional] Your end users IP Address. This value must be
		 *            in the format defined by IETF in the Internet-Draft
		 *            document titled Textual Representation of IPv4 and IPv6
		 *            Addresses. Ex: originatingIp=192.168.123.456. Set it to
		 *            null if not sending originating ip.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder originatingIp(String originatingIp) {
			this.originatingIp = originatingIp;
			return this;
		}

		/**
		 * @param extra
		 *            The Extra parameter you would like to send to Telesign to
		 *            take advantage of unreleased features.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder extra(Map<String, String> extra) {
			this.extra = extra;
			return this;
		}

		/**
		 * @param ucid
		 *            [Required] A string the specifies one of the <a href=
		 *            "http://docs.telesign.com/rest/content/xt/xt-use-case-codes.html#xref-use-case-codes"
		 *            >Use Case Codes</a>.
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder ucid(String ucid) {
			this.ucid = ucid;
			return this;
		}

		/**
		 * The type of fraud committed. List of available fraud type values:
		 * <ul>
		 * <li>chargeback</li>
		 * <li>coupon</li>
		 * <li>harass</li>
		 * <li>identity_theft</li>
		 * <li>other</li>
		 * <li>property_damage</li>
		 * <li>spam</li>
		 * <li>takeover</li>
		 * <li>telco</li>
		 * </ul>
		 * 
		 * @param fraud_type
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder fraud_type(String fraud_type) {
			this.fraud_type = fraud_type;
			return this;
		}

		/**
		 * A string value specifying when the fraud event occurred. The value
		 * must be in RFC 3339 time format (for example, yyyy-mm-ddThh:mm:ssZ).
		 * 
		 * @param occurred_at
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder occurred_at(String occurred_at) {
			this.occurred_at = occurred_at;
			return this;
		}

		/**
		 * A string enumeration value identifying the method used to verify the
		 * submitted phone number. List of available impact values:
		 * <ul>
		 * <li>telesign</li>
		 * <li>other</li>
		 * </ul>
		 * 
		 * @param verified_by
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder verified_by(String verified_by) {
			this.verified_by = verified_by;
			return this;
		}

		/**
		 * If you verified the user's phone number, then you use this parameter
		 * to specify when. The value must be in RFC 3339 time format (for
		 * example, yyyy-mm-ddThh:mm:ssZ).
		 * 
		 * @param verified_at
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder verified_at(String verified_at) {
			this.verified_at = verified_at;
			return this;
		}

		/**
		 * A string value specifying when you discovered the fraud event. The
		 * value must be in RFC 3339 time format (for example,
		 * yyyy-mm-ddThh:mm:ssZ).
		 * 
		 * @param discovered_at
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder discovered_at(String discovered_at) {
			this.discovered_at = discovered_at;
			return this;
		}

		/**
		 * If you managed to capture the user's IP address, then specify it
		 * here.
		 * 
		 * @param fraud_ip
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder fraud_ip(String fraud_ip) {
			this.fraud_ip = fraud_ip;
			return this;
		}

		/**
		 * A string enumeration value indicating the area of your business that
		 * was affected by this fraud event. List of available impact-type
		 * values:
		 * <ul>
		 * <li>revenue_loss</li>
		 * <li>operational_overhead</li>
		 * <li>customer_experience</li>
		 * <li>other</li>
		 * </ul>
		 * 
		 * @param impact_type
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder impact_type(String impact_type) {
			this.impact_type = impact_type;
			return this;
		}

		/**
		 * A string enumeration value indicating how severely your business was
		 * affected by this fraud event. List of available impact values:
		 * <ul>
		 * <li>low</li>
		 * <li>medium</li>
		 * <li>high</li>
		 * </ul>
		 * 
		 * @param impact
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder impact(String impact) {
			this.impact = impact;
			return this;
		}

		/**
		 * [Optional] This feature must be enabled in your account to be used.
		 * Please contact TeleSign Client Services to make changes to your
		 * account. A boolean indicating that you want the number listed to be
		 * added to your blocklist. These numbers will not be issued Verify
		 * requests until the block has passed its expiration date. Ex:
		 * add_to_blocklist=true
		 * 
		 * @param addToBlocklist
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder addToBlocklist(boolean addToBlocklist) {
			this.addToBlocklist = addToBlocklist;
			return this;
		}

		/**
		 * [Optional] If add_to_blocklist is true, then you use this parameter
		 * to specify when to start the block. If this value is not specified
		 * and add_to_blocklist is true, the current date and time is used. The
		 * value must be in RFC 3339 time format (for example,
		 * yyyy-mm-ddThh:mm:ssZ). [Example]
		 * blocklist_start_date=2014-01-24T00:43:44Z
		 * 
		 * @param blocklistStartDate
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder blocklistStartDate(String blocklistStartDate) {
			this.blocklistStartDate = blocklistStartDate;
			return this;
		}

		/**
		 * [Optional] If add_to_blocklist is true, then you use this parameter
		 * to specify when to end the block. If this value is not specified and
		 * add_to_blocklist is true, the block will never expire. The value must
		 * be in RFC 3339 time format (for example, yyyy-mm-ddThh:mm:ssZ).
		 * [Example] blocklist_end_date=2014-01-24T00:43:44Z
		 * 
		 * @param blocklistEndDate
		 * @return TelebureauBuilder A
		 *         {@link com.telesign.telebureau.Telebureau.TelebureauBuilder}
		 *         object.
		 */
		public TelebureauBuilder blocklistEndDate(String blocklistEndDate) {
			this.blocklistEndDate = blocklistEndDate;
			return this;
		}

		/*
		 * public TelebureauBuilder timeout(String timeout) { this.timeout =
		 * timeout; return this; }
		 */

		/**
		 * @return Telebureau A {@link com.telesign.telebureau.Telebureau}
		 *         object.
		 */
		public Telebureau create() {
			Telebureau TelebureauObj = new Telebureau(this);
			return TelebureauObj;
		}

	}

	public Telebureau(TelebureauBuilder builder) {
		this.customerId = builder.customerId;
		this.secretKey = builder.secretKey;
		this.connectTimeout = builder.connectTimeout;
		this.httpsProtocol = builder.httpsProtocol;
		this.readTimeout = builder.readTimeout;
		this.url = builder.url;
		this.extra = builder.extra;
		this.originatingIp = builder.originatingIp;
		this.sessionId = builder.sessionId;
		this.ucid = builder.ucid;
		this.ciphers = builder.ciphers;

		this.fraud_type = builder.fraud_type;
		this.occurred_at = builder.occurred_at;
		this.verified_by = builder.verified_by;
		this.verified_at = builder.verified_at;
		this.discovered_at = builder.discovered_at;
		this.fraud_ip = builder.fraud_ip;
		this.impact_type = builder.impact_type;
		this.impact = builder.impact;
		this.addToBlocklist = builder.addToBlocklist;
		this.blocklistStartDate = builder.blocklistStartDate;
		this.blocklistEndDate = builder.blocklistEndDate;
		// this.timeout = builder.timeout;
	}

	/**
	 * Send fraud_type, occurred_at in addition to phoneNo
	 * 
	 * @param phoneNo
	 */
	public TelebureauResponse createEvent(String phoneNo) {
		String result = null;
		try {

			TeleSignRequest tr = new RequestBuilder(customerId, secretKey)
					.baseUrl(url).subResource(TELEBUREAU_EVENT)
					.httpsProtocol(httpsProtocol).ciphers(ciphers)
					.httpMethod("POST").connectTimeout(connectTimeout)
					.readTimeout(readTimeout).create();
			String body = "phone_number=" + URLEncoder.encode(phoneNo, "UTF-8");

			if (null != fraud_type)
				body += "&fraud_type=" + URLEncoder.encode(fraud_type, "UTF-8");

			if (null != occurred_at)
				body += "&occurred_at="
						+ URLEncoder.encode(occurred_at, "UTF-8");

			if (null != verified_by)
				body += "&verified_by="
						+ URLEncoder.encode(verified_by, "UTF-8");

			if (null != verified_at)
				body += "&verified_at="
						+ URLEncoder.encode(verified_at, "UTF-8");

			if (null != discovered_at)
				body += "&discovered_at="
						+ URLEncoder.encode(discovered_at, "UTF-8");

			if (null != fraud_ip)
				body += "&fraud_ip=" + URLEncoder.encode(fraud_ip, "UTF-8");

			if (null != impact_type)
				body += "&impact_type="
						+ URLEncoder.encode(impact_type, "UTF-8");

			if (null != impact)
				body += "&impact=" + URLEncoder.encode(impact, "UTF-8");

			// need to check on this as not yet added to python sdk.
			if (!addToBlocklist) {
				body += "&add_to_blocklist="
						+ URLEncoder.encode(Boolean.toString(addToBlocklist),
								"UTF-8");
				if (null != blocklistStartDate)
					body += "&blocklist_start_date="
							+ URLEncoder.encode(blocklistStartDate, "UTF-8");
				if (null != blocklistEndDate)
					body += "&blocklist_end_date="
							+ URLEncoder.encode(blocklistEndDate, "UTF-8");
			}
			if (null != ucid)
				tr.addParam("ucid", ucid);

			if (null != extra)
				extraParams(tr);

			if (originatingIp != null && !originatingIp.isEmpty()
					&& IpValidator.isValidIpAddress(originatingIp)) {

				body += "&originating_ip="
						+ URLEncoder.encode(originatingIp, "UTF-8");
			}

			if (sessionId != null && !sessionId.isEmpty()) {

				body += "&session_id=" + URLEncoder.encode(sessionId, "UTF-8");
			}

			tr.setPostBody(body);

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException(
					"Exception while submitting Telebureau create event", e);
		}

		TelebureauResponse response = gson.fromJson(result,
				TelebureauResponse.class);

		return response;

	}

	public TelebureauResponse retrieve(String resource_id) {
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey)
					.baseUrl(url).subResource(TELEBUREAU_EVENT + resource_id)
					.httpsProtocol(httpsProtocol).ciphers(ciphers)
					.httpMethod("GET").connectTimeout(connectTimeout)
					.readTimeout(readTimeout).create();

			if (null != extra)
				extraParams(tr);

			if (originatingIp != null && !originatingIp.isEmpty()
					&& IpValidator.isValidIpAddress(originatingIp)) {

				tr.addParam("originating_ip", originatingIp);
			}

			if (sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException(
					"Exception while fetching status of Telebureau event associated with ref_id "
							+ resource_id + ": ", e);
		}
		TelebureauResponse response = gson.fromJson(result,
				TelebureauResponse.class);

		return response;
	}

	public TelebureauResponse delete(String resource_id) {
		String result = null;

		try {
			TeleSignRequest tr = new RequestBuilder(customerId, secretKey)
					.baseUrl(url).subResource(TELEBUREAU_EVENT + resource_id)
					.httpsProtocol(httpsProtocol).ciphers(ciphers)
					.httpMethod("DELETE").connectTimeout(connectTimeout)
					.readTimeout(readTimeout).create();

			if (null != extra)
				extraParams(tr);

			if (originatingIp != null) {

				tr.addParam("originating_ip", originatingIp);
			}

			if (sessionId != null) {

				tr.addParam("session_id", sessionId);
			}

			result = tr.executeRequest();
		} catch (Exception e) {
			throw new TelesignAPIException(
					"Exception while deleting of submitted Telebureau event associated with ref_id "
							+ resource_id + ": ", e);
		}
		TelebureauResponse response = gson.fromJson(result,
				TelebureauResponse.class);

		return response;
	}

	/**
	 * Parses extra params and adds to TeleSignRequest
	 * 
	 * @param tr
	 *            TeleSignRequest
	 */
	private void extraParams(TeleSignRequest tr) {
		for (Map.Entry<String, String> extraParam : extra.entrySet())
			tr.addParam(extraParam.getKey(), extraParam.getValue());
	}

	public static TelebureauBuilder initTelebureau(String customerId,
			String secretKey) {
		return new TelebureauBuilder(customerId, secretKey);
	}
}