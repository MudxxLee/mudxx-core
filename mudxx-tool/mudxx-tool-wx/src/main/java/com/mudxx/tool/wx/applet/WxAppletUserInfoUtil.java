package com.mudxx.tool.wx.applet;

import com.mudxx.common.utils.http.HttpClientUtil;
import com.mudxx.common.utils.json.JsonTool;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * description：微信小程序获取用户信息工具类
 * @author laiwen
 * @date 2019年10月09日 下午4:55:31
 */
@SuppressWarnings("ALL")
public class WxAppletUserInfoUtil {

    private static final Logger log = LoggerFactory.getLogger(WxAppletUserInfoUtil.class);

    /**
     * 温馨提示：如果小程序以及公众号绑定到了微信开放平台上面并且用户关注了相应的公众号，那么该接口是可以取到unionid的
     * 如果用户没有关注过公众号通过该接口是无法获取unionid的，但是我们可以让用户授权个人信息，进而利用解密用户敏感信息得到unionid
     * description：获取openid和sessionKey以及unionid（该接口仅仅适用于小程序，不适用于公众号）
     * user laiwen
     * time 2019-10-09 13:50
     * @param code 临时登录凭证（微信端登录code）
     * @param appid 小程序appId
     * @param secret 小程序appSecret
     * @return 返回微信接口提供的信息
     */
    public static Map<String, Object> getSessionKeyOrOpenId(String code, String appid, String secret) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>(16);
        //小程序appId
        requestUrlParam.put("appid", appid);
        //小程序appSecret
        requestUrlParam.put("secret", secret);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String jsonResult = HttpClientUtil.doPost(requestUrl, requestUrlParam);
        HashMap<String, Object> map = JsonTool.jsonToPojo(jsonResult, HashMap.class);
        log.info("微信接口响应的openid等数据信息：{}", map);
        return map;
    }

    /**
     * 小程序AES解密
     * description：解密用户敏感数据获取用户信息
     * user laiwen
     * time 2019-10-09 16:44
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param sessionKey 数据进行加密签名的密钥
     * @param iv 加密算法的初始向量
     * @return 返回用户敏感数据
     */
    public static Map<String, Object> getUserInfo(String encryptedData, String sessionKey, String iv) {
        //被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        //加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        //偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            //如果密钥不足16位，那么就补足，这个if中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JsonTool.jsonToPojo(result, HashMap.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
