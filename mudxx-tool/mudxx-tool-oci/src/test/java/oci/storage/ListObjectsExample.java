package oci.storage;

/** This is an automatically generated code sample.
 To make this code sample work in your Oracle Cloud tenancy,
 please replace the values for any parameters whose current values do not fit
 your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 boolean, number, and enum parameters with values not fitting your use case).
 */

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.*;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.responses.*;
import oci.storage.common.OCIObjectStorageConstants;
import oci.storage.common.OCIObjectStorageFactory;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.UUID;
import java.util.Arrays;


public class ListObjectsExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIObjectStorageFactory.getObjectStorageClient();

        /* Create a request and dependent object(s). */

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .namespaceName(OCIObjectStorageConstants.NAMESPACE_NAME)
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

