package com.mudxx.common.utils.array;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @description 数组工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:11:11
 */
@SuppressWarnings("ALL")
public class ArrayUtil {
	
	/**
	 * @description 将数组转换成以逗号拼接的字符串，比如数组[1, 2, 3]转换成字符串是1,2,3的形式
	 * @param t 待转换的数组
	 * @return 返回以逗号拼接后的字符串
	 */
	public static <T> String arrayToStr(T[] t) {
		return arrayToStr(t, ",");
	}
	
	/**
	 * @description 将数组转换成以指定分隔符拼接的字符串，比如指定分隔符为:，那么数组[1, 2, 3]转换成字符串是1:2:3的形式
	 * @param t 待转换的数组
	 * @param regex 指定分隔符
	 * @return 返回以指定分隔符拼接后的字符串
	 */
	public static <T> String arrayToStr(T[] t, String regex) {
		return StringUtils.join(t, regex);
	}
	
	/**
	 * @description 将含有逗号分隔符的字符串转换成字符串数组
	 * @param str 含有逗号分隔符的字符串
	 * @return 返回字符串数组
	 */
	public static String[] strToArray(String str) {
		return strToArray(str, ",");
	}
	
	/**
	 * @description 将含有指定分隔符的字符串转换成字符串数组
	 * @param str 含有指定分隔符的字符串
	 * @param regex 指定分隔符
	 * @return 返回字符串数组
	 */
	public static String[] strToArray(String str, String regex) {
		return str.split(regex);
	}
	
	/**
	 * @description 字符串数组转Integer数组
	 * @param strArr 字符串数组
	 * @return 返回Integer数组
	 */
	public static Integer[] strArrToIntArr(String[] strArr) {
		Integer[] intArr = new Integer[strArr.length];
	    for (int i = 0; i < strArr.length; i++) {
	    	intArr[i] = Integer.valueOf(strArr[i]);
	    }
	    return intArr;
	}
	
	/**
	 * @description 字符串数组转Long数组
	 * @param strArr 字符串数组
	 * @return 返回Long数组
	 */
	public static Long[] strArrToLongArr(String[] strArr) {
		Long[] longArr = new Long[strArr.length];
	    for (int i = 0; i < strArr.length; i++) {
			longArr[i] = Long.valueOf(strArr[i]);
	    }
	    return longArr;
	}

	/**
	 * @description 数组转字符串，格式无变化，比如数组[1, 2, 3]转换成字符串还是[1, 2, 3]的形式
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> String array2String(T[] t) {
		return Arrays.toString(t);
	}

	/**
	 * @description 数组转换为List，该方法不建议使用！
	 * @param t
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> array2List(T[] t) {
		return Arrays.asList(t);
	}
	
	/**
	 * 说明：使用可变数组作为入参
	 * 提醒：因为是可变并且非具体类型形式化参数，所以入参元素类型没有限制，因为如果是基本数据类型那么会自动装箱即封装为包装类型，
	 * 还有就是入参元素类型可以是任意类型，这个时候T就是? extends Object
	 * @description 数组转为ArrayList，使之可以增加或者删除集合元素
	 * @param t
	 * @return
	 */
	@SafeVarargs
	public static <T> ArrayList<T> asList(T... t) {
		if (t == null) {
			return null;
		}
		return new ArrayList<>(Arrays.asList(t));
	}
	
	/**
	 * 说明：使用可变数组作为入参
	 * 提醒：因为是可变并且非具体类型形式化参数，所以入参元素类型没有限制，因为如果是基本数据类型那么会自动装箱即封装为包装类型，
	 * 还有就是入参元素类型可以是任意类型，这个时候T就是? extends Object
	 * @description 数组转为ArrayList，使之可以增加或者删除集合元素
	 * @param t
	 * @return
	 */
	@SafeVarargs
	public static <T> ArrayList<T> toList(T... t) {
		if (t == null) {
			return null;
		}
		//虽然构造参数的有无以及大小对程序无影响，但是t.length是最优选择
		ArrayList<T> list = new ArrayList<>(t.length);
		boolean flag = Collections.addAll(list, t);
		if (flag) {
			return list;
		}
		return null;
	}

	/**
	 * 说明：使用常规（泛型）数组作为入参
	 * 警告：由于泛型限制，数组元素类型不可以是基本数据类型，必须是对象类型！
	 * @description 数组转为ArrayList，使之可以增加或者删除集合元素
	 * @param t
	 * @return
	 */
	public static <T> ArrayList<T> arrayToList(T[] t) {
		if (t == null) {
			return null;
		}
		//虽然构造参数的有无以及大小对程序无影响，但是t.length是最优选择
		ArrayList<T> list = new ArrayList<>(t.length);
		boolean flag = Collections.addAll(list, t);
		if (flag) {
			return list;
		}
		return null;
	}
	
	/**
	 * 说明：数组先转成ArrayList，然后又转成HashSet，性能开销比较大，不推荐这么做
	 * @description 判断数组是否包含特定值
	 * @param t 数组
	 * @param a 特定元素
	 * @return 如果数组包含特定元素返回true，否则返回false
	 */
	public static <T> boolean targetValueInArray(T[] t, T a) {
		Set<T> set = new HashSet<>(Arrays.asList(t));
		return set.contains(a);
	}
	
