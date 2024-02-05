package com.mudxx.common.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author laiwen
 * @description 阿里巴巴提供的fastjson常用工具类方法
 * @date 2019-07-26 16:01:44
 */
@SuppressWarnings("ALL")
public class FastjsonUtil {

    /**
     * 提醒：该方法与下面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     *
     * @param bean Bean对象
     * @param <T>  泛型
     * @return 返回JSONObject对象
     * @description Bean（包括Map）对象转JSONObject对象
     */
    public static <T> JSONObject bean2JsonObject(T bean) {
        //方式一：推荐（简洁）
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(bean));

        //方式二：不怎么推荐（稍微复杂一点）
        //JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(bean));

        return jsonObject;
    }

    /**
     * 提醒：该方法与上面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     *
     * @param bean Bean对象
     * @param <T>  泛型
     * @return 返回JSONObject对象
     * @description Bean（包括Map）对象转JSONObject对象
     */
    public static <T> JSONObject beanToJsonObject(T bean) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(bean);
        return jsonObject;
    }

    /**
     * 提醒：该方法与下面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     *
     * @param beanList Bean集合对象
     * @param <T>      泛型
     * @return 返回JSONArray对象
     * @description Bean（包括Map）集合对象转JSONArray对象
     */
    public static <T> JSONArray beanList2JsonArray(List<T> beanList) {
        //方式一：推荐（简洁）
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(beanList));

        //方式二：不怎么推荐（稍微复杂一点）
        //JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(beanList));

        return jsonArray;
    }

    /**
     * 提醒：该方法与上面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     *
     * @param beanList Bean集合对象
     * @param <T>      泛型
     * @return 返回JSONArray对象
     * @description Bean（包括Map）集合对象转JSONArray对象
     */
    public static <T> JSONArray beanListToJsonArray(List<T> beanList) {
        JSONArray jsonArray = (JSONArray) JSON.toJSON(beanList);
        return jsonArray;
    }

    /**
     * 提醒：这里的json格式的字符串形如："{}"
     *
     * @param jsonStr json格式的字符串
     * @param clazz   Bean的Class对象
     * @param <T>     泛型
     * @return 返回Bean对象
     * @description json格式的字符串转Bean（包括Map）对象
     */
    public static <T> T jsonStr2Bean(String jsonStr, Class<T> clazz) {
        T t = JSON.parseObject(jsonStr, clazz);
        return t;
    }

    /**
     * 提醒：这里的json格式的字符串形如："{}"
     *
     * @param jsonStr json格式的字符串
     * @param <K>     Map的 key 类型
     * @param <V>     Map的 value 类型
     * @return Map对象
     * @description json格式的字符串转Map对象
     */
    public static <K, V> Map<K, V> jsonStr2Map(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<Map<K, V>>() {
        });
    }

    /**
     * 提醒：这里的json格式的字符串形如："{}"
     *
     * @param jsonStr json格式的字符串
     * @param clazz   Bean的Class对象
     * @param <T>     泛型
     * @return 返回Bean对象
     * @description json格式的字符串转Bean（包括Map）对象
     */
    public static <K, V> Map<K, V> jsonStr2MapOrdered(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<Map<K, V>>() {
        }, Feature.OrderedField);
    }

    /**
     * 提醒：这里的json格式的字符串形如："[{}...]"
     *
     * @param jsonStr json格式的字符串
     * @param clazz   Bean的Class对象
     * @param <T>     泛型
     * @return 返回Bean集合对象
     * @description json格式的字符串转Bean（包括Map）集合对象
     */
    public static <T> List<T> jsonStr2BeanList(String jsonStr, Class<T> clazz) {
        List<T> ts = JSON.parseArray(jsonStr, clazz);
        return ts;
    }

    /**
     * 提醒：这里的json格式的字符串形如："{'':'',...}"
     *
     * @param jsonStr json格式的字符串
     * @return 返回JSONObject对象
     * @description json格式的字符串转JSONObject对象
     */
    public static JSONObject jsonStr2JSONObject(String jsonStr) {
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        return jsonObject;
    }

    /**
     * 提醒：这里的json格式的字符串形如："[{'':'',...},...]"
     *
     * @param jsonStr json格式的字符串
     * @return 返回JSONArray对象
     * @description json格式的字符串转JSONArray对象
     */
    public static JSONArray jsonStr2JSONArray(String jsonStr) {
        JSONArray jsonArray = (JSONArray) JSON.parse(jsonStr);
        return jsonArray;
    }

    // ************************************************************************************** //

    /**
     * @param key
     * @param value
     * @return
     * @description 生成JSONObject字符串(有且只有一个键值对)
     */
    public static String generateJsonObjectString(String key, Object value) {
        if (StringUtils.isNotBlank(key)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key, value);
            return jsonObject.toString();
        }
        return "";
    }

    /**
     * @param map
     * @return
     * @description Map转JSONObject字符串
     */
    public static String map2JsonObjectString(Map<String, Object> map) {
        if (map != null) {
            JSONObject jsonObject = new JSONObject();

            //方法一：该方法比较好
			/*for (Map.Entry<String, Object> entry : map.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}*/

            //方法二：
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject.toString();
        }
        return "";
    }

    /**
     * @param mapList
     * @return
     * @description mapList转JSONArray字符串
     */
    public static String mapList2JsonArrayString(List<Map<String, Object>> mapList) {
        if (mapList != null) {
            return convertMapList2JsonArray(mapList, new JSONArray()).toString();
        }
        return "";
    }

    /**
     * @param jsonObjectString
     * @return
     * @description JSONObject字符串转Map
     */
    public static Map<String, Object> jsonObjectString2Map(String jsonObjectString) {
        return JSON.parseObject(jsonObjectString, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * @param jsonArrayString
     * @return
     * @description JSONArray字符串转mapList
     */
    public static List<Map<String, Object>> jsonArrayString2MapList(String jsonArrayString) {
        return JSON.parseObject(jsonArrayString, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    /**
     * @param map
     * @return
     * @description Map转JSONObject
     */
    public static JSONObject map2JsonObject(Map<String, Object> map) {
        if (map != null) {
            JSONObject jsonObject = new JSONObject();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject;
        }
        return null;
    }

    /**
     * @param mapList
     * @return
     * @description mapList转JSONArray
     */
    public static JSONArray mapList2JsonArray(List<Map<String, Object>> mapList) {
        if (mapList != null) {
            return convertMapList2JsonArray(mapList, new JSONArray());
        }
        return null;
    }

    /**
     * @return
     * @description JSONObject转Map
     */
    public static Map<String, Object> jsonObject2Map(JSONObject jsonObject) {
        if (jsonObject != null) {
            Map<String, Object> map = new HashMap<>();

            //方法一：该方法比较好
			/*for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}*/

            //方法二：
            Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }
        return null;
    }

    /**
     * @return
     * @description JSONArray转mapList
     */
    public static List<Map<String, Object>> jsonArray2MapList(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                Map<String, Object> map = new HashMap<>();
                Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    map.put(entry.getKey(), entry.getValue());
                }
                mapList.add(map);
            }
            return mapList;
        }
        return null;
    }

    /**
     * @param JsonObject
     * @return
     * @description JSONObject字符串转JSONObject
     */
    public static JSONObject jsonObjectString2JsonObject(String jsonObject) {
        JSONObject result = null;
        if (StringUtils.isBlank(jsonObject)) {
            return result;
        }
        try {
            return JSON.parseObject(jsonObject);
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * @param JsonArray
     * @return
     * @description JSONArray字符串转JSONArray
     */
    public static JSONArray jsonArrayString2JsonArray(String jsonArray) {
        JSONArray result = null;
        if (StringUtils.isBlank(jsonArray)) {
            return result;
        }
        try {
            return JSON.parseArray(jsonArray);
        } catch (Exception e) {
            return result;
        }
    }

    // ************************************************************************************** //

    /**
     * @param jsonObjectString
     * @param clazz
     * @param <T>
     * @return
     * @description JSONObject字符串转javaBean对象
     */
    public static <T> T jsonObjectString2JavaBean(String jsonObjectString, Class<T> clazz) {
        return JSON.parseObject(jsonObjectString, clazz);
    }

    /**
     * @param jsonArrayString
     * @param clazz
     * @param <T>
     * @return
     * @description JSONArray字符串转javaBean的List集合
     */
    public static <T> List<T> jsonArrayString2JavaBeanList(String jsonArrayString, Class<T> clazz) {
        return JSON.parseArray(jsonArrayString, clazz);
    }

    /**
     * @param javaBean
     * @param isFormat 是否进行格式化：true进行格式化，false不进行格式化，null不进行格式化同false
     * @return
     * @description javaBean对象转JSONObject字符串
     */
    public static String javaBean2JsonObjectString(Object javaBean, Boolean isFormat) {
        if (isFormat != null) {
            return JSON.toJSONString(javaBean, isFormat);
        }
        return JSON.toJSONString(javaBean);
    }

    /**
     * @param javaBeanList
     * @param isFormat     是否进行格式化：true进行格式化，false不进行格式化，null不进行格式化同false
     * @param <T>
     * @return
     * @description javaBean的List集合转JSONArray字符串
     */
    public static <T> String javaBeanList2JsonArrayString(List<T> javaBeanList, Boolean isFormat) {
        if (isFormat != null) {
            return JSON.toJSONString(javaBeanList, isFormat);
        }
        return JSON.toJSONString(javaBeanList);
    }

    /**
     * @param javaBean
     * @return
     * @description javaBean转JSONObject，其实相当于已经转为了JSONObject字符串（toString）
     */
    public static JSONObject javaBean2JsonObject(Object javaBean) {
        return (JSONObject) JSON.toJSON(javaBean);
    }

    /**
     * @param javaBeanList
     * @param <T>
     * @return
     * @description javaBean的List转JSONArray，其实相当于已经转为了JSONArray字符串（toString）
     */
    public static <T> JSONArray javaBeanList2JsonArray(List<T> javaBeanList) {
        return (JSONArray) JSON.toJSON(javaBeanList);
    }

    // ************************************************************************************** //

    /**
     * @param jsonString
     * @return
     * @description JSON字符串转JSON，可能是JSONObject，也可能是JSONArray
     */
    public static Object jsonString2Json(String jsonString) {
        return JSON.parse(jsonString);
    }

    /**
     * @param mapOrMapList
     * @return 如果参数是Map，那么返回值就是JSONObject字符串；如果参数是List<Map>，那么返回值就是JSONArray字符串。
     * @description map转JSONString
     */
    public static String map2JsonString(Object mapOrMapList) {
        return JSON.toJSONString(mapOrMapList);
    }

    /**
     * @param mapList
     * @param jsonArray
     * @return
     * @description MapList 转 JsonArray
     */
    private static JSONArray convertMapList2JsonArray(List<Map<String, Object>> mapList, JSONArray jsonArray) {
        for (Map<String, Object> map : mapList) {
            JSONObject jsonObject = new JSONObject();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
