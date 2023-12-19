package com.mudxx.tool.oci.modules.services.events;

import com.mudxx.tool.oci.modules.security.auth.OCIAuthenticationFactory;
import com.mudxx.tool.oci.modules.security.auth.model.OCIAuthenticationProviderModel;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.events.EventsClient;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCIEventsClientFactory {

    /**
     * Create EventsClient
     *
     * @param providerModel
     * @return
     */
    public static EventsClient getClient(OCIAuthenticationProviderModel providerModel, Region region) {
        AuthenticationDetailsProvider provider = OCIAuthenticationFactory.getAuthenticationProvider(providerModel, region);
        return EventsClient.builder().build(provider);
    }

}
