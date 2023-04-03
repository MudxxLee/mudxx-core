package com.mudxx.common.exceptiion.function;

/**
 * 分支处理接口
 * @author laiw
 * @date 2023/3/30 16:07
 */
@FunctionalInterface
public interface BranchHandleFunction {

    /**
     * 分支操作
     * @param trueHandle 为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     **/
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
