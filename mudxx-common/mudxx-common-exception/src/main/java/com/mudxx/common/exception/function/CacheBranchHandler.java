package com.mudxx.common.exception.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 缓存分支处理
 *
 * @author laiwen
 */
public interface CacheBranchHandler<T, R> {

    /**
     * 缓存执行
     *
     * @param cacheTask 首先执行缓存任务
     * @param bizTask   其次执行业务任务
     * @param lastTask  最后执行任务(更新缓存)
     * @return 返回内容
     */
    R multiTaskHandle(Function cacheTask, Function bizTask, Consumer<? super R> lastTask);

}
