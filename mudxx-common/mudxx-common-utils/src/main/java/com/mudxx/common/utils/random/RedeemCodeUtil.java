package com.mudxx.common.utils.random;


import com.mudxx.common.utils.empty.EmptyUtil;

import java.util.Random;

/**
 * description: 随机码工具类
 * @author laiwen
 * @date 2021-09-08 09:46:07
 */
@SuppressWarnings("ALL")
public class RedeemCodeUtil {

    /**
     * 说明：当length为16，digit为4，分隔符为-的时候，生成比如：jRL7-1p25-0520-86Bp
     * description: 生成指定位数的随机码(数字和英文大小写字母随机混排)
     * @author laiwen
     * @date 2021-09-08 16:08:53
     * @param length 指定位数
     * @param digit 每几位
     * @param separator 分隔符
     * @return 返回随机码
     */
    public static String createBigSmallLetterStrOrNumberRedeemCode(Integer length, Integer digit, String separator) {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int intVal = (int) (Math.random() * 58 + 65);
            if (intVal >= 91 && intVal <= 96) {
                i--;
            }
            if (intVal < 91 || intVal > 96) {
                if (intVal % 2 == 0) {
                    str.append((char) intVal);
                } else {
                    str.append((int) (Math.random() * 10));
                }
                if (EmptyUtil.isNotEmpty(digit)) {
                    if (digit == 0) {
                        digit = 1;
                    }
                    if (i % digit == 0) {
                        if (EmptyUtil.isEmpty(separator)) {
                            separator = "-";
                        }
                        str.append(separator);
                    }
                }
            }

        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 说明：当length为16，digit为4，分隔符为-的时候，生成比如：4b80-1r4r-p794-0pv9
     * description: 生成指定位数的随机码(数字和英文小写字母随机混排)
     * @author laiwen
     * @date 2021-09-08 16:09:19
     * @param length 指定位数
     * @param digit 每几位
     * @param separator 分隔符
     * @return 返回随机码
     */
    public static String createSmallStrOrNumberRedeemCode(Integer length, Integer digit, String separator) {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int intVal = (int) (Math.random() * 26 + 97);
            if (intVal % 2 == 0) {
                str.append((char) intVal);
            } else {
                str.append((int) (Math.random() * 10));
            }
            if (EmptyUtil.isNotEmpty(digit)) {
                if (digit == 0) {
                    digit = 1;
                }
                if (i % digit == 0) {
                    if (EmptyUtil.isEmpty(separator)) {
                        separator = "-";
                    }
                    str.append(separator);
                }
            }
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 说明：当length为16，digit为4，分隔符为-的时候，生成比如：L8P2-B277-86H1-09RB
     * description: 生成指定位数的随机码(数字和英文大写字母随机混排)
     * @author laiwen
     * @date 2021-09-08 16:09:56
     * @param length 指定位数
     * @param digit 每几位
     * @param separator 分隔符
     * @return 返回随机码
     */
    public static String createBigStrOrNumberRedeemCode(Integer length, Integer digit, String separator) {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int intVal = (int) (Math.random() * 26 + 65);
            if (intVal % 2 == 0) {
                str.append((char) intVal);
            } else {
                str.append((int) (Math.random() * 10));
            }
            if (EmptyUtil.isNotEmpty(digit)) {
                if (digit == 0) {
                    digit = 1;
                }
                if (i % digit == 0) {
                    if (EmptyUtil.isEmpty(separator)) {
                        separator = "-";
                    }
                    str.append(separator);
                }
            }
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 说明：当length为16，digit为4，范围为0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ，
     *      分隔符为-的时候，生成比如：I8EM-PSWK-U1N0-JVSD，该方法属于通用方法
     * description: 生成指定位数的随机码
     * @author laiwen
     * @date 2021-09-08 17:47:44
     * @param length 指定位数
     * @param base 范围
     * @param digit 每几位
     * @param separator 分隔符
     * @return 返回随机码
     */
    public static String createRedeemCode(Integer length, String base, Integer digit, String separator) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String result = sb.toString();
        if (EmptyUtil.isNotEmpty(digit)) {
            if (digit == 0) {
                digit = 1;
            }
            String regex = "(.{" + digit + "})";
            if (EmptyUtil.isEmpty(separator)) {
                separator = "-";
            }
            result = result.replaceAll(regex, "$1" + separator);
            if (result.endsWith(separator)) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 说明：当length为16的时候，生成比如：FJPM-KY32-C6WX-W9X7
     *      随机字符为数字和英文大写字母，数字不包括0和1，英文大写字母不包括O和I，该方法属于特定方法
     * description: 生成指定位数的随机码
     * @author laiwen
     * @date 2021-09-08 17:57:37
     * @param length 指定位数
     * @return 返回随机码
     */
    public static String createRedeemCode(Integer length) {
        return createRedeemCode(length, "23456789ABCDEFGHJKLMNPQRSTUVWXYZ", 4, "-");
    }

}
