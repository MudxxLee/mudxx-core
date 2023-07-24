package com.mudxx.component.redis.lock.spring;

import com.mudxx.component.redis.lock.script.RedisLuaScript;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis限流
 *
 * @author laiw
 * @date 2023/7/14 15:51
 */
public class StringRedisFcyHelper {

    /**
     * redis模板
     */
    private final StringRedisTemplate stringRedisTemplate;

    public StringRedisFcyHelper(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 轮询尝试自增（阻塞线程，不公平竞争）
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     *
     * @param key                 键
     * @param maxCount            最大并发请求数
     * @param maxWaitTime         最大等待时间(秒，如评估业务耗时5s，则设定为6s)
     * @param retryIntervalMillis 重试间隔时间(毫秒，推荐：[50~500] 轮询次数[内存8G、CPU4核]： 50_34~36 100_30~33 200_20~22 500_10~12)
     * @return 是否获取成功
     */
    public boolean retryIncr(final String key, int maxCount, long maxWaitTime, long retryIntervalMillis) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount must be greater than zero");
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
            do {
                if (this.tryIncr(key, maxCount)) {
                    return true;
                }
                Thread.sleep(retryIntervalMillis);
                start = System.currentTimeMillis();
            } while (start < end);
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
     * 尝试自增(用Lua语言确保操作是原子性的)
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     *
     * @param key      键
     * @param maxCount 最大并发请求数
     * @return 是否自增成功
     */
    public boolean tryIncr(final String key, final int maxCount) {
        Boolean execute = stringRedisTemplate.execute(
                RedisLuaScript.INCR_REDIS_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(maxCount));
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 尝试自减(用Lua语言确保操作是原子性的)
     * <p>
     * 1、若 key 不存在，则不执行任何操作
     * <p>
     * 2、将 key 中储存的数字值减 1，若减后的值 <= 0，则删除该key
     *
     * @param key 键
     * @return 是否自减成功
     */
    public boolean tryDecr(final String key) {
        Boolean execute = stringRedisTemplate.execute(
                RedisLuaScript.DECR_REDIS_SCRIPT,
                Collections.singletonList(key));
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 轮询尝试自增（阻塞线程，不公平竞争）
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     * <p>
     * 3、重新设置过期时间（当超出最大限制后，只有等 key 过期时间到了才允许执行新的请求）
     *
     * @param key                 键
     * @param expireSecond        键过期时长(秒)
     * @param maxCount            最大并发请求数
     * @param maxWaitTime         最大等待时间(秒，如评估业务耗时5s，则设定为6s)
     * @param retryIntervalMillis 重试间隔时间(毫秒，推荐：[50~500] 轮询次数[内存8G、CPU4核]： 50_34~36 100_30~33 200_20~22 500_10~12)
     * @return 是否获取成功
     */
    public boolean retryIncrAndExpire(final String key, long expireSecond, int maxCount, long maxWaitTime, long retryIntervalMillis) {
        if (expireSecond <= 0) {
            throw new IllegalArgumentException("expireSecond must be greater than zero");
        }
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount must be greater than zero");
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
            do {
                if (this.tryIncrAndExpire(key, expireSecond, maxCount)) {
                    return true;
                }
                Thread.sleep(retryIntervalMillis);
                start = System.currentTimeMillis();
            } while (start < end);
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
     * 尝试自增(用Lua语言确保操作是原子性的)
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     * <p>
     * 3、重新设置过期时间（当超出最大限制后，只有等 key 过期时间到了才允许执行新的请求）
     *
     * @param key          键
     * @param expireSecond 键过期时长(秒)
     * @param maxCount     最大并发请求数
     * @return 是否自增成功
     */
    public boolean tryIncrAndExpire(final String key, final long expireSecond, final int maxCount) {
        Boolean execute = stringRedisTemplate.execute(
                RedisLuaScript.INCR_EXP_REDIS_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(expireSecond),
                String.valueOf(maxCount));
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 尝试自减(用Lua语言确保操作是原子性的)
     * <p>
     * 1、若 key 不存在，则不执行任何操作
     * <p>
     * 2、将 key 中储存的数字值减 1
     * <p>
     * 3、若 key 的未设置过期时间，则进行设置
     * <p>
     * 4、若减后的值 <= 0，则删除该key
     *
     * @param key          键
     * @param expireSecond 键过期时长(秒)
     * @return 是否自减成功
     */
    public boolean tryDecrAndExpire(final String key, final long expireSecond) {
        Boolean execute = stringRedisTemplate.execute(
                RedisLuaScript.DECR_EXP_REDIS_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(expireSecond));
        return Boolean.TRUE.equals(execute);
    }

}
