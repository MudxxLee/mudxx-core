package com.mudxx.component.redis.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * description: Redis模板工具类
 *
 * @author laiwen
 */
public final class RedisTemplateUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisTemplateUtils.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTemplateUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // =============================common============================

    /**
     * description: 设置缓存过期时间
     *
     * @param key  键
     * @param time 过期时间(秒)
     * @return 设置成功返回true，否则返回false
     * @date 2019-10-18 09:48:49
     */
    public Boolean expire(String key, Long time) {
        Assert.state(time > 0, "过期时间(秒)必须大于0！");
        try {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 根据key获取过期时间
     *
     * @param key 键，不能为null
     * @return 返回时间(秒)，返回0代表永不过期
     * @date 2019-10-18 09:58:22
     */
    public Long getExpire(String key) {
        Assert.notNull(key, "键不能为null！");
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * description: 判断key是否存在
     *
     * @param key 键
     * @return 如果存在返回true，否则返回false
     * @date 2019-10-18 09:59:49
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 删除缓存
     *
     * @param key 一个或多个
     * @date 2019-10-18 10:03:03
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollUtil.toCollection(Arrays.asList(key)));
            }
        }
    }

    // ============================String=============================

    /**
     * description: 获取String类型的缓存
     *
     * @param key 键，不能为null
     * @return 返回值
     * @date 2019-10-18 10:04:16
     */
    public Object get(String key) {
        Assert.notNull(key, "键不能为null！");
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * description: 添加String类型的缓存
     *
     * @param key   键
     * @param value 值
     * @return 添加成功返回true，否则返回false
     * @date 2019-10-18 10:19:05
     */
    public Boolean set(String key, Object value) {
        Assert.notNull(key, "键不能为null！");
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 添加String类型的缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)，time要大于0
     * @return 设置成功返回true，否则返回失败
     * @date 2019-10-18 10:22:06
     */
    public Boolean set(String key, Object value, Long time) {
        Assert.notNull(key, "键不能为null！");
        Assert.state(time > 0, "过期时间(秒)必须大于0！");
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 返回递增后的值
     * @date 2019-10-18 10:27:01
     */
    public Long increment(String key, Long delta) {
        Assert.notNull(key, "键不能为null！");
        Assert.state(delta > 0, "递增因子必须大于0！");
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * description: 递减
     *
     * @param key   键
     * @param delta 要减少几(大于0)
     * @return 返回递减后的值
     * @date 2019-10-18 10:36:29
     */
    public Long decrement(String key, Long delta) {
        Assert.notNull(key, "键不能为null！");
        Assert.state(delta > 0, "递减因子必须大于0！");
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 温馨提醒：该方法等同于redis的SETNX命令
     * description: 当键不存在时设置键的值，
     * 即在指定的key不存在时才会为key设置指定的值，否则不会为key执行设置操作即设置失败
     *
     * @param key   键
     * @param value 值
     * @return 设置成功返回true，设置失败返回false
     * @date 2019-12-03 15:03:48
     */
    public Boolean setIfAbsent(String key, Object value) {
        Assert.notNull(key, "键不能为null！");
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 温馨提醒：该方法等同于redis的SETNX命令
     * description: 当键不存在时设置键的值并设置过期时间，
     * 即在指定的key不存在时才会为key设置指定的值，否则不会为key执行设置操作即设置失败
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)，time要大于0
     * @return 设置成功返回true，设置失败返回false
     * @date 2019-12-03 15:03:54
     */
    public Boolean setIfAbsent(String key, Object value, Long time) {
        Assert.notNull(key, "键不能为null！");
        Assert.state(time > 0, "过期时间(秒)必须大于0！");
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 温馨提醒：该方法等同于redis的GETSET命令
     * description: 将给定key的值设为value，并返回 key的旧值(old value)，即先获取key之前的值，然后将key的值设置为value
     *
     * @param key   键
     * @param value 值
     * @return 返回key之前的值（旧值）
     * @date 2019-12-03 15:55:44
     */
    public Object getAndSet(String key, Object value) {
        Assert.notNull(key, "键不能为null！");
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    // ================================Map=================================

    /**
     * description: 获取Hash类型的缓存
     *
     * @param key   键，不能为null
     * @param field 项，不能为null
     * @return 返回值
     * @date 2019-10-18 10:53:58
     */
    public Object hget(String key, String field) {
        Assert.notNull(key, "键不能为null！");
        Assert.notNull(field, "项不能为null！");
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * description: 获取Hash类型对应的所有键值
     *
     * @param key 键
     * @return 返回对应的多个键值
     * @date 2019-10-18 11:21:46
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * description: 设置Hash类型的值
     *
     * @param key 键
     * @param map 对应多个键值
     * @return 设置成功返回true，否则返回false
     * @date 2019-10-18 11:23:14
     */
    public Boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 设置Hash类型的值并设置过期时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return 设置成功返回true，否则返回false
     * @date 2019-10-18 11:26:03
     */
    public Boolean hmset(String key, Map<String, Object> map, Long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return expire(key, time);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 向一张hash表中放入数据，如果key不存在将创建
     *
     * @param key   键
     * @param field 项
     * @param value 值
     * @return 设置成功返回true，否则返回false
     * @date 2019-10-18 11:37:16
     */
    public Boolean hset(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 向一张hash表中放入数据，如果key不存在将创建，并设置过期时间
     *
     * @param key   键
     * @param field 项
     * @param value 值
     * @param time  时间(秒) 注意：如果已存在的hash表有时间，这里将会替换原有的时间
     * @return 设置成功返回true，否则返回false
     * @date 2019-10-18 11:40:44
     */
    public Boolean hset(String key, String field, Object value, Long time) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return expire(key, time);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 根据field项删除Hash表中的值
     *
     * @param key   键，不能为null
     * @param field 项，可以是多个，不能为null
     * @date 2019-10-18 11:51:42
     */
    public void hdel(String key, Object... field) {
        Assert.notNull(key, "键不能为null！");
        Assert.notNull(field, "项可以是多个但不能为null！");
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * description: 判断Hash表中是否有该项的值
     *
     * @param key   键，不能为null
     * @param field 项，不能为null
     * @return 如果有返回true，否则返回false
     * @date 2019-10-18 11:57:48
     */
    public Boolean hHasKey(String key, String field) {
        Assert.notNull(key, "键不能为null！");
        Assert.notNull(field, "项不能为null！");
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * description: hash递增，如果不存在就会创建一个并把递增后的值返回
     *
     * @param key   键
     * @param field 项
     * @param by    递增值，即要增加几(大于0)
     * @return 返回递增后的值
     * @date 2019-10-18 13:01:48
     */
    public Double hincr(String key, String field, Double by) {
        Assert.state(by > 0, "递增值要大于0！");
        return redisTemplate.opsForHash().increment(key, field, by);
    }

    /**
     * description: hash递减，如果不存在就会创建一个并把递减后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要减少几(大于0)
     * @return 返回递减后的值
     * @date 2019-10-18 13:05:40
     */
    public Double hdecr(String key, String item, Double by) {
        Assert.state(by > 0, "递减值要大于0！");
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * description: 根据key获取Set中的所有值
     *
     * @param key 键
     * @return 返回Set中key对应的所有值
     * @date 2019-10-18 13:13:45
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * description: 判断value是否在key中存在
     *
     * @param key   键
     * @param value 值
     * @return 如果存在返回true，如果不存在返回false
     * @date 2019-10-18 13:15:57
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 将数据放入set缓存
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 返回放入Set缓存里面的值的个数
     * @date 2019-10-18 13:19:33
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * description: 将set数据放入缓存并设置过期时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值，可以是多个
     * @return 返回放入Set缓存里面的值的个数
     * @date 2019-10-18 13:22:06
     */
    public Long sSetAndTime(String key, Long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            Boolean expire = expire(key, time);
            if (expire) {
                return count;
            }
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * description: 获取set缓存的长度
     *
     * @param key 键
     * @return 返回key对应的Set的集合长度
     * @date 2019-10-18 13:26:24
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * description: 从Set缓存里面移除key中的值
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 返回移除的值的个数
     * @date 2019-10-18 13:28:41
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

    // ===============================list=================================

    /**
     * description: 获取list缓存的内容（索引0到-1代表获取所有值）
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 返回list缓存的内容
     * @date 2019-10-18 13:31:10
     */
    public List<Object> lGet(String key, Long start, Long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * description: 获取list缓存的长度
     *
     * @param key 键
     * @return 返回list缓存的长度
     * @date 2019-10-18 13:33:35
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * description: 通过索引获取list中的值
     *
     * @param key   键
     * @param index 索引，当index>=0时， 0表示第一个元素，1表示第二个元素，依次类推；当index<0时，-1表示倒数第一个元素，-2表示倒数第二个元素，依次类推
     * @return 返回根据索引从list缓存中获取的值
     * @date 2019-10-18 13:35:41
     */
    public Object lGetIndex(String key, Long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * description: 将list类型数据放入缓存（单个元素）
     *
     * @param key   键
     * @param value 值
     * @return 放入成功返回true，否则返回false
     * @date 2019-10-18 13:40:18
     */
    public Boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 将list类型数据放入缓存并设置过期时间（单个元素）
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间(秒)
     * @return 放入并设置过期时间成功返回true，否则返回false
     * @date 2019-10-18 13:54:35
     */
    public Boolean lSet(String key, Object value, Long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return expire(key, time);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 将list类型数据放入缓存（多个元素）
     *
     * @param key   键
     * @param value 值
     * @return 放入成功返回true，否则返回false
     * @date 2019-10-18 13:59:27
     */
    public Boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * description: 将list类型数据放入缓存并设置过期时间（多个元素）
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间(秒)
     * @return 放入并设置过期时间成功返回true，否则返回false
     * @date 2019-10-18 14:01:08
     */
    public Boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return expire(key, time);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 根据索引更新list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 更新成功返回true，否则返回false
     * @date 2019-10-18 14:02:31
     */
    public Boolean lUpdateIndex(String key, Long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * description: 从list集合中移除value值
     *
     * @param key   键
     * @param count 大于0按照从左到右的顺序移除count个值为value的数据；
     *              小于0按照从右到左的顺序移除-count个值为value的数据；
     *              等于0删除等于value的所有元素
     * @param value 值
     * @return 返回移除的个数
     * @date 2019-10-18 14:04:35
     */
    public Long lRemove(String key, Long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("执行异常：{}", e.getMessage(), e);
        }
        return 0L;
    }

}
