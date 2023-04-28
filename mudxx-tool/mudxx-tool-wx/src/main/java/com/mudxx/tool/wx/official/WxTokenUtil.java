package com.mudxx.tool.wx.official;


import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.equals.EqualsUtil;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * description: 配置微信服务器时需要进行Token验证的工具类
 * @author laiwen
 * @date 2020-01-07 16:37:41
 */
@SuppressWarnings("ALL")
public class WxTokenUtil {

    /**
     * description: 响应微信发送的Token验证
     * @author laiwen
     * @date 2020-01-07 16:49:29
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param token 待验证的Token
     * @return 验证成功返回true，否则返回false
     */
    public static Boolean checkSignature(String signature, String timestamp, String nonce, String token) {
        String checkText = null;
        Boolean flag = false;
        if (EmptyUtil.isNotEmpty(signature)) {
            // 将timestamp，nonce，token按照字典序排序
            String[] paramArr = new String[]{timestamp, nonce, token};
            Arrays.sort(paramArr);
            // 将排序后的结果拼接成一个字符串
            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                // 将拼接后的字符串进行sha1加密
                byte[] digest = md.digest(content.getBytes());
                checkText = byteToStr(digest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (EmptyUtil.isNotEmpty(checkText)) {
            // 将加密后的字符串与signature进行对比
            flag = EqualsUtil.isEquals(checkText, signature.toUpperCase());
        }
        return flag;
    }

    /**
     * description: 将字节数组转化为十六进制字符串
     * @author laiwen
     * @date 2020-01-07 16:47:09
     * @param byteArrays 字节数组
     * @return 返回十六进制字符串
     */
    private static String byteToStr(byte[] byteArrays) {
        StringBuilder str = new StringBuilder();
        for (byte byteArray : byteArrays) {
            str.append(byteToHexStr(byteArray));
        }
        return str.toString();
    }

    /**
     * description: 将字节转化为十六进制字符串
     * @author laiwen
     * @date 2020-01-07 16:46:05
     * @param myByte 字节
     * @return 返回十六进制字符串
     */
    private static String byteToHexStr(byte myByte) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tampArr = new char[2];
        tampArr[0] = digit[(myByte >>> 4) & 0X0F];
        tampArr[1] = digit[myByte & 0X0F];
        return new String(tampArr);
    }

}
