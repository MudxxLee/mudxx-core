package com.mudxx.tool.wx.pay;

import com.mudxx.common.utils.empty.EmptyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * description: 微信签名工具类
 * @author laiwen
 * @date 2019-12-05 18:02:30
 */
@SuppressWarnings("ALL")
public class WxSignUtil {

    private static final Logger log = LoggerFactory.getLogger(WxSignUtil.class);

    /**
     * description: 生成微信签名
     * @author laiwen
     * @date 2019-12-05 18:11:32
     * @param encode 字符编码
     * @param params 参数（字母序）
     * @param apiKey 密钥key
     * @return 返回签名
     */
    public static String sign(String encode, SortedMap<String, Object> params, String apiKey) {
        log.info("待签名参数：{}", params);
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                stringBuilder.append(k).append("=").append(v).append("&");
            }
        }
        stringBuilder.append("key=").append(apiKey);
        String sign = md5(stringBuilder.toString(), encode).toUpperCase();
        log.info("生成微信签名：{}", sign);
        return sign;
    }

    /**
     * description: 校验签名，规则是按参数名称a-z排序，遇到空值的参数不参加签名
     * @author laiwen
     * @date 2019-12-05 19:11:13
     * @param encode 字符编码
     * @param params 参数（字母序）
     * @param apiKey 密钥key
     * @return 校验通过返回true，否则返回false
     */
    public static Boolean validateSign(String encode, SortedMap<String, Object> params, String apiKey) {
        log.info("待签名参数：{}", params);
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                stringBuilder.append(k).append("=").append(v).append("&");
            }
        }
        stringBuilder.append("key=").append(apiKey);
        String mySign = md5(stringBuilder.toString(), encode).toUpperCase();
        log.info("自己生成签名结果：{}", mySign);
        String wxSign = params.get("sign").toString();
        log.info("微信生成签名结果：{}", wxSign);
        return wxSign.equals(mySign);
    }

    /**
     * description: 对字符串进行MD5签名
     * @author laiwen
     * @date 2019-12-05 18:21:37
     * @param origin 待MD5签名的字符串
     * @param charset 字符编码方式
     * @return 返回MD5签名后的字符串
     */
    public static String md5(String origin, String charset) {
        log.info("待MD5签名的字符串：{}", origin);
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (EmptyUtil.isEmpty(charset)) {
                result = byteArrayToHexString(md.digest(origin.getBytes()));
            } else {
                result = byteArrayToHexString(md.digest(origin.getBytes(charset)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("MD5签名结果：{}", result);
        return result;
    }

    /**
     * description: 将字节数组转换成16进制表示的字符串
     * @author laiwen
     * @date 2019-12-05 18:20:19
     * @param bytes 字节数组
     * @return 返回16进制表示的字符串
     */
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(byteToHexString(b));
        }
        return stringBuilder.toString();
    }

    /**
     * description: 将字节转换成16进制表示的字符串
     * @author laiwen
     * @date 2019-12-05 18:17:49
     * @param b 字节
     * @return 返回16进制表示的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 默认的密码字符串组合，用来将字节转换成16进制表示的字符，apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}
