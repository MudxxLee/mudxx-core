package com.mudxx.common.constants.enums;

/**
 * 终端类型枚举
 * @author laiwen
 */
public enum TerminalEnum {
    /**/
    APP("App", 604800L),
    Web("Web", 7200L),
    Android("Android", 604800L),
    IPhone("iPhone", 604800L);

    private final String code;
    private final Long expired;

    TerminalEnum(String code, Long expired) {
        this.code = code;
        this.expired = expired;
    }

    public String getCode() {
        return this.code;
    }

    public Long getExpired() {
        return this.expired;
    }

}
