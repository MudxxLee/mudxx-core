package com.mudxx.tool.oci.modules.services.storage;

import com.mudxx.tool.oci.modules.security.auth.OCIAuthenticationFactory;
import com.mudxx.tool.oci.modules.security.auth.model.OCIAuthenticationProviderModel;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCIStorageClientFactory {

    /**
     * Create ObjectStorageClient
     *
     * @param providerModel
     * @param region
     * @return
     */
    public static ObjectStorageClient getClient(OCIAuthenticationProviderModel providerModel, Region region) {
        AuthenticationDetailsProvider provider = OCIAuthenticationFactory.getAuthenticationProvider(providerModel, region);
        return ObjectStorageClient.builder().build(provider);
    }

}
