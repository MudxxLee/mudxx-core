package com.mudxx.captcha.core.factory;

import com.mudxx.captcha.core.common.Const;
import com.mudxx.captcha.core.service.CaptchaService;
import com.mudxx.captcha.core.service.cache.CaptchaCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * @author laiwen
 */
public class CaptchaServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaServiceFactory.class);

    public volatile static Map<String, CaptchaService> instances = new HashMap<>();
    public volatile static Map<String, CaptchaCacheService> cacheService = new HashMap<>();

    static {
        ServiceLoader<CaptchaService> services = ServiceLoader.load(CaptchaService.class);
        for (CaptchaService item : services) {
            instances.put(item.captchaType(), item);
        }
        logger.info("supported-captchaTypes-service:{}", instances.keySet());
        ServiceLoader<CaptchaCacheService> cacheServices = ServiceLoader.load(CaptchaCacheService.class);
        for (CaptchaCacheService item : cacheServices) {
            cacheService.put(item.type(), item);
        }
        logger.info("supported-captchaCache-service:{}", cacheService.keySet());
    }

    public static CaptchaService getInstance(Properties config) {
        String captchaType = config.getProperty(Const.CAPTCHA_TYPE, "default");
        CaptchaService ret = instances.get(captchaType);
        if (ret == null) {
            throw new RuntimeException("unsupported-[captcha.type]=" + captchaType);
        }
        ret.init(config);
        return ret;
    }

    public static CaptchaCacheService getCache(String cacheType) {
        return cacheService.get(cacheType);
    }

}
