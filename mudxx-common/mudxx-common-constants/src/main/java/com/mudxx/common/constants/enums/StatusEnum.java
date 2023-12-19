package com.mudxx.common.constants.enums;

/**
 * 枚举常量类
 *
 * @author laiwen
 */
public enum StatusEnum {
    /**
     * 状态
     */
    Enable(1, "启用"),
    Disable(0, "禁用");

    final Integer status;
    final String desc;

    private StatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
