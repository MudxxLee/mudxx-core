package com.mudxx.tool.oci.common.supplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/**
 * @author laiw
 * @date 2023/8/21 17:51
 */
public class OCIStringPrivateKeySupplier implements Supplier<InputStream> {
    private final String privateKey;

    public OCIStringPrivateKeySupplier(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public InputStream get() {
        try {
            return new ByteArrayInputStream(this.privateKey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception var2) {
            throw new IllegalArgumentException("Could not find private key: " + this.privateKey, var2);
        }
    }

}
