package com.mudxx.tool.wx.official;

import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.tool.wx.official.message.resp.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 消息处理工具类
 *
 * @author laiwen
 * @date 2020-01-08 11:06:14
 */
@SuppressWarnings("ALL")
public class WxMessageUtil {

    private static final Logger log = LoggerFactory.getLogger(WxMessageUtil.class);

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
    /**
     * 请求消息类型：语音
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
    /**
     * 请求消息类型：视频
     */
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：事件推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    /**
     * 事件类型：SCAN(用户已关注时的扫描带参数二维码)
     */
    public static final String EVENT_TYPE_SCAN = "SCAN";
    /**
     * 事件类型：LOCATION(上报地理位置)
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    /**
     * 事件类型：CLICK(自定义菜单)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 响应消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    /**
     * 响应消息类型：图片
     */
    public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
    /**
     * 响应消息类型：语音
     */
    public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
    /**
     * 响应消息类型：视频
     */
    public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
    /**
     * 响应消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
    /**
     * 响应消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * description: 解析微信发来的请求（XML）
     *
     * @param request http请求
     * @return 返回解析数据
     * @throws Exception 异常信息
     * @author laiwen
     * @date 2020-01-08 11:15:21
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>(16);
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        return map;
    }

    /**
     * description: 扩展xstream使其支持CDATA
     *
     * @author laiwen
     * @date 2020-01-13 14:00:42
     */
    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * description: 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:46:27
     */
    public static String messageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * description: 封装文本消息
     *
     * @param content      文本内容
     * @param toUserName   开发者微信号（公众号应用的微信号）
     * @param fromUserName 发送方帐号（用户openid）
     * @return 返回封装的文本消息
     * @author laiwen
     * @date 2020-01-12 17:54:50
     */
    public static String initTextMessage(String content, String toUserName, String fromUserName) {
        TextMessage textMessage = new TextMessage();
        textMessage.setFromUserName(toUserName);
        textMessage.setToUserName(fromUserName);
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setContent(content);
        textMessage.setMsgType(RESP_MESSAGE_TYPE_TEXT);
        String message = messageToXml(textMessage);
        log.info("响应给微信服务器的xml格式的文本消息数据：{}", message);
        return message;
    }

    /**
     * description: 图片消息对象转换成xml
     *
     * @param imageMessage 图片消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:46:06
     */
    public static String messageToXml(ImageMessage imageMessage) {
        xstream.alias("xml", imageMessage.getClass());
        return xstream.toXML(imageMessage);
    }

    /**
     * description: 封装图片消息
     *
     * @param mediaId      图片ID
     * @param toUserName   开发者微信号（公众号应用的微信号）
     * @param fromUserName 发送方帐号（用户openid）
     * @return 返回封装的图片消息
     * @author laiwen
     * @date 2020-01-12 17:55:42
     */
    public static String initImageMessage(String mediaId, String toUserName, String fromUserName) {
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setCreateTime(System.currentTimeMillis());
        Image image = new Image();
        image.setMediaId(mediaId);
        imageMessage.setImage(image);
        imageMessage.setMsgType(RESP_MESSAGE_TYPE_IMAGE);
        String message = messageToXml(imageMessage);
        log.info("响应给微信服务器的xml格式的图片消息数据：{}", message);
        return message;
    }

    /**
     * description: 语音消息对象转换成xml
     *
     * @param voiceMessage 语音消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:45:43
     */
    public static String messageToXml(VoiceMessage voiceMessage) {
        xstream.alias("xml", voiceMessage.getClass());
        return xstream.toXML(voiceMessage);
    }

    /**
     * description: 视频消息对象转换成xml
     *
     * @param videoMessage 视频消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:45:24
     */
    public static String messageToXml(VideoMessage videoMessage) {
        xstream.alias("xml", videoMessage.getClass());
        return xstream.toXML(videoMessage);
    }

    /**
     * description: 封装视频消息
     *
     * @param mediaId      媒体文件id
     * @param thumbMediaId 缩略图的媒体id
     * @param toUserName   开发者微信号（公众号应用的微信号）
     * @param fromUserName 发送方帐号（用户openid）
     * @return 返回封装的视频消息
     * @author laiwen
     * @date 2020-01-13 13:53:08
     */
    public static String initVideoMessage(String mediaId, String thumbMediaId, String toUserName, String fromUserName) {
        VideoMessage videoMessage = new VideoMessage();
        videoMessage.setFromUserName(toUserName);
        videoMessage.setToUserName(fromUserName);
        videoMessage.setCreateTime(System.currentTimeMillis());
        Video video = new Video();
        if (EmptyUtil.isNotEmpty(mediaId)) {
            video.setMediaId(mediaId);
        }
        if (EmptyUtil.isNotEmpty(thumbMediaId)) {
            video.setThumbMediaId(thumbMediaId);
        }
        videoMessage.setVideo(video);
        videoMessage.setMsgType(RESP_MESSAGE_TYPE_VIDEO);
        String message = messageToXml(videoMessage);
        log.info("响应给微信服务器的xml格式的视频消息数据：{}", message);
        return message;
    }

    /**
     * description: 音乐消息对象转换成xml
     *
     * @param musicMessage 音乐消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:45:03
     */
    public static String messageToXml(MusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * description: 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return 返回xml格式的数据
     * @author laiwen
     * @date 2020-01-08 13:44:43
     */
    public static String messageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }

}
