package oci.storage;

import cn.hutool.core.util.IdUtil;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.cims.IncidentClient;
import com.oracle.bmc.cims.model.SortBy;
import com.oracle.bmc.cims.model.SortOrder;
import com.oracle.bmc.cims.requests.ListIncidentResourceTypesRequest;
import com.oracle.bmc.cims.responses.ListIncidentResourceTypesResponse;
import oci.storage.common.OCIObjectStorageConstants;
import oci.storage.common.OCIObjectStorageFactory;

import java.io.ByteArrayInputStream;

public class Demo {

    public static void main(String[] args) throws Exception{

        AuthenticationDetailsProvider provider = OCIObjectStorageFactory.getProvider();

        /* Create a service client */
        IncidentClient client = IncidentClient.builder().build(provider);

        ListIncidentResourceTypesRequest listIncidentResourceTypesRequest = ListIncidentResourceTypesRequest.builder()
                .problemType("LIMIT")
                .compartmentId(OCIObjectStorageConstants.COMPARTMENT_ID)
                .ocid(provider.getUserId())
                .csi("support")
                .sortBy(SortBy.Severity)
                .opcRequestId(IdUtil.randomUUID())
                .sortOrder(SortOrder.Asc).build();

        /* Send request to the Client */
        ListIncidentResourceTypesResponse response = client.listIncidentResourceTypes(listIncidentResourceTypesRequest);
        System.out.println(response.getItems());
    }
}
