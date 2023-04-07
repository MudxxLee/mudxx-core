package com.mudxx.common.exception.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exception.code.biz.BizException;
import com.mudxx.common.exception.function.ThrowExceptionFunction;

/**
 * @author laiw
 * @date 2023/3/30 15:01
 */
public class VUtils {

    /**
     * 如果参数为true抛出异常
     * @param bool boolean
     */
    public static ThrowExceptionFunction isTrue(boolean bool) {
        return (errorCode, args) -> {
            if (bool) {
                throw new BizException(errorCode, args);
            }
        };
    }

    /**
     * 如果字符串为空抛出异常
     * @param str 字符串
     */
    public static ThrowExceptionFunction isBlank(String str) {
        return isTrue(StrUtil.isBlank(str));
    }

    /**
     * 如果对象为空抛出异常
     * @param object 对象
     */
    public static ThrowExceptionFunction isEmpty(Object object) {
        return isTrue(ObjectUtil.isEmpty(object));
    }

}
