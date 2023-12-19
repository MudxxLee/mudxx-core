package com.mudxx.common.web.security.model;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/12/4 13:41
 */
public class SecurityHeader implements Serializable {
    private static final long serialVersionUID = 4503771519323775447L;

    public Long timestamp;

    public String sign;

    public String nonce;

    public SecurityHeader() {
    }

    private SecurityHeader(Builder builder) {
        setTimestamp(builder.timestamp);
        setSign(builder.sign);
        setNonce(builder.nonce);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public static final class Builder {
        private Long timestamp;
        private String sign;
        private String nonce;

        public Builder() {
        }

        public Builder timestamp(Long val) {
            timestamp = val;
            return this;
        }

        public Builder sign(String val) {
            sign = val;
            return this;
        }

        public Builder nonce(String val) {
            nonce = val;
            return this;
        }

        public SecurityHeader build() {
            return new SecurityHeader(this);
        }
    }
}
