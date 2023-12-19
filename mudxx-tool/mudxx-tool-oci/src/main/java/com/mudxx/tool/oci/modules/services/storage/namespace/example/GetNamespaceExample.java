package com.mudxx.tool.oci.modules.services.storage.namespace.example;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.tool.oci.modules.security.auth.example.AuthenticationProviderExample;
import com.mudxx.tool.oci.modules.services.storage.OCIStorageClientFactory;
import com.oracle.bmc.Region;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

public class GetNamespaceExample {
    public static void main(String[] args) {

        ObjectStorageClient client = OCIStorageClientFactory.getClient(AuthenticationProviderExample.getProviderModel(), Region.AP_SEOUL_1);

        // "ocid1.compartment.oc1..aaaaaaaat6fed7rgdhs6cgfjuzll2wwgk3keaiq5io3ktqlpjchrvpsjuezq"
        /* Create a request and dependent object(s). */
        GetNamespaceRequest getNamespaceRequest = GetNamespaceRequest.builder()
                .opcClientRequestId(IdUtil.randomUUID())
                .compartmentId(null)
                .build();

        /* Send request to the Client */
        GetNamespaceResponse response = client.getNamespace(getNamespaceRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

