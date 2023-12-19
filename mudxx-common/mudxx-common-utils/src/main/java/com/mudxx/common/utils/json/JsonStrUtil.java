package com.mudxx.common.utils.json;

import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;

/**
 * @author laiw
 * @date 2023/10/19 16:04
 */
public class JsonStrUtil {

    private static final JSONConfig JSON_CONFIG = new JSONConfig();

    public static String toJsonStr(Object object) {
        return toJsonStr(object, false, false);
    }

    public static String toJsonStrWithNull(Object object) {
        return toJsonStr(object, false, true);
    }

    public static String toJsonStrPinyinOrder(Object object) {
        return toJsonStr(object, true, false);
    }

    public static String toJsonStrMelody(Object object) {
        return toJsonStr(object, true, true);
    }

    public static String toJsonStr(Object object, boolean isPinyinOrder, boolean ignoreNullValue) {
        if (object == null) {
            return null;
        }
        try {
            JSON_CONFIG.setKeyComparator(null);
            if (isPinyinOrder) {
                JSON_CONFIG.setKeyComparator(new PinyinComparator());
            }
            JSON_CONFIG.setIgnoreNullValue(ignoreNullValue);
            return JSONUtil.toJsonStr(object, JSON_CONFIG);
        } catch (Throwable e) {
            return "Parsing JSON string error";
        }
    }


}


