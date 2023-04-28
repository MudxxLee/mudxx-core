package com.mudxx.captcha.config;


import cn.hutool.core.util.StrUtil;
import com.mudxx.captcha.core.common.Const;
import com.mudxx.captcha.core.factory.CaptchaServiceFactory;
import com.mudxx.captcha.core.service.CaptchaService;
import com.mudxx.captcha.core.utils.ImageUtils;
import com.mudxx.captcha.properties.MdCaptchaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author laiwen
 */
@Configuration
public class MdCaptchaServiceAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MdCaptchaServiceAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(MdCaptchaProperties mdCaptchaProperties) {
        logger.info("自定义配置项：{}", mdCaptchaProperties.toString());
        Properties config = new Properties();
        config.put(Const.CAPTCHA_CACHETYPE, mdCaptchaProperties.getCacheType().name());
        config.put(Const.CAPTCHA_WATER_MARK, mdCaptchaProperties.getWaterMark());
        config.put(Const.CAPTCHA_FONT_TYPE, mdCaptchaProperties.getFontType());
        config.put(Const.CAPTCHA_TYPE, mdCaptchaProperties.getType().getCode());
        config.put(Const.CAPTCHA_INTERFERENCE_OPTIONS, mdCaptchaProperties.getInterferenceOptions());
        config.put(Const.ORIGINAL_PATH_JIGSAW, mdCaptchaProperties.getJigsaw());
        config.put(Const.ORIGINAL_PATH_PIC_CLICK, mdCaptchaProperties.getPicClick());
        config.put(Const.CAPTCHA_SLIP_OFFSET, mdCaptchaProperties.getSlipOffset());
        config.put(Const.CAPTCHA_AES_STATUS, String.valueOf(mdCaptchaProperties.getAesStatus()));
        config.put(Const.CAPTCHA_WATER_FONT, mdCaptchaProperties.getWaterFont());
        config.put(Const.CAPTCHA_CACAHE_MAX_NUMBER, mdCaptchaProperties.getCacheNumber());
        config.put(Const.CAPTCHA_TIMING_CLEAR_SECOND, mdCaptchaProperties.getTimingClear());

        config.put(Const.HISTORY_DATA_CLEAR_ENABLE, mdCaptchaProperties.isHistoryDataClearEnable() ? "1" : "0");

        config.put(Const.REQ_FREQUENCY_LIMIT_ENABLE, mdCaptchaProperties.getReqFrequencyLimitEnable() ? "1" : "0");
        config.put(Const.REQ_GET_LOCK_LIMIT, mdCaptchaProperties.getReqGetLockLimit() + "");
        config.put(Const.REQ_GET_LOCK_SECONDS, mdCaptchaProperties.getReqGetLockSeconds() + "");
        config.put(Const.REQ_GET_MINUTE_LIMIT, mdCaptchaProperties.getReqGetMinuteLimit() + "");
        config.put(Const.REQ_CHECK_MINUTE_LIMIT, mdCaptchaProperties.getReqCheckMinuteLimit() + "");
        config.put(Const.REQ_VALIDATE_MINUTE_LIMIT, mdCaptchaProperties.getReqVerifyMinuteLimit() + "");

        config.put(Const.CAPTCHA_FONT_SIZE, mdCaptchaProperties.getFontSize() + "");
        config.put(Const.CAPTCHA_FONT_STYLE, mdCaptchaProperties.getFontStyle() + "");
        config.put(Const.CAPTCHA_WORD_COUNT, mdCaptchaProperties.getClickWordCount() + "");

        if ((StrUtil.isNotBlank(mdCaptchaProperties.getJigsaw()) && mdCaptchaProperties.getJigsaw().startsWith("classpath:"))
                || (StrUtil.isNotBlank(mdCaptchaProperties.getPicClick()) && mdCaptchaProperties.getPicClick().startsWith("classpath:"))) {
            //自定义resources目录下初始化底图
            config.put(Const.CAPTCHA_INIT_ORIGINAL, "true");
            initializeBaseMap(mdCaptchaProperties.getJigsaw(), mdCaptchaProperties.getPicClick());
        }
        return CaptchaServiceFactory.getInstance(config);
    }

    private static void initializeBaseMap(String jigsaw, String picClick) {
        ImageUtils.cacheBootImage(getResourcesImagesFile(jigsaw + "/original/*.png"),
                getResourcesImagesFile(jigsaw + "/slidingBlock/*.png"),
                getResourcesImagesFile(picClick + "/*.png"));
    }

    public static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64Utils.encodeToString(bytes);
                String filename = resource.getFilename();
                imgMap.put(filename, string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgMap;
    }
}
