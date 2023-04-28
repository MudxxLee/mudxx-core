package com.mudxx.common.utils.list;

import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.reflect.ReflectUtil;

import java.util.*;

/**
 * @description List集合工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:12:11
 */
@SuppressWarnings("ALL")
public class ListUtil {

	/**
	 * 强调：去重含有属性的javaBean
	 * 说明：!objectList.isEmpty() 和 objectList.size() > 0作用相同。
	 * @description 去除List<Object>集合中重复的元素
	 * @param objectList 对象集合
	 * @param methodName 方法名
	 * @param classes 参数类型数组
	 * @param params 参数值数组
	 * @param <T> 泛型T
	 * @return 返回去重后的javaBean
	 */
	public static <T> List<T> removeDuplicate(List<T> objectList, String methodName, Class<?>[] classes, Object[] params) {
		if (objectList != null && !objectList.isEmpty()) {
			// 如果对象集合不为null并且元素个数不为0。
			for (int i = 0; i < objectList.size(); i++) {
				// 外循环
				// for (int j = i + 1; j < objectList.size(); j++) {
				// 内循环（这种循环方式有BUG所以注释掉不使用）
				for (int j = objectList.size() - 1; j > i; j--) {
					// 内循环
					if (ReflectUtil.invoke(objectList.get(i), methodName, classes, params) != null) {
						if (Objects.equals(ReflectUtil.invoke(objectList.get(i), methodName, classes, params),
								ReflectUtil.invoke(objectList.get(j), methodName, classes, params))) {
							objectList.remove(j);
						}
					}
				}
			}
		}
		return objectList;
	}

	/**
	 * 强调：去重含有属性的javaBean
	 * 说明：!objectList.isEmpty() 和 objectList.size() > 0作用相同。
	 * @description 去除List<Object>集合中重复的元素
	 * @param objectList 对象集合
	 * @param methodName 方法名，这里通常指的是属性的getter方法。
	 * @param <T> 泛型T
	 * @return 返回去重后的javaBean
	 */
	public static <T> List<T> removeDuplicate(List<T> objectList, String methodName) {
		// 方法一：代码重复
		/*if (objectList != null && !objectList.isEmpty()) {//如果对象集合不为null并且元素个数不为0。
			for (int i = 0; i < objectList.size(); i++) {//外循环
				//for (int j = i + 1; j < objectList.size(); j++) {//内循环（这种循环方式有BUG所以注释掉不使用）
				for (int j = objectList.size() - 1; j > i; j--) {//内循环
					if (ReflectUtil.invoke(objectList.get(i), methodName) != null) {
						if (Objects.equals(ReflectUtil.invoke(objectList.get(i), methodName),
								ReflectUtil.invoke(objectList.get(j), methodName))) {
							objectList.remove(j);
						}
					}
				}
			}
		}
		return objectList;*/

		// 方法二：代码精简
		return removeDuplicate(objectList, methodName, null, null);
	}

	/**
	 * 强调：该方法不是很好，不建议使用。
	 * 说明：去重Integer，Boolean等等基本数据类型的对象封装类型（以及String）
	 * 注意：在java里面集合里包裹的数据类型必须使用基本数据的对象封装类型，不能使用基本数据类型。
	 * @description 去重list集合里面的数据（基本数据类型的对象封装类型，
	 * 		 因为list集合元素只能是对象类型，不可以是基本数据类型），不过需要进行强制转换
	 * @param list list集合
	 * @return 返回去重之后的list集合
	 */
	public static List<?> removeDuplicateForBase(List<?> list) {
		List<Object> newList = new ArrayList<>();
		// 1、传统写法
		for (Object object : list) {
			if (!newList.contains(object)) {
				newList.add(object);
			}
		}
		// 2、jdk1.8新特性
		/*list.stream().filter(object -> !newList.contains(object))
				     //.forEach(object -> newList.add(object));
					 .forEach(newList::add);*/
		return newList;
	}

