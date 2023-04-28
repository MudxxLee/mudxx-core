package com.mudxx.tool.wx.official.message.resp;

/**
 * description: 语音消息
 * @author laiwen
 * @date 2020-01-08 11:40:35
 */
public class VoiceMessage extends BaseMessage {

	/**
	 * 语音
	 */
	private com.mudxx.tool.wx.official.message.resp.Voice Voice;

	public com.mudxx.tool.wx.official.message.resp.Voice getVoice() {
		return Voice;
	}
	public void setVoice(com.mudxx.tool.wx.official.message.resp.Voice voice) {
		Voice = voice;
	}

}
