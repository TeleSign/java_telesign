package com.telesign.util;


public class ProxyHostPort {

  private final String proxyHost;
  private final int proxyPort;

  private ProxyHostPort(String proxyHost, int proxyPort) {
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
  }

  public String getProxyHost() {
    return proxyHost;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  public static ProxyHostPort buildProxy(String proxyHost, int proxyPort) {
    return (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) ?
        new ProxyHostPort(proxyHost, proxyPort) :
        null;
  }
}
