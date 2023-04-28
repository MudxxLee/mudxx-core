package com.mudxx.common.utils.code;

import com.mudxx.common.utils.date.CalendarUtil;

import java.util.Random;

/**
 * @description 获取唯一ID工具类
 * @author laiwen
 * @date 2018年12月16日 下午3:09:17
 */
@SuppressWarnings("ALL")
public class NumberGenerator {

	/**
	 * @description 方案一
	 * @return 返回唯一ID（16位）
	 */
	public static synchronized String generatorId() {
		// 1、获取当前时间的毫秒值（比如：1544944665139）
		long millis = System.currentTimeMillis();
		// 2、生成三位随机数，如果不足三位前面补0（比如：092）
		String random = String.format("%03d", new Random().nextInt(999));
		// 3、返回毫秒值 + 三位随机数（比如：1544944665139092）
		return millis + random;
	}

	/**
	 * @description 方案二
	 * @return 返回唯一ID（19位）
	 */
	public static synchronized String genUniqueId() {
		// 1、获取当前时间的毫秒值（比如：1554860962244）
		long millis = System.currentTimeMillis();
		// 2、生成六位随机数（比如：911177）
		Integer number = new Random().nextInt(900000) + 100000;
		// 3、返回毫秒值 + 六位随机数（比如：1554860962244911177）
		return millis + String.valueOf(number);
	}

	/**
	 * @description 方案三
	 * @return 返回唯一ID（21位）
	 */
	public static synchronized String createId() {
		// 1、获取当前时间的毫秒值（比如：1544946138587）
		long millis = System.currentTimeMillis();
		// 2、生成九位随机数（比如：126560585）
		long random = (long) ((Math.random() + 1) * 100000000);
		// 3、返回毫秒值 + 八位随机数（比如：154494613858726560585）
		return millis + String.valueOf(random).substring(1);
	}

	/**
	 * @description 方案四
	 * @return 返回唯一编号（24位）
	 */
	public static synchronized String createCode() {
		// 1、当前时间（年月日）（8位）
		String nowDate = CalendarUtil.getNowDate("yyyyMMdd");
		// 2、根据方案一，返回唯一ID（16位）
		String id = generatorId();
		// 3、当前时间（年月日）（8位） + 唯一ID（16位） = 唯一编号（24位）
		return nowDate + id;
	}

	public static void main(String[] args) {
		System.out.println(generatorId());
		System.out.println(genUniqueId());
		System.out.println(createId());
		System.out.println(createCode());
	}

}
