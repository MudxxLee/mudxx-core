package com.mudxx.captcha.config;

import com.mudxx.captcha.core.factory.CaptchaServiceFactory;
import com.mudxx.captcha.core.service.cache.CaptchaCacheService;
import com.mudxx.captcha.properties.MdCaptchaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储策略自动配置.
 *
 * @author laiwen
 */
@Configuration
public class MdCaptchaStorageAutoConfiguration {

    @Bean(name = "mdCaptchaCacheService")
    public CaptchaCacheService captchaCacheService(MdCaptchaProperties mdCaptchaProperties){
        //缓存类型redis/local/....
        return CaptchaServiceFactory.getCache(mdCaptchaProperties.getCacheType().name());
    }
}
