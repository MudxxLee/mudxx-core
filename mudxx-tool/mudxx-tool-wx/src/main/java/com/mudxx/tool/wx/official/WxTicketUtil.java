package com.mudxx.tool.wx.official;

import com.mudxx.common.utils.code.CodeUtil;
import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 获取微信公众号带参二维码的Ticket值相关工具类
 * @author laiwen
 * @date 2020-01-07 14:06:54
 */
@SuppressWarnings("ALL")
public class WxTicketUtil {

    private static final Logger log = LoggerFactory.getLogger(WxTicketUtil.class);

    /**
     * 临时二维码（数字）
     */
    private static final String QR_SCENE = "QR_SCENE";

    /**
     * 临时二维码（字符串）
     */
    private static final String QR_STR_SCENE = "QR_STR_SCENE";

    /**
     * 永久二维码（数字）
     */
    private static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";

    /**
     * 永久二维码（字符串）
     */
    private static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";

    /**
     * 创建二维码请求地址
     */
    private static final String CREATE_TICKET_PATH = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    /**
     * 通过ticket换取二维码请求地址
     */
    private static final String SHOW_QR_CODE_PATH = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    /**
     * description: 创建临时带参数二维码（数字），获取带参二维码ticket
     * @author laiwen
     * @date 2020-01-06 16:26:21
     * @param accessToken access_token
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @param sceneId 场景Id
     * @return 返回带参二维码的ticket值
     */
    public static String createTempTicket(String accessToken, String expireSeconds, Integer sceneId) {
        // 封装scene_id
        Map<String, Integer> intMap = new HashMap<>(16);
        intMap.put("scene_id", sceneId);
        // 封装scene
        Map<String, Map<String, Integer>> mapMap = new HashMap<>(16);
        mapMap.put("scene", intMap);
        // 封装请求参数
        Map<String, Object> paramsMap = new HashMap<>(16);
        paramsMap.put("expire_seconds", expireSeconds);
        paramsMap.put("action_name", QR_SCENE);
        paramsMap.put("action_info", mapMap);
        // 请求参数转换成json格式
        String json = JsonTool.objectToJson(paramsMap);
        // 执行POST的JSON请求
        String result = HttpClientUtil.doPostJson(CREATE_TICKET_PATH + accessToken, json);
        log.info("调用创建二维码接口响应数据：{}", result);
        // 获取ticket
        Map map = JsonTool.jsonToPojo(result, Map.class);
        Object ticket = map.get("ticket");
        log.info("接口凭证ticket：{}", ticket);
        return ticket == null ? null : ticket.toString();
    }

    /**
     * description: 创建临时带参数二维码（字符串），获取带参二维码ticket
     * @author laiwen
     * @date 2020-01-06 16:26:21
     * @param accessToken access_token
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @param sceneStr 场景字符串
     * @return 返回带参二维码的ticket值
     */
    public static String createTempStrTicket(String accessToken, String expireSeconds, String sceneStr) {
        // 封装scene_str
        Map<String, String> strMap = new HashMap<>(16);
        strMap.put("scene_str", sceneStr);
        // 封装scene
        Map<String, Map<String, String>> mapMap = new HashMap<>(16);
        mapMap.put("scene", strMap);
        // 封装请求参数
        Map<String, Object> paramsMap = new HashMap<>(16);
        paramsMap.put("expire_seconds", expireSeconds);
        paramsMap.put("action_name", QR_STR_SCENE);
        paramsMap.put("action_info", mapMap);
        // 请求参数转换成json格式
        String json = JsonTool.objectToJson(paramsMap);
        // 执行POST的JSON请求
        String result = HttpClientUtil.doPostJson(CREATE_TICKET_PATH + accessToken, json);
        log.info("调用创建二维码接口响应数据：{}", result);
        // 获取ticket
        Map map = JsonTool.jsonToPojo(result, Map.class);
        Object ticket = map.get("ticket");
        log.info("接口凭证ticket：{}", ticket);
        return ticket == null ? null : ticket.toString();
    }

    /**
     * description: 创建永久带参数二维码（数字），获取带参二维码ticket
     * @author laiwen
     * @date 2020-01-06 16:30:58
     * @param accessToken access_token
     * @param sceneId 场景Id
     * @return 返回带参二维码的ticket值
     */
    public static String createForeverTicket(String accessToken, Integer sceneId) {
        // 封装scene_id
        Map<String, Integer> intMap = new HashMap<>(16);
        intMap.put("scene_id", sceneId);
        // 封装scene
        Map<String, Map<String, Integer>> mapMap = new HashMap<>(16);
        mapMap.put("scene", intMap);
        // 封装请求参数
        Map<String, Object> paramsMap = new HashMap<>(16);
        paramsMap.put("action_name", QR_LIMIT_SCENE);
        paramsMap.put("action_info", mapMap);
        // 请求参数转换成json格式
        String json = JsonTool.objectToJson(paramsMap);
        // 执行POST的JSON请求
        String result = HttpClientUtil.doPostJson(CREATE_TICKET_PATH + accessToken, json);
        log.info("调用创建二维码接口响应数据：{}", result);
        // 获取ticket
        Map map = JsonTool.jsonToPojo(result, Map.class);
        Object ticket = map.get("ticket");
        log.info("接口凭证ticket：{}", ticket);
        return ticket == null ? null : ticket.toString();
    }

    /**
     * description: 创建永久带参数二维码（字符串），获取带参二维码ticket
     * @author laiwen
     * @date 2020-01-06 16:32:02
     * @param accessToken access_token
     * @param sceneStr 场景字符串
     * @return 返回带参二维码的ticket值
     */
    public static String createForeverStrTicket(String accessToken, String sceneStr) {
        // 封装scene_str
        Map<String, String> strMap = new HashMap<>(16);
        strMap.put("scene_str", sceneStr);
        // 封装scene
        Map<String, Map<String, String>> mapMap = new HashMap<>(16);
        mapMap.put("scene", strMap);
        // 封装请求参数
        Map<String, Object> paramsMap = new HashMap<>(16);
        paramsMap.put("action_name", QR_LIMIT_STR_SCENE);
        paramsMap.put("action_info", mapMap);
        // 请求参数转换成json格式
        String json = JsonTool.objectToJson(paramsMap);
        // 执行POST的JSON请求
        String result = HttpClientUtil.doPostJson(CREATE_TICKET_PATH + accessToken, json);
        log.info("调用创建二维码接口响应数据：{}", result);
        // 获取ticket
        Map map = JsonTool.jsonToPojo(result, Map.class);
        Object ticket = map.get("ticket");
        log.info("接口凭证ticket：{}", ticket);
        return ticket == null ? null : ticket.toString();
    }

    /**
     * description: 通过ticket获取二维码图片地址
     * @author laiwen
     * @date 2020-01-06 17:18:47
     * @param ticket ticket
     * @return 返回二维码图片地址
     */
    public static String showQrCode(String ticket) {
        // 获取微信公众号带参二维码图片地址
        String qrCodeUrl = SHOW_QR_CODE_PATH + CodeUtil.strToUrlCode(ticket);
        log.info("微信公众号带参二维码图片地址：{}", qrCodeUrl);
        return qrCodeUrl;
    }

}
