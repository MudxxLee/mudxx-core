package com.mudxx.common.exceptiion.function;

import com.mudxx.common.exceptiion.code.IErrorCode;

/**
 * 抛异常接口
 * @author laiw
 * @date 2023/3/30 14:59
 */
@FunctionalInterface
public interface ThrowExceptionFunction {

    /**
     * 抛出自定义异常信息
     * @param errorCode 自定义错误码
     */
    void throwMessage(IErrorCode errorCode);

}