	/**
	 * 说明：代码高效，推荐这么做
	 * @description 判断数组是否包含特定值
	 * @param a 特定元素
	 * @param t 数组
	 * @return 如果数组包含特定元素返回true，否则返回false
	 */
	public static <T> boolean targetValueInArray(T a, T[] t) {
		boolean flag = false;
		for (T param : t) {
			if (a.equals(param)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 说明：T[] toArray(T[] a)方法比较完美，可以方便我们使用（其中a的长度最好设置为集合的长度）！
	 * @description 该方法作用不大，其实直接调用List的toArray()方法即可。
	 * @param list 对象集合
	 * @return 返回Object数组
	 */
	public static <T> Object[] list2Array(List<T> list) {
		return list.toArray();
	}

	/**
	 * @description 给数组所有元素统一赋值
	 * @param array
	 * @param value
	 * @param <T>
	 * @return
	 */
	public static <T> T[] setValueForArrayAll(T[] array, Object value) {
		Arrays.fill(array, value);
		return array;
	}

	/**
	 * 注意：包括开始索引但不包括结束索引
	 * @description 给数组部分元素统一赋值，可以是一个元素，也可以是多个元素
	 * @param array
	 * @param start 开始索引位置
	 * @param end 结束索引位置
	 * @param value
	 * @param <T>
	 * @return
	 */
	public static <T> T[] setValueForArrayPart(T[] array, int start, int end, Object value) {
		Arrays.fill(array, start, end, value);
		return array;
	}

	/**
	 * @description 对数组所有的元素进行升序排序
	 * @param array
	 * @param <T>
	 * @return
	 */
	public static <T> T[] sortForArrayAll(T[] array) {
		Arrays.sort(array);
		return array;
	}

	/**
	 * 注意：包括开始索引但不包括结束索引
	 * @description 对数组的部分元素进行升序排序
	 * @param array
	 * @param start 开始索引位置
	 * @param end 结束索引位置
	 * @param <T>
	 * @return
	 */
	public static <T> T[] sortForArrayPart(T[] array, int start, int end) {
		Arrays.sort(array, start, end);
		return array;
	}

	/**
	 * 注意：泛型T指的是对象数据类型，基本数据类型不符合
	 * @description 比较两个数组是否相等，数组元素个数和元素的值都相等
	 * @param array1
	 * @param array2
	 * @param <T>
	 * @return
	 */
	public static <T> boolean equalsArray(T[] array1, T[] array2) {
		return Arrays.equals(array1, array2);
	}

	/**
	 * @description 查找某个元素在数组中的索引位置（索引从0开始）
	 * @param array
	 * @param element
	 * @param <T>
	 * @return 如果数组中存在该元素返回该元素在数组中的索引位置，如果不存在返回一个负数
	 */
	public static <T> int searchElementIndexInArray(T[] array, Object element) {
		//先进行升序排序再进行查找
		Arrays.sort(array);
		return Arrays.binarySearch(array, element);
	}

	/**
	 * @description 判断一个字符串元素在字符串数组中出现了几次（次数）
	 * @param baseStr 字符串元素
	 * @param strings 字符串数组
	 * @return
	 */
	public static int eleSumInArray(String baseStr, String[] strings) {
		if (null == baseStr || "".equals(baseStr) || null == strings) {
			//其实"".equals(baseStr)等同于baseStr.length() == 0
			return 0;
		}
		int sum = 0;
		for (String string : strings) {
			boolean result = baseStr.equals(string);
			//三元表达式，这里只能使用++sum，不能使用sum++（如果使用sum++，sum会始终为0）
			sum = result ? ++sum : sum;
		}
		return sum;
	}

	/**
	 * @description List<Object[]>转字符串
	 * @param list
	 * @return
	 */
	public static String listObjectArrayToString(List<Object[]> list) {
		StringBuilder string1 = new StringBuilder();
		for (Object[] objects : list) {
			StringBuilder string2 = new StringBuilder();
			for (Object object : objects) {
				string2.append(StringUtils.stripToEmpty(object.toString())).append(", ");
			}
			string1.append(StringUtils.stripToEmpty(string2.toString())).append("; ");
		}
		return string1.toString();
	}

	/**
	 * @description 把数组转换成TreeSet方便判断(数组元素不能重复，即使重复在Set里面也只会存在一个)
	 * @param args
	 * @return
	 */
	public static TreeSet<String> arrayToTreeSet(String[] args) {
		TreeSet<String> result = new TreeSet<>();
		if (null == args) {
			return result;
		}
		result.addAll(Arrays.asList(args));
		return result;
	}

	/**
	 * @description 把数组转换成LinkedHashSet方便判断(数组元素不能重复，即使重复在Set里面也只会存在一个)
	 * @param args
	 * @return
	 */
	public static LinkedHashSet<String> arrayToLinkedHashSet(String[] args) {
		LinkedHashSet<String> result = new LinkedHashSet<>();
		if (null == args) {
			return result;
		}
		result.addAll(Arrays.asList(args));
		return result;
	}
	
	/**
	 * @description 字符串数组拼接字符串
	 * @param start 头字符串
	 * @param end 尾字符串
	 * @param strArr 待拼接字符串数组
	 * @return 返回拼接后的字符串
	 */
	public static String strArr2String(String start, String end, String... strArr) {
		StringBuilder sb = new StringBuilder();
		if (start != null) {
			sb.append(start);
		}
		for (String s : strArr) {
			sb.append(s);
		}
		if (end != null) {
			sb.append(end);
		}
		return sb.toString();
	}

}
