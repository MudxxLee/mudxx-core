package com.mudxx.common.exception.code.biz;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 参数异常扩展
 * @author laiw
 * @date 2023/3/30 14:10
 */
public enum ParameterErrorCode implements IErrorCode {
    /**
     * 父级定义
     */
    PARAMETER_ERROR("50001", "参数异常"),
    /**
     * 子级定义
     */
    NULL_ERROR("5000101", "%s不能为空"),
    INVALID_VALUE("5000102", "%s值无效"),
    NOT_IN_RANGE("5000103", "%s不在取值范围内：[%s]"),




    ILLEGAL_CHARACTER("5000199", "参数中含有非法字符"),
    ;


    private final String code;
    private final String message;

    ParameterErrorCode(String code, String message) {
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
