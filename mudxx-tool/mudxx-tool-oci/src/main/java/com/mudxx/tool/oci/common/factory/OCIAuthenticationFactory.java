package com.mudxx.tool.oci.common.factory;

import com.mudxx.tool.oci.common.supplier.OCIStringPrivateKeySupplier;
import com.mudxx.tool.oci.common.model.OCIAuthenticationProviderModel;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCIAuthenticationFactory {

    public final static Region DEFAULT_REGION = Region.AP_SEOUL_1;

    /**
     * Create Authentication provider
     *
     * @param providerModel
     * @return
     */
    public static AuthenticationDetailsProvider getDefaultAuthenticationProvider(OCIAuthenticationProviderModel providerModel) {
        return getAuthenticationProvider(providerModel, DEFAULT_REGION);
    }

    /**
     * Create Authentication provider
     *
     * @param providerModel
     * @return
     */
    public static AuthenticationDetailsProvider getAuthenticationProvider(OCIAuthenticationProviderModel providerModel, Region region) {
        return SimpleAuthenticationDetailsProvider.builder()
                .userId(providerModel.getUserId())
                .tenantId(providerModel.getTenancy())
                .fingerprint(providerModel.getFingerprint())
                .region(region)
                .privateKeySupplier(getPrivateKeySupplier(providerModel))
                .build();
    }

    private static Supplier<InputStream> getPrivateKeySupplier(OCIAuthenticationProviderModel providerModel) {
        Supplier<InputStream> privateKeySupplier;
        if (providerModel.getPrivateKeyFile() != null) {
            privateKeySupplier = new SimplePrivateKeySupplier(providerModel.getPrivateKeyFile());
        } else {
            privateKeySupplier = new OCIStringPrivateKeySupplier(providerModel.getPrivateKey());
        }
        return privateKeySupplier;
    }

}