	/**
	 * 强调：该方法比较好用，建议使用该方法。
	 * 说明：去重Integer，Boolean等等基本数据类型的对象封装类型（以及String）
	 * 注意：在java里面集合里包裹的数据类型必须使用基本数据的对象封装类型，不能使用基本数据类型。
	 * @description 去重list集合里面的数据（不包括基本数据类型，因为泛型T只对对象类型有效果）
	 * @param list list集合
	 * @return 返回去重之后的list集合
	 */
	public static <T> List<T> removeDuplicateForObject(List<T> list) {
		List<T> newList = new ArrayList<>();
		// 1、传统写法
		for (T t : list) {
			if (!newList.contains(t)) {
				newList.add(t);
			}
		}
		// 2、jdk1.8新特性
		/*list.stream().filter(t -> !newList.contains(t))
				//.forEach(t -> newList.add(t));
				.forEach(newList::add);*/
		return newList;
	}

	/**
	 * @description Iterator<T>转List<T>
	 * @param iterator 待转化的迭代器
	 * @param <T> 泛型T
	 * @return 返回转化后的List集合
	 */
	public static <T> List<T> iteratorToList(Iterator<T> iterator) {
		List<T> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
	
	/**
	 * 说明：该方法其实就是clear()方法的另一种实现方式，效果相同
	 * 提醒：建议使用LinkedList来进行删除集合元素操作
	 * @description 在集合迭代的时候移除List中的所有元素
	 * @param list
	 * @return 如果移除集合中所有元素成功那么就返回true，否则返回false
	 */
	public static <T> boolean removeAllParamsInList(List<T> list) {
		Iterator<T> iterator = list.iterator();
		while (iterator.hasNext()) {
			// 该步骤不可或缺，必须在remove执行之前进行
			iterator.next();
			// 循环遍历删除集合里面的所有元素
			iterator.remove();
		}
		if (list.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 提醒：建议使用LinkedList来进行删除集合元素操作
	 * @description 在集合迭代的时候移除List中的某个指定元素（可能不止一处）
	 * @param list
	 * @param t
	 * @return 返回移除指定元素后的集合
	 */
	public static <T> List<T> removeParamsInList(List<T> list, T t) {
		Iterator<T> iterator = list.iterator();
		while (iterator.hasNext()) {
			T next = iterator.next();
			if (t.equals(next)) {
				// 删除指定的元素
				iterator.remove();
			}
		}
		return list;
	}
	
	/**
	 * @description List集合通过get方法获取元素建议使用ArrayList
	 * @param list
	 * @param index
	 * @return
	 */
	public static <T> T getParam(List<T> list, int index) {
		try {
			return list.get(index);
		} catch (Exception e) {
			throw new RuntimeException("集合下标越界，请在集合下标范围内获取元素！");
		}
	}
	
	/**
	 * @description List集合通过set方法设置元素建议使用ArrayList
	 * @param list
	 * @param index
	 * @param t
	 * @return
	 */
	public static <T> List<T> setParam(List<T> list, int index, T t) {
		try {
			list.set(index, t);
			return list;
		} catch (Exception e) {
			throw new RuntimeException("集合下标越界，请在集合下标范围内设置元素！");
		}
	}
	
	/**
	 * @description List集合通过add方法添加元素建议使用LinkedList
	 * @param list
	 * @param t 元素对象
	 * @param index 元素位置索引
	 * @return
	 */
	public static <T> List<T> addParam(List<T> list, T t, Integer index) {
		if (EmptyUtil.isEmpty(index)) {
			// index为null
			list.add(t);
		} else if (EmptyUtil.isNotEmpty(t)) {
			// index不为null，t不为null
			list.add(index, t);
		}
		return list;
	}

	/**
	 * 提醒：该方法只删除集合中指定的一处元素，而不是删除集合中的所有该元素！（删除所有该指定元素推荐使用removeParamsInList方法）
	 * @description List集合通过remove方法删除元素建议使用LinkedList
	 * @param list
	 * @param t 元素对象
	 * @param index 元素位置索引
	 * @return
	 */
	public static <T> List<T> removeParam(List<T> list, T t, Integer index) {
		if (EmptyUtil.isNotEmpty(t)) {
			// t不为null
			// 第一次出现位置
			list.remove(t);
		} else if (EmptyUtil.isNotEmpty(index)) {
			// t为null，index不为null
			list.remove(index);
		}
		return list;
	}
	
	/**
	 * @description 将List集合中的元素进行升序排列（主要是针对数字类型对象）
	 * @param list
	 * @return
	 */
	public static List asc(List list) {
		// 升序
		Collections.sort(list);
		return list;
	}
	
	/**
	 * @description 将List集合中的元素进行降序排列（主要是针对数字类型对象）
	 * 1、先升序
	 * 2、再反转
	 * @param list
	 * @return
	 */
	public static List desc(List list) {
		// 升序
		Collections.sort(list);
		// 反转
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * @description 将List集合转换为Set集合，元素去重并且升序排列（主要是针对数字类型对象）
	 * @param list
	 * @return
	 */
	public static Set list2SetAsc(List list) {
		// 升序
		Collections.sort(list);
		// List转LinkedHashSet（按照元素插入顺序排列）
		return new LinkedHashSet<>(list);
	}
	
	/**
	 * @description 将List集合转换为Set集合，元素去重并且降序排列（主要是针对数字类型对象）
	 * @param list
	 * @return
	 */
	public static Set list2SetDesc(List list) {
		// 升序
		Collections.sort(list);
		// 反转
		Collections.reverse(list);
		// List转LinkedHashSet（按照元素插入顺序排列）
		return new LinkedHashSet<>(list);
	}
	
	/**
	 * 说明：虽然是无序排列，但是一旦排好顺序就不再改变，只不过不是按照插入顺序来排列而已，而是根据hash算法来进行排序
	 * @description 将List集合转换为Set集合，集合元素不重复并且无序（这里的无序指的是没有按照元素插入的先后顺序排列）
	 * @param list
	 * @return
	 */
	public static <T> Set<T> list2Set(List<T> list) {
		// List转HashSet（元素不按照插入顺序排列，即随机排列）
		return new HashSet<>(list);
	}
	
	/**
	 * @description 获取List集合中的最大值（一般应用于集合包装对象类型为整数）
	 * @param list
	 * @return
	 */
	public static Integer getMax(List<? extends Integer> list) {
		return Collections.max(list);
	}
	
	/**
	 * @description 获取List集合中的最小值（一般应用于集合包装对象类型为整数）
	 * @param list
	 * @return
	 */
	public static Integer getMin(List<? extends Integer> list) {
		return Collections.min(list);
	}
	
	/**
	 * 提醒：除非必要否则尽量不要使用该方法，因为该方法非常损耗性能
	 * 说明：List接口的两个实现类ArrayList和LinkedList都是线程不安全的类
	 * @description 将List集合由线程不安全变为线程安全
	 * @param list
	 * @return
	 */
	public static <T> List<T> sync(List<T> list) {
		// 线程同步
		return Collections.synchronizedList(list);
	}
	
	/**
	 * 说明：每调用一次该方法，那么集合里的元素顺序都会发生变化
	 * @description 元素顺序随机化，即洗牌
	 * @param list
	 * @return
	 */
	public static <T> List<T> randomList(List<T> list) {
		// 集合元素随机化，即打乱集合元素原来的排列顺序
		Collections.shuffle(list);
		return list;
	}

	/**
	 * description：list集合元素随机打乱位置（方案一）
	 * @param list list集合
	 * @param <T> 泛型
	 */
	public static <T> void shuffle(List<T> list) {
		int size = list.size();
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			// 随机索引范围[0,size)，前闭后开
			int randomPos = random.nextInt(size);
			// 当前元素与随机元素交换
			T temp = list.get(i);
			list.set(i, list.get(randomPos));
			list.set(randomPos, temp);
		}
	}

	/**
	 * description：list集合元素随机打乱位置（方案二）
	 * @param list list集合
	 * @param <T> 泛型
	 */
	public static <T> void shuffle2(List<T> list) {
		int size = list.size();
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			// 随机索引范围[0,size)，前闭后开
			int randomPos = random.nextInt(size);
			// 当前元素与随机元素交换
			Collections.swap(list, i, randomPos);
		}
	}

	/**
	 * description：list集合元素随机打乱位置（方案三）
	 * @param list list集合
	 * @param <T> 泛型
	 */
	public static <T> void shuffle3(List<T> list) {
		// 打乱顺序
		Collections.shuffle(list);
	}
	
}
