package com.mudxx.tool.wx.applet;

import java.util.Map;

/**
 * description: 模板消息封装对象
 * @author laiwen
 * @date 2019-11-19 17:28:14
 */
public class WxTemplateMessage {

    /**
     * 接收者（用户）的 openid
     */
    private String touser;

    /**
     * 所需下发的模板消息的id
     */
    private String template_id;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    private String page;

    /**
     * 表单提交场景下，为submit事件带上的 formId；支付场景下，为本次支付的prepay_id
     */
    private String form_id;

    /**
     * 模板内容，不填则下发空模板
     */
    private Map<String, Object> data;

    /**
     * 模板需要放大的关键词，不填则默认无放大
     */
    private String emphasis_keyword;

    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }
    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Map<String, Object> getData() {
        return data;
    }
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }
    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }

}
