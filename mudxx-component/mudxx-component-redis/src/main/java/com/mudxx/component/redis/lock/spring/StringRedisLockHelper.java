package com.mudxx.component.redis.lock.spring;

import com.mudxx.component.redis.lock.script.RedisLuaScript;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁(不可重入)
 * <p>
 * 确保锁的实现同时满足以下四个条件：
 * <p>
 * 1、互斥性：在任意时刻，只有一个客户端能持有锁；
 * <p>
 * 2、不会发生死锁：即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁；
 * <p>
 * 3、具有容错性：只要大部分的Redis节点正常运行，客户端就可以加锁和解锁；
 * <p>
 * 4、解铃还须系铃人：加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 *
 * @author laiw
 * @date 2023/7/14 15:51
 */
public class StringRedisLockHelper {
    /**
     * redis模板
     */
    private final StringRedisTemplate stringRedisTemplate;

    public StringRedisLockHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 轮询尝试获取分布式锁（阻塞线程，不公平竞争）
     * <p>
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
     * <p>
     * 2. 已有锁存在，不做任何操作
     *
     * @param lockKey             锁
     * @param lockValue           锁的内容
     * @param expireSecond        锁过期时长(秒)
     * @param maxWaitTime         最大等待时间(秒，如评估业务耗时5s，则设定为6s)
     * @param retryIntervalMillis 重试间隔时间(毫秒，推荐：[50~500] 轮询次数[内存8G、CPU4核]： 50_34~36 100_30~33 200_20~22 500_10~12)
     * @return 是否获取成功
     */
    public boolean retryLock(final String lockKey, final String lockValue, long expireSecond, long maxWaitTime, long retryIntervalMillis) {
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
     * <p>
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的内容。
     * <p>
     * 2. 已有锁存在，不做任何操作
     *
     * @param lockKey      锁
     * @param lockValue    锁的内容
     * @param expireSecond 锁过期时长(秒)
     * @return 是否获取成功
     */
    public boolean tryLock(final String lockKey, final String lockValue, long expireSecond) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        Boolean absent = opsForValue.setIfAbsent(lockKey, lockValue, expireSecond, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(absent);
    }

    /**
     * 释放分布式锁
     * <p>
     * 1、用Lua语言确保操作是原子性的
     * <p>
     * 2、获取锁对应的value值，检查是否相等，如果相等则删除锁（解锁）
     *
     * @param lockKey   锁
     * @param lockValue 锁的内容
     * @return 是否释放成功
     */
    public boolean tryUnLock(final String lockKey, final String lockValue) {
        Boolean execute = stringRedisTemplate.execute(
                RedisLuaScript.UNLOCK_REDIS_SCRIPT,
                Collections.singletonList(lockKey),
                lockValue);
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 释放分布式锁
     * <p>
     * 1、用Lua语言确保操作是原子性的
     * <p>
     * 2、获取锁对应的value值，检查是否相等，如果相等则删除锁（解锁）
     *
     * @param lockKey   锁
     * @param lockValue 锁的内容
     */
    public void unLock(final String lockKey, final String lockValue) {
        this.tryUnLock(lockKey, lockValue);
    }

}
