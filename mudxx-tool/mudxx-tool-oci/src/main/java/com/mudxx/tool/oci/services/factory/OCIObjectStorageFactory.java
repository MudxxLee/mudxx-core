package com.mudxx.tool.oci.services.factory;

import com.mudxx.tool.oci.common.factory.OCIAuthenticationFactory;
import com.mudxx.tool.oci.common.model.OCIAuthenticationProviderModel;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCIObjectStorageFactory {

    /**
     * Create ObjectStorageClient
     *
     * @param providerModel
     * @return
     */
    public static ObjectStorageClient getObjectStorageClient(OCIAuthenticationProviderModel providerModel) {
        AuthenticationDetailsProvider provider = OCIAuthenticationFactory.getDefaultAuthenticationProvider(providerModel);
        return ObjectStorageClient.builder().build(provider);
    }

    /**
     * Create ObjectStorageClient
     *
     * @param providerModel
     * @return
     */
    public static ObjectStorageClient getObjectStorageClient(OCIAuthenticationProviderModel providerModel, Region region) {
        AuthenticationDetailsProvider provider = OCIAuthenticationFactory.getAuthenticationProvider(providerModel, region);
        return ObjectStorageClient.builder().build(provider);
    }

}
