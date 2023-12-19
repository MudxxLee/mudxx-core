package com.mudxx.common.constants.enums;

/**
 * 枚举常量类
 *
 * @author laiwen
 */
public enum IsDelEnum {
    /**
     * 删除状态
     */
    NOT_DEL(0, "未删除"),
    Deleted(1, "已删除"),
    Recycled(2, "已回收");

    final Integer status;
    final String desc;

    private IsDelEnum(Integer status, String desc) {
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
