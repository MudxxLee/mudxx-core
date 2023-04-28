package com.mudxx.captcha.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.InputStream;

/**
 * @author laiw
 * @date 2023/4/10 17:52
 */
public class FontUtils {
    private static final Logger logger = LoggerFactory.getLogger(FontUtils.class);

    /**
     * 加载resources下的font字体
     * 部署在linux中，如果没有安装中文字段，水印和点选文字，中文无法显示，
     * 通过加载resources下的font字体解决，无需在linux中安装字体
     */
    public static Font loadFont(String fontStr, final int hanZiSize) {
        try {
            if (fontStr.toLowerCase().endsWith(".ttf")
                    || fontStr.toLowerCase().endsWith(".ttc")
                    || fontStr.toLowerCase().endsWith(".otf")) {
                InputStream inputStream = FontUtils.class.getResourceAsStream("/fonts/" + fontStr);
                if (inputStream != null) {
                    return Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(Font.BOLD, hanZiSize / 2);
                }
            } else {
                return new Font(fontStr, Font.BOLD, hanZiSize / 2);
            }
        } catch (Exception e) {
            logger.error("load font error:{}", e.getMessage(), e);
        }
        return null;
    }

}
