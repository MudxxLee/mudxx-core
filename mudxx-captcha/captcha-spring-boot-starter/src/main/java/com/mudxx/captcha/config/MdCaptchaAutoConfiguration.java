package com.mudxx.captcha.config;

import com.mudxx.captcha.properties.MdCaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author laiwen
 */
@Configuration
@EnableConfigurationProperties(MdCaptchaProperties.class)
@ComponentScan("com.mudxx.captcha")
@Import({MdCaptchaServiceAutoConfiguration.class, MdCaptchaStorageAutoConfiguration.class})
public class MdCaptchaAutoConfiguration {
}
