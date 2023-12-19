package com.mudxx.tool.oci.modules.security.auth;

import com.mudxx.tool.oci.modules.security.auth.model.OCIAuthenticationProviderModel;
import com.mudxx.tool.oci.modules.security.auth.supplier.OCIStringPrivateKeySupplier;
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
