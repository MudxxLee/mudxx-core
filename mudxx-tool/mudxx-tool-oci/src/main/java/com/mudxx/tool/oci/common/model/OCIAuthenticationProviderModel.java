package com.mudxx.tool.oci.common.model;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/8/22 13:47
 */
public class OCIAuthenticationProviderModel implements Serializable {
    private static final long serialVersionUID = 7904960083796288433L;
    private String userId;
    private String tenancy;
    private String fingerprint;
    private String privateKey;
    private String privateKeyFile;

    public OCIAuthenticationProviderModel() {
    }

    private OCIAuthenticationProviderModel(Builder builder) {
        setUserId(builder.userId);
        setTenancy(builder.tenancy);
        setFingerprint(builder.fingerprint);
        setPrivateKey(builder.privateKey);
        setPrivateKeyFile(builder.privateKeyFile);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenancy() {
        return tenancy;
    }

    public void setTenancy(String tenancy) {
        this.tenancy = tenancy;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public static final class Builder {
        private String userId;
        private String tenancy;
        private String fingerprint;
        private String privateKey;
        private String privateKeyFile;

        public Builder() {
        }

        public Builder userId(String val) {
            userId = val;
            return this;
        }

        public Builder tenancy(String val) {
            tenancy = val;
            return this;
        }

        public Builder fingerprint(String val) {
            fingerprint = val;
            return this;
        }

        public Builder privateKey(String val) {
            privateKey = val;
            return this;
        }

        public Builder privateKeyFile(String val) {
            privateKeyFile = val;
            return this;
        }

        public OCIAuthenticationProviderModel build() {
            return new OCIAuthenticationProviderModel(this);
        }
    }
}
