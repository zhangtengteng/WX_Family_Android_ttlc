package com.xweisoft.wx.family.service.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * <一句话功能简述>
 *  Trust manager to accept any server
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FakeTrustManager implements X509TrustManager 
{
	private static final X509Certificate[] ACCEPTEDISSUERS = new X509Certificate[] {};
	
	/**
	 * check client trust
	 * @param chain chain
	 * @param authType auttype
	 * @throws CertificateException exception
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(
	 * java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException 
	{
	}
	
	/**
	 * check client trust
     * @param chain chain
     * @param authType auttype
     * @throws CertificateException exception
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(
	 *     java.security.cert.X509Certificate[], java.lang.String)
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException 
	{
	}

	/**
	 * Method to see if client certificate is trusted. Always returns true.
	 * 
	 * @param chain X509Certificate data
	 * @return Always returns true
	 */
	public boolean isClientTrusted(final X509Certificate[] chain) 
	{
		return true;
	}

	/**
	 * Method to see if server certificate is trusted. Always returns true.
	 * 
	 * @param chain X509Certificate data
	 * @return Always returns true
	 */
	public boolean isServerTrusted(final X509Certificate[] chain) 
	{
		return true;
	}

	/**
	 * Returns list of accepted certificates. As this is a fake trust manager,
	 * it is empty.
	 * @return x509 object
	 */
	public X509Certificate[] getAcceptedIssuers() 
	{
		return ACCEPTEDISSUERS;
	}
}
