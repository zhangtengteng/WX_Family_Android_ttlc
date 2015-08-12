package com.xweisoft.wx.family.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * AES加密类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  guoac
 * @version  [版本号, 2015-1-15]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SecurityUtil
{
    private static String TAG = "===SecurityUtil===";
    
    private static final String AES = "AES";
    
    private static final String CRYPT_KEY = "1wifixsoftwasaes";
    
    /**
     * 加密(供外部调用)
     * <功能详细描述>
     * @param data
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public final static String encrypt(String data)
    {
        if (TextUtils.isEmpty(data))
            return null;
        try
        {
            return byte2hex(encrypt(data.getBytes(), CRYPT_KEY));
        }
        catch (Exception e)
        {
            LogX.getInstance().e(TAG, e.toString());
            return null;
        }
    }
    
    /**
     * 加密
     * <功能详细描述>
     * @param src
     * @param key
     * @return
     * @throws Exception [参数说明]
     * 
     * @return byte[] [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("TrulyRandom")
    private static byte[] encrypt(byte[] src, String key) throws Exception
    {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }
    
    /**
     * 二行制转十六进制字符串
     * <功能详细描述>
     * @param b
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("DefaultLocale")
    private static String byte2hex(byte[] b)
    {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
            {
                hs = hs + "0" + stmp;
            }
            else
            {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    
    /**
     * MD5 加密
     * <一句话功能简述>
     * <功能详细描述>
     * @param string
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String MD5(String string)
    {
        byte[] hash = null;
        try
        {
            hash = MessageDigest.getInstance("MD5")
                    .digest(string.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash)
        {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
            
        }
        return hex.toString();
    }
}
