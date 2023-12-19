package com.mudxx.tool.oci.modules.services.storage.object;

import com.mudxx.tool.oci.common.constans.OCIConstants;
import com.mudxx.tool.oci.modules.services.storage.bucket.request.OCIGetBucketRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.ListObjects;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;

public class OCIListObjectsRemote {

    public static ListObjects listObjects(ObjectStorageClient client, OCIGetBucketRequest request) {
        /* Create a request and dependent object(s). */
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .namespaceName(request.getNamespaceName())
                .bucketName(request.getBucketName())
                .prefix(request.getPrefix())
                .start(request.getNextStartWith())
                .end(null)
                .limit(request.getLimit())
                .delimiter("/")
                .fields("name,size,timeCreated,timeModified")
                .startAfter(null)
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        ListObjectsResponse response = client.listObjects(listObjectsRequest);
        if (OCIConstants.HTTP_STATUS_CODE != response.get__httpStatusCode__()) {
            throw new RuntimeException("OCI get bucket error");
        }
        return response.getListObjects();
    }


}

