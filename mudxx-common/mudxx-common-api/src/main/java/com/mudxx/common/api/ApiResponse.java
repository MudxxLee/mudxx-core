package com.mudxx.common.api;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/10/19 16:04
 */
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 2074360283227278237L;

    private Integer code;

    private String message;

    private String body;

    private T data;

    public ApiResponse(Integer code, String message, String body, T data) {
        this.code = code;
        this.message = message;
        this.body = body;
        this.data = data;
    }

    public ApiResponse(Integer code, String message, String body) {
        this(code, message, body, null);
    }

    public boolean successful() {
        if (this.code == null) {
            return false;
        }
        return this.code >= 200 && this.code <= 299;
    }

    public boolean unsuccessful() {
        return !successful();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}


