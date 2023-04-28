package com.mudxx.tool.wx.official.menu;

/**
 * description: 小程序菜单
 * @author laiwen
 * @date 2020-01-10 21:43:30
 */
public class AppletButton extends Button {

    /**
     * 菜单类型：miniprogram
     * 说明：必填项；必须是 miniprogram ，表示小程序类型；这个参数是小程序出来后新增的类型。
     */
    private String type;

    /**
     * 跳转地址
     * 说明：必填项； 你的网页链接，表示如果用户微信客户端版本很老，无法打开你的小程序，将会跳转到这个 url 链接上。
     */
    private String url;

    /**
     * 小程序appid
     * 说明：小程序的appid，在小程序后台获取
     */
    private String appid;

    /**
     * 小程序页面路径
     * 说明：小程序页面路径；表示用户点击菜单后，跳转到哪个小程序页面，现在还不支持参数
     */
    private String pagepath;

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

    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }
    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

}
