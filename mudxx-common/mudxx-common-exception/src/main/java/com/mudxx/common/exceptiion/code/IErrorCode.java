package com.mudxx.common.exceptiion.code;

/**
 * 封装API的错误码
 *
 * @author laiwen
 * @date 2019/4/19
 */
public interface IErrorCode {

    /**
     * code
     * @return 定义码
     */
    String getCode();

    /**
     * message
     * @return 描述
     */
    String getMessage();
}
