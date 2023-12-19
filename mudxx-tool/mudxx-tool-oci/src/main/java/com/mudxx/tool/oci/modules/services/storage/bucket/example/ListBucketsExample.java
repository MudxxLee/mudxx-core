package com.mudxx.tool.oci.modules.services.storage.bucket.example;

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
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest;
import com.oracle.bmc.objectstorage.responses.ListBucketsResponse;

import java.util.ArrayList;
import java.util.Arrays;


public class ListBucketsExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIStorageClientFactory.getClient(AuthenticationProviderExample.getProviderModel(), Region.AP_SEOUL_1);

        /* Create a request and dependent object(s). */

        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
                .namespaceName("")
                .compartmentId("")
                .limit(1)
                .page(null)
                .fields(new ArrayList<>(Arrays.asList(ListBucketsRequest.Fields.Tags)))
                .opcClientRequestId(IdUtil.randomUUID())
                .build();

        /* Send request to the Client */
        ListBucketsResponse response = client.listBuckets(listBucketsRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

