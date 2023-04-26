package com.mudxx.component.redis;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * string redis 工具类
 *
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
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 时间
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
     *
     * @param key 缓存键值
     */
    public boolean hasKey(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 删除单个对象如果存在
     *
     * @param key 缓存键值
     */
    public boolean removeIfExists(final String key) {
        if (hasKey(key)) {
            return delete(key);
        }
        return false;
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    public boolean delete(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    /**
     * 根据前缀删除符合的对象
     * keys的操作会导致数据库暂时被锁住，其他的请求都会被堵塞；业务量大的时候会出问题
     *
     * @param prefixKey 缓存键值的前缀
     */
    public long deleteKeys(final String prefixKey) {
        Set<String> keys = stringRedisTemplate.keys(prefixKey + "*");
        return ObjectUtil.defaultIfNull(stringRedisTemplate.delete(keys), 0L);
    }

    /**
     * 缓存自增
     *
     * @param key   需自增的key
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
     * @param key Redis键
     * @return
     */
    public Long getExpire(final String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * 获取当前剩余过期时间
     *
     * @param key      Redis键
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
     * 添加Set缓存
     *
     * @param key  Redis键
     * @param args 添加的元素
     */
    public void addForSet(final String key, String... args) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        setOps.add(key, args);
    }

    /**
     * 获取Set缓存
     *
     * @param key Redis键
     */
    public Set<String> getForSet(final String key) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        return setOps.members(key);
    }

}