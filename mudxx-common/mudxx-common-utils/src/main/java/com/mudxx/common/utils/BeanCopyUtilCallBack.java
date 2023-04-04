package com.mudxx.common.utils;

@FunctionalInterface
public interface BeanCopyUtilCallBack<S, T> {
	void callBack(S t, T s);
}