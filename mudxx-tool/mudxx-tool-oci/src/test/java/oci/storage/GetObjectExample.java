package oci.storage;

/** This is an automatically generated code sample.
 To make this code sample work in your Oracle Cloud tenancy,
 please replace the values for any parameters whose current values do not fit
 your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 boolean, number, and enum parameters with values not fitting your use case).
 */

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import oci.storage.common.OCIObjectStorageConstants;
import oci.storage.common.OCIObjectStorageFactory;


public class GetObjectExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIObjectStorageFactory.getObjectStorageClient();

        /* Create a request and dependent object(s). */

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .namespaceName(OCIObjectStorageConstants.NAMESPACE_NAME)
                .bucketName("agile-dev-bucket-20230818-1124")
                .objectName("agile-dev-object-20230818-1802.png")
                .versionId(null)
                .ifMatch(null)
                .ifNoneMatch(null)
                .opcClientRequestId(IdUtil.randomUUID())
                .opcSseCustomerAlgorithm(null)
                .opcSseCustomerKey(null)
                .opcSseCustomerKeySha256(null)
                .httpResponseContentDisposition(null)
                .httpResponseCacheControl(null)
                .httpResponseContentType(null)
                .httpResponseContentLanguage(null)
                .httpResponseContentEncoding(null)
                .httpResponseExpires(null)
                .build();

        /* Send request to the Client */
        GetObjectResponse response = client.getObject(getObjectRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

