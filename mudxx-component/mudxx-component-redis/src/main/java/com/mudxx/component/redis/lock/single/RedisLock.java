package com.mudxx.component.redis.lock.single;

import com.mudxx.component.redis.lock.script.RedisLuaConstants;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁
 *
 * @author lw
 * @date 2021年1月18日
 */
public class RedisLock {

    private static final int SLEEP_MILLIS = 50;

    private final StringRedisTemplate redisTemplate;

    public RedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加锁，有阻塞
     *
     * @param key     锁
     * @param expire  锁过期时长
     * @param timeout 获取锁最大超时时长
     * @return 锁内容
     */
    public String lock(String key, long expire, long timeout) {
        long startTime = System.currentTimeMillis();
        String token;
        do {
            token = tryLock(key, expire);
            if (token == null) {
                if ((System.currentTimeMillis() - startTime) > (timeout - SLEEP_MILLIS)) {
                    break;
                }
                try {
                    //try 50 per sec
                    Thread.sleep(SLEEP_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } while (token == null);

        return token;
    }

    /**
     * 加锁，无阻塞
     *
     * @param key    锁
     * @param expire 锁过期时长
     * @return 锁内容
     */
    public String tryLock(String key, long expire) {
        String token = UUID.randomUUID().toString();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            return null;
        }
        RedisConnection conn = factory.getConnection();
        try {
            Boolean result = conn.set(key.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8),
                    Expiration.from(expire, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            if (Boolean.TRUE.equals(result)) {
                return token;
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory, false);
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param key   锁
     * @param token 锁内容
     * @return 是否成功
     */
    public boolean unlock(String key, String token) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = key.getBytes(StandardCharsets.UTF_8);
        keysAndArgs[1] = token.getBytes(StandardCharsets.UTF_8);
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (factory == null) {
            return false;
        }
        RedisConnection conn = factory.getConnection();
        try {
            Long result = conn.scriptingCommands().eval(RedisLuaConstants.UNLOCK_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.INTEGER, 1, keysAndArgs);
            if (result != null && result > 0) {
                return true;
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory, false);
        }
        return false;
    }

}
