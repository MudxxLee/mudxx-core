package com.mudxx.common.utils.math;


import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.regex.RegexUtil;

import java.math.BigDecimal;

/**
 * description: BigDecimal类常用的加，减，乘，除
 * @author laiwen
 * @date 2018-05-30 14:57:30
 */
@SuppressWarnings("ALL")
public class MathUtil {

	/******************************************【四舍五入】******************************************/

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 15:07:51
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和，2位精确度
	 */
	public static String addHalfUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.add(b2).toString(), 2);
	}

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 15:12:53
	 * @param value1 被加数
	 * @param value2 加数
	 * @param len 精确度
	 * @return 两个参数的和
	 */
	public static String addHalfUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.add(b2).toString(), len);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 15:17:51
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差，2位精确度
	 */
	public static String subHalfUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.subtract(b2).toString(), 2);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 15:20:52
	 * @param value1 被减数
	 * @param value2 减数
	 * @param len 精确度
	 * @return 两个参数的差
	 */
	public static String subHalfUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.subtract(b2).toString(), len);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 15:23:23
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积，2位精确度
	 */
	public static String mulHalfUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.multiply(b2).toString(), 2);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 15:27:45
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @param len 精确度
	 * @return 两个参数的积
	 */
	public static String mulHalfUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfUp(b1.multiply(b2).toString(), len);
	}

	/**
	 * description: 提供精确的除法运算方法div
	 * @author laiwen
	 * @date 2018-05-30 15:33:23
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale 精确范围
	 * @return 两个参数的商
	 */
	public static String divHalfUp(String value1, String value2, int scale) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		try {
			// 如果精确范围小于0，抛出异常信息
			if (scale < 0) {
				throw new Exception("精确度不能小于0");
			}
		} catch (Exception e) {
			return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * description: 进行四舍五入操作
	 * @author laiwen
	 * @date 2018-05-30 15:38:21
	 * @param d 需要进行四舍五入的数
	 * @param len 精确度
	 * @return 返回四舍五入之后的数
	 */
	public static String roundHalfUp(String d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).toString();
	}

	/******************************************【四舍五入】******************************************/

	/******************************************【五舍六入】******************************************/

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 16:11:23
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和，2位精确度
	 */
	public static String addHalfDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.add(b2).toString(), 2);
	}

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 16:23:14
	 * @param value1 被加数
	 * @param value2 加数
	 * @param len 精确度
	 * @return 两个参数的和
	 */
	public static String addHalfDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.add(b2).toString(), len);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 16:25:23
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差，2位精确度
	 */
	public static String subHalfDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.subtract(b2).toString(), 2);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 16:33:25
	 * @param value1 被减数
	 * @param value2 减数
	 * @param len 精确度
	 * @return 两个参数的差
	 */
	public static String subHalfDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.subtract(b2).toString(), len);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 16:37:12
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积，2位精确度
	 */
	public static String mulHalfDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.multiply(b2).toString(), 2);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 16:45:55
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @param len 精确度
	 * @return 两个参数的积
	 */
	public static String mulHalfDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundHalfDown(b1.multiply(b2).toString(), len);
	}

	/**
	 * description: 提供精确的除法运算方法div
	 * @author laiwen
	 * @date 2018-05-30 16:50:51
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale 精确范围
	 * @return 两个参数的商
	 */
	public static String divHalfDown(String value1, String value2, int scale) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		try {
			// 如果精确范围小于0，抛出异常信息
			if (scale < 0) {
				throw new Exception("精确度不能小于0");
			}
		} catch (Exception e) {
			return b1.divide(b2, 2, BigDecimal.ROUND_HALF_DOWN).toString();
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).toString();
	}

	/**
	 * description: 进行五舍六入操作
	 * @author laiwen
	 * @date 2018-05-30 16:57:33
	 * @param d 需要进行五舍六入的数
	 * @param len 精确度
	 * @return 返回五舍六入之后的数
	 */
	public static String roundHalfDown(String d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_HALF_DOWN是BigDecimal的一个常量，表示进行五舍六入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_DOWN).toString();
	}

	/******************************************【五舍六入】******************************************/

	/******************************************【只舍不入】******************************************/

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 17:01:24
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和，2位精确度
	 */
	public static String addDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.add(b2).toString(), 2);
	}

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 17:13:33
	 * @param value1 被加数
	 * @param value2 加数
	 * @param len 精确度
	 * @return 两个参数的和
	 */
	public static String addDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.add(b2).toString(), len);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 17:18:34
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差，2位精确度
	 */
	public static String subDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.subtract(b2).toString(), 2);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 17:23:21
	 * @param value1 被减数
	 * @param value2 减数
	 * @param len 精确度
	 * @return 两个参数的差
	 */
	public static String subDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.subtract(b2).toString(), len);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 17:27:34
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积，2位精确度
	 */
	public static String mulDown(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.multiply(b2).toString(), 2);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 17:30:22
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @param len 精确度
	 * @return 两个参数的积
	 */
	public static String mulDown(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundDown(b1.multiply(b2).toString(), len);
	}

	/**
	 * description: 提供精确的除法运算方法div
	 * @author laiwen
	 * @date 2018-05-30 17:38:55
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale 精确范围
	 * @return 两个参数的商
	 */
	public static String divDown(String value1, String value2, int scale) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		try {
			// 如果精确范围小于0，抛出异常信息
			if (scale < 0) {
				throw new Exception("精确度不能小于0");
			}
		} catch (Exception e) {
			return b1.divide(b2, 2, BigDecimal.ROUND_DOWN).toString();
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_DOWN).toString();
	}

	/**
	 * description: 进行只舍不入操作
	 * @author laiwen
	 * @date 2018-05-30 17:47:33
	 * @param d 需要进行只舍不入的数
	 * @param len 精确度
	 * @return 返回只舍不入之后的数
	 */
	public static String roundDown(String d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_DOWN是BigDecimal的一个常量，表示进行只舍不入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_DOWN).toString();
	}

	/******************************************【只舍不入】******************************************/

	/******************************************【只入不舍】******************************************/

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 18:05:58
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和，2位精确度
	 */
	public static String addUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.add(b2).toString(), 2);
	}

	/**
	 * description: 提供精确加法计算的add方法
	 * @author laiwen
	 * @date 2018-05-30 18:12:22
	 * @param value1 被加数
	 * @param value2 加数
	 * @param len 精确度
	 * @return 两个参数的和
	 */
	public static String addUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.add(b2).toString(), len);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 18:17:44
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差，2位精确度
	 */
	public static String subUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.subtract(b2).toString(), 2);
	}

	/**
	 * description: 提供精确减法运算的sub方法
	 * @author laiwen
	 * @date 2018-05-30 18:33:37
	 * @param value1 被减数
	 * @param value2 减数
	 * @param len 精确度
	 * @return 两个参数的差
	 */
	public static String subUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.subtract(b2).toString(), len);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 18:43:56
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积，2位精确度
	 */
	public static String mulUp(String value1, String value2) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.multiply(b2).toString(), 2);
	}

	/**
	 * description: 提供精确乘法运算的mul方法
	 * @author laiwen
	 * @date 2018-05-30 18:55:53
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @param len 精确度
	 * @return 两个参数的积
	 */
	public static String mulUp(String value1, String value2, int len) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return roundUp(b1.multiply(b2).toString(), len);
	}

	/**
	 * description: 提供精确的除法运算方法div
	 * @author laiwen
	 * @date 2018-05-30 18:58:59
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale 精确范围
	 * @return 两个参数的商
	 */
	public static String divUp(String value1, String value2, int scale) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		try {
			// 如果精确范围小于0，抛出异常信息
			if (scale < 0) {
				throw new Exception("精确度不能小于0");
			}
		} catch (Exception e) {
			return b1.divide(b2, 2, BigDecimal.ROUND_UP).toString();
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_UP).toString();
	}

	/**
	 * description: 进行只入不舍操作
	 * @author laiwen
	 * @date 2018-05-30 18:59:51
	 * @param d 需要进行只入不舍的数
	 * @param len 精确度
	 * @return 返回只入不舍之后的数
	 */
	public static String roundUp(String d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_UP是BigDecimal的一个常量，表示进行只入不舍的操作
		return b1.divide(b2, len, BigDecimal.ROUND_UP).toString();
	}

	/******************************************【只入不舍】******************************************/

	/**
	 * description: 使用java正则表达式去掉数字多余的.与0
	 * @author laiwen
	 * @date 2021-05-24 14:57:18
     * @param numStr 数字的字符串格式
     * @return 返回去掉多余的.与0的数字字符串格式
     */
    public static String subZeroAndDot(String numStr) {
    	if (EmptyUtil.isNotEmpty(numStr)) {
			// 非空
    		if (numStr.indexOf(".") > 0) {
				// 去掉多余的0
            	numStr = numStr.replaceAll("0+?$", "");
				// 如果最后一位是.则去掉
            	numStr = numStr.replaceAll("[.]$", "");
            }
    	}
        return numStr;
    }

	/**
	 * description: 元转分
	 * @author laiwen
	 * @date 2021-05-24 14:57:58
	 * @param yuan 待转换的元
	 * @return 转换后的分（没有小数）
	 */
	public static String yuanToFen(String yuan) {
		return mulHalfUp(yuan, "100", 0);
	}

	/**
	 * description: 分转元
	 * @author laiwen
	 * @date 2021-05-24 14:58:09
	 * @param fen 待转换的分
	 * @return 转换后的元（2位小数）
	 */
	public static String fenToYuan(String fen) {
		return divHalfUp(fen, "100", 2);
	}

	/**
	 * description: 将一个数的每三个数字加上逗号处理
	 * 示例1：5000000.00 --> 5,000,000.00
	 * 示例2：20000000 --> 20,000,000
	 * @author laiwen
	 * @date 2021-05-24 14:35:50
	 * @param str 无逗号的数字
	 * @return 加上逗号的数字
	 */
	public static String addComma(String str) {
		if (str == null) {
			str = "";
		}
		// 需要添加逗号的字符串（整数）
		String addCommaStr;
		// 小数，等逗号添加完后，最后在末尾补上
		String tmpCommaStr = "";
		if (str.contains(".")) {
			addCommaStr = str.substring(0, str.indexOf("."));
			tmpCommaStr = str.substring(str.indexOf("."));
		} else {
			addCommaStr = str;
		}
		// 将传进数字反转
		String reverseStr = new StringBuilder(addCommaStr).reverse().toString();
		String strTemp = "";
		for (int i = 0; i < reverseStr.length(); i++) {
			if (i * 3 + 3 > reverseStr.length()) {
				strTemp += reverseStr.substring(i * 3);
				break;
			}
			strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
		}
		// 将 "5,000,000," 中最后一个","去除
		if (strTemp.endsWith(",")) {
			strTemp = strTemp.substring(0, strTemp.length() - 1);
		}
		// 将数字重新反转，并将小数拼接到末尾
		return new StringBuilder(strTemp).reverse().toString() + tmpCommaStr;
	}

	/**
	 * description: 将加上逗号处理的数字的逗号去掉
	 * 示例1：5,000,000.00 --> 5000000.00
	 * 示例2：20,000,000 --> 20000000
	 * @author laiwen
	 * @date 2021-05-24 14:37:43
	 * @param str 加上逗号的数字
	 * @return 无逗号的数字
	 */
	public static String removeComma(String str) {
		if (str == null) {
			str = "";
		}
		// 去除字符串里面的逗号
		return str.replaceAll(",", "");
	}

    /**
     * description: 比较两个字符串类型的数字的大小
     * @author laiwen
     * @date 2019-11-11 14:55:23
     * @param value1 被比较的数字（字符串类型）
     * @param value2 比较的数字（字符串类型）
     * @return 如果前者大返回1，相等返回0，前者小返回-1
     */
	public static Integer compare(String value1, String value2) {
		BigDecimal bd1 = new BigDecimal(value1);
		BigDecimal bd2 = new BigDecimal(value2);
		return bd1.compareTo(bd2);
	}

    /**
     * description: 比较两个数字的大小
     * @author laiwen
     * @date 2019-11-11 14:55:30
     * @param bd1 被比较的数字
     * @param bd2 比较的数字
     * @return 如果前者大返回1，相等返回0，前者小返回-1
     */
	public static Integer compare(BigDecimal bd1, BigDecimal bd2) {
		return bd1.compareTo(bd2);
	}

	/**
	 * description: 比较两个字符串类型的数字是否相等
	 * @author laiwen
	 * @date 2019-11-11 14:09:39
	 * @param value1 被比较的数字（字符串类型）
	 * @param value2 比较的数字（字符串类型）
	 * @return 如果相等返回true，否则返回false
	 */
	public static Boolean isEquals(String value1, String value2) {
		Boolean flag = false;
		BigDecimal bd1 = new BigDecimal(value1);
		BigDecimal bd2 = new BigDecimal(value2);
		if (bd1.compareTo(bd2) == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * description: 比较两个数字是否相等
	 * @author laiwen
	 * @date 2019-11-11 14:09:47
	 * @param bd1 被比较的数字
	 * @param bd2 比较的数字
	 * @return 如果相等返回true，否则返回false
	 */
	public static Boolean isEquals(BigDecimal bd1, BigDecimal bd2) {
		Boolean flag = false;
		if (bd1.compareTo(bd2) == 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * description: 比较两个字符串类型的数字是否不相等
	 * @author laiwen
	 * @date 2019-11-11 14:09:39
	 * @param value1 被比较的数字（字符串类型）
	 * @param value2 比较的数字（字符串类型）
	 * @return 如果不相等返回true，否则返回false
	 */
	public static Boolean isNotEquals(String value1, String value2) {
		Boolean flag = false;
		BigDecimal bd1 = new BigDecimal(value1);
		BigDecimal bd2 = new BigDecimal(value2);
		if (bd1.compareTo(bd2) != 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * description: 比较两个数字是否不相等
	 * @author laiwen
	 * @date 2019-11-11 14:09:47
	 * @param bd1 被比较的数字
	 * @param bd2 比较的数字
	 * @return 如果不相等返回true，否则返回false
	 */
	public static Boolean isNotEquals(BigDecimal bd1, BigDecimal bd2) {
		Boolean flag = false;
		if (bd1.compareTo(bd2) != 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * description: 获取指定数字中的小数位数
	 * @author laiwen
	 * @date 2022-05-26 14:33:48
	 * @param number 数字字符串
	 * @return 返回指定数字中的小数位数
	 */
	public static Integer getDecimalPlaces(String number) {
		Boolean flag = RegexUtil.isNumeric(number);
		if (!flag) {
			throw new RuntimeException("指定字符串不是数字！");
		}
		String[] array = number.split("\\.");
		int arrayLength = array.length;
		if (arrayLength == 2) {
			return array[1].length();
		}
		return 0;
	}

}
