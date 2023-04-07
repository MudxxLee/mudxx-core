package com.mudxx.common.exception.utils;

import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exception.function.BranchHandleFunction;
import com.mudxx.common.exception.function.PresentOrElseHandler;

/**
 * @author laiw
 * @date 2023/3/31 10:12
 */
public class JudgmentUtils {

    /**
     * 参数为true或false时，分别进行不同的操作
     * @param bool boolean
     */
    public static BranchHandleFunction isTureOrFalse(boolean bool){
        return (trueHandle, falseHandle) -> {
            if (bool) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    /**
     * 参数为true或false时，分别进行不同的操作
     * @param str 字符串
     */
    public static PresentOrElseHandler<?> isBlankOrNotBlank(String str){
        return (consumer, runnable) -> {
            if (StrUtil.isBlank(str)){
                runnable.run();
            } else {
                consumer.accept(str);
            }
        };
    }



    public static void main(String[] args) {
        JudgmentUtils.isTureOrFalse(StrUtil.isBlank("2")).trueOrFalseHandle(() -> {
            System.out.println("11");
        }, () -> {
            System.out.println("22");
        });

        JudgmentUtils.isBlankOrNotBlank("我是").presentOrElseHandle(JudgmentUtils::doAction, JudgmentUtils::doBlank);
    }

    private static void doAction(Object o) {
        System.out.println(o);
    }

    private static void doBlank() {
        System.out.println("bb");
    }


}
