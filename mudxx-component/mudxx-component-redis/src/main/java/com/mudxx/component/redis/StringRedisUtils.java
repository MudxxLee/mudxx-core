package com.mudxx.component.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * string redis 工具类
 * @author laiw
 * @date 2023/3/31 10:32
 */
@Component
public class StringRedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public void set(final String key, final String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public void set(final String key, final String value, final long timeout, final TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等(单位:秒)
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     */
    public void set(final String key, final String value, final long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public String get(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 判断缓存是否存在
     * @param key 缓存键值
     */
    public boolean hasKey(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 删除单个对象如果存在
     * @param key 缓存键值
     */
    public boolean removeIfExists(final String key) {
        if(hasKey(key)) {
            return delete(key);
        }
        return false;
    }

    /**
     * 缓存自增
     * @param key 需自增的key
     * @param value 自增值
     * @return 自增后的值
     */
    public Long increment(final String key, final long value) {
        return stringRedisTemplate.opsForValue().increment(key, value);
    }

    /**
     * 如果不存在则设置
     * true: 表示key不存在时设置成功
     */
    public boolean setIfNotExists(final String key, final String value, final long timeout, final TimeUnit unit) {
        Boolean execute = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8),
                Expiration.from(timeout, unit),
                RedisStringCommands.SetOption.SET_IF_ABSENT)
        );
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 如果不存在则设置(单位:秒)
     * true: 表示key不存在时设置成功
     */
    public boolean setIfNotExists(final String key, final String value, final long timeout) {
        return setIfNotExists(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取当前剩余过期时间
     *
     * @param key     Redis键
     * @param timeUnit 时间颗粒度
     * @return
     */
    public Long getExpire(final String key, final TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 设置有效时间(单位:秒)
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean setExpire(final String key, final long timeout) {
        return setExpire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean setExpire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(stringRedisTemplate.expire(key, timeout, unit));
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean delete(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

}