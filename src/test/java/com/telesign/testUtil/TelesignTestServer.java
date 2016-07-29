package com.telesign.testUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TelesignTestServer implements Container {

	public static String EMTPY_STRING = "";

	public static int serverPort;

	public static String KEYSTORE_PROPERTY = "javax.net.ssl.keyStore";

	public static String KEYSTORE_PASSWORD_PROPERTY = "javax.net.ssl.keyStorePassword";

	public static String KEYSTORE_TYPE_PROPERTY = "javax.net.ssl.keyStoreType";

	public static String KEYSTORE_ALIAS_PROPERTY = "javax.net.ssl.keyStoreAlias";

	public static String RESOURCE_DIR = "src/test/resources/";
	
	public static String JSON_RESOURCE_DIR = "src/test/resources/json/";
	
	private static Server server;
	private static Connection connectionHttps;

	public static boolean debug = false;

	public void handle(Request request, Response resp) {
		try {
			if (debug)
				TelesignTestServer.logRequest(request);
			
			String requestUrl = request.getPath().toString();
			String path = TestUtil.path.get(requestUrl.replaceAll("\\d*$", ""));
			TestUtil.Path resource = TestUtil.Path.valueOf(path);
			
			StringBuilder jsonFilePath = new StringBuilder(JSON_RESOURCE_DIR);
			switch (resource){
			case V1_PHONEID_STANDARD: 
				jsonFilePath = jsonFilePath.append("phoneId_standard.json");
				break;
			case V1_PHONEID_CONTACT:
				jsonFilePath = jsonFilePath.append("phoneId_contact.json");
				break;				
			case V1_PHONEID_LIVE:
				jsonFilePath = jsonFilePath.append("phoneId_live.json");
				break;
			case V1_PHONEID_SCORE:
				jsonFilePath = jsonFilePath.append("phoneId_score.json");
				break;
			case V1_PHONEID_SIM_SWAP_CHECK:
				jsonFilePath = jsonFilePath.append("phoneId_sim_swap.json");
				break;
			case V1_PHONEID_CALL_FORWARD:
				jsonFilePath = jsonFilePath.append("phoneId_call_forward.json");
				break;
			case V1_PHONEID_NUMBER_DEACTIVATION:
				jsonFilePath = jsonFilePath.append("phoneId_number_deactivation.json");
				break;
			case V1_VERIFY_CALL:
				jsonFilePath = jsonFilePath.append("verify_call.json");
				break;
			case V1_VERIFY_SMART:
				jsonFilePath = jsonFilePath.append("verify_smart_verify.json");
				break;
			case V1_VERIFY_SMS:
				jsonFilePath = jsonFilePath.append("verify_sms.json");
				break;
			case V2_VERIFY_PUSH:
				jsonFilePath = jsonFilePath.append("verify_push.json");
				break;
			case V2_VERIFY_REGISTRATION:
				jsonFilePath = jsonFilePath.append("verify_registration.json");
				break;
			case V2_VERIFY_TOKEN:
				jsonFilePath = jsonFilePath.append("verify_soft_token.json");
				break;
			default:
				break;
			
			}
			
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = ((JsonObject) jsonParser
					.parse(new FileReader(jsonFilePath.toString())));

			PrintStream body = resp.getPrintStream();
			long time = System.currentTimeMillis();

			resp.setCode(200);
			resp.setDescription("OK");
			resp.setValue("Content-Type", "application/json; charset=UTF-8");
			resp.setValue("Server", "Telesign/v1.0.0 (Embedded server)");
			resp.setInteger("Content-Length", jsonObject.toString().length());
			resp.setValue("Connection", "keep-alive");
			resp.setDate("Date", System.currentTimeMillis());
			resp.setValue("Last-Modified", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(Calendar.getInstance().getTime()));
			resp.setStatus(Status.OK);

			body.println(jsonObject.toString());
			body.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void create() throws Exception {

		serverPort = 1443;
		if (debug)
			System.setProperty("javax.net.debug", "ssl");
		System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		System.setProperty("sun.security.ssl.allowLegacyHelloMessages", "true");
		System.setProperty(KEYSTORE_PROPERTY, RESOURCE_DIR
				+ "testkeystore.jks");
		System.setProperty(KEYSTORE_PASSWORD_PROPERTY,
				"123456");

		Container container = new TelesignTestServer();
		SocketAddress address = new InetSocketAddress(
				TelesignTestServer.serverPort);

		SSLContext sslContext = TelesignTestServer.createSSLContext();
		server = new ContainerServer(container);
		connectionHttps = new SocketConnection(server);
		connectionHttps.connect(address, sslContext);

	}

	public static SSLContext createSSLContext() throws Exception {

		String keyStoreFile = System
				.getProperty(TelesignTestServer.KEYSTORE_PROPERTY);
		String keyStorePassword = System.getProperty(
				TelesignTestServer.KEYSTORE_PASSWORD_PROPERTY,
				TelesignTestServer.EMTPY_STRING);
		String keyStoreType = System.getProperty(
				TelesignTestServer.KEYSTORE_TYPE_PROPERTY,
				KeyStore.getDefaultType());

		KeyStore keyStore = loadKeyStore(keyStoreFile, keyStorePassword,
				keyStoreType);

		KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

		SSLContext sslContext = SSLContext.getInstance("TLS");

		TrustManager[] trustAllCerts = trustCertificates();

		sslContext.init(keyManagerFactory.getKeyManagers(), null,
				new SecureRandom());

		if (debug)
			printServerCiphersANDProtocols(sslContext);
		return sslContext;
	}

	/**
	 * @param keyStoreFile
	 * @param keyStorePassword
	 * @param keyStoreType
	 * @return KeyStore
	 * @throws FileNotFoundException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 */
	public static KeyStore loadKeyStore(String keyStoreFile,
			String keyStorePassword, String keyStoreType)
			throws FileNotFoundException, KeyStoreException, IOException,
			NoSuchAlgorithmException, CertificateException {
		KeyStore keyStore = null;
		FileInputStream keyStoreFileInpuStream = null;
		try {
			if (keyStoreFile != null) {
				keyStoreFileInpuStream = new FileInputStream(keyStoreFile);

				keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(keyStoreFileInpuStream,
						keyStorePassword.toCharArray());
			}
		} finally {
			if (keyStoreFileInpuStream != null) {
				keyStoreFileInpuStream.close();
			}
		}
		return keyStore;
	}

	/**
	 * For trusting all certificates.
	 * 
	 * @return TrustManager[]
	 */
	public static TrustManager[] trustCertificates() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) throws CertificateException {
				return;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) throws CertificateException {
				return;
			}
		} };
		return trustAllCerts;
	}

	/**
	 * Used in debugging TestServer
	 * 
	 * @param sslContext
	 * @throws IOException
	 */
	public static void printServerCiphersANDProtocols(SSLContext sslContext)
			throws IOException {
		String[] cipherSuites = sslContext.getSocketFactory()
				.getDefaultCipherSuites();
		for (String cipher : cipherSuites)
			System.out.println("Server Cipher: " + cipher);

		SSLSocket socket = (SSLSocket) sslContext.getSocketFactory()
				.createSocket();
		System.out.println("Local address: "
				+ socket.getLocalAddress().toString());
		String[] protocols = socket.getSupportedProtocols();
		for (String protocol : protocols)
			System.out.println("Available protocol: " + protocol);
	}

	/**
	 * Logs request received by server. Used in debugging server.
	 * 
	 * @param request
	 * @throws IOException
	 */
	public static void logRequest(final Request request) throws IOException {
		StringBuilder builder = new StringBuilder();

		builder.append(">>>RECEIVED REQUEST: \n");
		builder.append(request);
		builder.append(request.getContent());

		System.out.println(builder);
	}

	/**
	 * Call to stop test server
	 * 
	 * @throws IOException
	 */
	public static void stopServer() {
		try {
			connectionHttps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
