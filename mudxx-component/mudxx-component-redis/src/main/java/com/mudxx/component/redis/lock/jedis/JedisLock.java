package com.mudxx.component.redis.lock.jedis;

import com.mudxx.component.redis.lock.script.RedisLockScript;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * Redis分布式锁(不可重入)
 * 确保锁的实现同时满足以下四个条件：
 * 1、互斥性：在任意时刻，只有一个客户端能持有锁；
 * 2、不会发生死锁：即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁；
 * 3、具有容错性：只要大部分的Redis节点正常运行，客户端就可以加锁和解锁；
 * 4、解铃还须系铃人：加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 *
 * @author lw
 * @date 2023-07-14 15:39
 */
public class JedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long UNLOCK_SUCCESS = 1L;
    /**
     * 只在键不存在时，才对键进行设置操作。 SET key value NX 效果等同于 SETNX key value
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 设置键的过期时间为 millisecond 毫秒。 SET key value PX millisecond 效果等同于 PSETEX key millisecond value
     */
    private static final String SET_EXPIRE_TIME_PX = "PX";
    /**
     * 设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key second value
     */
    private static final String SET_EXPIRE_TIME_EX = "EX";


    /**
     * 尝试获取分布式锁
     * 1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的内容。
     * 2. 已有锁存在，不做任何操作
     *
     * @param jedis        Redis客户端
     * @param lockKey      锁
     * @param lockValue    锁的内容
     * @param expireSecond 超期时间(秒)
     * @return 是否获取成功
     */
    public static boolean tryLock(Jedis jedis, String lockKey, String lockValue, int expireSecond) {
        /*
         * 第一个为key：使用key来当锁，因为key是唯一的；
         * 第二个为value：为何需要设置value？原因就是分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值，在解锁的时候就可以有依据；
         *  lockValue 可以使用UUID.randomUUID().toString()方法生成。
         * 第三个为nxxx：这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
         * 第四个为expx：这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
         * 第五个为time：与第四个参数相呼应，代表key的过期时间。
         */
        String result = jedis.set(lockKey, lockValue, SET_IF_NOT_EXIST, SET_EXPIRE_TIME_EX, expireSecond);
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     * 1、用Lua语言确保操作是原子性的
     * 2、获取锁对应的value值，检查是否相等，如果相等则删除锁（解锁）
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param lockValue 锁的内容
     * @return 是否释放成功
     */
    public static boolean unLock(Jedis jedis, String lockKey, String lockValue) {
        Object result = jedis.eval(RedisLockScript.UNLOCK_SCRIPT, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
        return UNLOCK_SUCCESS.equals(result);
    }
}
