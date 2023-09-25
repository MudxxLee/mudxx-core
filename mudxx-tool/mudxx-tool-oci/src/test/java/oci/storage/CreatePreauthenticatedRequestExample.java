package oci.storage;

/** This is an automatically generated code sample.
 To make this code sample work in your Oracle Cloud tenancy,
 please replace the values for any parameters whose current values do not fit
 your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 boolean, number, and enum parameters with values not fitting your use case).
 */

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.*;
import com.oracle.bmc.objectstorage.requests.*;
import com.oracle.bmc.objectstorage.responses.*;
import oci.storage.common.OCIObjectStorageConstants;
import oci.storage.common.OCIObjectStorageFactory;

import java.util.Date;


public class CreatePreauthenticatedRequestExample {
    public static void main1(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIObjectStorageFactory.getObjectStorageClient();

        /* Create a request and dependent object(s). */
        CreatePreauthenticatedRequestDetails createPreauthenticatedRequestDetails = CreatePreauthenticatedRequestDetails.builder()
                .name("agile-dev-pre-authenticated-request-20230823-1556")
                .bucketListingAction(PreauthenticatedRequest.BucketListingAction.ListObjects)
                .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectReadWrite)
                .objectName("agile-dev-object-20230823-1556.png")
                .timeExpires(DateUtil.offsetMinute(new Date(), 150))
                .build();

        CreatePreauthenticatedRequestRequest createPreauthenticatedRequestRequest = CreatePreauthenticatedRequestRequest.builder()
                .namespaceName(OCIObjectStorageConstants.NAMESPACE_NAME)
                .bucketName("agile-dev-bucket-20230818-1124")
                .createPreauthenticatedRequestDetails(createPreauthenticatedRequestDetails)
                .opcClientRequestId(IdUtil.randomUUID())
                .build();

        /* Send request to the Client */
        CreatePreauthenticatedRequestResponse response = client.createPreauthenticatedRequest(createPreauthenticatedRequestRequest);
        System.out.println(JSONUtil.toJsonStr(response));
        System.out.println(JSONUtil.toJsonStr(response.getPreauthenticatedRequest().getFullPath()));
    }

    public static void main(String[] args) {
        DateTime date = DateUtil.date();
        System.out.println("pre-authenticated-request-" + DateUtil.format(date, "yyyyMMdd-HHmmssSSS"));
    }


}

