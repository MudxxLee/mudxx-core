package com.mudxx.tool.oci.services.storage.remote.object;

import com.mudxx.tool.oci.services.storage.request.object.OCIGetObjectRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;


public class OCIGetObjectRemote {

    public static GetObjectResponse getObject(ObjectStorageClient client, OCIGetObjectRequest request) {
        /* Create a request and dependent object(s). */
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .namespaceName(request.getNamespaceName())
                .bucketName(request.getBucketName())
                .objectName(request.getObjectName())
                .versionId(null)
                .ifMatch(null)
                .ifNoneMatch(null)
                .opcSseCustomerAlgorithm(null)
                .opcSseCustomerKey(null)
                .opcSseCustomerKeySha256(null)
                .httpResponseContentDisposition(null)
                .httpResponseCacheControl(null)
                .httpResponseContentType(null)
                .httpResponseContentLanguage(null)
                .httpResponseContentEncoding(null)
                .httpResponseExpires(null)
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        return client.getObject(getObjectRequest);
    }


}

