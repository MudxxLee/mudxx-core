package com.mudxx.common.utils.domain;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mudxx.common.constants.enumext.TerminalEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author laiw
 * @date 2023/4/17 17:08
 */
public class EnumUtils {

    /**
     * 根据枚举值获取枚举类
     * DemoEnum demo = EnumUtils.getEnumByValue(DemoEnum::getCode, "demo");
     *
     * @param condition 条件字段
     * @param value     枚举属性值
     */
    public static <E extends Enum<E>, C> E getEnumBy(Func1<E, C> condition, C value) {
        return EnumUtil.getBy(condition, value);
    }

    /**
     * 判断某个字段值是否存在枚举中
     *
     * @param condition 条件字段
     * @param value     枚举属性值
     * @return 是否存在
     */
    public static <E extends Enum<E>, C> boolean contains(Func1<E, C> condition, C value) {
        return getEnumBy(condition, value) != null;
    }

    /**
     * 判断目标枚举（targetEnum）的某个字段（condition）的值是否和入参value匹配
     *
     * @param targetEnum 目标枚举
     * @param condition  条件字段
     * @param value      枚举属性值
     * @return 是否存在
     */
    public static <E extends Enum<E>, C> boolean contains(E targetEnum, Func1<E, C> condition, C value) {
        if (ObjectUtil.isAllNotEmpty(targetEnum, condition)) {
            Object fieldValue = ReflectUtil.getFieldValue(targetEnum, LambdaUtil.getFieldName(condition));
            C targetEnumFieldValue = (C) fieldValue;
            return ObjectUtil.equal(targetEnumFieldValue, value);
        }
        return false;
    }

    /**
     * 根据字段值拼接字段值的value
     *
     * @param clazz     枚举类
     * @param fieldName 字段值
     */
    public static String valueJoinString(Class<? extends Enum<?>> clazz, String fieldName, CharSequence delimiter) {
        List<Object> fieldValues = EnumUtil.getFieldValues(clazz, fieldName);
        List<String> collect = fieldValues.stream().map(String::valueOf).collect(Collectors.toList());
        return String.join(delimiter, collect);
    }


    public static void main(String[] args) {

//        TerminalEnum anEnum = EnumUtil.getBy(TerminalEnum::getCode, "App");
//        System.out.println(EnumUtil.getBy(TerminalEnum::getCode, "App"));
//        System.out.println(EnumUtil.getNameFieldMap(TerminalEnum.class, "code"));

        System.out.println(EnumUtils.getEnumBy(TerminalEnum::getCode, "Web"));
        System.out.println(EnumUtils.contains(TerminalEnum::getCode, "Web"));
        System.out.println(EnumUtils.contains(TerminalEnum.APP, TerminalEnum::getCode, "App"));
        System.out.println(EnumUtils.valueJoinString(TerminalEnum.class, "code", ","));

    }
}
