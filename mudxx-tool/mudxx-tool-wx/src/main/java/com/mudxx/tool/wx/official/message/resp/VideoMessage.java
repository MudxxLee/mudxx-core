package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 视频消息
 * @author laiwen
 * @date 2020-01-08 11:40:06
 */
public class VideoMessage extends BaseMessage {

	/**
	 * 视频
	 */
	private com.mudxx.tool.wx.official.message.resp.Video Video;

	public com.mudxx.tool.wx.official.message.resp.Video getVideo() {
		return Video;
	}
	public void setVideo(com.mudxx.tool.wx.official.message.resp.Video video) {
		Video = video;
	}

}
