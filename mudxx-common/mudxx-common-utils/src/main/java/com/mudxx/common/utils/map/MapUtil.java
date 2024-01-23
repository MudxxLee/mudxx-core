package com.mudxx.common.utils.map;

import com.mudxx.common.utils.array.ArrayUtil;
import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.equals.EqualsUtil;
import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonUtil;
import com.mudxx.common.utils.list.ListUtil;
import com.mudxx.common.utils.xml.XmlUtil;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * @description Map工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:12:21
 */
@SuppressWarnings("ALL")
public class MapUtil {

	/**
	 * 温馨提示：如果oldMap为null，则返回值newMap也为null
	 * @description Map<String, Object>转Map<String, String>
	 * @param oldMap Map<String, Object>
	 * @return 返回Map<String, String>
	 */
	public static Map<String, String> objConvertStr(Map<String, Object> oldMap) {
		Map<String, String> newMap = null;
		if (EmptyUtil.isNotEmpty(oldMap)) {
			newMap = new HashMap<>(16);
			for (Entry<String, Object> entry : oldMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value == null) {
					newMap.put(key, null);
				} else {
					newMap.put(key, value.toString());
				}
			}
		}
		return newMap;
	}

	/**
	 * @description 根据key删除Map集合中的键值对
	 * @param map Map对象
	 * @param k 键
	 * @return 返回删除键值对之后的Map对象
	 */
	public static <K, V> Map<K, V> removeKeyValue(Map<K, V> map, K k) {
		// 简化写法
		// map.entrySet().removeIf(next -> Objects.equals(k, next.getKey()));

		// 传统写法
		Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<K, V> next = iterator.next();
			if (Objects.equals(k, next.getKey())) {
				iterator.remove();
			}
		}

		return map;
	}

	/**
	 * @description 根据key删除Map集合中的键值对
	 * @param map Map对象
	 * @param k 键
	 * @return 返回删除键值对之后的Map对象
	 */
	public static <K, V> Map<K, V> removeKeyFromMap(Map<K, V> map, K k) {
		// 简化写法
		// map.keySet().removeIf(next -> Objects.equals(k, next));

		// 传统写法
		Iterator<K> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			K next = iterator.next();
			if (Objects.equals(k, next)) {
				iterator.remove();
			}
		}

		return map;
	}

	/**
	 * 说明：如果想同时循环遍历得到key和value，建议选择该方法key2List和value2List方法！
	 * 提示：因为Map中的key是唯一不重复的所以此处List集合中的元素也是不重复的
	 * @description 循环遍历Map集合中的key将其转化成List集合
	 * @param map 待循环遍历的Map集合
	 * @return 返回key的List集合
	 */
	public static <K, V> List<K> key2List(Map<K, V> map) {
		Set<K> keySet = map.keySet();
		Iterator<K> iterator = keySet.iterator();
		return ListUtil.iteratorToList(iterator);
	}

	/**
	 * 说明：如果只想循环遍历得到key，建议选择该方法key2Set！
	 * 提示：因为Map中的key是唯一不重复的所以此处使用HashSet刚刚好
	 * @description 循环遍历Map集合中的key将其转化成Set集合
	 * @param map 待循环遍历的Map集合
	 * @return 返回key的Set集合
	 */
	public static <K, V> Set<K> key2Set(Map<K, V> map) {
		Set<Entry<K, V>> entrySet = map.entrySet();
		Iterator<Entry<K, V>> iterator = entrySet.iterator();
		Set<K> set = new HashSet<>();
		while (iterator.hasNext()) {
			Entry<K, V> entry = iterator.next();
			set.add(entry.getKey());
		}
		return set;
	}

	/**
	 * 提示：因为Map中的value是允许重复的，所以此处List集合中也是很有可能存在重复的元素
	 * @description 循环遍历Map集合中的value将其转化成List集合
	 * @param map 待循环遍历的Map集合
	 * @return 返回value的List集合
	 */
	public static <K, V> List<V> value2List(Map<K, V> map) {
		Set<Entry<K, V>> entrySet = map.entrySet();
		Iterator<Entry<K, V>> iterator = entrySet.iterator();
		List<V> list = new ArrayList<>();
		while (iterator.hasNext()) {
			Entry<K, V> entry = iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}

	/**
	 * @description 解决map中的value值出现中文乱码问题
	 * @param map Map集合
	 * @return 返回没有中文乱码的数据
	 */
	public static Map<String, Object> resolveMapMessy(Map<String, Object> map) {
		Map<String, Object> data = new HashMap<>(16);
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				data.put(key, resolveMessy((String) value));
			} else {
				data.put(key, value);
			}
		}
		return data;
	}

	/**
	 * @description map字符串转数组
	 * 比如{id=1, username=张三, birthday=Sun Jul 30 19:05:09 CST 2017, sex=1, address=苏州}
	 * 转成[id=1, username=张三, birthday=Sun Jul 30 19:05:09 CST 2017, sex=1, address=苏州]
	 * @param mapString map调用toString()方法转换成的字符串。或者实体类复写的equals方法转换成的map字符串格式。
	 * @return 字符串数组，这是个对象，如果想获得字符串，需要使用Arrays.toString()方法转换。
	 */
	public static String[] mapStringToArray(String mapString) {
		String subMapString = mapString.substring(1, mapString.length() - 1);
		return subMapString.split(", ");
	}

	/**
	 * 说明：这里我使用LinkedHashMap，保证转换后键值对顺序不变。
	 * @description map字符串转map对象
	 * @param mapString map字符串转Map对象
	 * @return 返回LinkedHashMap对象
	 */
	public static Map<String, String> mapStringToMap(String mapString) {
		String[] keyEqValueArray = mapStringToArray(mapString);
		Map<String, String> map = new LinkedHashMap<>(16);
		for (String keyEqValue : keyEqValueArray) {
			String[] keyValue = keyEqValue.split("=");
			String key = keyValue[0];
			String value = keyValue[1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 说明：如果入参是HashMap对象的话，没有按照key的字典顺序来排序（升序）
	 * 提醒：如果入参是TreeMap对象的话，不需要担心排序问题，因为TreeMap内部就是按照key的字典顺序来排序（升序）
	 * @description 把Map转换成get请求参数类型，如 {name=20, age=30}转换后变成 name=20&age=30
	 * @param map map集合
	 * @return 返回指定格式的字符串数据
	 */
	public static String mapToGet(Map<String, Object> map) {
		StringBuilder result = new StringBuilder();
		if (null == map || 0 == map.size()) {
			return result.toString();
		}
		Set<String> keys = map.keySet();
		for (String key : keys) {
			result.append(key).append("=").append(map.get(key)).append("&");
		}
		return StringUtils.isBlank(result.toString()) ? result.toString() : result.substring(0, result.length() - 1);
	}

	/**
	 * 说明：如果入参是HashMap对象的话，没有按照key的字典顺序来排序（升序）
	 * 提醒：如果入参是TreeMap对象的话，不需要担心排序问题，因为TreeMap内部就是按照key的字典顺序来排序（升序）
	 * @description 获取签名串，将Map集合的键值对拼接成形如key1=value1&key2=value2格式的字符串
	 * @param map 待拼接的Map集合
	 * @return 返回形如key1=value1&key2=value2格式的字符串
	 */
	public static String getSignChain(Map<String, String> map) {
	    // 拼接待签名串
		StringBuilder signChain = new StringBuilder();
		boolean first = true;
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (StringUtils.isEmpty(value)) {
				// 过滤空值
				continue;
			}
			if (first) {
				signChain.append(key).append("=").append(value);
				first = false;
			} else {
				signChain.append("&").append(key).append("=").append(value);
			}
		}
		return signChain.toString();
	}

	/**
	 * 说明：有按照key的字典顺序来排序（升序）
	 * @description 把Map集合所有键值对按照键排序，并按照“参数=参数值”的模式用拼接符拼接成字符串
	 * @param params 需要排序并参与字符拼接的Map集合
	 * @param isEncode 是否需要进行编码，true表示需要，false表示不需要
	 * @param join 拼接符
	 * @return 返回拼接后字符串
	 */
	public static String createLinkStringByGet(Map<String, String> params, Boolean isEncode, String join) {
		List<String> keys = new ArrayList<>(params.keySet());
		Collections.sort(keys);
		StringBuilder preStr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (isEncode) {
				try {
					value = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (i == keys.size() - 1) {
				// 拼接时，不包括最后一个拼接符
				preStr.append(key).append("=").append(value);
			} else {
				preStr.append(key).append("=").append(value).append(join);
			}
		}
		return preStr.toString();
	}

	/**
	 * 说明：有按照key的字典顺序来排序（升序）
	 * @description 把Map集合所有键值对按照键排序，并按照“参数=参数值”的模式用&拼接成字符串
	 * @param params 需要排序并参与字符拼接的Map集合
	 * @param isEncode 是否需要进行编码，true表示需要，false表示不需要
	 * @return 返回拼接后字符串
	 */
	public static String createLinkStringByGet(Map<String, String> params, Boolean isEncode) {
		return createLinkStringByGet(params, isEncode, "&");
	}

	/**
	 * 说明：有按照key的字典顺序来排序（升序）
	 * @description 把Map集合所有键值对按照键排序，并按照“参数=参数值”的模式用&拼接成字符串，不需要进行编码
	 * @param params 需要排序并参与字符拼接的Map集合
	 * @return 返回拼接后字符串
	 */
	public static String createLinkStringByGet(Map<String, String> params) {
		return createLinkStringByGet(params, false, "&");
	}

	/**
	 * @description 把Map集合所有键值对按照键排序，并按照“参数=参数值”的模式用&拼接成字符串，不需要进行编码，末尾追加附加内容
	 * @param params 需要排序并参与字符拼接的Map集合
	 * @param attach 附加内容
	 * @return 返回拼接后字符串
	 */
	public static String createLinkStringByGet(Map<String, String> params, String attach) {
		return createLinkStringByGet(params, false, "&") + attach;
	}

	/**
	 * 说明：返回的Map集合是<String, Object>类型的
	 * @description 把一串参数字符串转换成Map，如"?a=3&b=4"转换为Map{a=3, b=4}
	 * @param args get请求参数格式的字符串
	 * @return 返回map集合
	 */
	public static Map<String, Object> getToMap(String args) {
		Map<String, Object> result = new HashMap<>(16);
		if (StringUtils.isBlank(args)) {
			return null;
		}
		args = args.trim();
		// if ("?".equals(args.charAt(0))) {
		if (args.startsWith("?")) {
			args = args.substring(1);
		}
		String[] argsArray = args.split("&");
		for (String ag : argsArray) {
			if (!StringUtils.isBlank(ag) && ag.contains("=")) {
				// ag.contains("=")相当于ag.indexOf("=") > 0
				String[] keyValue = ag.split("=");
				String key = keyValue[0];
				String value = "";
				for (int i = 1; i < keyValue.length; i++) {
					value = keyValue[i] + "=";
				}
				value = value.length() > 0 ? value.substring(0, value.length()-1) : value;
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * 说明：返回的Map集合是<String, String>类型的
	 * @description 把一串参数字符串转换成Map，如"?a=3&b=4"转换为Map{a=3, b=4}
	 * @param args get请求参数格式的字符串
	 * @return 返回map集合
	 */
	public static Map<String, String> getToMapStr(String args) {
		Map<String, String> result = new HashMap<>(16);
		if (StringUtils.isBlank(args)) {
			return null;
		}
		args = args.trim();
		// if ("?".equals(args.charAt(0))) {
		if (args.startsWith("?")) {
			args = args.substring(1);
		}
		String[] argsArray = args.split("&");
		for (String ag : argsArray) {
			if (!StringUtils.isBlank(ag) && ag.contains("=")) {
				// ag.contains("=")相当于ag.indexOf("=") > 0
				String[] keyValue = ag.split("=");
				String key = keyValue[0];
				String value = "";
				for (int i = 1; i < keyValue.length; i++) {
					value = keyValue[i] + "=";
				}
				value = value.length() > 0 ? value.substring(0, value.length() - 1) : value;
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * 温馨提示：该方法考虑全面，在任何情况下都适用，属于万能方法！
	 * @description 将请求参数（接口或者页面表单）转换成Map<String, Object>的格式
	 * @param request 请求对象
	 * @return 返回请求参数的Map集合，key是请求的name，value是请求的值。
	 */
	public static Map<String, Object> getParamsMap(HttpServletRequest request) {
		Map<String, Object> paramsMap = new HashMap<>(16);
		String method = request.getMethod();
		// 获取请求参数的内容类型
		String contentType = request.getHeader("Content-Type");
		if (EmptyUtil.isEmpty(contentType)) {
			// HTTP协议中，如果不指定Content-Type，则默认传递的参数就是application/x-www-form-urlencoded类型
			contentType = "application/x-www-form-urlencoded";
		}
		// isGet为true表示GET请求
		Boolean isGet = EqualsUtil.isEqualsIgnoreCase("GET", method);
		// isPostAndForm为true表示POST请求并且是类似表单key=value提交的方式
		Boolean isPostAndForm = EqualsUtil.isEqualsIgnoreCase("POST", method) && contentType.contains("application/x-www-form-urlencoded");
		if (isGet || isPostAndForm) {
			// 如果是GET请求或者是POST请求并且Content-Type: application/x-www-form-urlencoded; charset=UTF-8请求数据类型
			// 使用getParameterMap转Map（value本质上都是字符串类型）
			paramsMap = getQueryParams(request);
		} else if (EqualsUtil.isEqualsIgnoreCase("POST", method)) {
			// 如果是POST请求
			// 使用IO流转字符串
			String requestContent = HttpClientUtil.getRequestContent(request);
			if (EmptyUtil.isEmpty(requestContent)) {
				if (contentType.contains("multipart/form-data")) {
					// Content-Type: multipart/form-data; boundary=jwwLhMIc-A1mYMma8wragKRLf9fU0VD3woGHZ; charset=UTF-8请求数据类型（其中boundary是变化的）
					// 解析二进制文件数据
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
					// 类似于input的name属性
					Iterator<String> fileNames = multipartRequest.getFileNames();
					while (fileNames.hasNext()) {
						String fileName = fileNames.next();
						List<MultipartFile> multipartFiles = multipartRequest.getFiles(fileName);
						paramsMap.put(fileName, multipartFiles);
					}
					Map<String, String[]> parameterMap = multipartRequest.getParameterMap();
					for (Entry<String, String[]> entry : parameterMap.entrySet()) {
						// 参数名如果相同的话，多个值我们以,分隔
						// 一般情况下我们不考虑有相同参数名的情况，即String[]只有一个元素
						String value = ArrayUtil.arrayToStr(entry.getValue());
						paramsMap.put(entry.getKey(), value);
					}
				}
			} else if (contentType.contains("application/json")) {
				// Content-Type: application/json; charset=UTF-8请求数据类型
				// 解析JSON格式数据
				// json字符串转Map（value数据类型根据实际传递情况决定）
				paramsMap = JsonUtil.toMap(requestContent, String.class, Object.class);
			} else if (contentType.contains("text/xml") || contentType.contains("application/xml")) {
				// Content-Type: text/xml; charset=UTF-8请求数据类型
				// Content-Type: application/xml; charset=UTF-8请求数据类型
				// 解析XML结构数据
				paramsMap = XmlUtil.xmlParse(requestContent);
			}
		}
		// 如果map集合里面连一个键值对元素都没有那么size为0
		return paramsMap;
	}

	/**
	 * 温馨提示：如果是GET请求或者是POST的application/x-www-form-urlencoded，推荐使用该方法！
	 * @description 将请求参数（接口或者页面表单）转换成Map<String, String>的格式
	 * @param request 请求对象
	 * @return 返回请求参数的Map集合，key是请求的name，value是请求的值。
	 */
	public static Map<String, String> getParamsMapValueStr(HttpServletRequest request) {
		Map<String, Object> paramsMap = getParamsMap(request);
		Map<String, String> paramsMapValueStr = new HashMap<>(16);
		for (Entry<String, Object> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue() == null ? null : entry.getValue().toString();
			paramsMapValueStr.put(key, value);
		}
		return paramsMapValueStr;
	}

	/**
	 * 温馨提示：虽然方法的返回值类型是Map<String, Object>，实质上是Map<String, String>
	 * 提醒：支持GET请求，对于POST请求仅支持enctype=application/x-www-form-urlencoded
	 * 强调：我们规定如果有2个或者2个以上相同的name，那么所有name对应的value必须相同
	 * 说明：请求的参数要保证name值不重复尽量做到唯一，不然一个name对应多个值不好区分
	 * @description 获取请求参数
	 * @param request 请求对象
	 * @return 返回请求参数的Map集合，key是请求的name，value是请求的值。
	 */
	public static Map<String, Object> getQueryParams(HttpServletRequest request) {
		// 获取字符编码格式
		String characterEncoding = request.getCharacterEncoding();
		// 之所以返回的map中的value为字符串类型的数组，是为了解决表单中有多个name值一样的项。
		Map<String, String[]> map = request.getParameterMap();
		Map<String, Object> params = new HashMap<>(map.size());
		for (Entry<String, String[]> entry : map.entrySet()) {
			// 参数名如果相同的话，多个值我们以,分隔
			// 一般我们只考虑请求的参数名是唯一的情况
			// 请求方式，一般都是GET请求或者POST请求。
			String method = request.getMethod();
			String key = entry.getKey();
			String value = ArrayUtil.arrayToStr(entry.getValue());
			if (EqualsUtil.isEqualsIgnoreCase("GET", method)) {
				// 如果是GET请求我们需要考虑解决中文乱码的问题。
				if (EqualsUtil.isEqualsIgnoreCase(Consts.UTF_8.toString(), characterEncoding)) {
					// 如果是UTF-8编码我们不需要转码
					params.put(key, value);
				} else {
					// 如果不是UTF-8编码我们需要转码
					params.put(key, resolveMessy(value));
				}
			} else {
				// POST请求我们不需要考虑解决中文乱码问题，因为我们已经在web.xml里面配置过乱码过滤器了。
				params.put(key, value);
			}
		}
		return params;
	}

	/**
	 * 提醒：支持GET请求，对于POST请求仅支持enctype=application/x-www-form-urlencoded
	 * 强调：我们规定如果有2个或者2个以上相同的name，那么所有name对应的value必须相同
	 * 说明：请求的参数要保证name值不重复尽量做到唯一，不然一个name对应多个值不好区分
	 * @description 获取请求参数
	 * @param request 请求对象
	 * @return 返回请求参数的Map集合，key是请求的name，value是请求的值。
	 */
	public static Map<String, String> getQueryParamsString(HttpServletRequest request) {
		// 获取字符编码格式
		String characterEncoding = request.getCharacterEncoding();
		// 之所以返回的map中的value为字符串类型的数组，是为了解决表单中有多个name值一样的项。
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> params = new HashMap<>(map.size());
		for (Entry<String, String[]> entry : map.entrySet()) {
			// 参数名如果相同的话，多个值我们以,分隔
			// 一般我们只考虑请求的参数名是唯一的情况
			// 请求方式，一般都是GET请求或者POST请求。
			String method = request.getMethod();
			String key = entry.getKey();
			String value = ArrayUtil.arrayToStr(entry.getValue());
			if (EqualsUtil.isEqualsIgnoreCase("GET", method)) {
				// 如果是GET请求我们需要考虑解决中文乱码的问题。
				if (EqualsUtil.isEqualsIgnoreCase(Consts.UTF_8.toString(), characterEncoding)) {
					// 如果是UTF-8编码我们不需要转码
					params.put(key, value);
				} else {
					// 如果不是UTF-8编码我们需要转码
					params.put(key, resolveMessy(value));
				}
			} else {
				// POST请求我们不需要考虑解决中文乱码问题，因为我们已经在web.xml里面配置过乱码过滤器了。
				params.put(key, value);
			}
		}
		return params;
	}

	/**
	 * 说明：主要是用来解决GET请求中文乱码问题（比如?name=张三）。
	 * @description 字符编码转换
	 * @param oldData 待转换的字符串
	 * @return 返回转换之后的字符串
	 */
	private static String resolveMessy(String oldData) {
		try {
			return new String(oldData.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//**************************************************************************************//

	/**
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBean javaBean对象
	 */
	public static void mapToJavaBean(Map<String, Object> map, Object javaBean) throws Exception {
		// 解决字符串转Date的问题。
		ConvertUtils.register(new DateLocaleConverter(), Date.class);
		/*ConvertUtils.register((clazz, value) -> {
		    // 解决字符串转Date的问题，但是不支持Date类型！
			System.out.println("注册字符串转换为Date类型转换器");
			if (value == null) {
				return null;
			}
			if (!(value instanceof String)) {
				throw new ConversionException("只支持字符串转换 !");
			}
			String str = (String) value;
			if (str.trim().equals("")) {
				return null;
			}
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return sd.parse(str);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}, Date.class);*/
		if (map == null || javaBean == null) {
			return;
		}
		// Map转javaBean
		BeanUtils.populate(javaBean, map);
	}

	/**
	 * 说明：该方法最好，推荐使用！
	 * 该方法虽然很好可以解决字符串转日期以及其他基本类型，但是仍然解决不了字符串转日期时间
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBeanClass javaBean的类对象
	 * @param <T> 泛型
	 * @return 返回javaBean
	 * @throws Exception 异常信息
	 */
	public static <T> T mapToJavaBean(Map<String, Object> map, Class<T> javaBeanClass) throws Exception {
		// 解决字符串转Date的问题。
		ConvertUtils.register(new DateLocaleConverter(), Date.class);
		if (map == null) {
			return null;
		}
		T javaBean = javaBeanClass.newInstance();
		BeanUtils.populate(javaBean, map);
		return javaBean;
	}

	/**
	 * 说明：该方法不太好用，需要进行强制转换，不推荐使用。
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 Map 转 javaBean
	 * @param map Map集合
	 * @param javaBeanClass javaBean的类对象
	 * @return 返回javaBean
	 * @throws Exception 异常信息
	 */
	public static Object mapToJavaBean2(Map<String, Object> map, Class<?> javaBeanClass) throws Exception {
		// 解决字符串转Date的问题。
		ConvertUtils.register(new DateLocaleConverter(), Date.class);
		if (map == null) {
			return null;
		}
		Object javaBean = javaBeanClass.newInstance();
		BeanUtils.populate(javaBean, map);
		return javaBean;
	}

	/**
	 * 温馨提示：BeanMap(javaBean)转Map<String, Object>存在局限性，如果报错，返回值类型改成BeanMap即可
	 * 说明：Map<String, Object>对<?, ?>进行了强制转换，使用上效果很好，但是有局限性（不过基本上够用了）
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 javaBean 转 Map
	 * @param javaBean javaBean对象
	 * @return 返回值类型是BeanMap，即Map的一个子类
	 */
	public static BeanMap javaBeanToMap(Object javaBean) {
		if (javaBean == null) {
			return null;
		}
		return new BeanMap(javaBean);
	}

	/**
	 * 说明：该方法最好，通用无缺点，但使用上效果最差（因为要进行强制转换）
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 javaBean 转 Map
	 * @param javaBean javaBean对象
	 * @return 返回值类型是BeanMap，即Map的一个子类
	 */
	public static Map<?, ?> javaBeanToMap2(Object javaBean) {
		if (javaBean == null) {
			return null;
		}
		return new BeanMap(javaBean);
	}

	/**
	 * 说明：该方法比较完美，强烈推荐！（如果报错，返回的时候进行强转即可）
	 * @description 利用org.apache.commons.beanutils.BeanUtils工具类实现 javaBean 转 Map
	 * @param javaBean javaBean对象
	 * @return 返回值类型是BeanMap，即Map的一个子类
	 */
	public static <K, V> Map<K, V> javaBeanToMap3(Object javaBean) {
		if (javaBean == null) {
			return null;
		}
		return (Map<K, V>) new BeanMap(javaBean);
	}

	//**************************************************************************************//

	/**
	 * @description 利用Introspector和PropertyDescriptor实现 Map 转 javaBean
	 * @param map Map集合
	 * @param javaBean javaBean对象
	 */
	public static void map2JavaBean(Map<String, Object> map, Object javaBean) throws Exception {
		if (map == null || javaBean == null) {
			return;
		}
		// BeanInfo顾名思义即javaBean信息
		BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
		// PropertyDescriptor[]相当于javaBean的属性的数组集合
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		// PropertyDescriptor相当于javaBean的单个属性
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			// javaBean的某个属性名
			String key = propertyDescriptor.getName();
			if (map.containsKey(key)) {
				// 如果map里面含有该key，即javaBean的某个属性名
				// 获取key即属性对应的属性值
				Object value = map.get(key);
				// 某个属性的setter方法
				Method setter = propertyDescriptor.getWriteMethod();
				// 给javaBean的某个属性设置值
				setter.invoke(javaBean, value);
			}
		}
	}

	/**
	 * 说明：该方法最好，推荐使用！
	 * 该方法既解决不了字符串转基本类型，更解决不了字符串转日期以及日期时间
	 * @description 利用Introspector和PropertyDescriptor实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBeanClass javaBean的类对象
	 * @param <T> 泛型
	 * @return 返回javaBean对象
	 * @throws Exception 异常信息
	 */
	public static <T> T map2JavaBean(Map<String, Object> map, Class<T> javaBeanClass) throws Exception {
		if (map == null) {
			return null;
		}
		T javaBean = javaBeanClass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(javaBeanClass);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method setter = propertyDescriptor.getWriteMethod();
			if (setter != null) {
				String key = propertyDescriptor.getName();
				// 如果key是基本数据类型，不用担心，因为jvm会自动封装
				// 即使会自动封装，但是一般我们在定义javaBean的属性的时候，类型不设置为基本数据类型而是封装类型
				// 如果给map设置的key的value是基本数据类型，不用担心，因为通过getter拿到的value会自动封装成对象类型
				// 如果map里面没有找到对应的key，那么会返回null。
				Object value = map.get(key);
				setter.invoke(javaBean, value);
			}
		}
		return javaBean;
	}

	/**
	 * 说明：该方法不太好用，需要进行强制转换，不推荐使用。
	 * @description 利用Introspector和PropertyDescriptor实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBeanClass javaBean的类对象
	 * @return 返回javaBean对象
	 * @throws Exception 异常信息
	 */
	public static Object map2JavaBean2(Map<String, Object> map, Class<?> javaBeanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object javaBean = javaBeanClass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(javaBeanClass);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method setter = propertyDescriptor.getWriteMethod();
			if (setter != null) {
				setter.invoke(javaBean,map.get(propertyDescriptor.getName()));
			}
		}
		return javaBean;
	}

	/**
	 * @description 利用Introspector和PropertyDescriptor实现 javaBean 转 Map
	 * @param javaBean javaBean对象
	 * @return 返回map集合
	 */
	public static Map<String, Object> javaBean2Map(Object javaBean) throws Exception {
		if (javaBean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>(16);
		// BeanInfo顾名思义即javaBean信息
		BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
		// PropertyDescriptor[]相当于javaBean的属性的数组集合
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		// PropertyDescriptor相当于javaBean的单个属性
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			// javaBean的某个属性名
			String key = propertyDescriptor.getName();
			if (!"class".equals(key)) {
				// 如果该属性名称不是class，即是我们自定义的属性
				// 某个属性的getter方法
				Method getter = propertyDescriptor.getReadMethod();
				// 获取javaBean的某个属性的属性值
				Object value = getter.invoke(javaBean);
				// map添加键值对
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * @description 利用Introspector和PropertyDescriptor实现 javaBean 转 Map
	 * @param javaBean javaBean对象
	 * @return 返回Map集合
	 */
	public static Map<String, Object> javaBean2Map2(Object javaBean) throws Exception {
		if (javaBean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>(16);
		// BeanInfo顾名思义即javaBean信息
		BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
		// PropertyDescriptor[]相当于javaBean的属性的数组集合
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		// PropertyDescriptor相当于javaBean的单个属性
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			// javaBean的某个属性名
			String key = propertyDescriptor.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				// 如果key为class
				// 跳出本次循环继续下一次循环
				continue;
			}
			Method getter = propertyDescriptor.getReadMethod();
			Object value = getter == null ? null : getter.invoke(javaBean);
			map.put(key, value);
		}
		return map;
	}

	//**************************************************************************************//

	/**
	 * 注意：该方法一般应用于对Map<String, String>类型的集合进行操作，也就是说value是字符串类型！（当然value也可以是其他类型）
	 * 		其实页面传过来的请求数据都是字符串类型的（比如新增和修改），还有我们写的接口需要传入的参数类型一般也是字符串类型的！
	 * 强调：调用该方法前提必须要保证map的key和javaBean的属性名相同才行！
	 * @description 使用reflect反射来实现 Map 转 javaBean
	 * @param map 数据的Map集合，key对应javaBean的属性名，value对应javaBean的属性值
	 * @param javaBeanClass javaBean的Class对象
	 * @param <T> 泛型
	 * @return 返回javaBean实例
	 * @throws Exception 异常信息
	 */
	public static <T> T getJavaBeanByMap(Map<String, Object> map, Class<T> javaBeanClass) throws Exception {
		T javaBean = javaBeanClass.newInstance();
		for (String key : map.keySet()) {
			if (EmptyUtil.isNotEmpty(map.get(key))) {
				// 首先要确保数据不能为空
				// 1、仅仅获取本类的所有属性对象（包括私有属性）
				// Field[] fields = javaBeanClass.getDeclaredFields();

				// 2、获取本类以及父类的所有属性对象（包括私有属性）（不包括父类Object）
				Field[] fields = getFields(javaBean);

				setFieldForJavaBean(javaBean, map, key, fields);
			}
		}
		return javaBean;
	}

	/**
	 * @description 获取本类以及父类的所有属性对象（包括私有属性）（不包括父类Object）
	 * @param javaBean 简单java对象
	 * @return 返回属性对象数组
	 */
	private static <T> Field[] getFields(T javaBean) {
		List<Field> fieldList = getFieldList(javaBean);

		// 集合转数组方式一：（通用）
		// return fieldList.toArray(new Field[fieldList.size()]);

		// 集合转数组方式二：（特定）
		return listToArray(fieldList);
	}

	/**
	 * @description 获取本类以及父类的所有属性对象（包括私有属性）（不包括父类Object）
	 * @param javaBean 简单java对象
	 * @return 返回属性对象集合
	 */
	private static <T> List<Field> getFieldList(T javaBean) {
		List<Field> fieldList = new ArrayList<>();
		Class<?> tempClass = javaBean.getClass();
		// Class不为null
		Boolean classNotEmpty = EmptyUtil.isNotEmpty(tempClass);
		// Class不为Object
		Boolean classNotEqualsObject = EqualsUtil.isNotEquals(tempClass.getName().toLowerCase(), "java.lang.object");
		while (classNotEmpty && classNotEqualsObject) {
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			// 得到父类，然后赋给自己
			tempClass = tempClass.getSuperclass();
		}
		return fieldList;
	}

	/**
	 * @description Field集合转Field数组
	 * @param fieldList Field集合
	 * @return 返回Field数组
	 */
	private static Field[] listToArray(List<Field> fieldList) {
		int size = fieldList.size();
		Field[] fields = new Field[size];
		for (int i = 0; i < size; i++) {
			fields[i] = fieldList.get(i);
		}
		return fields;
	}

	/**
	 * 注意：该方法一般应用于对Map<String, String>类型的集合进行操作，也就是说value是字符串类型！（当然value也可以是其他类型）
	 * 		其实页面传过来的请求数据都是字符串类型的（比如新增和修改），还有我们写的接口需要传入的参数类型一般也是字符串类型的！
	 * 强调：调用该方法前提必须要保证map的key和javaBean的属性名相同才行！
	 * @description 更新javaBean中属性的值
	 * @param map 数据的Map集合，key对应javaBean的属性名，value对应javaBean的属性值
	 * @param javaBean 待设置属性值的javaBean
	 * @throws Exception 异常信息
	 */
	public static void updateJavaBean(Map<String, Object> map, Object javaBean) throws Exception {
		for (String key : map.keySet()) {
			if (EmptyUtil.isNotEmpty(map.get(key))) {
				// 首先要确保数据不能为空
				// 1、仅仅获取本类的所有属性对象（包括私有属性）
				// Field[] fields = javaBean.getClass().getDeclaredFields();

				// 2、获取本类以及父类的所有属性对象（包括私有属性）（不包括父类Object）
				Field[] fields = getFields(javaBean);

				setFieldForJavaBean(javaBean, map, key, fields);
			}
		}
	}

	/**
	 * 说明：该方法既解决了字符串转Integer类型，也解决了字符串转日期以及日期时间类型，
	 * 		如果还有其他类型需要转换继续添加判断即可！
	 * @description 设置javaBean中属性的值
	 * @param javaBean 待设置属性值的javaBean
	 * @param map 数据的Map集合，key对应javaBean的属性名，value对应javaBean的属性值
	 * @param key Map集合的key
	 * @param fields javaBean的属性数组
	 * @throws Exception 异常信息
	 */
	private static void setFieldForJavaBean(Object javaBean, Map<String, Object> map, String key, Field[] fields) throws Exception {
		for (Field field : fields) {
			if (field.getName().equals(key)) {
				// field.getName()是否可以调用与属性修饰范围无关，属性即使是被私有修饰也可以调用
				// 设置和获取私有属性值必须设置可访问标志为true
				field.setAccessible(true);
				if (field.getType() == Date.class) {
					// 日期类型
					Date date;
					if (map.get(key) instanceof String) {
						date = CalendarUtil.commonToDate(map.get(key).toString());
					} else {
						date = (Date) map.get(key);
					}
					field.set(javaBean, date);
				} else if (field.getType() == Byte.class) {
					// Byte类型
					field.set(javaBean, Byte.valueOf(map.get(key).toString()));
				} else if (field.getType() == Short.class) {
					// Short类型
					field.set(javaBean, Short.valueOf(map.get(key).toString()));
				} else if (field.getType() == Integer.class) {
					// Integer类型
					field.set(javaBean, Integer.valueOf(map.get(key).toString()));
				} else if (field.getType() == Long.class) {
					// Long类型
					field.set(javaBean, Long.valueOf(map.get(key).toString()));
				} else if (field.getType() == Double.class) {
					// Double类型
					field.set(javaBean, Double.valueOf(map.get(key).toString()));
				} else if (field.getType() == Float.class) {
					// Float类型
					field.set(javaBean, Float.valueOf(map.get(key).toString()));
				} else if (field.getType() == Boolean.class) {
					// Boolean类型
					field.set(javaBean, Boolean.valueOf(map.get(key).toString()));
				} else if (field.getType() == BigDecimal.class) {
					// BigDecimal类型
					field.set(javaBean, new BigDecimal(map.get(key).toString()));
				} else {
					// 字符串类型
					field.set(javaBean, map.get(key).toString());
				}
			}
		}
	}

	/**
	 * 说明：该方法最好，推荐使用！
	 * 该方法既解决不了字符串转基本类型，更解决不了字符串转日期以及日期时间
	 * @description 使用reflect反射来实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBeanClass javaBean的类对象
	 * @param <T> 泛型
	 * @return 返回javaBean
	 * @throws Exception 异常信息
	 */
	public static <T> T convertMap2JavaBean(Map<String, Object> map, Class<T> javaBeanClass) throws Exception {
		if (map == null) {
			return null;
		}
		T javaBean = javaBeanClass.newInstance();
		// 获取所有申明的属性
		// Field[] fields = javaBean.getClass().getDeclaredFields();
		// 获取所有申明的属性
		Field[] fields = javaBeanClass.getDeclaredFields();
		for (Field field : fields) {
			// 属性修饰符
			int modifier = field.getModifiers();
			if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier)) {
				// 如果属性修饰符是static或者final
				// 跳出本次循环继续下一次循环
				continue;
			}
			// 设置是否允许访问，true表示允许访问（private修饰外部也可以访问，但是并没有改变修饰范围）
			field.setAccessible(true);
			// 属性名
			String key = field.getName();
			// 属性值（如果map中没有key则返回null）
			Object value = map.get(key);
			// 给属性名设置属性值
			field.set(javaBean, value);
		}
		return javaBean;
	}

	/**
	 * 说明：该方法不太好用，需要进行强制转换，不推荐使用。
	 * @description 使用reflect反射来实现 Map 转 javaBean
	 * @param map map集合
	 * @param javaBeanClass javaBean的类对象
	 * @return 返回javaBean
	 * @throws Exception 异常信息
	 */
	public static Object convertMap2JavaBean2(Map<String, Object> map, Class<?> javaBeanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object javaBean = javaBeanClass.newInstance();
		// 获取所有申明的属性
		Field[] fields = javaBeanClass.getDeclaredFields();
		for (Field field : fields) {
			// 属性修饰符
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				// 如果属性修饰符是static或者final
				// 跳出本次循环继续下一次循环
				continue;
			}
			// 设置是否允许访问，true表示允许访问（private修饰外部也可以访问，但是并没有改变修饰范围）
			field.setAccessible(true);
			// 给属性名设置属性值（如果map中没有field.getName()则map.get返回null）
			field.set(javaBean, map.get(field.getName()));
		}
		return javaBean;
	}

	/**
	 * @description 使用reflect反射来实现 javaBean 转 Map（不包括父类的属性）
	 * @param javaBean javaBean
	 * @return 返回Map
	 * @throws Exception 异常信息
	 */
	public static Map<String, Object> convertJavaBean2Map(Object javaBean) throws Exception {
		if (javaBean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>(16);
		// 获取所有申明的属性
		Field[] declaredFields = javaBean.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			// 设置允许访问
			field.setAccessible(true);
			// field.getName()属性名，field.get(javaBean)即javaBean的属性值
			map.put(field.getName(), field.get(javaBean));
		}
		return map;
	}

	/**
	 * @description 使用reflect反射来实现 javaBean 转 Map（包括父类的属性但是不包括Object）
	 * @param javaBean javaBean
	 * @return 返回Map
	 * @throws Exception 异常信息
	 */
	public static Map<String, Object> convertJavaBeanAll2Map(Object javaBean) throws Exception {
		if (javaBean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>(16);
		// 获取本类以及父类的所有属性对象（包括私有属性）（不包括父类Object）
		List<Field> fieldList = getFieldList(javaBean);
		for (Field field : fieldList) {
			// 设置允许访问
			field.setAccessible(true);
			// field.getName()属性名，field.get(javaBean)即javaBean的属性值
			map.put(field.getName(), field.get(javaBean));
		}
		return map;
	}

	/**
	 * @description javaBean转Map，但是要确保javaBean的所有属性值均为字符串类型！
	 * @param javaBean 待转换的实体类对象
	 * @return 返回Map<String, String>类型的Map
	 */
	public static Map<String, String> javaBeanToMapValueStr(Object javaBean) {
		Map<String, String> map2 = new HashMap<>(16);
		try {
			Map<String, Object> map1 = convertJavaBeanAll2Map(javaBean);
			for (Entry<String, Object> entry : map1.entrySet()) {
				map2.put(entry.getKey(), (String) entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map2;
	}

	/**
	 * 说明：该方法不适用于自定义的复杂数据类型作为属性类型（除非自己重写了toString方法）
	 * @description javaBean转Map，如果javaBean的属性类型不是字符串类型，那么使用其他方法将其转换成字符串类型！
	 * @param javaBean 待转换的实体类对象
	 * @return 返回Map<String, String>类型的Map
	 */
	public static Map<String, String> javaBeanToMapValueStr2(Object javaBean) {
		Map<String, String> map2 = new HashMap<>(16);
		try {
			Map<String, Object> map1 = convertJavaBeanAll2Map(javaBean);
			for (Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof String) {
					// 如果是字符串类型
					map2.put(entry.getKey(), (String) entry.getValue());
				} else if (entry.getValue() instanceof Date) {
					// 如果是日期类型
					// yyyy-MM-dd HH:mm:ss
					map2.put(entry.getKey(), CalendarUtil.dateToString((Date) entry.getValue()));
				} else {
					// 如果是其他类型，比如Byte、Short、Integer、Long、Float、Double、Character、Boolean、BigDecimal
					map2.put(entry.getKey(), entry.getValue().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map2;
	}

	/**
	 * @description 判断Map集合里所有key对应的value是否都为空
	 * @param map 待判断的Map集合
	 * @return 如果都为空那么返回true，否则返回false
	 */
	public static <K, V> Boolean isBlank(Map<K, V> map) {
		if (EmptyUtil.isEmpty(map)) {
			throw new RuntimeException("【调用此方法之前请先确保入参不为空！】");
		}
		Set<Entry<K, V>> entrySet = map.entrySet();
		// 初始化默认都为空
		boolean flag = true;
		for (Entry<K, V> entry : entrySet) {
			if (EmptyUtil.isNotEmpty(entry.getValue())) {
				flag = false;
				// 如果有一个key对应的value不为空，那么就跳出for循环，即Map不是所有key对应的value为空
				break;
			}
		}
		return flag;
	}

	/**
	 * @description 判断Map集合里所有key对应的value是否至少有一个不为空
	 * @param map 待判断的Map集合
	 * @return 如果至少有一个不为空那么返回true，否则返回false
	 */
	public static <K, V> Boolean isNotBlank(Map<K, V> map) {
		return !isBlank(map);
	}

	public static void main(String[] args) {
		System.out.println(mapStringToMap("{gmt_create=2024-01-15 14:28:26, charset=UTF-8, gmt_payment=2024-01-15 14:28:38, notify_time=2024-01-15 14:28:39, subject=agilewing, sign=UohRLgIFWjx+DiyT8mpY+RqzwvfXc24HFcSoB4bSuuexx5pb6Z+GoRpZoqhmceyXgXpIaPxpudJ3xAeYzLIVGWQn5F4Y7MkQOYu6i8dV9fhMQzmBDyv0+SjbswfumJvD1XHMg+iz7tYZCI/8Lrc16c4PVD0+SaEWLlz9Mi1T054+jA81pQ2Y96r74phr0pQVcNobGueVJDCyFrs5irYSP9dm7schCAUPAJsjni4tlZWJZgiwE7mU4iG03+N1dsQpafjFvPdlg/jsuGCfW4sS7lovzRXA6KWwVJmNrFz7dWVqhAQjDNYyFUB6cqqxLjHoTR2IBD0ID1Eglp+8LK+CJQ==, buyer_id=2088721025368419, invoice_amount=0.05, version=1.0, notify_id=2024011501222142839168410501875020, fund_bill_list=[{\"amount\":\"0.05\",\"fundChannel\":\"ALIPAYACCOUNT\"}], notify_type=trade_status_sync, out_trade_no=P-11051E00072903, total_amount=0.05, trade_status=TRADE_SUCCESS, trade_no=2024011522001468410501821436, auth_app_id=9021000133698136, receipt_amount=0.05, point_amount=0.00, buyer_pay_amount=0.05, app_id=9021000133698136, sign_type=RSA2, seller_id=2088721028138804}"));
	}
}
