package com.mudxx.tool.oci.modules.services.storage.object.request;

import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * @author laiw
 * @date 2023/8/22 15:57
 */
public class OCICreatePreauthenticatedRequest implements Serializable {
    private static final long serialVersionUID = -1361755006216004387L;
    private String namespaceName;
    private String bucketName;
    private String objectName;
    private CreatePreauthenticatedRequestDetails.AccessType accessType;
    /**
     * 桶的限制
     */
    private PreauthenticatedRequest.BucketListingAction bucketListingAction;
    private Date timeExpires;

    public OCICreatePreauthenticatedRequest() {
    }

    private OCICreatePreauthenticatedRequest(Builder builder) {
        setNamespaceName(builder.namespaceName);
        setBucketName(builder.bucketName);
        setObjectName(builder.objectName);
        setAccessType(builder.accessType);
        setBucketListingAction(builder.bucketListingAction);
        setTimeExpires(builder.timeExpires);
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

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public CreatePreauthenticatedRequestDetails.AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(CreatePreauthenticatedRequestDetails.AccessType accessType) {
        this.accessType = accessType;
    }

    public PreauthenticatedRequest.BucketListingAction getBucketListingAction() {
        return bucketListingAction;
    }

    public void setBucketListingAction(PreauthenticatedRequest.BucketListingAction bucketListingAction) {
        this.bucketListingAction = bucketListingAction;
    }

    public Date getTimeExpires() {
        return timeExpires;
    }

    public void setTimeExpires(Date timeExpires) {
        this.timeExpires = timeExpires;
    }

    public static final class Builder {
        private String namespaceName;
        private String bucketName;
        private String objectName;
        private CreatePreauthenticatedRequestDetails.AccessType accessType;
        private PreauthenticatedRequest.BucketListingAction bucketListingAction;
        private Date timeExpires;

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

        public Builder objectName(String val) {
            objectName = val;
            return this;
        }

        public Builder accessType(CreatePreauthenticatedRequestDetails.AccessType val) {
            accessType = val;
            return this;
        }

        public Builder bucketListingAction(PreauthenticatedRequest.BucketListingAction val) {
            bucketListingAction = val;
            return this;
        }

        public Builder timeExpires(Date val) {
            timeExpires = val;
            return this;
        }

        public OCICreatePreauthenticatedRequest build() {
            return new OCICreatePreauthenticatedRequest(this);
        }
    }
}
