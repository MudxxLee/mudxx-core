package com.mudxx.component.redis.lock.script;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * redis脚本命令
 * <p>
 * [自增] incr：将 key 中储存的数字值增一
 * <p>
 * 如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 INCR 操作。
 * <p>
 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
 * <p>
 * [自减] decr：将 key 中储存的数字值减一。
 * <p>
 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
 * <p>
 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
 *
 * @author laiw
 * @date 2023/7/14 11:03
 */
public class RedisLuaScript {

    /**
     * 释放锁脚本
     */
    public static final RedisScript<Boolean> UNLOCK_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.UNLOCK_SCRIPT, Boolean.class);

    /**
     * 自增脚本
     */
    public static final RedisScript<Boolean> INCR_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.INCR_SCRIPT, Boolean.class);
    /**
     * 自减脚本
     */
    public static final RedisScript<Boolean> DECR_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.DECR_SCRIPT, Boolean.class);

    /**
     * 自增脚本(过期设置)
     */
    public static final RedisScript<Boolean> INCR_EXP_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.INCR_EXP_SCRIPT, Boolean.class);
    /**
     * 自减脚本(过期设置)
     */
    public static final RedisScript<Boolean> DECR_EXP_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.DECR_EXP_SCRIPT, Boolean.class);

    /**
     * 自增备份脚本(过期设置)
     */
    public static final RedisScript<Long> INCR_BAK_REDIS_SCRIPT = new DefaultRedisScript<>(RedisLuaConstants.INCR_BAK_SCRIPT, Long.class);

}
