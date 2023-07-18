package com.mudxx.component.redis.lock.script;

/**
 * redis脚本命令
 * redis.call('hget', key, lockKey)
 *
 *
 *
 * @author laiw
 * @date 2023/7/14 11:03
 */
public class RedisLockScript {

    /**
     * 解锁脚本，原子操作
     */
    public static final String UNLOCK_SCRIPT =
            "local lockKey=KEYS[1]\n" +
            "local lockValue=ARGV[1]\n" +
            "if redis.call(\"get\",lockKey) == lockValue\n"
                    + "then\n"
                    + "    return redis.call(\"del\",lockKey)\n"
                    + "else\n"
                    + "    return 0\n"
                    + "end";

}
