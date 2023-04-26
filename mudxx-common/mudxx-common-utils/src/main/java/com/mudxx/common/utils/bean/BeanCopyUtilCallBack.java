package com.mudxx.common.utils.bean;

@FunctionalInterface
public interface BeanCopyUtilCallBack<S, T> {
	void callBack(S t, T s);
}