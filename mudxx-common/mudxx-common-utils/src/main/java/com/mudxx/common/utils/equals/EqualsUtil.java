package com.mudxx.common.utils.equals;

import com.mudxx.common.utils.empty.EmptyUtil;

import java.util.Objects;

/**
 * description: 判断两个对象是否相等工具类
 * @author laiwen
 * @date 2019-07-26 21:12:33
 */
@SuppressWarnings("ALL")
public class EqualsUtil {

    /**
     * description: 判断两个对象是否相等（基本数据类型包装类以及字符串）
     * @author laiwen
     * @date 2019-07-26 21:16:02
     * @param a 对象a
     * @param b 对象b
     * @return 如果相等返回true，否则返回false
     */
    public static Boolean isEquals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * description: 判断两个对象是否不相等（基本数据类型包装类以及字符串）
     * @author laiwen
     * @date 2019-07-26 21:19:15
     * @param a 对象a
     * @param b 对象b
     * @return 如果不相等返回true，否则返回false
     */
    public static Boolean isNotEquals(Object a, Object b) {
        return !isEquals(a, b);
    }

    /**
     * description: 判断两个对象是否深度相等（基本数据类型包装类以及字符串还有数组）
     * @author laiwen
     * @date 2019-07-26 21:22:26
     * @param a 对象a
     * @param b 对象b
     * @return 如果深度相等返回true，否则返回false
     */
    public static Boolean isDeepEquals(Object a, Object b) {
        return Objects.deepEquals(a, b);
    }

    /**
     * description: 判断两个对象是否深度不相等（基本数据类型包装类以及字符串还有数组）
     * @author laiwen
     * @date 2019-07-26 21:29:37
     * @param a 对象a
     * @param b 对象b
     * @return 如果不深度相等返回true，否则返回false
     */
    public static Boolean isNotDeepEquals(Object a, Object b) {
        return !isDeepEquals(a, b);
    }

    /**
     * description: 判断两个对象是否相等，如果是不同类型的对象但是toString方法的返回值相同也认为两个对象相等
     * @author laiwen
     * @date 2021-08-20 21:33:59
     * @param a 对象a
     * @param b 对象b
     * @return 如果广义相等返回true，否则返回false
     */
    public static Boolean isBroadEquals(Object a, Object b) {
        if (isNotEquals(a, b)) {
            if (EmptyUtil.isNotEmpty(a) && EmptyUtil.isNotEmpty(b)) {
                if (isEquals(a.toString(), b.toString())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * description: 判断两个对象是否不相等，同时满足两个不同类型的对象的toString方法的返回值也不同才认为不相等
     * @author laiwen
     * @date 2021-08-20 23:14:41
     * @param a 对象a
     * @param b 对象b
     * @return 如果不广义相等返回true，否则返回false
     */
    public static Boolean isNotBroadEquals(Object a, Object b) {
        return !isBroadEquals(a, b);
    }

    /**
     * description: 判断两个字符串（忽略大小写）是否相等
     * @author laiwen
     * @date 2022-12-08 17:51:02
     * @param a 字符串a
     * @param b 字符串b
     * @return 如果相等返回true，否则返回false
     */
    public static Boolean isEqualsIgnoreCase(String a, String b) {
        return (a == b) || (a != null && a.equalsIgnoreCase(b));
    }

    /**
     * description: 判断两个字符串（忽略大小写）是否不相等
     * @author laiwen
     * @date 2022-12-08 17:51:08
     * @param a 字符串a
     * @param b 字符串b
     * @return 如果不相等返回true，否则返回false
     */
    public static Boolean isNotEqualsIgnoreCase(String a, String b) {
        return !isEqualsIgnoreCase(a, b);
    }

}
