package com.mudxx.common.exception.utils;

import com.mudxx.common.exception.code.IErrorCode;
import com.mudxx.common.exception.code.biz.BizException;
import com.mudxx.common.exception.function.ThrowExceptionFunction;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author laiw
 * @date 2023/3/30 15:01
 */
public class VUtils {

    /**
     * 如果参数为true抛出异常
     *
     * @param bool boolean
     */
    public static ThrowExceptionFunction isTrue(boolean bool) {
        return new ThrowExceptionFunction() {
            @Override
            public void throwMessage(IErrorCode errorCode, Object... args) {
                if (bool) {
                    throw new BizException(errorCode, args);
                }
            }

            @Override
            public void throwExtMessage(IErrorCode errorCode, String message, Object... args) {
                if (bool) {
                    throw new BizException(errorCode.getCode(), message, args);
                }
            }
        };
    }

    /**
     * 如果参数为false抛出异常
     *
     * @param bool boolean
     */
    public static ThrowExceptionFunction isFalse(boolean bool) {
        return isTrue(Boolean.FALSE.equals(bool));
    }

    /**
     * 如果对象为空抛出异常
     *
     * @param object 对象
     */
    public static ThrowExceptionFunction isNull(Object object) {
        return isTrue(Objects.isNull(object));
    }

    /**
     * 如果对象为空抛出异常
     * 支持以下类型:
     * <p>
     * CharSequence:如果长度为零，则认为为空。
     * <p>
     * 数组:如果长度为零，则视为空数组。
     * <p>
     * 集合:如果没有元素，则视为空集合。
     * <p>
     * Map:如果没有键值映射，则认为是空的。
     *
     * @param object 对象
     */
    public static ThrowExceptionFunction isEmpty(Object object) {
        return isTrue(ObjectUtils.isEmpty(object));
    }

    /**
     * 如果对象不为空抛出异常
     * 支持以下类型:
     * <p>
     * CharSequence:如果长度为零，则认为为空。
     * <p>
     * 数组:如果长度为零，则视为空数组。
     * <p>
     * 集合:如果没有元素，则视为空集合。
     * <p>
     * Map:如果没有键值映射，则认为是空的
     *
     * @param object 对象
     */
    public static ThrowExceptionFunction isNotEmpty(Object object) {
        return isTrue(ObjectUtils.isNotEmpty(object));
    }

    /**
     * 如果字符串为空抛出异常
     *
     * @param str 字符串
     */
    public static ThrowExceptionFunction isBlank(String str) {
        return isTrue(StringUtils.isBlank(str));
    }

    /**
     * 如果字符串不为空抛出异常
     *
     * @param str 字符串
     */
    public static ThrowExceptionFunction isNotBlank(String str) {
        return isTrue(StringUtils.isNotBlank(str));
    }

}
