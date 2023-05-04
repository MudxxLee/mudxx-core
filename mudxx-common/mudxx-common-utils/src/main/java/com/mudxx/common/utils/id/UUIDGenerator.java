package com.mudxx.common.utils.id;

import java.util.UUID;

/**
 * UUID id
 *
 * @author laiw
 * @date 2023/4/28 13:47
 */
public class UUIDGenerator {

    /**
     * 生成UUID(去除-)
     *
     * @return String
     */
    public static String id() {
        return uid().replace("-", "");
    }

    /**
     * 生成UUID
     *
     * @return String
     */
    public static String uid() {
        return UUID.randomUUID().toString();
    }

}
