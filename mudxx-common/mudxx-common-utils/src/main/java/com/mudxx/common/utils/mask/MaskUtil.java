package com.mudxx.common.utils.mask;

/**
 * @description 对一些比较敏感的数字进行打码的工具类，比如手机号、用户名、身份证号、银行卡号等等
 * @author laiwen
 * @date 2019-07-27 16:49
 */
@SuppressWarnings("ALL")
public class MaskUtil {

    /**
     * @description 对指定的手机号进行打码处理
     * @param mobileNo 指定的手机号
     * @return 返回经过打码处理的手机号
     */
    public static String maskMobileNo(String mobileNo) {
        // 格式形如：1XX******XX
        if (null == mobileNo || "".equals(mobileNo) || mobileNo.length() < 6) {
            // 判断请求是否为空，长度是否正确
            return mobileNo;
        }

        // 遮挡字符串
        String maskStr = getMaskStr(mobileNo.length() - 5);
        // 前缀（3位）
        String preFix = mobileNo.substring(0, 3);
        // 后缀（2位）
        String subFix = mobileNo.substring(mobileNo.length() - 2);

        return preFix + maskStr + subFix;
    }

    /**
     * @description 对指定的用户名进行打码处理
     * @param userName 指定的用户名
     * @return 返回经过打码处理的用户名
     */
    public static String maskUserName(String userName) {
        // 格式形如：**X
        if (null == userName || "".equals(userName) || userName.length() < 2) {
            // 判断请求是否为空，长度是否正确
            return userName;
        }

        // 遮挡字符串
        String maskStr = getMaskStr(userName.length() - 1);
        // 后缀（1位）
        String subFix = userName.substring(userName.length() - 1);

        return maskStr + subFix;
    }

    /**
     * @description 对指定的身份证号进行打码处理
     * @param idNo 指定的身份证号
     * @return 返回经过打码处理的身份证号
     */
    public static String maskIdNo(String idNo) {
        // 格式形如：X****************X
        if (null == idNo || "".equals(idNo) || idNo.length() < 3) {
            // 判断请求是否为空，长度是否正确
            return idNo;
        }

        // 遮挡字符串
        String maskStr = getMaskStr(idNo.length() - 2);
        // 前缀（1位）
        String preFix = idNo.substring(0, 1);
        // 后缀（1位）
        String subFix = idNo.substring(idNo.length() - 1);

        return preFix + maskStr + subFix;
    }

    /**
     * @description 对指定的银行卡号进行打码处理
     * @param bankCardNo 指定的银行卡号
     * @return 返回经过打码处理的银行卡号
     */
    public static String maskBankCardNo(String bankCardNo) {
        // 格式形如：***************XXXX
        if (null == bankCardNo || "".equals(bankCardNo) || bankCardNo.length() < 5) {
            // 判断请求是否为空，长度是否正确
            return bankCardNo;
        }

        // 遮挡字符串
        String maskStr = getMaskStr(bankCardNo.length() - 4);
        // 后缀（4位）
        String subFix = bankCardNo.substring(bankCardNo.length() - 4);

        return maskStr + subFix;
    }

    /**
     * @description 获取遮挡的字符串（使用*处理）
     * @param len 遮挡长度
     * @return 返回指定遮挡长度的*
     */
    private static String getMaskStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

}
