package com.mudxx.tool.wx.official.menu;

/**
 * description: click类型的点击型菜单
 * @author laiwen
 * @date 2020-01-10 18:31:26
 */
public class ClickButton extends Button {

    /**
     * 菜单类型：click
     */
    private String type;

    /**
     * 自定义关键字
     */
    private String key;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

}
