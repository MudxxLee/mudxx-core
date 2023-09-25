package com.mudxx.tool.oci.utils;

import java.util.UUID;

/**
 * @author laiw
 * @date 2023/8/22 15:52
 */
public class IdUtils {
    public IdUtils() {
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

}