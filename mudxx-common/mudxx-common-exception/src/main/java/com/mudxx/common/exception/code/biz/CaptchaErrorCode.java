package com.mudxx.common.exception.code.biz;

import com.mudxx.common.exception.code.IErrorCode;

/**
 * 验证码异常扩展
 *
 * @author laiwen
 */
public enum CaptchaErrorCode implements IErrorCode {
    /**
     * 父级定义
     */
    CAPTCHA_ERROR("50006", "验证码异常"),
    /**
     * 子级定义
     */
    CAPTCHA_INVALID("5000601", "验证码已失效，请重新获取"),
    CAPTCHA_CHECK_ERROR("5000602", "验证失败"),
    CAPTCHA_BASEMAP_NULL("5000603", "底图未初始化成功，请检查路径"),
    CAPTCHA_GET_ERROR("5000604", "获取验证码失败,请联系管理员"),

    REQ_LIMIT_GET_ERROR("5000631", "get接口请求次数超限，请稍后再试!"),
    REQ_INVALID("5000632", "无效请求，请重新获取验证码"),
    REQ_LOCK_GET_ERROR("5000633", "接口验证失败数过多，请稍后再试"),
    REQ_LIMIT_CHECK_ERROR("5000634", "check接口请求次数超限，请稍后再试!"),
    REQ_LIMIT_VERIFY_ERROR("5000635", "verify接口请求次数超限!"),
    ;
    private final String code;
    private final String message;

    CaptchaErrorCode(String code, String message) {
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
