package com.mudxx.tool.oci.modules.services.storage.bucket.request;

import java.io.Serializable;
import java.util.Map;

/**
 * @author laiw
 * @date 2023/8/22 15:57
 */
public class OCICreateBucketRequest implements Serializable {
    private static final long serialVersionUID = -1361755006216004387L;
    private String namespaceName;
    private String compartmentId;
    private String bucketName;
    private Map<String, String> metadata;
    private Map<String, String> freeformTags;

    public OCICreateBucketRequest() {
    }

    private OCICreateBucketRequest(Builder builder) {
        setNamespaceName(builder.namespaceName);
        setCompartmentId(builder.compartmentId);
        setBucketName(builder.bucketName);
        setMetadata(builder.metadata);
        setFreeformTags(builder.freeformTags);
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getFreeformTags() {
        return freeformTags;
    }

    public void setFreeformTags(Map<String, String> freeformTags) {
        this.freeformTags = freeformTags;
    }

    public static final class Builder {
        private String namespaceName;
        private String compartmentId;
        private String bucketName;
        private Map<String, String> metadata;
        private Map<String, String> freeformTags;

        public Builder() {
        }

        public Builder namespaceName(String val) {
            namespaceName = val;
            return this;
        }

        public Builder compartmentId(String val) {
            compartmentId = val;
            return this;
        }

        public Builder bucketName(String val) {
            bucketName = val;
            return this;
        }

        public Builder metadata(Map<String, String> val) {
            metadata = val;
            return this;
        }

        public Builder freeformTags(Map<String, String> val) {
            freeformTags = val;
            return this;
        }

        public OCICreateBucketRequest build() {
            return new OCICreateBucketRequest(this);
        }
    }
}
