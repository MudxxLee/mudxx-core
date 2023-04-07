package com.mudxx.common.exception.function;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 抛异常接口
 * @author laiw
 * @date 2023/3/30 14:59
 */
@FunctionalInterface
public interface ThrowExtExceptionFunction {

    /**
     * 抛出自定义异常信息
     * @param errorCode 自定义错误码
     * @param message 异常信息
     * @param args 扩展信息
     */
    void throwMessage(IErrorCode errorCode, String message, Object... args);

}
