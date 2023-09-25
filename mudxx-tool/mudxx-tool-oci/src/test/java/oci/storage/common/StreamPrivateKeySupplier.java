package oci.storage.common;

import cn.hutool.core.io.IoUtil;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author laiw
 * @date 2023/8/21 17:51
 */
public class StreamPrivateKeySupplier implements Supplier<InputStream> {
    private final String privateKey;

    public StreamPrivateKeySupplier(String privateKey) {
        this.privateKey = privateKey;
    }

    public InputStream get() {
        try {
            return IoUtil.toUtf8Stream(this.privateKey);
        } catch (Exception var2) {
            throw new IllegalArgumentException("Could not find private key: " + this.privateKey, var2);
        }
    }

}
