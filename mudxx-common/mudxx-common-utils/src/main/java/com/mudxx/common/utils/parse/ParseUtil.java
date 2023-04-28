package com.mudxx.common.utils.parse;

/**
 * @description 一个替换java字符串中${}或者{}等占位符的工具类
 * @author laiwen
 * @date 2019年1月4日 上午10:42:59
 */
@SuppressWarnings("ALL")
public class ParseUtil {
	
	/**
	 * @description 将字符串content中由${和}组成的占位符依次替换为params动态数组中的值
	 * @author laiwen
	 * @param content 含有占位符的字符串
	 * @param params 动态数组
	 * @return 返回占位符被参数替换后的字符串内容
	 */
	public static String parseEl(String content, Object... params) {
		return parse(content, "${", "}", params);
	}

	/**
	 * @description 将字符串content中由{和}组成的占位符依次替换为params动态数组中的值
	 * @author laiwen
	 * @param content 含有占位符的字符串
	 * @param params 动态数组 
	 * @return 返回占位符被参数替换后的字符串内容
	 */
	public static String parseBraces(String content, Object... params) {
		return parse(content, "{", "}", params);
	}

	/**
	 * @description 将字符串content中由open和close组成的占位符依次替换为params动态数组中的值
	 * @author laiwen
	 * @param content 含有占位符的字符串
	 * @param open 占位符左半边部分，比如${或者{
	 * @param close 占位符右半边部分，比如}
	 * @param params 动态数组
	 * @return 返回占位符被参数替换后的字符串内容
	 */
	public static String parse(String content, String open, String close, Object... params) {
		if (params == null || params.length <= 0) {
			return content;
		}
		int argsIndex = 0;
		if (content == null || content.isEmpty()) {
			return "";
		}
		char[] src = content.toCharArray();
		int offset = 0;
		int start = content.indexOf(open, offset);
		if (start == -1) {
			return content;
		}
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				builder.append(src, offset, start - offset - 1).append(open);
				offset = start + open.length();
			} else {
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				builder.append(src, offset, start - offset);
				offset = start + open.length();
				int end = content.indexOf(close, offset);
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						expression.append(src, offset, end - offset - 1).append(close);
						offset = end + close.length();
						end = content.indexOf(close, offset);
					} else {
						expression.append(src, offset, end - offset);
						offset = end + close.length();
						break;
					}
				}
				if (end == -1) {
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					String value = (argsIndex <= params.length - 1) ? (params[argsIndex] == null ? "" : params[argsIndex].toString()) : expression.toString();
					builder.append(value);
					offset = end + close.length();
					argsIndex++;
				}
			}
			start = content.indexOf(open, offset);
		}
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		
		//输出：我的名字是{}，结果是true，可信度是%100；说明：\\{}会将{}进行转义，不会被当做占位符被参数进行替换
		String content1 = parse("我的名字是\\{}，结果是{}，可信度是%{}", "{", "}", true, 100);
		System.out.println(content1);

		//输出：我的名字是齐天大圣孙悟空，结果是true，可信度是%100
		String content2 = parseEl("我的名字是${}，结果是${}，可信度是%${}", "齐天大圣孙悟空", true, 100);
		System.out.println(content2);
		
		//输出：我的名字是齐天大圣孙悟空，结果是true，可信度是%100
		String content3 = parseBraces("我的名字是{}，结果是{}，可信度是%{}", "齐天大圣孙悟空", true, 100);
		System.out.println(content3);
		
	}

}
