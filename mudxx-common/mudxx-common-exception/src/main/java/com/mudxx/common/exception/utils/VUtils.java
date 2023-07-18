package com.mudxx.common.exception.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exception.code.IErrorCode;
import com.mudxx.common.exception.code.biz.BizErrorCode;
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
        return new ThrowExceptionFunction() {
            @Override
            public void throwMessage(IErrorCode errorCode, Object... args) {
                if (bool) {
                    throw new BizException(errorCode, args);
                }
            }
            @Override
            public void throwMessage(IErrorCode errorCode, String message, Object... args) {
                if (bool) {
                    throw new BizException(errorCode.getCode(), message, args);
                }
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

    public static void main(String[] args) {
        VUtils.isTrue(true).throwMessage(BizErrorCode.BIZ_ERROR);
        VUtils.isTrue(true).throwMessage(BizErrorCode.BIZ_ERROR, "测完");
        VUtils.isEmpty(true).throwMessage(BizErrorCode.BIZ_ERROR);
        VUtils.isEmpty(null).throwMessage(BizErrorCode.BIZ_ERROR, "测完");
        VUtils.isBlank("true").throwMessage(BizErrorCode.BIZ_ERROR);
        VUtils.isBlank("").throwMessage(BizErrorCode.BIZ_ERROR, "测完");
    }

}
