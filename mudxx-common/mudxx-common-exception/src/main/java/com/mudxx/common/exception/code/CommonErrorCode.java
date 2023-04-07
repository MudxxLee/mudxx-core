package com.mudxx.common.exception.code;

/**
 * 枚举了一些常用API操作码
 *
 * @author laiwen
 */
public enum CommonErrorCode implements IErrorCode {
    /**
     *
     */
    SUCCESS("200", "操作成功"),
    SYSTEM_ERROR("300", "系统错误"),
    UNAUTHORIZED("401", "暂未登录"),
    FORBIDDEN("403", "没有相关权限"),
    NOT_FOUND("404","API找不到");

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
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
