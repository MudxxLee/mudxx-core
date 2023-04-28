package com.mudxx.common.utils.code;

import com.mudxx.common.utils.crypt.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @description 编码工具类，包括BASE64编码解码、Unicode编码解码（仅支持中文）、URLCode编码解码
 * @author laiwen
 * @date 2018年10月25日 下午1:37:35
 */
@SuppressWarnings("ALL")
public class CodeUtil {

	private static final Logger log = LoggerFactory.getLogger(CodeUtil.class);

	/**
	 * @description 一次性判断多个或单个对象至少有一个为空。
	 * @param objects
	 * @return 只要有一个元素为Blank，则返回true。
	 */
	public static boolean atLeastOneIsBlank(Object... objects) {
		boolean result = false;
		// System.out.println(objects.length);//可以确定的是objects的类型是数组，不是集合
		for (Object object : objects) {
			if (null == object || "".equals(object.toString().trim())
					|| "null".equals(object.toString().trim())
					|| "[]".equals(object.toString().trim())
					|| "[null]".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	//****************************************【BASE64编码解码】***********************************************//

	/**
	 * @description 将字符串str进行BASE64编码
	 * @param str【要编码的字符串】
	 * @param bl 【true|false,true:去掉结尾补充的'=',false:不做处理】
	 * @return
	 */
	public static String getBase64(String str, boolean... bl) {
		if (atLeastOneIsBlank(str)) {
			return null;
		}
		String base64 = Base64Util.encode(str.getBytes());
		//bl为false
		if (bl.length != 0 && !bl[0]) {
			return base64;
		}
		//去掉"="，比如7dkbExN6+mybfpdZ0WrDVg==去掉=就是7dkbExN6+mybfpdZ0WrDVg
		//不传布尔类型参数默认和bl为true相同
		if (!atLeastOneIsBlank(bl) || bl[0]) {
			// 使用replaceAll()也可以。必须要重新赋值，仅调用方法是不行的
			base64 = base64.replace("=", "");
		}
		return base64;
	}

	/**
	 * @description 将BASE64编码的字符串str进行解码
	 * @param str
	 * @return
	 */
	public static String getStrByBase64(String str) {
		if (atLeastOneIsBlank(str)) {
			return "";
		}
		try {
			byte[] bytes = Base64Util.decode(str);
			return new String(bytes);
		} catch (Exception e) {
			return "";
		}
	}
	
	//****************************************【BASE64编码解码】***********************************************//
	
	//***************************************【Unicode编码解码】***********************************************//

	/**
	 * @description 字符串转换为Unicode编码（字符串转16进制数）
	 * @param str
	 * @return
	 */
	public static String strToUnicode(String str) {
		String[] strArray = new String[str.length()];
		String s = "";
		// 方法一
		// for (int i = 0; i < str.length(); i++) {
		// 方法二
		for (int i = 0; i < strArray.length; i++) {
			int num = str.charAt(i) & 0xffff;
			strArray[i] = Integer.toHexString(num);
			s += "\\u" + strArray[i];
		}
		return s;
	}

	/**
	 * @description Unicode编码转中文字符串
	 * （该方法貌似看似用不着，因为直接打印Unicode编码就可以得到我们想要的中文字符串）
	 * @param unicode
	 * @return
	 */
	public static String unicodeToStr(String unicode) {
		int n = unicode.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = unicode.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}
	
	//***************************************【Unicode编码解码】***********************************************//
	
	//***************************************【URLCode编码解码】***********************************************//

	/**
	 * @description 字符串转URLCode（URLCode编码）
	 * @param str
	 * @return
	 */
	public static String strToUrlCode(String str) {
		try {
			// 转码
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("字符串转换为URLCode失败，str：" + str, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @description URLCode转字符串（URLCode解码）
	 * @param urlCode
	 * @return
	 */
	public static String urlCodeToStr(String urlCode) {
		try {
			// 解码
			return URLDecoder.decode(urlCode, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("URLCode转换为字符串失败，urlCode：" + urlCode, e);
			e.printStackTrace();
			return null;
		}
	}
	
	//***************************************【URLCode编码解码】***********************************************//

	public static void main(String[] args) {
		
		String str = "123 甲乙  @ hehe...";
		
		// 1、BASE64编码解码
		String base64 = getBase64(str);
		// BASE64编码结果：MTIzIOeUsuS5mSAgQCBoZWhlLi4u
		System.out.println(base64);
		String strByBase64 = getStrByBase64(base64);
		// BASE64解码结果：123 甲乙  @ hehe...
		System.out.println(strByBase64);
		
		// 2、Unicode编码解码（有缺陷）
		String strToUnicode = strToUnicode("这种编码解码方式仅仅只支持中文字符串，阿拉伯数字和英文字母不支持！");
		// Unicode编码结果：\u8fd9\u79cd\u7f16\u7801\u89e3\u7801\u65b9\u5f0f\u4ec5\u4ec5\u53ea\u652f\u6301\u4e2d\u6587\u5b57\u7b26\u4e32\uff0c\u963f\u62c9\u4f2f\u6570\u5b57\u548c\u82f1\u6587\u5b57\u6bcd\u4e0d\u652f\u6301\uff01
		System.out.println(strToUnicode);
		String unicodeToStr = unicodeToStr(strToUnicode);
		// Unicode解码结果：这种编码解码方式仅仅只支持中文字符串，阿拉伯数字和英文字母不支持！
		System.out.println(unicodeToStr);
		
		// 3、URLCode编码解码
		String strToUrlCode = strToUrlCode(str);
		// URLCode编码结果：123+%E7%94%B2%E4%B9%99++%40+hehe...
		System.out.println(strToUrlCode);
		String urlCodeToStr = urlCodeToStr(strToUrlCode);
		// URLCode解码结果：123 甲乙  @ hehe...
		System.out.println(urlCodeToStr);
	}

}
