package com.mudxx.common.exception.code.biz;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 错误码
 *
 * @author laiwen
 * @date 2019/4/19
 */
public enum BizErrorCode implements IErrorCode {
    /**
     * 业务错误码
     */
    BIZ_ERROR("500", "%s"),
    NOT_EXIST("501", "%s不存在"),
    EXISTS("502", "%s已存在"),
    DISABLED("503", "%s已禁用"),
    UNSUPPORTED("504", "不支持的：%s"),

    ;


    private final String code;
    private final String message;

    BizErrorCode(String code, String message) {
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
