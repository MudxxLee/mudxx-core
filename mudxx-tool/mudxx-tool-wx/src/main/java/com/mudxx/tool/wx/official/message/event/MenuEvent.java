package com.mudxx.tool.wx.official.message.event;

/**
 * description: 自定义菜单事件
 * @author laiwen
 * @date 2020-01-08 11:34:17
 */
public class MenuEvent extends BaseEvent {

	/**
	 * 事件KEY值，与自定义菜单接口中KEY值对应
	 */
	private String EventKey;

	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

}
