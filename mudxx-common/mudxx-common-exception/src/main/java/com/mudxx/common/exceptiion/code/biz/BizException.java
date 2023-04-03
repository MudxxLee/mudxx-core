package com.mudxx.common.exceptiion.code.biz;

import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exceptiion.code.IErrorCode;

/**
 * @author laiw
 * @date 2023/3/30 15:08
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 5890881510911485848L;
    private String code;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 使用自定义消息
     * @param message  详情
     */
    public BizException(String message) {
        super(message);
        this.code = BizErrorCode.BIZ_ERROR.getCode();
    }
    /**
     * 使用枚举传参
     *
     * @param errorCode 异常枚举
     */
    public BizException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 使用自定义消息
     *
     * @param errorCode 异常枚举
     * @param message  详情
     */
    public BizException(IErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    /**
     * 使用自定义消息
     * @param code 枚举值
     * @param message 详情
     * @param args args
     */
    public BizException(String code, String message, Object... args) {
        super(message == null ? message : StrUtil.format(message, args));
        this.code = code;
    }

    /**
     * #带data的BizException异常
     * @param code 枚举值
     * @param message 详情
     * @param data 数据
     * @param args args
     */
    public BizException(String code, String message, Object data, Object... args) {
        super(args == null ? message : StrUtil.format(message, args));
        this.code = code;
        this.data = data;
    }
}

