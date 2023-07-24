package com.mudxx.component.redis.lock.script;

/**
 * redis可重入锁
 * redis脚本命令
 * redis.call('hget', lockKey, lockValue)
 *
 * @author laiw
 * @date 2023/7/14 11:03
 */
public class RedisRLockScript {

    public static final String LOCK_SCRIPT =
            "local lockKey=KEYS[1]\n" +
            "local lockValue=ARGV[1]\n" +
            "local expireSecond=ARGV[2]\n" +
            "local exists=redis.call('exists',lockKey)\n" +
            "if exists==0 then\n" +
            "   redis.call('hset',lockKey,lockValue,1)\n" +
            "   redis.call('expire',lockKey,expireSecond)\n" +
            "   return 1\n" +
            "end\n" +
            "local value=redis.call('hget',lockKey,lockValue)\n" +
            "if value then \n" +
            "   redis.call('hincrby',lockKey,lockValue,1)\n" +
            "   redis.call('expire',lockKey,expireSecond)\n" +
            "   return 1\n" +
            "end\n" +
            "return 0\n";

    public static final String UNLOCK_SCRIPT =
            "local lockKey=KEYS[1]\n" +
            "local lockValue=ARGV[1]\n" +
            "local value=redis.call('hget',lockKey,lockValue)\n" +
            "if value then\n" +
            "   if tonumber(value)>1 then\n" +
            "       redis.call('hincrby',lockKey,lockValue,-1)\n" +
            "   else\n" +
            "       redis.call('del',lockKey)\n" +
            "   end\n" +
            "end\n" +
            "return 1";

}
