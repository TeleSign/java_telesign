package com.telesign.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TLSSocketFactory extends SSLSocketFactory {

	public SSLContext sslContext;
	private SSLSocketFactory sslSocketFactory;
	public static String ciphers, protocols;
	public static String SRC_RESOURCE = "src/main/resources/";
	public static String REQUEST_PROPERTIES = "telesignRequest.properties";
	public static String URL_HOST;
	public static final String LOCALHOST = "localhost";

	private static final String PREFFERED_CIPHERS = "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,"
			+ "TLS_RSA_WITH_AES_128_GCM_SHA256,TLS_RSA_WITH_AES_256_GCM_SHA384,"
			+ "TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256";
	private static final String PREFERRED_PROTOCOLS = "TLSv1.1,TLSv1.2,TLSv1.3";	
	
	public TLSSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, IOException{
		initTLSSocketFactory();		
	}
	
	public SSLContext getSSLContext(){		
		return sslContext;
	}
	private void initTLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, IOException{
		// If ciphers have not been set or set as null then use the preferred protocols
		if(null == ciphers || ciphers.isEmpty())
			ciphers = PREFFERED_CIPHERS;
		if(protocols.isEmpty())
			protocols = PREFERRED_PROTOCOLS;
		
		sslContext = SSLContext.getInstance("TLS"); // This also sneaks TLSv1 & sslv3, we restrict this by passing allowed protocols
		
		if(URL_HOST.equalsIgnoreCase(LOCALHOST)){
			TrustManager[] trustAllCerts = trustCertificates();
			sslContext.init(null,trustAllCerts,new SecureRandom());			
			//printServerCiphersANDProtocols(sslContext);
		}else{
			sslContext.init(null,null,new SecureRandom());
		}
		
		sslSocketFactory = sslContext.getSocketFactory();
		// If unit test cases not running and ciphers are not null
		if(!URL_HOST.equalsIgnoreCase(LOCALHOST) && null != ciphers)
			cipherSuite = getCipherList();
		protocolList = getProtocolList();
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
			int localPort) throws IOException {
		
		return enableTLSOnSocket(sslSocketFactory.createSocket(address, port, localAddress, localPort));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		
		return enableTLSOnSocket(sslSocketFactory.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		
		return enableTLSOnSocket(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		
		return enableTLSOnSocket(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(Socket s, String host, int port,	boolean autoClose) throws IOException {

		return enableTLSOnSocket(sslSocketFactory.createSocket(s, host, port, autoClose));
	}
	
	@Override
	public String[] getSupportedCipherSuites() {
		
		return cipherSuite;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return cipherSuite;
	}

	
	private Socket enableTLSOnSocket(Socket socket) {
        if(socket != null && (socket instanceof SSLSocket)) {
        	((SSLSocket)socket).setEnabledProtocols(protocolList);
        	if(!URL_HOST.equalsIgnoreCase(LOCALHOST))
        		((SSLSocket)socket).setEnabledCipherSuites(cipherSuite);            
        }
        return socket;
    }	
	
	private String[] cipherSuite;
	
	protected String[] getCipherList()
    {   // Passing list of supported ciphers        
		String[] preferredCiphers = ciphers.split(",");
		
        String[] availableCiphers = null;

        availableCiphers = sslSocketFactory.getSupportedCipherSuites();
        Arrays.sort(availableCiphers); // Later on going to apply binary search       
        
        List<String> cipherList = new ArrayList<String>();
        for(int i = 0; i < preferredCiphers.length; i++)
        {
            int idx = Arrays.binarySearch(availableCiphers, preferredCiphers[i]);
            if(idx >= 0)
            	cipherList.add(preferredCiphers[i]);
        }

        return cipherList.toArray(new String[0]);
    }	
	
	private String[] protocolList;
	
	protected String[] getProtocolList()
    {	// List of preferred protocols to be used        
		String[] preferredProtocols = protocols.split(",");
		
        String[] availableProtocols = null;

        SSLSocket socket = null;

                  
        try {
			socket = (SSLSocket)sslSocketFactory.createSocket();
			
			availableProtocols = socket.getSupportedProtocols();
			Arrays.sort(availableProtocols); // later going to apply binary search
			
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        

        List<String> protocolList = new ArrayList<String>();
        for(int i = 0; i < preferredProtocols.length; i++)
        {
            int idx = Arrays.binarySearch(availableProtocols, preferredProtocols[i]);
            if(idx >= 0)
            	protocolList.add(preferredProtocols[i]);
        }

        return protocolList.toArray(new String[0]);
    }
	
	public static TrustManager[] trustCertificates() {
		TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
 
                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
 
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };
		return trustAllCerts;
	}

	public void runTests(){
	    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
	        {
	            public boolean verify(String hostname, SSLSession session)
	            {	                
	                return true;	                
	            }
	        });
	}
	
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
}
