package com.xweisoft.wx.family.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <一句话功能简述>
 * AES加密解密，采用byte2hex转码
 * 
 * @author  administrator
 * @version  [版本号, 2013-10-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AES
{
    private static final String TAG = "===AES===";
    
	/**
	 * [加密]<BR>
	 * @param sSrc 源字符串
	 * @param sKey 加密key
	 * @return 加密后字符串
	 * @throws Exception 异常抛出
	 */
	public static String encrypt(String sSrc, String sKey) throws Exception
	{
		if (sKey == null)
		{
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16)
		{
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		//此处使用BASE64做转码功能，同时能起到2次加密的作用。
		return Base64.encode(encrypted);
	}

	/**
	 * [解密]<BR>
	 * @param sSrc 源字符串
     * @param sKey 解密key
     * @return 解密后字符串
     * @throws Exception 异常抛出
	 */
	public static String decrypt(String sSrc, String sKey) throws Exception
	{
		try
		{
			// 判断Key是否正确
			if (sKey == null)
			{
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16)
			{
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(
					"0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64.decode(sSrc);//先用base64解密
			try
			{
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			}
			catch (Exception e)
			{
				LogX.getInstance().e(TAG, e.toString());
				return null;
			}
		}
		catch (Exception ex)
		{
		    LogX.getInstance().e(TAG, ex.toString());
			return null;
		}
	}
}
