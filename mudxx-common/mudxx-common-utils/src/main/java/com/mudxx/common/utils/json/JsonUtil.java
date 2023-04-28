package com.mudxx.common.utils.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * description: JSON处理工具类
 * @author laiwen
 * @date 2021-05-19 14:30:23
 */
@SuppressWarnings("ALL")
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * description: 对Object对象进行JSON序列化
     * @author laiwen
     * @date 2021-05-19 11:41:53
     * @param object 待序列化对象
     * @return 返回序列化结果
     */
    public static String toString(Object object) {
        if (object == null) {
            return null;
        }
        if (object.getClass() == String.class) {
            return (String) object;
        }
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error("JSON序列化出错：" + object, e);
            return null;
        }
    }

    /**
     * description: 对JSON字符串进行JSON解析，解析成bean
     * @author laiwen
     * @date 2021-05-19 11:41:59
     * @param json 待解析的JSON字符串
     * @param clazz bean对应类的字节码
     * @param <T> 泛型
     * @return 返回解析结果
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("JSON解析出错：" + json, e);
            return null;
        }
    }

    /**
     * description: 对JSON字符串进行JSON解析，解析成元素为bean的List集合
     * @author laiwen
     * @date 2021-05-19 11:42:05
     * @param json 待解析的JSON字符串
     * @param clazz bean对应类的字节码
     * @param <T> 泛型
     * @return 返回解析结果
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.error("JSON解析出错：" + json, e);
            return null;
        }
    }

    /**
     * description: 对JSON字符串进行JSON解析，解析成Map集合
     * @author laiwen
     * @date 2021-05-19 11:42:11
     * @param json 待解析的JSON字符串
     * @param kClazz key对应类型的字节码
     * @param vClazz value对应类型的字节码
     * @param <K> key的泛型
     * @param <V> value的泛型
     * @return 返回解析结果
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructMapType(Map.class, kClazz, vClazz));
        } catch (Exception e) {
            log.error("JSON解析出错：" + json, e);
            return null;
        }
    }

    /**
     * description: 对JSON字符串进行JSON解析，解析结果类型自定义，比如：Map<String, List<Object>>
     * @author laiwen
     * @date 2021-05-19 11:42:17
     * @param json 待解析的JSON字符串
     * @param type 类型引用
     * @param <T> 泛型
     * @return 返回解析结果
     */
    public static <T> T toCustom(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.error("JSON解析出错：" + json, e);
            return null;
        }
    }

}
