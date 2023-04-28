package com.mudxx.common.utils.crypt;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * description: 3DES加密工具类
 * @author laiwen
 * @date 2019-07-09 19:03:37
 */
@SuppressWarnings("ALL")
public class DESUtil {

    /**
     * 3DES加密
     * @param plainText 明文
     * @param secretKey 密钥（32位）
     * @return 返回密文
     */
    public static String encode(String plainText, String secretKey) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        Key deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(secretKey.substring(0, 8).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes("utf-8"));
        //Base64加密
        return Base64Util.encode(encryptData);
    }

    /**
     * 3DES解密
     * @param encryptText 密文
     * @param secretKey 密钥（32位）
     * @return 返回明文
     */
    public static String decode(String encryptText, String secretKey) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        Key deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(secretKey.substring(0, 8).getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        //Base64解密
        byte[] decryptData = cipher.doFinal(Base64Util.decode(encryptText));
        return new String(decryptData, "utf-8");
    }

}
