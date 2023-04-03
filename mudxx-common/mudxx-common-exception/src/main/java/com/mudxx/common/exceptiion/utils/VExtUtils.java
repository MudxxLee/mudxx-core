package com.mudxx.common.exceptiion.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exceptiion.code.biz.BizException;
import com.mudxx.common.exceptiion.function.ThrowExtExceptionFunction;

/**
 * @author laiw
 * @date 2023/3/30 15:01
 */
public class VExtUtils {

    /**
     * 如果参数为true抛出异常
     * @param bool boolean
     */
    public static ThrowExtExceptionFunction isTrue(boolean bool) {
        return (errorCode, errorMessage, args) -> {
            if (bool) {
                throw new BizException(errorCode.getCode(), errorMessage, args);
            }
        };
    }

    /**
     * 如果字符串为空抛出异常
     * @param str 字符串
     */
    public static ThrowExtExceptionFunction isBlank(String str) {
        return isTrue(StrUtil.isBlank(str));
    }

    /**
     * 如果对象为空抛出异常
     * @param object 对象
     */
    public static ThrowExtExceptionFunction isEmpty(Object object) {
        return isTrue(ObjectUtil.isEmpty(object));
    }

}
