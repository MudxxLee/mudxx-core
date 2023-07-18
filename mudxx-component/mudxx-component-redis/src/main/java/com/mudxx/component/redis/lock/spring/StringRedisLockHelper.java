package com.mudxx.component.redis.lock.spring;

import com.mudxx.component.redis.lock.script.RedisLockScript;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author laiw
 * @date 2023/7/14 15:51
 */
public class StringRedisLockHelper {
    /**
     * 释放锁脚本
     */
    private static final RedisScript<Boolean> UNLOCK_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLockScript.UNLOCK_SCRIPT, Boolean.class);
    /**
     * redis模板
     */
    private final StringRedisTemplate stringRedisTemplate;

    public StringRedisLockHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.SECONDS.toMillis(6L));
    }

    /**
     * 轮询尝试获取分布式锁(阻塞线程)
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
     * 2. 已有锁存在，不做任何操作
     *
     * @param lockKey             锁
     * @param lockValue           锁的内容
     * @param expireSecond        锁过期时长(秒)
     * @param maxWaitTime         最大等待时间(秒)
     * @param retryIntervalMillis 重试间隔时间(毫秒)
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue, long expireSecond, long maxWaitTime, long retryIntervalMillis) {
        if (maxWaitTime <= 0) {
            throw new IllegalArgumentException("maxWaitTime must be greater than zero");
        }
        if (retryIntervalMillis <= 0) {
            throw new IllegalArgumentException("retryIntervalMillis must be greater than zero");
        }
        long start = System.currentTimeMillis();
        long end = start + TimeUnit.SECONDS.toMillis(maxWaitTime);
        try {
            while (start < end) {
                if (this.tryLock(lockKey, lockValue, expireSecond)) {
                    return true;
                }
                Thread.sleep(retryIntervalMillis);
                start = System.currentTimeMillis();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 如果捕获InterruptedException异常后不处理，会处理不当
            // 需加上Thread.currentThread.interrupt()来设置中断状态（因为抛出异常后中断标示会被清除）
            // 让外界通过判断Thread.currentThread().isInterrupted()标示来决定是否终止线程还是继续下去
            Thread.currentThread().interrupt();
        }
        return false;
    }

    /**
     * 尝试获取分布式锁
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的内容。
     * 2. 已有锁存在，不做任何操作
     *
     * @param lockKey      锁
     * @param lockValue    锁的内容
     * @param expireSecond 锁过期时长(秒)
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue, long expireSecond) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        Boolean absent = opsForValue.setIfAbsent(lockKey, lockValue, expireSecond, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(absent);
    }

    /**
     * 释放分布式锁
     * 1、用Lua语言确保操作是原子性的
     * 2、获取锁对应的value值，检查是否相等，如果相等则删除锁（解锁）
     *
     * @param lockKey   锁
     * @param lockValue 锁的内容
     * @return 是否释放成功
     */
    public boolean unLock(String lockKey, String lockValue) {
        Boolean execute = stringRedisTemplate.execute(UNLOCK_REDIS_SCRIPT, Collections.singletonList(lockKey), lockValue);
        return Boolean.TRUE.equals(execute);
    }

}
