package com.mudxx.common.utils.crypt.md5;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description MD5加密工具类（5种加密方式结果都相同）
 * @author laiwen
 * @date 2018年7月12日 下午4:27:03
 */
@SuppressWarnings("ALL")
public class Md5Util {
	
	//===========================================【MD5加密方式一】===============================================//
	
	/**
	 * @description 使用md5的算法进行加密
	 * @param clearText 待加密的字符串（明文）
	 * @return 返回md5加密后的字符串（密文）（32位）
	 */
	public static String md5(String clearText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(clearText.getBytes());
			byte[] b = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			clearText = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clearText;
	}
	
	//===========================================【MD5加密方式二】===============================================//
	
	/**
	 * @description 使用md5的算法进行加密
	 * @param clearText 待加密的字符串（明文）
	 * @return 返回md5加密后的字符串（密文）（32位）
	 */
	public static String digestByMd5(String clearText) {
		byte[] secretBytes;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(clearText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		// 16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}
	
	//===========================================【MD5加密方式三】===============================================//
	
	/**
	 * @description 判断字符串的md5加密码是否与一个已知的md5加密码相匹配 
	 * @param clearText 明文
	 * @param cipherText 密文
	 * @return 如果匹配返回true，否则返回false
	 */
	public static boolean checkClearText(String clearText, String cipherText) {  
        String s = getMd5(clearText);
        return s.equals(cipherText);  
    }
	
	/**
	 * @description 使用md5的算法进行加密
	 * @param clearText 待加密的字符串（明文）
	 * @return 返回md5加密后的字符串（密文）（32位）
	 */
	public static String getMd5(String clearText) {
        return getMd5(clearText.getBytes());
    }
	
	/**
	 * @description 使用md5的算法进行加密
	 * @param bytes 字符串对应的字节数组
	 * @return 返回md5加密后的字符串（密文）（32位）
	 */
	public static String getMd5(byte[] bytes) {
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(bytes);  
	        return bufferToHex(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("没有MD5这个算法！");
		}
    } 
	
	/** 
     * @description 生成文件的md5密文
     * @param file 待加密的文件
     * @return 返回文件经过MD5加密后的密文
     * @throws Exception 异常信息
     */  
    public static String getFileMd5(File file) throws Exception {
    	MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        InputStream fis;  
        fis = new FileInputStream(file);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bufferToHex(messagedigest.digest());  
    }

	private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);  
    }
	
	private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
	
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c0 = hexDigits[(bt & 0xf0) >> 4];
		// 取字节中低 4 位的数字转换
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }
	
	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	//===========================================【MD5加密方式四】===============================================//
	
	/**
	 * 说明：该方法其实跟方式三中的getMD5(byte[] bytes)相同
	 * @description 使用md5的算法进行加密
	 * @param content 字符串对应的字节数组
	 * @return 返回md5加密后的字符串（密文）（32位）
	 */
    public static String md5(byte[] content) {
    	char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f'};
    	try {
    		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
    		mdTemp.update(content);
    		byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
    	} catch (Exception e) {
			return null;
		}
    }
    
    //===========================================【MD5加密方式五】===============================================//
    
    /**
     * @description 生成签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param inputCharset 编码格式
     * @return 返回生成的签名字符串
     */
    public static String sign(String text, String key, String inputCharset) {
    	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, inputCharset));
    }
    
    /**
     * @description 判断生成的签名字符串是否与已知的签名结果相同
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param inputCharset 编码格式
     * @return 如果相同返回true，否则返回false
     */
    public static boolean verify(String text, String sign, String key, String inputCharset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, inputCharset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * @description 将字符串转换成字节数组
     * @param content 字符串
     * @param charset 编码方式
     * @return 返回字节数组
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    public static void main(String[] args) {
    	//96e79218965eb72c92a549dd5a330112
		System.out.println(md5("111111"));
		System.out.println(digestByMd5("111111"));
	}
    
}
