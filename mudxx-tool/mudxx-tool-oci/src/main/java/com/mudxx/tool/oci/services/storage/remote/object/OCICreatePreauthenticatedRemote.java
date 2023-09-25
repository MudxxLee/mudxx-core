package com.mudxx.tool.oci.services.storage.remote.object;

/**
 * This is an automatically generated code sample.
 * To make this code sample work in your Oracle Cloud tenancy,
 * please replace the values for any parameters whose current values do not fit
 * your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 * boolean, number, and enum parameters with values not fitting your use case).
 */

import com.mudxx.tool.oci.common.constans.OCIConstants;
import com.mudxx.tool.oci.services.storage.request.object.OCICreatePreauthenticatedRequest;
import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author laiwen
 */
public class OCICreatePreauthenticatedRemote {

    private static final SimpleDateFormat FORMAT_ = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
    private static final String PREFIX_NAME = "pre-authenticated-request-";

    public static PreauthenticatedRequest createPreauthenticatedRequest(
            ObjectStorageClient client, OCICreatePreauthenticatedRequest request) {

        /* Create a request and dependent object(s). */
        CreatePreauthenticatedRequestDetails createPreauthenticatedRequestDetails = CreatePreauthenticatedRequestDetails.builder()
                .name(getName())
                .objectName(request.getObjectName())
                .accessType(request.getAccessType())
                .bucketListingAction(request.getBucketListingAction())
                .timeExpires(request.getTimeExpires())
                .build();

        CreatePreauthenticatedRequestRequest createPreauthenticatedRequestRequest = CreatePreauthenticatedRequestRequest.builder()
                .namespaceName(request.getNamespaceName())
                .bucketName(request.getBucketName())
                .createPreauthenticatedRequestDetails(createPreauthenticatedRequestDetails)
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        CreatePreauthenticatedRequestResponse response = client.createPreauthenticatedRequest(createPreauthenticatedRequestRequest);
        if (OCIConstants.HTTP_STATUS_CODE != response.get__httpStatusCode__()) {
            throw new RuntimeException("OCI get bucket error");
        }
        return response.getPreauthenticatedRequest();
    }

    private static String getName() {
        try {
            return PREFIX_NAME + FORMAT_.format(new Date());
        } catch (Exception e) {
            return PREFIX_NAME + System.currentTimeMillis();
        }
    }

}

