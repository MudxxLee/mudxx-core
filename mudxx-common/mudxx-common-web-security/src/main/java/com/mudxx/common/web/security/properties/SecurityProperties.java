package com.mudxx.common.web.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;


/**
 * @author laiw
 * @date 2023/12/1 16:58
 */
@ConfigurationProperties(prefix = "security", ignoreInvalidFields = true)
public class SecurityProperties implements Serializable {
    private static final long serialVersionUID = 4588420776026506192L;

    private Long maxSeconds = 60L;

    private String redisKey = "SECURITY:NONCE";

    private String headerTimestamp = "X-Security-Timestamp";

    private String headerSign = "X-Security-Sign";

    private String headerNonce = "X-Security-Nonce";

    public Long getMaxSeconds() {
        return maxSeconds;
    }

    public void setMaxSeconds(Long maxSeconds) {
        this.maxSeconds = maxSeconds;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getHeaderSign() {
        return headerSign;
    }

    public void setHeaderSign(String headerSign) {
        this.headerSign = headerSign;
    }

    public String getHeaderTimestamp() {
        return headerTimestamp;
    }

    public void setHeaderTimestamp(String headerTimestamp) {
        this.headerTimestamp = headerTimestamp;
    }

    public String getHeaderNonce() {
        return headerNonce;
    }

    public void setHeaderNonce(String headerNonce) {
        this.headerNonce = headerNonce;
    }
}
