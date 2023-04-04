package com.mudxx.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author laiwen
 */
public class BeanCopyUtils extends BeanUtils {

	/**
	 * 集合数据的拷贝
	 * 
	 * @param sources: 数据源类
	 * @param target:  目标类::new(eg: UserVO::new)
	 */
	public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
		if (sources != null) {
			return copyListProperties(sources, target, (String) null);
		}
		return null;
	}
	
	public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target,String... ignoreProperties) {
		if (sources != null) {
			return copyListProperties(sources, target, null,ignoreProperties);
		}
		return null;
	}

	/**
	 * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
	 * 
	 * @param sources:  数据源类
	 * @param target:   目标类::new(eg: UserVO::new)
	 * @param callBack: 回调函数
	 */
	public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target,
			BeanCopyUtilCallBack<S, T> callBack,String... ignoreProperties) {
		List<T> list = new ArrayList<>(sources.size());
		for (S source : sources) {
			T t = target.get();
			copyProperties(source, t,ignoreProperties);
			list.add(t);
			if (callBack != null) {
				// 回调
				callBack.callBack(source, t);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param source 数据源类
	 * @param target 目标类::new(eg: UserVO::new)
	 */
	public static <S , T> T copyObjectProperties(S source,Supplier<T> target) {
		if (source != null) {
			return copyObjectProperties(source, target, (String) null);
		}
		return null;
	}
	
	/**
	 * 
	 * @param source 数据源类
	 * @param target 目标类::new(eg: UserVO::new)
	 */
	public static <S , T> T copyObjectProperties(S source,Supplier<T> target,String... ignoreProperties) {
		if (source != null) {
			return copyObjectProperties(source, target, null,ignoreProperties); 
		}
		return null;
	}
	
	/**
	 * 
	 * @param source 数据源类
	 * @param target 目标类::new(eg: UserVO::new)
	 * @param callBack 回调函数
	 */
	public static <S , T> T copyObjectProperties(S source,Supplier<T> target,BeanCopyUtilCallBack<S, T> callBack,String... ignoreProperties) {
		T t= target.get();
		copyProperties(source, t, ignoreProperties);
		if (callBack != null) {
			callBack.callBack(source, t);
		}
		return t;
	}
}