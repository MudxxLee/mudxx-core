package com.mudxx.tool.wx.official.message.req;

/**
 * description: 视频消息
 * @author laiwen
 * @date 2020-01-08 11:36:33
 */
public class VideoMessage extends BaseMessage {

	/**
	 * 视频消息媒体id
	 */
	private String MediaId;

	/**
	 * 视频消息缩略图的媒体id
	 */
	private String ThumbMediaId;

	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

}
