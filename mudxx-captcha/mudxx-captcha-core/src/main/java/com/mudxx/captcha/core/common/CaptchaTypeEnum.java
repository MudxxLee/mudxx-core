package com.mudxx.captcha.core.common;

/**
 * @author laiwen
 */
public enum CaptchaTypeEnum {
    /**
     * 滑块拼图.
     */
    BLOCK_PUZZLE("blockPuzzle","滑块拼图"),
    /**
     * 文字点选.
     */
    CLICK_WORD("clickWord","文字点选"),
    /**
     * 默认.
     */
    DEFAULT("default","默认");

    private final String code;
    private final String desc;

    CaptchaTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String  getCode(){ return this.code;}

    public String getDesc(){ return this.desc;}

    /**
     * 根据codeValue获取枚举
     */
    public static CaptchaTypeEnum getByCode(String code){
        for (CaptchaTypeEnum e : CaptchaTypeEnum.values()){
            if(e.code.equals(code)){ return e;}
        }
        return null;
    }

    /**
     * 根据codeValue获取描述
     */
    public static String getDescByCode(String code){
        CaptchaTypeEnum enumItem = getByCode(code);
        return enumItem == null ? "" : enumItem.getDesc();
    }

    /**
     * 验证codeValue是否有效
     */
    public static boolean validateByCode(String code){
        return getByCode(code) != null;
    }

    /**
     * 列出所有值字符串
     */
    public static String getString(){
        StringBuilder buffer = new StringBuilder();
        for (CaptchaTypeEnum e : CaptchaTypeEnum.values()){
            buffer.append(e.getCode()).append("--").append(e.getDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }

}
