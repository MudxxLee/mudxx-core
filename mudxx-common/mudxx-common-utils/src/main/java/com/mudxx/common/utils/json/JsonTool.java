package com.mudxx.common.utils.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @description JSON工具类（com.fasterxml.jackson）
 * @author laiwen
 * @date 2018年5月30日 上午9:33:58
 */
@SuppressWarnings("ALL")
public class JsonTool {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * @description 将pojo对象转换成json字符串（对象也可以是Map对象）
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
    		return MAPPER.writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * @description 将json结果集转化为pojo对象（如果不知道pojo的具体类型，我们可以使用Map）
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
        	return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @description 将json数据转换成pojo对象list（如果不知道pojo的具体类型，我们可以使用Map）
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		return MAPPER.readValue(jsonData, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
}
