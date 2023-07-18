package com.mudxx.common.exception.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mudxx.common.exception.function.BranchHandleFunction;
import com.mudxx.common.exception.function.CacheBranchHandler;
import com.mudxx.common.exception.function.PresentOrElseHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author laiw
 * @date 2023/3/31 10:12
 */
public class JudgmentUtils {

    /**
     * 参数为true或false时，分别进行不同的操作
     *
     * @param bool boolean
     */
    public static BranchHandleFunction isTureOrFalse(boolean bool) {
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
     *
     * @param str 字符串
     */
    public static PresentOrElseHandler<?> isBlankOrNotBlank(String str) {
        return (consumer, runnable) -> {
            if (StrUtil.isBlank(str)) {
                runnable.run();
            } else {
                consumer.accept(str);
            }
        };
    }

    /**
     * 优先读取缓存、缓存不存在则读取业务数据、业务数据读取成功则重新设置缓存
     *
     * @param t
     * @param <T>
     * @return
     */
    public static  <T, R> CacheBranchHandler<T, R> readAndWriteCache(T t) {
        return (first, second, last) -> {
            R f = (R) first.apply(t);
            if (ObjectUtil.isNotEmpty(f)) {
                return f;
            }
            R s = (R) second.apply(t);
            if (ObjectUtil.isNotEmpty(s)) {
                last.accept(s);
                return s;
            }
            return null;
        };
    }


    public static void main(String[] args) {
//        JudgmentUtils.isTureOrFalse(StrUtil.isBlank("2")).trueOrFalseHandle(() -> {
//            System.out.println("11");
//        }, () -> {
//            System.out.println("22");
//        });

//        JudgmentUtils.isBlankOrNotBlank("我是").presentOrElseHandle(JudgmentUtils::doAction, JudgmentUtils::doBlank);

        JudgmentUtils judgmentUtils = new JudgmentUtils();
        JudgmentUtils.readAndWriteCache("wo").multiTaskHandle(
                judgmentUtils::getCache,
                judgmentUtils::getAdminByUsername,
                judgmentUtils::setCache);

    }

    private static void doAction(Object o) {
        System.out.println(o);
    }

    private static void doBlank() {
        System.out.println("bb");
    }


    private Object getCache(Object username) {
        Map<String, Object> r = new HashMap<>();
        r.put((String) username, "111");
        return null;
    }

    public Object getAdminByUsername(Object username) {
        Map<String, Object> r = new HashMap<>();
        r.put((String) username, "111");
        return r;
    }

    private void setCache(Object o) {
        System.out.println("设置缓存: " + o.toString());
    }


}
