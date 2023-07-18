package com.mudxx.component.redis.mvc;

import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * jedis客户端集群版实现类
 *
 * @author laiwen
 */
public class JedisClientCluster implements JedisClient {

    private final JedisCluster jedisCluster;

    public JedisClientCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public Long decr(String key) {
        return jedisCluster.decr(key);
    }

    @Override
    public Long expire(String key, Integer seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    @Override
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public Long hdel(String key, String field) {
        return jedisCluster.hdel(key, field);
    }

    @Override
    public Long hdel(String[] fields, String key) {
        return jedisCluster.hdel(key, fields);
    }

    @Override
    public Long hdel(String key, String... fields) {
        return jedisCluster.hdel(key, fields);
    }

    @Override
    public Long setnx(String key, String value) {
        return jedisCluster.setnx(key, value);
    }

    @Override
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    @Override
    public Long lpush(String key, String... value) {
        return jedisCluster.lpush(key, value);
    }

    @Override
    public Long lpushx(String key, String... value) {
        return jedisCluster.lpushx(key, value);
    }

    @Override
    public Long rpush(String key, String... value) {
        return jedisCluster.rpush(key, value);
    }

    @Override
    public Long rpushx(String key, String... value) {
        return jedisCluster.rpushx(key, value);
    }

    @Override
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        return jedisCluster.lrange(key, start, stop);
    }

    @Override
    public Long lrem(String key, long count, String value) {
        return jedisCluster.lrem(key, count, value);
    }

    @Override
    public String ltrim(String key, long start, long stop) {
        return jedisCluster.ltrim(key, start, stop);
    }

    @Override
    public String lpop(String key) {
        return jedisCluster.lpop(key);
    }

    @Override
    public String rpop(String key) {
        return jedisCluster.rpop(key);
    }

}
