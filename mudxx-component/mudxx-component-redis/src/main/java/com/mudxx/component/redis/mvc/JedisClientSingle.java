package com.mudxx.component.redis.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * jedis客户端单机版实现类
 *
 * @author laiwen
 */
public class JedisClientSingle implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget(key, field);
        jedis.close();
        return value;
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key, field, value);
        jedis.close();
        return result;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long value = jedis.incr(key);
        jedis.close();
        return value;
    }

    @Override
    public Long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long value = jedis.decr(key);
        jedis.close();
        return value;
    }

    @Override
    public Long expire(String key, Integer seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, seconds);
        jedis.close();
        return result;
    }

    @Override
    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long seconds = jedis.ttl(key);
        jedis.close();
        return seconds;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String[] fields, String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, fields);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String key, String... fields) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, fields);
        jedis.close();
        return result;
    }

    @Override
    public Long setnx(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.setnx(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.getSet(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long lpush(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.lpush(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long lpushx(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.lpushx(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long rpush(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.rpush(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long rpushx(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.rpushx(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.llen(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.lrange(key, start, stop);
        jedis.close();
        return result;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.lrem(key, count, value);
        jedis.close();
        return result;
    }

    @Override
    public String ltrim(String key, long start, long stop) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.ltrim(key, start, stop);
        jedis.close();
        return result;
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.lpop(key);
        jedis.close();
        return result;
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.rpop(key);
        jedis.close();
        return result;
    }

}
