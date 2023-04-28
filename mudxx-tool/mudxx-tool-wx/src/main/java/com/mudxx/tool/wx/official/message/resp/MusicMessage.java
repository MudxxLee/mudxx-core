package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 音乐消息
 * @author laiwen
 * @date 2020-01-08 11:38:55
 */
public class MusicMessage extends BaseMessage {

	/**
	 * 音乐
	 */
	private com.mudxx.tool.wx.official.message.resp.Music Music;

	public com.mudxx.tool.wx.official.message.resp.Music getMusic() {
		return Music;
	}
	public void setMusic(com.mudxx.tool.wx.official.message.resp.Music music) {
		Music = music;
	}

}
