package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 视频model
 * @author laiwen
 * @date 2020-01-08 11:39:46
 */
public class Video {

	/**
	 * 媒体文件id
	 */
	private String MediaId;

	/**
	 * 缩略图的媒体id
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
