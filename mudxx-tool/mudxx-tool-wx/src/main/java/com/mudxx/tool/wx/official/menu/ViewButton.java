package com.mudxx.tool.wx.official.menu;

/**
 * description: view类型的视图型菜单
 * @author laiwen
 * @date 2020-01-10 18:26:31
 */
public class ViewButton extends Button {

    /**
     * 菜单类型：view
     */
    private String type;

    /**
     * 自定义视图地址
     */
    private String url;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
