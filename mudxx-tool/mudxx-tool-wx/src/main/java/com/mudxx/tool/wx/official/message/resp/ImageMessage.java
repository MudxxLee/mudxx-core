package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 图片消息
 * @author laiwen
 * @date 2020-01-08 11:38:27
 */
public class ImageMessage extends BaseMessage {

	/**
	 * 图片
	 */
	private com.mudxx.tool.wx.official.message.resp.Image Image;

	public com.mudxx.tool.wx.official.message.resp.Image getImage() {
		return Image;
	}
	public void setImage(com.mudxx.tool.wx.official.message.resp.Image image) {
		Image = image;
	}

}
