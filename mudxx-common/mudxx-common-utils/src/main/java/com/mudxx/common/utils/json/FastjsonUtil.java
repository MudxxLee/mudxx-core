package com.mudxx.common.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @description 阿里巴巴提供的fastjson常用工具类方法
 * @author laiwen
 * @date 2019-07-26 16:01:44
 */
@SuppressWarnings("ALL")
public class FastjsonUtil {

    /**
     * 提醒：该方法与下面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     * @description Bean（包括Map）对象转JSONObject对象
     * @param bean Bean对象
     * @param <T> 泛型
     * @return 返回JSONObject对象
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
     * @description Bean（包括Map）对象转JSONObject对象
     * @param bean Bean对象
     * @param <T> 泛型
     * @return 返回JSONObject对象
     */
    public static <T> JSONObject beanToJsonObject(T bean) {
        JSONObject jsonObject = (JSONObject) JSON.toJSON(bean);
        return jsonObject;
    }

    /**
     * 提醒：该方法与下面的那一个方法实现的功能相同，两个方法都推荐使用，两者二选一即可！
     * @description Bean（包括Map）集合对象转JSONArray对象
     * @param beanList Bean集合对象
     * @param <T> 泛型
     * @return 返回JSONArray对象
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
     * @description Bean（包括Map）集合对象转JSONArray对象
     * @param beanList Bean集合对象
     * @param <T> 泛型
     * @return 返回JSONArray对象
     */
    public static <T> JSONArray beanListToJsonArray(List<T> beanList) {
        JSONArray jsonArray = (JSONArray) JSON.toJSON(beanList);
        return jsonArray;
    }

    /**
     * 提醒：这里的json格式的字符串形如："{}"
     * @description json格式的字符串转Bean（包括Map）对象
     * @param jsonStr json格式的字符串
     * @param clazz Bean的Class对象
     * @param <T> 泛型
     * @return 返回Bean对象
     */
    public static <T> T jsonStr2Bean(String jsonStr, Class<T> clazz) {
        T t = JSON.parseObject(jsonStr, clazz);
        return t;
    }

    /**
     * 提醒：这里的json格式的字符串形如："[{}...]"
     * @description json格式的字符串转Bean（包括Map）集合对象
     * @param jsonStr json格式的字符串
     * @param clazz Bean的Class对象
     * @param <T> 泛型
     * @return 返回Bean集合对象
     */
    public static <T> List<T> jsonStr2BeanList(String jsonStr, Class<T> clazz) {
        List<T> ts = JSON.parseArray(jsonStr, clazz);
        return ts;
    }

    /**
     * 提醒：这里的json格式的字符串形如："{'':'',...}"
     * @description json格式的字符串转JSONObject对象
     * @param jsonStr json格式的字符串
     * @return 返回JSONObject对象
     */
    public static JSONObject jsonStr2JSONObject(String jsonStr) {
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        return jsonObject;
    }

    /**
     * 提醒：这里的json格式的字符串形如："[{'':'',...},...]"
     * @description json格式的字符串转JSONArray对象
     * @param jsonStr json格式的字符串
     * @return 返回JSONArray对象
     */
    public static JSONArray jsonStr2JSONArray(String jsonStr) {
        JSONArray jsonArray = (JSONArray) JSON.parse(jsonStr);
        return jsonArray;
    }

    // ************************************************************************************** //

