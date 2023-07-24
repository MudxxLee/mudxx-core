package com.mudxx.component.redis.lock.script;

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
 * 如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 DECR 操作。
 * <p>
 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
 *
 * @author laiw
 * @date 2023/7/14 11:03
 */
public class RedisLuaConstants {

    /**
     * 解锁脚本，原子操作
     */
    public static final String UNLOCK_SCRIPT = "" +
            "local key = KEYS[1]\n" +
            "local value = ARGV[1]\n" +
            "if redis.call('get', key) == value\n"
            + "then\n"
            + "    return redis.call('del', key)\n"
            + "else\n"
            + "    return 0\n"
            + "end";

    /**
     * 自增脚本，原子操作
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public static final String INCR_SCRIPT = "" +
            "local key = KEYS[1]\n" +
            "local count = tonumber(ARGV[1])\n" +
            "local current = redis.call('get', key)\n" +
            "if current and tonumber(current) >= count then\n" +
            "    return 0\n" +
            "end\n" +
            "redis.call('incr', key)\n" +
            "return 1\n";

    /**
     * 自减脚本，原子操作
     * <p>
     * 1、若 key 不存在，则不执行任何操作
     * <p>
     * 2、将 key 中储存的数字值减 1，若减后的值 <= 0，则删除该key
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public static final String DECR_SCRIPT = "" +
            "local key = KEYS[1]\n" +
            "local exists = redis.call('exists', key)\n" +
            "if exists == 0 then\n" +
            "    return 0\n" +
            "end\n" +
            "local current = redis.call('decr', key)\n" +
            "if current and tonumber(current) <= 0 then\n" +
            "    redis.call('del', key)\n" +
            "end\n" +
            "return 1\n";

    /**
     * 自增脚本，原子操作
     * <p>
     * 1、若 key 存在且值 value >= maxCount，则不执行任何操作
     * <p>
     * 2、执行 INCR 操作（若 key 不存在会创建一个，那么 key 的值会先被初始化为 0，然后再执行）
     * <p>
     * 3、重新设置过期时间（当超出最大限制后，只有等 key 过期时间到了才允许执行新的请求）
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public static final String INCR_EXP_SCRIPT = "" +
            "local key = KEYS[1]\n" +
            "local expireSecond = ARGV[1]\n" +
            "local count = tonumber(ARGV[2])\n" +
            "local current = redis.call('get', key)\n" +
            "if current and tonumber(current) >= count then\n" +
            "    return 0\n" +
            "end\n" +
            "redis.call('incr', key)\n" +
            "redis.call('expire', key, expireSecond)\n" +
            "return 1\n";

    /**
     * 自减脚本，原子操作
     * <p>
     * 1、若 key 不存在，则不执行任何操作
     * <p>
     * 2、将 key 中储存的数字值减 1
     * <p>
     * 3、若 key 的未设置过期时间，则进行设置
     * <p>
     * 4、若减后的值 <= 0，则删除该key
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     */
    public static final String DECR_EXP_SCRIPT = "" +
            "local key = KEYS[1]\n" +
            "local expireSecond = ARGV[1]\n" +
            "local exists = redis.call('exists', key)\n" +
            "if exists == 0 then\n" +
            "    return 0\n" +
            "end\n" +
            "local current = redis.call('decr', key)\n" +
            "if redis.call('ttl', key) == -1 then\n" +
            "    redis.call('expire', key, expireSecond)\n" +
            "end\n" +
            "if current and tonumber(current) <= 0 then\n" +
            "    redis.call('del', key)\n" +
            "end\n" +
            "return 1\n";
}
