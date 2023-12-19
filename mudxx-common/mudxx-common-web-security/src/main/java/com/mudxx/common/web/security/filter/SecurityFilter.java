package com.mudxx.common.web.security.filter;

import cn.hutool.core.util.NumberUtil;
import com.mudxx.common.exception.code.biz.SecurityErrorCode;
import com.mudxx.common.web.response.CommonResult;
import com.mudxx.common.web.security.model.SecurityHeader;
import com.mudxx.common.web.security.properties.SecurityProperties;
import com.mudxx.common.web.security.util.HttpDataUtils;
import com.mudxx.common.web.security.util.SignUtils;
import com.mudxx.common.web.security.wrapper.SecurityRequestWrapper;
import com.mudxx.common.web.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 安全过滤器
 * <p>
 * 防篡改：基于签名
 * 步骤1：客户端使用约定好的秘钥对传输的参数进行加密，得到签名值sign1，并且将签名值也放入请求的参数中，发送请求给服务端
 * 步骤2：服务端接收到客户端的请求，然后使用约定好的秘钥对请求的参数再次进行签名，得到签名值sign2。
 * 步骤3：服务端比对sign1和sign2的值，如果不一致，就认定为被篡改，非法请求。
 * 防重放：基于nonce + timestamp 的方案
 * timestamp只能在60s内进行重放攻击
 * nonce随机数可以保证接口只能被调用一次
 *
 * @author laiw
 * @date 2023/12/1 17:00
 */
@Slf4j
public class SecurityFilter implements Filter {

    private final SecurityProperties securityProperties;

    private final StringRedisTemplate stringRedisTemplate;

    public SecurityFilter(SecurityProperties securityProperties, StringRedisTemplate stringRedisTemplate) {
        this.securityProperties = securityProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        log.info("过滤URL={}", httpRequest.getRequestURI());

        // HttpServletRequest包装类
        HttpServletRequestWrapper requestWrapper = new SecurityRequestWrapper(httpRequest);

        // 验证请求头是否存在
        String timestamp = httpRequest.getHeader(this.securityProperties.getHeaderTimestamp());
        String sign = httpRequest.getHeader(this.securityProperties.getHeaderSign());
        String nonce = httpRequest.getHeader(this.securityProperties.getHeaderNonce());
        if (!NumberUtil.isLong(timestamp) || StringUtils.isBlank(sign) || StringUtils.isBlank(nonce)) {
            WebUtils.writeJsonToClient(httpResponse, CommonResult.failed(SecurityErrorCode.ILLEGAL_HEADER));
            return;
        }

        // 构建请求头
        SecurityHeader header = new SecurityHeader.Builder()
                .timestamp(Long.parseLong(timestamp))
                .sign(sign)
                .nonce(nonce)
                .build();

        /*
         * 1.重放验证
         *  判断timestamp时间戳与当前时间是否操过60s（过期时间根据业务情况设置）,如果超过了就提示签名过期。
         */
        long now = System.currentTimeMillis() / 1000;
        if (now - header.getTimestamp() > this.securityProperties.getMaxSeconds()) {
            WebUtils.writeJsonToClient(httpResponse, CommonResult.failed(SecurityErrorCode.EXPIRED_REQUEST));
            return;
        }

        /*
         * 2.签名验证
         *  参数放入SortedMap中对其进行字典排序，前端构建签名时同样需要对参数进行字典排序
         */
        boolean passed;
        SortedMap<String, String> paramMap;
        switch (httpRequest.getMethod()) {
            case "GET":
                paramMap = HttpDataUtils.getUrlParams(requestWrapper);
                passed = SignUtils.verifySign(paramMap, header);
                break;
            case "POST":
                paramMap = HttpDataUtils.getBodyParams(requestWrapper);
                passed = SignUtils.verifySign(paramMap, header);
                break;
            default:
                passed = false;
                break;
        }
        if (!passed) {
            // 验签错误
            WebUtils.writeJsonToClient(httpResponse, CommonResult.failed(SecurityErrorCode.ARGUMENT_ERROR));
            return;
        }

        // 3.随机数验证 如果不存在则设置nonce
        final String redisKey = String.join(":", this.securityProperties.getRedisKey(), header.getNonce());
        if (!this.setIfNotExists(redisKey, header.getNonce(), this.securityProperties.getMaxSeconds())) {
            // 请求重复
            WebUtils.writeJsonToClient(httpResponse, CommonResult.failed(SecurityErrorCode.REPEATED_REQUEST));
            return;
        }

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    /**
     * 如果不存在则设置
     * true: 表示key不存在时设置成功
     */
    private boolean setIfNotExists(final String key, final String value, final long timeout) {
        Boolean execute = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8),
                Expiration.from(timeout, TimeUnit.SECONDS),
                RedisStringCommands.SetOption.SET_IF_ABSENT)
        );
        return Boolean.TRUE.equals(execute);
    }

}
