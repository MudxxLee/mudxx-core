package com.mudxx.common.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * description: 与java反射机制相关工具类
 * @author laiwen
 * @date 2018-05-23 11:12:41
 */
@SuppressWarnings("ALL")
public class ReflectUtil {

	/**
	 * 说明：该方法适用于调用无参方法
	 * description: 通过对象以及对象的方法名调用对象的方法
	 * @author laiwen
	 * @date 2018-05-23 11:46:22
	 * @param object 对象
	 * @param methodName 方法名
	 * @return 如果methodName方法有返回值，那么returnValue为该返回值，否则returnValue为null
	 */
	public static Object invoke(Object object, String methodName) {
		Object returnValue = invoke(object, methodName, null, null);
		return returnValue;
	}

	/**
	 * 强调：该方法相对来讲使用面比较广泛，建议使用该方法。
	 * 说明：被调用的方法如果有返回值那么本方法返回其返回值，否则返回null
	 * 补充：我们一般情况下利用反射机制都是调用别的类的公共方法，很少调用私有方法，因为我们定义私有方法的初衷就是只在本类里面使用。
	 * description: 通过对象以及对象的方法名和参数调用对象的方法
	 * @author laiwen
	 * @date 2018-05-23 11:17:22
	 * @param object 对象
	 * @param methodName 方法名
	 * @param classes 参数类型数组
	 * @param params 参数值数组
	 * @return 如果methodName方法有返回值，那么returnValue为该返回值，否则returnValue为null
	 */
	public static Object invoke(Object object, String methodName, Class<?>[] classes, Object[] params) {
		try {
			Class<?> clazz = object.getClass();
			// 使用getDeclaredMethod得到的方法可以是公共、受保护、默认（包访问）、私有方法，但不包括继承的方法
			Method method = clazz.getDeclaredMethod(methodName, classes);
			Object returnValue = null;
			if (method.isAccessible()) {
				// 如果是public修饰的方法（公共方法）
				returnValue = method.invoke(object, params);
			} else {
				// 如果是private修饰的方法（私有方法）
				method.setAccessible(true);
				returnValue = method.invoke(object, params);
				method.setAccessible(false);
			}
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("程序运行异常！");
		}
	}

	/**
	 * 注意：该方法不建议使用
	 * 强调：使用该方法需要注意，用该方法获取javaBean的属性值即调用getter方法为null，一般用来调用其他方法。
	 * 说明：被调用的方法如果有返回值那么本方法返回其返回值，否则返回null
	 * 补充：我们一般情况下利用反射机制都是调用别的类的公共方法，很少调用私有方法，因为我们定义私有方法的初衷就是只在本类里面使用。
	 * description: 通过对象以及对象的方法名和参数调用对象的方法
	 * @author laiwen
	 * @date 2018-05-23 11:28:22
	 * @param clazz 类对象
	 * @param methodName 方法名
	 * @param classes 参数类型数组
	 * @param params 参数值数组
	 * @return 如果methodName方法有返回值，那么returnValue为该返回值，否则returnValue为null
	 */
	public static <T> Object invoke(Class<T> clazz, String methodName, Class<?>[] classes, Object[] params) {
		try {
			T t = clazz.newInstance();
			// 使用getDeclaredMethod得到的方法可以是公共、受保护、默认（包访问）、私有方法，但不包括继承的方法
			Method method = clazz.getDeclaredMethod(methodName, classes);
			Object returnValue = null;
			if (method.isAccessible()) {
				// 如果是public修饰的方法（公共方法）
				returnValue = method.invoke(t, params);
			} else {
				// 如果是private修饰的方法（私有方法）
				method.setAccessible(true);
				returnValue = method.invoke(t, params);
				method.setAccessible(false);
			}
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("程序运行异常！");
		}
	}

	/**
	 * description: 获取javaBean的属性名对应的属性值
	 * @author laiwen
	 * @date 2018-05-23 13:28:22
	 * @param javaBean 实体类
	 * @param fieldName 属性名
	 * @return 返回属性值
	 */
	public static Object getValueByFieldName(Object javaBean, String fieldName) {
		Field field = getFieldByFieldName(javaBean, fieldName);
		Object valueGet = null;
		if (field != null) {
			valueGet = getOrSetValue(javaBean, field, null, true);
		}
		return valueGet;
	}

	/**
	 * description: 设置javaBean的属性名对应的属性值
	 * @author laiwen
	 * @date 2018-05-23 13:48:23
	 * @param javaBean 实体类
	 * @param fieldName 属性名
	 * @param valueSet 属性值
	 */
	public static void setValueByFieldName(Object javaBean, String fieldName, Object valueSet) {
		Field field = getFieldByFieldName(javaBean, fieldName);
		if (field != null) {
			getOrSetValue(javaBean, field, valueSet, false);
		}
	}

	/**
	 * description: 获取javaBean的属性名对应的属性对象
	 * @author laiwen
	 * @date 2018-05-23 14:29:12
	 * @param javaBean 实体类
	 * @param fieldName 属性名
	 * @return 返回属性类型
	 */
	private static Field getFieldByFieldName(Object javaBean, String fieldName) {
		for (Class<?> superClass = javaBean.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * description: 获取或者设置实体类的属性值
	 * @author laiwen
	 * @date 2018-05-23 14:48:29
	 * @param javaBean 实体类
	 * @param field 属性
	 * @param valueSet 待设置属性值
	 * @param isGet 如果为true，则表示获取属性值；如果为false，则表示设置属性值。
	 * @return 如果是获取属性值，那么返回值有使用的价值，否则如果是设置属性值那么该返回值没有使用的必要。
	 */
	private static Object getOrSetValue(Object javaBean, Field field, Object valueSet, boolean isGet) {
		Object valueGet = null;
		try {
			if (isGet) {
				if (field.isAccessible()) {
					valueGet = field.get(javaBean);
				} else {
					field.setAccessible(true);
					valueGet = field.get(javaBean);
					field.setAccessible(false);
				}
			} else {
				if (field.isAccessible()) {
					field.set(javaBean, valueSet);
				} else {
					field.setAccessible(true);
					field.set(javaBean, valueSet);
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueGet;
	}

}
