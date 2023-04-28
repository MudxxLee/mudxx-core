package com.mudxx.common.utils.code;

import java.lang.Character.UnicodeBlock;

/**
 * description: 字符编码相关工具类
 * @author laiwen
 * @date 2021-10-15 11:12:06
 */
@SuppressWarnings("ALL")
public class CodeTool {

    /**
     * description: utf-8转unicode
     * @author laiwen
     * @date 2021-10-15 10:36:30
     * @param utf8Str utf-8编码的字符串
     * @return 返回unicode编码的字符串
     */
    public static String utf8ToUnicode(String utf8Str) {
        char[] myBuffer = utf8Str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < utf8Str.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                // 英文及数字等
                sb.append(myBuffer[i]);
            } else if(ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                // 全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char)j);
            } else {
                // 汉字
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * description: unicode转utf-8
     * @author laiwen
     * @date 2021-10-15 10:36:35
     * @param unicodeStr unicode编码的字符串
     * @return 返回utf-8编码的字符串
     */
    public static String unicodeToUtf8(String unicodeStr) {
        char aChar;
        int len = unicodeStr.length();
        StringBuilder sb = new StringBuilder(len);
        for (int x = 0; x < len;) {
            aChar = unicodeStr.charAt(x++);
            if (aChar == '\\') {
                aChar = unicodeStr.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = unicodeStr.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    sb.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    sb.append(aChar);
                }
            } else {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

}
