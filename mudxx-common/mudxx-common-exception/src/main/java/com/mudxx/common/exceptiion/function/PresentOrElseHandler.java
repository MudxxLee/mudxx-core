package com.mudxx.common.exceptiion.function;

import java.util.function.Consumer;

/**
 * 空值与非空值分支处理
 * @author laiwen
 */
public interface PresentOrElseHandler <T> {

    /**
     * 值不为空时执行消费操作
     * 值为空时执行其他的操作
     *
     * @param action 值不为空时，执行的消费操作
     * @param emptyAction 值为空时，执行的操作
     */
    void presentOrElseHandle(Consumer<? super T> action, Runnable emptyAction);

}
