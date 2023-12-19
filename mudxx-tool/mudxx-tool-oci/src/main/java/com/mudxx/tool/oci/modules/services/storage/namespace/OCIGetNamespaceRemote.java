package com.mudxx.tool.oci.modules.services.storage.namespace;

import com.mudxx.tool.oci.utils.IdUtils;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

public class OCIGetNamespaceRemote {

    public static String getNamespace(ObjectStorageClient client, String compartmentId) {
        /* Create a request and dependent object(s). */
        GetNamespaceRequest getNamespaceRequest = GetNamespaceRequest.builder()
                .compartmentId(compartmentId)
                .opcClientRequestId(IdUtils.randomUUID())
                .build();

        /* Send request to the Client */
        GetNamespaceResponse response = client.getNamespace(getNamespaceRequest);
        return response.getValue();
    }

}

