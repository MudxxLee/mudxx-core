package com.mudxx.common.utils.http.okhttp;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/9/22 09:26
 */
public class OkHttpResponse implements Serializable {
    private static final long serialVersionUID = -6943029898507980808L;
    private Boolean responseBool;
    private Integer responseCode;
    private String responseBody;

    public OkHttpResponse() {
    }

    private OkHttpResponse(Builder builder) {
        setResponseBool(builder.responseBool);
        setResponseCode(builder.responseCode);
        setResponseBody(builder.responseBody);
    }

    public Boolean getResponseBool() {
        return responseBool;
    }

    public void setResponseBool(Boolean responseBool) {
        this.responseBool = responseBool;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public static final class Builder {
        private Boolean responseBool;
        private Integer responseCode;
        private String responseBody;

        public Builder() {
        }

        public Builder responseBool(Boolean val) {
            responseBool = val;
            return this;
        }

        public Builder responseCode(Integer val) {
            responseCode = val;
            return this;
        }

        public Builder responseBody(String val) {
            responseBody = val;
            return this;
        }

        public OkHttpResponse build() {
            return new OkHttpResponse(this);
        }
    }
}
