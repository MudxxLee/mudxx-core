package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 文本消息
 * @author laiwen
 * @date 2020-01-08 11:39:27
 */
public class TextMessage extends BaseMessage {

	/**
	 * 回复的消息内容
	 */
	private String Content;

	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

}