    /**
     * @description 生成JSONObject字符串(有且只有一个键值对)
     * @param key
     * @param value
     * @return
     */
    public static String generateJsonObjectString(String key, Object value){
        if(StringUtils.isNotBlank(key)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key,value);
            return jsonObject.toString();
        }
        return "";
    }

    /**
     * @description Map转JSONObject字符串
     * @param map
     * @return
     */
    public static String map2JsonObjectString(Map<String,Object> map){
        if(map != null){
            JSONObject jsonObject = new JSONObject();

            //方法一：该方法比较好
			/*for (Map.Entry<String, Object> entry : map.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}*/

            //方法二：
            Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,Object> entry = iterator.next();
                jsonObject.put(entry.getKey(),entry.getValue());
            }
            return jsonObject.toString();
        }
        return "";
    }

    /**
     * @description mapList转JSONArray字符串
     * @param mapList
     * @return
     */
    public static String mapList2JsonArrayString(List<Map<String,Object>> mapList){
        if(mapList != null){
            return convertMapList2JsonArray(mapList,new JSONArray()).toString();
        }
        return "";
    }

    /**
     * @description JSONObject字符串转Map
     * @param jsonObjectString
     * @return
     */
    public static Map<String, Object> jsonObjectString2Map(String jsonObjectString){
        return JSON.parseObject(jsonObjectString, new TypeReference<Map<String, Object>>(){});
    }

    /**
     * @description JSONArray字符串转mapList
     * @param jsonArrayString
     * @return
     */
    public static List<Map<String, Object>> jsonArrayString2MapList(String jsonArrayString){
        return JSON.parseObject(jsonArrayString, new TypeReference<List<Map<String, Object>>>(){});
    }

    /**
     * @description Map转JSONObject
     * @param map
     * @return
     */
    public static JSONObject map2JsonObject(Map<String,Object> map){
        if(map != null){
            JSONObject jsonObject = new JSONObject();
            Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,Object> entry = iterator.next();
                jsonObject.put(entry.getKey(),entry.getValue());
            }
            return jsonObject;
        }
        return null;
    }

    /**
     * @description mapList转JSONArray
     * @param mapList
     * @return
     */
    public static JSONArray mapList2JsonArray(List<Map<String,Object>> mapList){
        if(mapList != null){
            return convertMapList2JsonArray(mapList,new JSONArray());
        }
        return null;
    }

    /**
     * @description JSONObject转Map
     * @return
     */
    public static Map<String, Object> jsonObject2Map(JSONObject jsonObject){
        if(jsonObject != null){
            Map<String, Object> map = new HashMap<>();

            //方法一：该方法比较好
			/*for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}*/

            //方法二：
            Iterator<Map.Entry<String,Object>> iterator = jsonObject.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,Object> entry = iterator.next();
                map.put(entry.getKey(),entry.getValue());
            }
            return map;
        }
        return null;
    }

    /**
     * @description JSONArray转mapList
     * @return
     */
    public static List<Map<String, Object>> jsonArray2MapList(JSONArray jsonArray){
        if(jsonArray != null){
            List<Map<String, Object>> mapList = new ArrayList<>();
            for(Object object : jsonArray){
                JSONObject jsonObject = (JSONObject) object;
                Map<String, Object> map = new HashMap<>();
                Iterator<Map.Entry<String,Object>> iterator = jsonObject.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,Object> entry = iterator.next();
                    map.put(entry.getKey(),entry.getValue());
                }
                mapList.add(map);
            }
            return mapList;
        }
        return null;
    }

    /**
     * @description JSONObject字符串转JSONObject
     * @param JsonObject
     * @return
     */
    public static JSONObject jsonObjectString2JsonObject(String jsonObject){
        JSONObject result = null;
        if(StringUtils.isBlank(jsonObject)){
            return result;
        }
        try {
            return JSON.parseObject(jsonObject);
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * @description JSONArray字符串转JSONArray
     * @param JsonArray
     * @return
     */
    public static JSONArray jsonArrayString2JsonArray(String jsonArray){
        JSONArray result = null;
        if(StringUtils.isBlank(jsonArray)){
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
     * @description JSONObject字符串转javaBean对象
     * @param jsonObjectString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonObjectString2JavaBean(String jsonObjectString, Class<T> clazz){
        return JSON.parseObject(jsonObjectString,clazz);
    }

    /**
     * @description JSONArray字符串转javaBean的List集合
     * @param jsonArrayString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArrayString2JavaBeanList(String jsonArrayString, Class<T> clazz){
        return JSON.parseArray(jsonArrayString,clazz);
    }

    /**
     * @description javaBean对象转JSONObject字符串
     * @param javaBean
     * @param isFormat 是否进行格式化：true进行格式化，false不进行格式化，null不进行格式化同false
     * @return
     */
    public static String javaBean2JsonObjectString(Object javaBean, Boolean isFormat){
        if(isFormat != null){
            return JSON.toJSONString(javaBean,isFormat);
        }
        return JSON.toJSONString(javaBean);
    }

    /**
     * @description javaBean的List集合转JSONArray字符串
     * @param javaBeanList
     * @param isFormat 是否进行格式化：true进行格式化，false不进行格式化，null不进行格式化同false
     * @param <T>
     * @return
     */
    public static <T> String javaBeanList2JsonArrayString(List<T> javaBeanList, Boolean isFormat){
        if(isFormat != null){
            return JSON.toJSONString(javaBeanList,isFormat);
        }
        return JSON.toJSONString(javaBeanList);
    }

    /**
     * @description javaBean转JSONObject，其实相当于已经转为了JSONObject字符串（toString）
     * @param javaBean
     * @return
     */
    public static JSONObject javaBean2JsonObject(Object javaBean){
        return (JSONObject) JSON.toJSON(javaBean);
    }

    /**
     * @description javaBean的List转JSONArray，其实相当于已经转为了JSONArray字符串（toString）
     * @param javaBeanList
     * @param <T>
     * @return
     */
    public static <T> JSONArray javaBeanList2JsonArray(List<T> javaBeanList){
        return (JSONArray) JSON.toJSON(javaBeanList);
    }

    // ************************************************************************************** //

    /**
     * @description JSON字符串转JSON，可能是JSONObject，也可能是JSONArray
     * @param jsonString
     * @return
     */
    public static Object jsonString2Json(String jsonString){
        return JSON.parse(jsonString);
    }

    /**
     * @description map转JSONString
     * @param mapOrMapList
     * @return 如果参数是Map，那么返回值就是JSONObject字符串；如果参数是List<Map>，那么返回值就是JSONArray字符串。
     */
    public static String map2JsonString(Object mapOrMapList){
        return JSON.toJSONString(mapOrMapList);
    }

    /**
     * @description MapList 转 JsonArray
     * @param mapList
     * @param jsonArray
     * @return
     */
    private static JSONArray convertMapList2JsonArray(List<Map<String,Object>> mapList, JSONArray jsonArray){
        for(Map<String,Object> map : mapList){
            JSONObject jsonObject = new JSONObject();
            Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,Object> entry = iterator.next();
                jsonObject.put(entry.getKey(),entry.getValue());
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
