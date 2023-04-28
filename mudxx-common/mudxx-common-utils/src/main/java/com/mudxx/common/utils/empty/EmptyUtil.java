package com.mudxx.common.utils.empty;

import java.util.Collection;
import java.util.Map;

/**
 * description: Java对象空判断工具
 * @author laiwen
 * @date 2018-05-28 21:02:11
 */
@SuppressWarnings("ALL")
public class EmptyUtil {

	/**
	 * 构造函数私有化，不允许new实例对象，只允许通过类名静态调用
	 */
	private EmptyUtil() {}

	/**
	 * 温馨提示：JSONArray（List接口的实现类）判断是否为空也是调用该方法
	 * description: 判断集合对象是否为空
	 * @author laiwen
	 * @date 2018-05-28 21:17:19
	 * @param collection 集合对象
	 * @return 如果集合对象为null或者集合元素个数为0，那么返回true，否则返回false
	 */
	public static Boolean isEmpty(Collection<?> collection) {
		//collection.size()==0和collection.isEmpty()等价
		return isNull(collection) || collection.size() < 1;
	}
	
	/**
	 * 温馨提示：JSONObject（Map接口的实现类）判断是否为空也是调用该方法
	 * description: 判断Map对象是否为空
	 * @author laiwen
	 * @date 2018-05-28 21:23:33
	 * @param map Map对象
	 * @return 如果Map对象为null或者Map键值对个数为0，那么返回true，否则返回false
	 */
	public static Boolean isEmpty(Map<?, ?> map) {
		//map.size()==0和map.isEmpty()等价
		return isNull(map) || map.size() < 1;
	}

	/**
	 * description: 判断数组是否为空
	 * @author laiwen
	 * @date 2018-05-28 21:29:45
	 * @param array 数组对象
	 * @return 如果数组对象为null或者数组元素个数为0，那么返回true，否则返回false
	 */
	public static Boolean isEmpty(Object[] array) {
		return isNull(array) || array.length < 1;
	}

	/**
	 * description: 判断字符串是否为空
	 * @author laiwen
	 * @date 2018-05-28 21:38:07
	 * @param text 字符串对象
	 * @return 如果字符串为null或者字符串(去两边空格)长度为0，那么返回true，否则返回false
	 */
	public static Boolean isEmpty(String text) {
		return isNull(text) || text.trim().length() < 1;
	}

	/**
	 * 温馨提示：如果参数类型是Collection<?>、Map<?, ?>、Object[]、String不会优先调用该方法（包括子类），
	 * 			而是会优先调用上面提供的重载方法即遵循靠近原则，如果是BigDecimal、Date等等其他类型则调用该方法。
	 * description: 判断对象是否为空
	 * @author laiwen
	 * @date 2018-05-28 21:49:18
	 * @param object 对象
	 * @return 如果对象为空返回true，否则返回false
	 */
	public static Boolean isEmpty(Object object) {
		if (object instanceof Collection) {
			return isEmpty((Collection<?>) object);
		} else if (object instanceof Map) {
			return isEmpty((Map<?, ?>) object);
		} else if (object instanceof Object[]) {
			return isEmpty((Object[]) object);
		} else if (object instanceof String) {
			return isEmpty((String) object);
		}
		return isNull(object);
	}

	/**
	 * description: 判断集合对象是否不为空
	 * @author laiwen
	 * @date 2018-05-28 21:55:29
	 * @param collection 集合对象
	 * @return 如果集合对象不为null并且集合元素个数大于0则返回true，否则返回false
	 */
	public static Boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * description: 判断Map对象是否不为空
	 * @author laiwen
	 * @date 2018-05-28 22:18:38
	 * @param map Map对象
	 * @return 如果Map对象不为null并且Map键值对个数大于0则返回true，否则返回false
	 */
	public static Boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * description: 判断数组对象是否不为空
	 * @author laiwen
	 * @date 2018-05-28 22:28:47
	 * @param array 数组对象
	 * @return 如果数组对象不为null并且数组元素个数大于0则返回true，否则返回false
	 */
	public static Boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * description: 判断字符串对象是否不为空
	 * @author laiwen
	 * @date 2018-05-28 22:36:57
	 * @param text 字符串对象
	 * @return 如果字符串不为null并且字符串(去两边空格)长度大于0，那么返回true，否则返回false
	 */
	public static Boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}

	/**
	 * description: 判断对象是否不为空
	 * @author laiwen
	 * @date 2018-05-28 22:44:06
	 * @param object 对象
	 * @return 如果对象不为空就返回true，否则返回false
	 */
	public static Boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	/**
	 * description: 判断对象是否为null
	 * @author laiwen
	 * @date 2018-05-28 21:10:15
	 * @param object 对象
	 * @return 如果对象为null就返回true，否则返回false
	 */
	private static Boolean isNull(Object object) {
		return object == null;
	}

}
