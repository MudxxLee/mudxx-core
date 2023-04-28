package com.mudxx.common.utils.clone;

/**
 * description: 克隆支持类，提供默认的克隆方法
 * @author laiwen
 * @date 2020-04-14 14:40:19
 */
@SuppressWarnings("ALL")
public class CloneSupport<T> implements Cloneable<T> {

    /**
     * description: 对当前对象进行克隆（浅复制）
     * @author laiwen
     * @date 2020-04-14 14:41:04
     * @return 返回克隆后的对象
     */
    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneRuntimeException(e);
        }
    }

}
