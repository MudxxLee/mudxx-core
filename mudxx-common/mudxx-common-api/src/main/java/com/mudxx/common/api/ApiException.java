package com.mudxx.common.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laiwen
 */
public class ApiException extends Exception {
    private static final long serialVersionUID = -4322013542730177208L;
    private static final int SYS_ERROR_CODE = -99;

    private Integer code;

    private Map<String, List<String>> responseHeaders;

    private String responseBody;

    public ApiException(Integer code, String message, Throwable throwable, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    public ApiException(Integer code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(code, message, null, responseHeaders, responseBody);
    }

    public ApiException(Integer code, String message, Map<String, List<String>> responseHeaders) {
        this(code, message, null, responseHeaders, null);
    }

    public ApiException(String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(SYS_ERROR_CODE, message, null, responseHeaders, responseBody);
    }

    public ApiException(String message, Throwable throwable, Map<String, List<String>> responseHeaders) {
        this(SYS_ERROR_CODE, message, throwable, responseHeaders, null);
    }

    public String toSimpleJsonStr() {
        try {
            Map<String, Object> jsonStr = new HashMap<>();
            jsonStr.put("code", this.getCode());
            jsonStr.put("message", this.getMessage());
            jsonStr.put("responseBody", this.getResponseBody());
            return JSON.toJSONString(jsonStr, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            return "{\"code\":" + SYS_ERROR_CODE + ",\"message\":\"Unknown error\"}";
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}


