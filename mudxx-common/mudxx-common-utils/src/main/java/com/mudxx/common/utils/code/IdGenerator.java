package com.mudxx.common.utils.code;

import java.util.UUID;

/**
 * @author laiw
 * @date 2023/4/28 13:47
 */
public class IdGenerator {

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
