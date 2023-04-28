package com.mudxx.common.utils.clone;

/**
 * description: 克隆支持接口
 * @author laiwen
 * @date 2020-04-14 14:36:35
 */
public interface Cloneable<T> extends java.lang.Cloneable {

    /**
     * description: 克隆当前对象，浅复制
     * @author laiwen
     * @date 2020-04-14 14:39:11
     * @return 返回克隆后的对象
     */
    T clone();

}
