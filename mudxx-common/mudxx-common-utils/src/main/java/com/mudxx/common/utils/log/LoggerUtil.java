package com.mudxx.common.utils.log;

import com.mudxx.common.utils.empty.EmptyUtil;

/**
 * @description 日志管理工具类
 * @author laiwen
 * @date 2019-07-27 15:35
 */
@SuppressWarnings("ALL")
public class LoggerUtil {

    /**
     * @description 方法内部开始打印日志
     * @param clazz 当前所在类对象
     * @return 返回拼接的日志信息
     */
    public static String intoMethod(Class clazz) {
        return intoMethod(clazz, null);
    }

    /**
     * @description 方法内部开始打印日志（不一定是在方法的一开始）
     * @param clazz 当前所在类对象
     * @param event 事件名称，比如发送邮件，发送短信等等，如果没有可以设置为null
     * @return 返回拼接的日志信息
     */
    public static String intoMethod(Class clazz, String event) {
        //获取当前类名以及当前方法名
        String[] classNameAndMethodName = getClassNameAndMethodName(clazz);

        //如果事件信息不为null，就拼接事件信息
        String eventMsg = "";
        if (EmptyUtil.isNotEmpty(event)) {
            eventMsg = "::" + event;
        }

        //返回需要打印的日志信息
        return "============= " + classNameAndMethodName[0] + "::" + classNameAndMethodName[1] + eventMsg + " start =============";
    }

    /**
     * @description 方法内部结束打印日志
     * @param clazz 当前所在类对象
     * @return 返回拼接的日志信息
     */
    public static String outMethod(Class clazz) {
        return outMethod(clazz, null);
    }

    /**
     * @description 方法内部结束打印日志（不一定是在方法的最末尾）
     * @param clazz 当前所在类对象
     * @param event 事件名称，比如发送邮件，发送短信等等，如果没有可以设置为null
     * @return 返回拼接的日志信息
     */
    public static String outMethod(Class clazz, String event) {
        //获取当前类名以及当前方法名
        String[] classNameAndMethodName = getClassNameAndMethodName(clazz);

        //如果事件信息不为null，就拼接事件信息
        String eventMsg = "";
        if (EmptyUtil.isNotEmpty(event)) {
            eventMsg = "::" + event;
        }

        //返回需要打印的日志信息
        return "============= " + classNameAndMethodName[0] + "::" + classNameAndMethodName[1] + eventMsg + " end =============";
    }

    /**
     * @description 获取当前所在类名（类全名）以及当前运行方法名
     * @param clazz 当前所在类对象
     * @return 返回当前所在类名以及当前运行方法名的数组
     */
    public static String[] getClassNameAndMethodName(Class clazz) {
        String[] classNameAndMethodName = new String[2];

        //获取当前所在类名（类全名）
        String className = clazz.getName();
        classNameAndMethodName[0] = className;

        //运行数组
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        //获取当前运行的方法名
        String methodName = stackTrace[1].getMethodName();
        classNameAndMethodName[1] = methodName;

        return classNameAndMethodName;
    }

}
