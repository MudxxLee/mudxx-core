package com.mudxx.tool.oci.modules.services.storage.object.example;

/**
 * This is an automatically generated code sample.
 * To make this code sample work in your Oracle Cloud tenancy,
 * please replace the values for any parameters whose current values do not fit
 * your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 * boolean, number, and enum parameters with values not fitting your use case).
 */

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.tool.oci.modules.security.auth.example.AuthenticationProviderExample;
import com.mudxx.tool.oci.modules.services.storage.OCIStorageClientFactory;
import com.oracle.bmc.Region;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;


public class ListObjectsExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIStorageClientFactory.getClient(AuthenticationProviderExample.getProviderModel(), Region.AP_SEOUL_1);

        /* Create a request and dependent object(s). */

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .namespaceName("")
                .bucketName("agile-dev-bucket-20230818-1124")
                .prefix(null)
                .start("ttttt01/")
                .end(null)
                .limit(1)
                .delimiter("/")
                .fields("name,size,etag,timeCreated,md5,timeModified,storageTier,archivalState")
                .opcClientRequestId(IdUtil.randomUUID())
                .startAfter(null)
                .build();

        /* Send request to the Client */
        ListObjectsResponse response = client.listObjects(listObjectsRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

