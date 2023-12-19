package com.mudxx.tool.oci.modules.services.support;

import com.mudxx.tool.oci.modules.security.auth.OCIAuthenticationFactory;
import com.mudxx.tool.oci.modules.security.auth.model.OCIAuthenticationProviderModel;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.cims.IncidentClient;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCISupportClientFactory {

    /**
     * Create IncidentClient
     *
     * @param providerModel
     * @param region
     * @return
     */
    public static IncidentClient getClient(OCIAuthenticationProviderModel providerModel, Region region) {
        AuthenticationDetailsProvider provider = OCIAuthenticationFactory.getAuthenticationProvider(providerModel, region);
        return IncidentClient.builder().build(provider);
    }

}
