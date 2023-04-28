package com.mudxx.common.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 正则表达式工具类
 * @author laiwen
 * @date 2018-06-01 18:20:51
 */
@SuppressWarnings("ALL")
public class RegexUtil {

	/**
	 * 正则表达式字符串
	 */
	private String regex;

	/**
	 * 待验证字符串
	 */
	private String toBeVerify;

	private final static RegexUtil INSTANCE = new RegexUtil();

	private RegexUtil() {}

	public static RegexUtil getInstance() {
		return INSTANCE;
	}

	/**
	 * description: 由正则表达式自定义构造函数
	 * @author laiwen
	 * @date 2018-06-01 19:01:58
	 * @param regex
	 */
	public RegexUtil(String regex) {
		this.regex = regex;
	}

	/**
	 * description: 由正则表达式和待验证字符串自定义构造函数
	 * @author laiwen
	 * @date 2018-06-01 19:05:56
	 * @param regex
	 * @param toBeVerify
	 */
	public RegexUtil(String regex, String toBeVerify) {
		this.regex = regex;
		this.toBeVerify = toBeVerify;
	}

	/**
	 * description: 根据正则表达式验证字符串，RegexUtil(String regex)
	 * @author laiwen
	 * @date 2018-06-01 19:12:22
	 * @param toBeVerify 待验证字符串
	 * @return 返回验证结果，如果验证通过返回true，否则返回false
	 */
	public Boolean regex(String toBeVerify) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(toBeVerify);
		return matcher.matches();
	}

	/**
	 * description: 根据正则表达式验证字符串，RegexUtil(String regex, String toBeVerify)
	 * @author laiwen
	 * @date 2018-06-01 19:20:51
	 * @return 返回验证结果，如果验证通过返回true，否则返回false
	 */
	public Boolean regex() {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(toBeVerify);
		return matcher.matches();
	}

	/**
	 * description: 静态方法，可类名调用
	 * @author laiwen
	 * @date 2018-06-01 19:23:57
	 * @param regex 正则表达式字符串
	 * @param toBeVerify 待验证字符串
	 * @return 返回验证结果，如果验证通过返回true，否则返回false
	 */
	public static Boolean regex(String regex, String toBeVerify) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(toBeVerify);
		return matcher.matches();
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getToBeVerify() {
		return toBeVerify;
	}

	public void setToBeVerify(String toBeVerify) {
		this.toBeVerify = toBeVerify;
	}

	/**
	 * description: 数字校验（可以匹配正负、整数、小数，比较全面）
	 * @author laiwen
	 * @date 2022-05-26 15:45:30
	 * @param str 待校验字符串
	 * @return 返回验证结果，如果验证通过返回true，否则返回false
	 */
	public static Boolean isNumeric(String str) {
		return regex("^[+-]?([0-9]+(.[0-9]+)?)$", str);
	}

	/**
	 * description: 金额校验（匹配字符串，要求是数字，大于0，并且最多含有2位小数）
	 * @author laiwen
	 * @date 2022-05-26 13:31:34
	 * @param str 待校验字符串
	 * @return 返回验证结果，如果验证通过返回true，否则返回false
	 */
	public static Boolean isMoney(String str) {
		return regex("^[+]?([0-9]+(.[0-9]{1,2})?)$", str);
	}

}
