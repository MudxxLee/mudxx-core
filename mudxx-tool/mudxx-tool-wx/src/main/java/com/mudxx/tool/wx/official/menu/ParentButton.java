package com.mudxx.tool.wx.official.menu;

/**
 * description: 父菜单
 * @author laiwen
 * @date 2020-01-10 18:41:48
 */
public class ParentButton extends Button {

    /**
     * 子菜单数组
     */
    private Button[] sub_button;

    public Button[] getSub_button() {
        return sub_button;
    }
    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }

}
