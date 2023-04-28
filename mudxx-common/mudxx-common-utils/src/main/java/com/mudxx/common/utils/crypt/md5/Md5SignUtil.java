package com.mudxx.common.utils.crypt.md5;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @description MD5签名工具类
 * @author laiwen
 * @date 2018年11月16日 下午4:43:17
 */
@SuppressWarnings("ALL")
public class Md5SignUtil {

	/**
	 * @description MD5签名（缺省按照&进行连接）
	 * @param params 签名参数
	 * @param key 签名密钥
	 * @return 返回签名数据
	 */
	public static String buildSign(Map<String, String> params, String key) {
		return buildSign(params, key, "&");
	}
	
	/**
	 * @description MD5签名
	 * @param params 签名参数
	 * @param key 签名密钥
	 * @param join 连接符
	 * @return 返回签名数据
	 */
	public static String buildSign(Map<String, String> params, String key, String join) {
		//1、过滤掉签名参数中的空值以及签名参数，使其不参与签名
		Map<String, String> filter = paramsFilter(params);
		//2、将签名参数根据连接符进行拼装，组成我们想要格式的字符串（不包含签名密钥）
		String text = getText(filter, join);
		//3、签名密钥键值对
		String keyValue = "&key=" + key;
		//4、字符编码（默认utf-8）
		String charset = "utf-8";
		//5、将签名参数进行MD5签名
		return Md5SignUtil.sign(text, keyValue, charset);
	}

	/**
	 * @description MD5签名（缺省按照&进行连接）（更灵活）
	 * @param params 签名参数
	 * @param keyValue 签名密钥键值对（格式例如"&key=" + key，格式不限死）
	 * @return 返回签名数据
	 */
	public static String buildSigns(Map<String, String> params, String keyValue) {
		return buildSigns(params, keyValue, "&");
	}

	/**
	 * @description MD5签名（更灵活）
	 * @param params 签名参数
	 * @param keyValue 签名密钥键值对（格式例如"&key=" + key，格式不限死）
	 * @param join 连接符
	 * @return 返回签名数据
	 */
	public static String buildSigns(Map<String, String> params, String keyValue, String join) {
		//1、过滤掉签名参数中的空值以及签名参数，使其不参与签名
		Map<String, String> filter = paramsFilter(params);
		//2、将签名参数根据连接符进行拼装，组成我们想要格式的字符串（不包含签名密钥）
		String text = getText(filter, join);
		//3、字符编码（默认utf-8）
		String charset = "utf-8";
		//4、将签名参数进行MD5签名
		return Md5SignUtil.sign(text, keyValue, charset);
	}

	/**
	 * @description 过滤掉签名参数中的空值以及签名参数，使其不参与签名
	 * @param params 签名参数
	 * @return 返回参与MD5签名的Map参数集合
	 */
	public static Map<String, String> paramsFilter(Map<String, String> params) {
		Map<String, String> result = new HashMap<>(16);
		if (params == null || params.size() <= 0) {
			return result;
		}
		for (String key : params.keySet()) {
			String value = params.get(key);
			if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key) || "signKey".equals(key)) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * @description 将签名参数根据连接符进行拼装，组成我们想要格式的字符串（不包含签名密钥）
	 * @param params 签名参数
	 * @param join 连接符
	 * @return 返回签名参数经过连接符拼装过后的字符串
	 */
	public static String getText(Map<String, String> params, String join) {
		List<String> keys = new ArrayList<>(params.keySet());
		Collections.sort(keys);
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {
				// 拼接时，不包括最后一个&字符
				text.append(key).append("=").append(value);
			} else {
				text.append(key).append("=").append(value).append(join);
			}
		}
		return text.toString();
	}

	/**
	 * @description 将签名参数进行MD5签名
	 * @param text 签名参数（不包含签名密钥）
	 * @param keyValue 签名密钥键值对（格式例如"&key=" + key，格式不限死）
	 * @param charset 字符编码（基本采用utf-8）
	 * @return 返回签名结果
	 */
	public static String sign(String text, String keyValue, String charset) {
		String content = text + keyValue;
		byte[] contentBytes = getContentBytes(content, charset);
		return DigestUtils.md5Hex(contentBytes);
	}

	/**
	 * @description 将签名参数进行MD5签名
	 * @param content 签名参数（如果有签名密钥则包含签名密钥）
	 * @param charset 字符编码（基本采用utf-8）
	 * @return 返回签名结果
	 */
	public static String sign(String content, String charset) {
		byte[] contentBytes = getContentBytes(content, charset);
		return DigestUtils.md5Hex(contentBytes);
	}

	/**
	 * @description 将签名参数进行MD5签名（默认采用utf-8）
	 * @param content 签名参数（如果有签名密钥则包含签名密钥）
	 * @return 返回签名结果
	 */
	public static String sign(String content) {
		byte[] contentBytes = getContentBytes(content, "utf-8");
		return DigestUtils.md5Hex(contentBytes);
	}
	
	/**
	 * @description 验证签名
	 * @param params 请求参数（包含签名）
	 * @param key 签名密钥
	 * @return 如果签名正确返回true，否则返回false
	 */
    public static Boolean validateSign(Map<String, String> params, String key) {
        boolean flag = false;
        if (params.containsKey("sign")) {
        	//请求参数签名sign（可能进行了转大写）
            String requestSign = params.get("sign");
            //根据请求参数（排除请求参数签名sign）重新生成签名sign
            String nativeSign = buildSign(params, key);
            //忽略大小写之后进行比较两个签名结果是否相等
            flag = requestSign.equalsIgnoreCase(nativeSign);
        }
        return flag;
    }

	/**
	 * @description 验证签名（更灵活）
	 * @param params 请求参数（包含签名）
	 * @param keyValue 签名密钥键值对（格式例如"&key=" + key，格式不限死）
	 * @return 如果签名正确返回true，否则返回false
	 */
	public static Boolean validateSigns(Map<String, String> params, String keyValue) {
		boolean flag = false;
		if (params.containsKey("sign")) {
			//请求参数签名sign（可能进行了转大写）
			String requestSign = params.get("sign");
			//根据请求参数（排除请求参数签名sign）重新生成签名sign
			String nativeSign = buildSigns(params, keyValue);
			//忽略大小写之后进行比较两个签名结果是否相等
			flag = requestSign.equalsIgnoreCase(nativeSign);
		}
		return flag;
	}
	
	/**
	 * @description 获取进行MD5签名的字节数组
	 * @param content 签名字符串（完整包括签名密钥）
	 * @param charset 字符编码（默认utf-8）
	 * @return 返回字节数组
	 */
	public static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误，指定的编码集不对，您目前指定的编码集是：" + charset);
		}
	}

}
