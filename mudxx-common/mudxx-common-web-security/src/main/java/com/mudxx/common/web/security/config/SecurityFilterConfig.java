package com.mudxx.common.web.security.config;

import com.mudxx.common.web.security.filter.SecurityFilter;
import com.mudxx.common.web.security.properties.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * @author laiw
 * @date 2023/12/1 16:58
 */
@Configuration
public class SecurityFilterConfig {

    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private StringRedisTemplate stringRedisUtils;

    @Bean
    public FilterRegistrationBean<Filter> contextFilterRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(this.securityFilter());
        registration.addUrlPatterns("/api/v1/*");
        registration.setName("SecurityFilter");
        // 设置过滤器被调用的顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter securityFilter() {
        return new SecurityFilter(securityProperties, stringRedisUtils);
    }
}
