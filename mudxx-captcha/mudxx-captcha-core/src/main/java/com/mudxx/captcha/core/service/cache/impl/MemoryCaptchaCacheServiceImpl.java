package com.mudxx.captcha.core.service.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.mudxx.captcha.core.service.cache.CaptchaCacheService;
import com.mudxx.captcha.core.utils.CacheUtils;

/**
 * 对于分布式部署的应用，我们建议应用自己实现CaptchaCacheService，比如用Redis，参考service/spring-boot代码示例。
 * 如果应用是单点的，也没有使用redis，那默认使用内存。
 * 内存缓存只适合单节点部署的应用，否则验证码生产与验证在节点之间信息不同步，导致失败。
 * @Title: 默认使用内存当缓存
 * @author lide1202@hotmail.com
 * @date 2020-05-12
 */
public class MemoryCaptchaCacheServiceImpl implements CaptchaCacheService {

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        CacheUtils.set(key, value, expiresInSeconds);
    }

    @Override
    public boolean exists(String key) {
        return CacheUtils.exists(key);
    }

    @Override
    public void delete(String key) {
        CacheUtils.delete(key);
    }

    @Override
    public String get(String key) {
        return CacheUtils.get(key);
    }

	@Override
	public Long increment(String key, long val) {
        String value = StrUtil.blankToDefault(get(key), "0");
    	Long ret = Long.parseLong(value) + val;
		CacheUtils.set(key, ret.toString(),0);
		return ret;
	}

	@Override
    public String type() {
        return "local";
    }
}
