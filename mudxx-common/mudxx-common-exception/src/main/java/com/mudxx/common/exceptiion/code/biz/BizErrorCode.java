package com.mudxx.common.exceptiion.code.biz;

import com.mudxx.common.exceptiion.code.IErrorCode;

/**
 * 封装API的错误码
 *
 * @author laiwen
 * @date 2019/4/19
 */
public enum BizErrorCode implements IErrorCode {
    /**
     */
    BIZ_ERROR("500", "业务错误"),
    NOT_EXIST("501", "%s 不存在"),
    EXISTS("502", "%s 已存在"),


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
