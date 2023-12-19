package com.mudxx.tool.oci.modules.services.storage.bucket.request;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/8/22 15:57
 */
public class OCIGetBucketRequest implements Serializable {
    private static final long serialVersionUID = -1361755006216004387L;
    private String namespaceName;
    private String bucketName;
    private String prefix;
    private Integer limit;
    private String nextStartWith;

    public OCIGetBucketRequest() {
    }

    private OCIGetBucketRequest(Builder builder) {
        setNamespaceName(builder.namespaceName);
        setBucketName(builder.bucketName);
        setPrefix(builder.prefix);
        setLimit(builder.limit);
        setNextStartWith(builder.nextStartWith);
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNextStartWith() {
        return nextStartWith;
    }

    public void setNextStartWith(String nextStartWith) {
        this.nextStartWith = nextStartWith;
    }

    public static final class Builder {
        private String namespaceName;
        private String bucketName;
        private String prefix;
        private Integer limit;
        private String nextStartWith;

        public Builder() {
        }

        public Builder namespaceName(String val) {
            namespaceName = val;
            return this;
        }

        public Builder bucketName(String val) {
            bucketName = val;
            return this;
        }

        public Builder prefix(String val) {
            prefix = val;
            return this;
        }

        public Builder limit(Integer val) {
            limit = val;
            return this;
        }

        public Builder nextStartWith(String val) {
            nextStartWith = val;
            return this;
        }

        public OCIGetBucketRequest build() {
            return new OCIGetBucketRequest(this);
        }
    }
}
