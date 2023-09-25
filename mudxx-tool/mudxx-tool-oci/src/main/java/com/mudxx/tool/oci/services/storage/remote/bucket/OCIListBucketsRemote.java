package com.mudxx.tool.oci.services.storage.remote.bucket;

import com.mudxx.tool.oci.services.storage.request.bucket.OCIListBucketsRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest;
import com.oracle.bmc.objectstorage.responses.ListBucketsResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class OCIListBucketsRemote {

    public static ListBucketsResponse listBuckets(ObjectStorageClient client, OCIListBucketsRequest request) {
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
                .namespaceName(request.getBucketName())
                .compartmentId(request.getCompartmentId())
                .limit(1)
                .page(null)
                .fields(new ArrayList<>(Arrays.asList(ListBucketsRequest.Fields.Tags)))
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        return client.listBuckets(listBucketsRequest);
    }


}

