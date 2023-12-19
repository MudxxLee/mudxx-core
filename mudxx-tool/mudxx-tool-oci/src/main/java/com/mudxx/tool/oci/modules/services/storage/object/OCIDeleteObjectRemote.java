package com.mudxx.tool.oci.modules.services.storage.object;

import com.mudxx.tool.oci.modules.services.storage.object.request.OCIDeleteObjectRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.responses.DeleteObjectResponse;

public class OCIDeleteObjectRemote {

    public static DeleteObjectResponse deleteObject(ObjectStorageClient client, OCIDeleteObjectRequest request) {
        /* Create a request and dependent object(s). */
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .namespaceName(request.getNamespaceName())
                .bucketName(request.getBucketName())
                .objectName(request.getObjectName())
                .ifMatch(null)
                .versionId(null)
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        return client.deleteObject(deleteObjectRequest);
    }

}

