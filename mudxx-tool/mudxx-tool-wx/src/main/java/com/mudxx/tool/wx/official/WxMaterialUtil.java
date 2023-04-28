package com.mudxx.tool.wx.official;

import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 公众号素材相关工具类
 * @author laiwen
 * @date 2020-01-13 17:33:35
 */
@SuppressWarnings("ALL")
public class WxMaterialUtil {

    private static final Logger log = LoggerFactory.getLogger(WxMaterialUtil.class);

    /**
     * 获取永久素材的列表接口地址（POST）
     */
    public static final String BATCHGET_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    /**
     * 获取永久素材的总数（GET）
     */
    public static final String MATERIAL_COUNT_URL = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";

    /**
     * description: 获取永久素材的列表
     * @author laiwen
     * @date 2020-01-13 17:42:22
     * @param accessToken 调用接口凭证
     * @param type 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return 返回接口响应内容
     */
    public static String batchgetMaterial(String accessToken, String type, Integer offset, Integer count) {
        // 请求地址
        String url = BATCHGET_MATERIAL_URL.replace("ACCESS_TOKEN", accessToken);

        // 封装请求参数
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        map.put("offset", offset);
        map.put("count", count);
        String json = JsonTool.objectToJson(map);

        // 执行接口调用
        String result = HttpClientUtil.doPostJson(url, json);
        log.info("获取永久素材的列表接口调用响应结果：{}", result);

        // 返回响应结果
        return result;
    }

    /**
     * description: 获取永久素材的总数
     * @author laiwen
     * @date 2020-01-13 18:12:57
     * @param accessToken 调用接口凭证
     * @return 返回接口响应内容
     */
    public static String getMaterialCount(String accessToken) {
        // 请求地址
        String url = MATERIAL_COUNT_URL.replace("ACCESS_TOKEN", accessToken);

        // 执行接口调用
        String result = HttpClientUtil.doGet(url);
        // 比如：{"voice_count":0,"video_count":1,"image_count":17,"news_count":1}
        log.info("获取永久素材的总数接口调用响应结果：{}", result);

        // 返回响应结果
        return result;
    }

}
