package com.mudxx.tool.oci.modules.services.storage.object.request;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/8/22 15:57
 */
public class OCIListObjectsRequest implements Serializable {
    private static final long serialVersionUID = -1361755006216004387L;
    private String namespaceName;
    private String bucketName;

    public OCIListObjectsRequest() {
    }

    private OCIListObjectsRequest(Builder builder) {
        setNamespaceName(builder.namespaceName);
        setBucketName(builder.bucketName);
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

    public static final class Builder {
        private String namespaceName;
        private String bucketName;

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

        public OCIListObjectsRequest build() {
            return new OCIListObjectsRequest(this);
        }
    }
}
