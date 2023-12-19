package com.mudxx.common.exception.code.biz;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 安全异常扩展
 *
 * @author laiw
 * @date 2023/3/30 14:10
 */
public enum SecurityErrorCode implements IErrorCode {
    /**
     * 父级定义
     */
    SECURITY_ERROR("50003", "安全异常"),
    /**
     * 子级定义
     */
    ILLEGAL_HEADER("5000301", "非法的请求头"),
    EXPIRED_REQUEST("5000302", "过期的请求"),
    REPEATED_REQUEST("5000303", "重复的请求"),
    ARGUMENT_ERROR("5000304", "验签错误"),
    ;


    private final String code;
    private final String message;

    SecurityErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
