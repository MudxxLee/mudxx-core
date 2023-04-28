package com.mudxx.tool.wx.applet;

import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * description: 微信常用工具类
 * @author laiwen
 * @date 2019-12-19 16:13:34
 */
@SuppressWarnings("ALL")
public class WxCommonUtil {

    private static final Logger log = LoggerFactory.getLogger(WxCommonUtil.class);

    /**
     * 温馨提示：该方法不仅适用于小程序，同样也适用于公众号
     * description: 根据appId和appSecret获取access_token等信息
     * @author laiwen
     * @date 2019-12-19 16:14:42
     * @param appId appId
     * @param appSecret appSecret
     * @return 返回access_token等信息
     */
    public static Map<String, Object> getAccessToken(String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + appId + "&secret=" + appSecret;
        // 比如：{"access_token":"ACCESS_TOKEN","expires_in":7200}
        String resultJson = HttpClientUtil.doGet(url);
        // LinkedHashMap
        Map map = JsonTool.jsonToPojo(resultJson, Map.class);
        // 输出在调用接口获取access_token失败以及成功情况下返回的内容
        Object errcode = map.get("errcode");
        if (EmptyUtil.isNotEmpty(errcode)) {
            Object errmsg = map.get("errmsg");
            log.info("调用微信获取access_token接口返回的错误代码：{}，错误消息：{}", errcode, errmsg);
        } else {
            Object accessToken = map.get("access_token");
            log.info("根据appId和appSecret获取的access_token：{}", accessToken);
        }
        return map;
    }

    /**
     * description: 发送模板消息
     * @author laiwen
     * @date 2019-11-19 17:05:19
     * @param accessToken 接口调用凭证
     * @param wxTemplateMessage 模板消息封装对象
     * @return 返回接口响应的数据
     */
    public static Map<String, Object> sendTemplateMessage(String accessToken, WxTemplateMessage wxTemplateMessage) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        // 请求参数为json字符串
        String paramsJson = JsonTool.objectToJson(wxTemplateMessage);
        // 微信响应比如：{"errcode":0,"errmsg":"ok"}
        String resultJson = HttpClientUtil.doPostJson(url, paramsJson);
        // LinkedHashMap
        return JsonTool.jsonToPojo(resultJson, Map.class);
    }

    /**
     * description: 发送订阅消息
     * @author laiwen
     * @date 2019-12-19 16:48:57
     * @param accessToken 接口调用凭证
     * @param wxSubscribeMessage 订阅消息封装对象
     * @return 返回接口响应的数据
     */
    public static Map<String, Object> sendSubscribeMessage(String accessToken, WxSubscribeMessage wxSubscribeMessage) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        // 请求参数为json字符串
        String paramsJson = JsonTool.objectToJson(wxSubscribeMessage);
        // 微信响应比如：{"errcode":0,"errmsg":"ok"}
        String resultJson = HttpClientUtil.doPostJson(url, paramsJson);
        // LinkedHashMap
        return JsonTool.jsonToPojo(resultJson, Map.class);
    }

}
