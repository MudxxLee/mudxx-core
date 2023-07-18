package com.mudxx.component.redis.lock.jedis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author laiw
 * @date 2023/7/14 15:51
 */
public class JedisLockHelper {
    /**
     * 轮询睡眠间隔时长
     */
    private static final long POLL_SLEEP_MILLIS = 50L;
    /**
     * Redis客户端
     */
    private final Jedis jedis;
    /**
     * 锁
     */
    private final String lockKey;
    /**
     * 锁的内容
     */
    private final String lockValue;
    /**
     * 超期时间
     */
    private final int expireSecond;

    public JedisLockHelper(Jedis jedis, String lockKey, String lockValue, int expireSecond) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.expireSecond = expireSecond;
    }

    /**
     * 轮询尝试获取分布式锁(阻塞线程)
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
     * 2. 已有锁存在，不做任何操作
     *
     * @param maxWaitTime         最大等待时间(秒)
     * @param retryIntervalMillis 重试间隔时间(毫秒)
     * @return 是否获取成功
     */
    public boolean tryLock(long maxWaitTime, long retryIntervalMillis) {
        if (jedis == null) {
            return false;
        }
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
                if (JedisLock.tryLock(jedis, lockKey, lockValue, expireSecond)) {
                    return true;
                }
                Thread.sleep(POLL_SLEEP_MILLIS);
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
     * 释放分布式锁并关闭jedis连接
     *
     * @return 是否释放成功
     */
    public boolean unLock() {
        try {
            return JedisLock.unLock(jedis, lockKey, lockValue);
        } finally {
            jedis.close();
        }
    }


}
