package com.mudxx.tool.wx.official.message.req;

/**
 * description: 文本消息
 * @author laiwen
 * @date 2020-01-08 11:36:23
 */
public class TextMessage extends BaseMessage {

	/**
	 * 消息内容
	 */
	private String Content;

	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

}
