package com.mudxx.common.utils.string;

/**
 * description: 去除字符串中的空格以及空白字符相关工具类
 * @author laiwen
 * @date 2021-12-24 17:39:37
 */
public class TrimUtil {

    /**
     * description: 去除字符串首尾空格，包括制表符等空白字符
     * @author laiwen
     * @date 2021-12-24 17:49:53
     * @param str 待处理字符串
     * @return 返回去除首尾空格以及制表符等空白字符后的字符串
     */
    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    /**
     * description: 去除字符串首尾以及中间空格，不包括制表符等空白字符
     * @author laiwen
     * @date 2021-12-24 17:49:59
     * @param str 待处理字符串
     * @return 返回去除首尾以及中间空格后的字符串
     */
    public static String trimHmt(String str) {
        if (str == null) {
            return null;
        }
        return str.replace(" ", "");
    }

    /**
     * description: 去除字符串首尾以及中间空格，包括制表符等空白字符
     * @author laiwen
     * @date 2021-12-24 17:50:05
     * @param str 待处理字符串
     * @return 返回去除首尾、中间空格以及制表符等空白字符后的字符串
     */
    public static String trimAll(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\s*", "");
    }

}
