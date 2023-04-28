package com.mudxx.tool.wx.official;

import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.equals.EqualsUtil;
import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonTool;
import com.mudxx.tool.wx.official.menu.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * description: 微信公众号相关工具类
 * @author laiwen
 * @date 2020-01-11 19:21:16
 */
@SuppressWarnings("ALL")
public class WxOfficialUtil {

    private static final Logger log = LoggerFactory.getLogger(WxOfficialUtil.class);

    /**
     * 温馨提示：获取到用户的基本信息以及unionid的前提条件是用户必须关注公众号
     *          这里的accessToken我们一般通过https://api.weixin.qq.com/cgi-bin/token?
     *          grant_type=client_credential&appid=APPID&secret=APPSECRET获取
     * description: 获取公众号用户基本信息（包括unionid）（该接口仅仅适用于公众号，不适用于小程序）
     * @author laiwen
     * @date 2020-01-11 19:22:45
     * @param accessToken 调用接口凭证
     * @param openid 普通用户的标识，对当前公众号唯一
     * @return 返回用户基本信息
     */
    public static Map<String, Object> accessUserInfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?" +
                "access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
        String resultJson = HttpClientUtil.doGet(url);
        log.info("获取公众号用户基本信息接口响应数据：{}", resultJson);
        // LinkedHashMap
        return JsonTool.jsonToPojo(resultJson, Map.class);
    }

    /**
     * description: 调用微信接口创建公众号菜单
     * @author laiwen
     * @date 2020-01-11 19:33:12
     * @param menu 菜单对象
     * @param accessToken 调用接口凭证
     * @return 返回0表示创建菜单成功，返回其他值则表示创建菜单失败
     */
    public static Integer createMenu(Menu menu, String accessToken) {
        // 拼装创建菜单的url
        String menuCreateUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        // 将菜单对象转换成json字符串
        String jsonMenu = JsonTool.objectToJson(menu);
        // 调用接口创建菜单
        String result = HttpClientUtil.doPostJson(menuCreateUrl, jsonMenu);
        // 将响应数据转换成Map对象
        Map map = JsonTool.jsonToPojo(result, Map.class);
        // 创建菜单状态
        Integer errcode = Integer.valueOf(map.get("errcode").toString());
        if (EqualsUtil.isNotEquals(errcode, 0)) {
            String errmsg = map.get("errmsg").toString();
            log.info("创建菜单失败，错误码errcode={}，错误信息errmsg={}", errcode, errmsg);
        }
        return errcode;
    }

    /**
     * 提醒：微信公众号网页授权OAuth2.0开发相关接口（即使不关注公众号也能使用，不非要用户授权）
     * description: 使用code获取网页授权接口调用凭证，即网页授权access_token（包含openid）
     *              说明：网页授权接口调用凭证，注意：此access_token与基础支持的access_token不同
     *              静默授权：snsapi_base，没有弹窗，只能获取用户的openId
     *              （非静默授权不仅可以调用该接口，还可以通过access_token、openid获取用户信息）
     * @author laiwen
     * @date 2020-11-03 16:55:32
     * @param appId 公众号的唯一标识
     * @param appSecret 公众号的appSecret
     * @param code 微信回调的code
     * @return 返回网页授权access_token、openid等信息
     */
    public static Map<String, Object> getOauth2AccessToken(String appId, String appSecret, String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId
                + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        String resultJson = HttpClientUtil.doGet(url);
        // LinkedHashMap
        Map map = JsonTool.jsonToPojo(resultJson, Map.class);
        // 输出在调用接口获取access_token失败以及成功情况下返回的内容
        Object errcode = map.get("errcode");
        if (EmptyUtil.isNotEmpty(errcode)) {
            Object errmsg = map.get("errmsg");
            log.info("获取网页授权接口调用凭证返回的错误代码：{}，错误消息：{}", errcode, errmsg);
        } else {
            log.info("获取到的网页授权接口调用凭证相关内容：{}", JsonTool.objectToJson(map));
        }
        return map;
    }

    /**
     * 提醒：微信公众号网页授权OAuth2.0开发相关接口（即使不关注公众号也能使用，但需要用户授权）
     * description: 通过access_token、openid获取用户信息
     *              非静默授权：snsapi_userinfo，有弹框弹出需要用户手动点击确认授权。可以获取openId，用户的头像、昵称等
     *              （静默授权：snsapi_base，没有弹窗，只能获取用户的openId，不能调用该接口）
     * @author laiwen
     * @date 2020-11-03 17:06:43
     * @param accessToken 网页授权接口调用凭证，注意：此access_token与基础支持的access_token不同
     * @param openid 公众号用户唯一标识
     * @return 返回用户昵称、性别等信息
     */
    public static Map<String, Object> getOauth2UserInfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
        String resultJson = HttpClientUtil.doGet(url);
        // LinkedHashMap
        Map map = JsonTool.jsonToPojo(resultJson, Map.class);
        // 输出在调用接口获取access_token失败以及成功情况下返回的内容
        Object errcode = map.get("errcode");
        if (EmptyUtil.isNotEmpty(errcode)) {
            Object errmsg = map.get("errmsg");
            log.info("通过access_token、openid获取用户信息返回的错误代码：{}，错误消息：{}", errcode, errmsg);
        } else {
            log.info("通过access_token、openid获取到的用户信息：{}", JsonTool.objectToJson(map));
        }
        return map;
    }

}
