package com.mudxx.tool.wx.official.message.req;

/**
 * description: 图片消息
 * @author laiwen
 * @date 2020-01-08 11:35:35
 */
public class ImageMessage extends BaseMessage {

	/**
	 * 图片链接
	 */
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

}
