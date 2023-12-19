package com.mudxx.common.constants.enums;

/**
 * 枚举常量类
 *
 * @author laiwen
 */
public enum StateEnum {
    /**
     * 状态
     */
    Enable("Enable", "启用"),
    Disable("Disable", "禁用"),
    Delete("Delete", "删除"),

    ;

    final String state;
    final String desc;

    private StateEnum(String state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public String getStatus() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

}
