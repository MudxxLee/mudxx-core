package com.mudxx.common.utils.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * description: 拼音相关工具类
 * @author laiwen
 * @date 2021-08-27 13:04:29
 */
@SuppressWarnings("ALL")
public class PinyinUtil {

    /**
     * description: 获取中文字符串大写首字母，比如：清华大学 -> QHDX
     * @author laiwen
     * @date 2021-08-27 11:51:12
     * @param content 需要转化的中文字符串
     * @return 返回大写首字母缩写的字符串
     */
    public static String getPinYinHeadChar(String content) {
        StringBuilder convert = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char word = content.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

}
