package com.mudxx.common.web.response;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.mudxx.common.exceptiion.code.CommonErrorCode;
import com.mudxx.common.exceptiion.code.IErrorCode;

/**
 * 通用返回对象
 *
 * @author laiwen
 */
public class CommonResult<T> {
    private String code;
    private String message;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    protected CommonResult(IErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this, new JSONConfig().setIgnoreNullValue(false));
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(CommonErrorCode.SUCCESS, data);
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return success(null);
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 提示信息
     * @param data 数据
     */
    public static <T> CommonResult<T> failed(String code, String message, T data) {
        return new CommonResult<T>(code, message, data);
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String code, String message) {
        return failed(code, message, null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return failed(errorCode.getCode(), message);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return failed(errorCode, null);
    }

    /**
     * 未登录返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> unauthorized(String message) {
        return failed(CommonErrorCode.UNAUTHORIZED, message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized() {
        return failed(CommonErrorCode.UNAUTHORIZED);
    }

    /**
     * 未授权返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> forbidden(String message) {
        return failed(CommonErrorCode.FORBIDDEN, message);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden() {
        return failed(CommonErrorCode.FORBIDDEN);
    }

    /**
     * API找不到返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> notfound(String message) {
        return failed(CommonErrorCode.NOT_FOUND, message);
    }

    /**
     * API找不到返回结果
     */
    public static <T> CommonResult<T> notfound() {
        return failed(CommonErrorCode.NOT_FOUND);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
