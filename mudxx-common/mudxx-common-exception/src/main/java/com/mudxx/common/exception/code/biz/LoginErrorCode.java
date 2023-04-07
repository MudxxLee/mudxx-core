package com.mudxx.common.exception.code.biz;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 登录异常扩展
 * @author laiw
 * @date 2023/3/30 14:10
 */
public enum LoginErrorCode implements IErrorCode {
    /**
     * 父级定义
     */
    LOGIN_ERROR("50002", "登录异常"),
    /**
     * 子级定义
     */
    NULL_ERROR("5000201", "用户名或密码错误"),


    ;


    private final String code;
    private final String message;

    LoginErrorCode(String code, String message) {
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
