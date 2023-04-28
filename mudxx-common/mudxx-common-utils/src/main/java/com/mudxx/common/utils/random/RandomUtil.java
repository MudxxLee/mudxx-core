package com.mudxx.common.utils.random;

import java.util.Random;
import java.util.UUID;

/**
 * @description 随机字符串生成工具类
 * @author laiwen
 * @date 2018年7月19日 下午4:23:47
 */
@SuppressWarnings("ALL")
public class RandomUtil {

	/**
	 * @description 生产UUID
	 * @return 返回原始未做任何处理的36位的UUID字符串
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	/**
	 * @description 生产不带横线的UUID
	 * @return 返回已经去除了4个横线的32位的UUID字符串
	 */
	public static String getUuidNoLine() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * @description 根据指定位数和范围生成随机字符串
	 * @param length 指定位数
	 * @param base 范围
	 * @return 返回生成的随机字符串
	 */
	public static String random(int length, String base) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @description 生成指定位数的数字、大小写字母随机字符串
	 * @param length 指定位数
	 * @return 返回生成的随机字符串
	 */
	public static String randomNumChar(int length) {
		String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @description 根据指定长度生成大小写字母随机字符串
	 * @param length 指定位数
	 * @return 返回生成的随机字符串
	 */
	public static String randomChar(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @description 根据指定长度生成随机数字字符串
	 * @param length 指定位数
	 * @return 返回生成的随机字符串
	 */
	public static String randomNum(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

}
