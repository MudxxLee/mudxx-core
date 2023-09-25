package com.mudxx.tool.oci.services.storage.remote.bucket;

import com.mudxx.tool.oci.common.constans.OCIConstants;
import com.mudxx.tool.oci.services.storage.request.bucket.OCIGetBucketRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.Bucket;
import com.oracle.bmc.objectstorage.requests.GetBucketRequest;
import com.oracle.bmc.objectstorage.responses.GetBucketResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class OCIGetBucketRemote {

    public static Bucket getBucket(ObjectStorageClient client, OCIGetBucketRequest request) {
        /* Create a request and dependent object(s). */
        GetBucketRequest getBucketRequest = GetBucketRequest.builder()
                .namespaceName(request.getNamespaceName())
                .bucketName(request.getBucketName())
                .ifMatch(null)
                .ifNoneMatch(null)
                .fields(new ArrayList<>(Arrays.asList(
                        GetBucketRequest.Fields.ApproximateCount,
                        GetBucketRequest.Fields.ApproximateSize)))
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        GetBucketResponse response = client.getBucket(getBucketRequest);
        if (OCIConstants.HTTP_STATUS_CODE != response.get__httpStatusCode__()) {
            throw new RuntimeException("OCI get bucket error");
        }
        return response.getBucket();
    }


}

