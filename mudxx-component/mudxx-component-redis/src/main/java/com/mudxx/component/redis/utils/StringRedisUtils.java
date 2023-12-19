package com.mudxx.component.redis.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * string redis 工具类
 *
 * @author laiw
 * @date 2023/3/31 10:32
 */
@SuppressWarnings({"unused"})
public class StringRedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public StringRedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /*------------------------------------------------key相关操作------------------------------------------------*/

    /**
     * 删除单个对象
     *
     * @param key 缓存键值 缓存键值
     */
    public boolean delete(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    /**
     * 批量删除key
     *
     * @param keys 缓存键值s 缓存键值
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * 序列化key
     *
     * @param key 缓存键值 缓存键值
     */
    public byte[] dump(String key) {
        return stringRedisTemplate.dump(key);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键值 缓存键值
     */
    public boolean hasKey(final String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    /**
     * 删除单个对象如果存在
     *
     * @param key 缓存键值 缓存键值
     */
    public boolean removeIfExists(final String key) {
        if (hasKey(key)) {
            return delete(key);
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param key     缓存键值     缓存键值
     * @param timeout 过期时长
     */
    public Boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key     缓存键值     缓存键值
     * @param timeout 过期时长
     * @param unit    时间单位
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key  缓存键值  缓存键值
     * @param date 过期时间
     */
    public Boolean expireAt(String key, Date date) {
        return stringRedisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern 正则
     */
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     缓存键值     缓存键值
     * @param dbIndex 数据库
     */
    public Boolean move(String key, int dbIndex) {
        return stringRedisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 缓存键值 缓存键值
     */
    public Boolean persist(String key) {
        return stringRedisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  缓存键值  缓存键值
     * @param unit 时间单位
     */
    public Long getExpire(String key, TimeUnit unit) {
        return stringRedisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 缓存键值 缓存键值
     */
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     */
    public String randomKey() {
        return stringRedisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 缓存键值
     * @param newKey 缓存键值
     */
    public void rename(String oldKey, String newKey) {
        stringRedisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 oldKey
     *
     * @param oldKey 缓存键值
     * @param newKey 缓存键值
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 缓存键值 缓存键值
     */
    public DataType type(String key) {
        return stringRedisTemplate.type(key);
    }

    /*------------------------------------------------string相关操作------------------------------------------------*/

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存键值   缓存的键值
     * @param value value 缓存的值
     */
    public void set(final String key, final String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存键值      缓存的键值
     * @param value    value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public void set(final String key, final String value, final long timeout, final TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等(单位:秒)
     *
     * @param key     缓存键值     缓存的键值
     * @param value   value   缓存的值
     * @param timeout 时间
     */
    public void set(final String key, final String value, final long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值 缓存键值
     *            缓存键值对应的数据
     */
    public String get(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 根据前缀删除符合的对象
     * keys的操作会导致数据库暂时被锁住，其他的请求都会被堵塞；业务量大的时候会出问题
     *
     * @param prefixKey 缓存键值的前缀
     */
    public long deleteKeys(final String prefixKey) {
        Set<String> keys = stringRedisTemplate.keys(prefixKey + "*");
        if (keys != null) {
            return ObjectUtil.defaultIfNull(stringRedisTemplate.delete(keys), 0L);
        }
        return 0L;
    }

    /**
     * 缓存自增+1
     * 如果key不存在会创建一个，默认value为0
     *
     * @param key 缓存键值 需自增的key
     *            自增后的值
     */
    public Long increment(final String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 缓存自减-1
     * 如果key不存在会创建一个，默认value为0
     *
     * @param key 缓存键值 需自增的key
     *            自减后的值
     */
    public Long decrement(final String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    /**
     * 缓存自增
     * 如果key不存在会创建一个，默认value为0
     *
     * @param key   缓存键值   需自增的key
     * @param value value 自增值
     *              自增后的值
     */
    public Long increment(final String key, final long value) {
        return stringRedisTemplate.opsForValue().increment(key, value);
    }

    /**
     * 缓存自减
     * 如果key不存在会创建一个，默认value为0
     *
     * @param key   缓存键值   需自增的key
     * @param value value 自减值
     *              自减后的值
     */
    public Long decrement(final String key, final long value) {
        return stringRedisTemplate.opsForValue().decrement(key, value);
    }

    /**
     * 如果不存在则设置
     * true: 表示key不存在时设置成功
     */
    public boolean setIfNotExists(final String key, final String value, final long timeout, final TimeUnit unit) {
        Boolean execute = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(
                key.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8),
                Expiration.from(timeout, unit),
                RedisStringCommands.SetOption.SET_IF_ABSENT)
        );
        return Boolean.TRUE.equals(execute);
    }

    /**
     * 如果不存在则设置(单位:秒)
     * true: 表示key不存在时设置成功
     */
    public boolean setIfNotExists(final String key, final String value, final long timeout) {
        return setIfNotExists(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间(单位:秒)
     *
     * @param key     缓存键值     Redis键
     * @param timeout 超时时间
     *                true=设置成功；false=设置失败
     */
    public boolean setExpire(final String key, final long timeout) {
        return setExpire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     缓存键值     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     *                true=设置成功；false=设置失败
     */
    public boolean setExpire(final String key, final long timeout, final TimeUnit unit) {
        return Boolean.TRUE.equals(stringRedisTemplate.expire(key, timeout, unit));
    }

    /**
     * 添加Set缓存
     *
     * @param key  缓存键值  Redis键
     * @param args 添加的元素
     */
    public void addForSet(final String key, String... args) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        setOps.add(key, args);
    }

    /**
     * 获取Set缓存
     *
     * @param key 缓存键值 Redis键
     */
    public Set<String> getForSet(final String key) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        return setOps.members(key);
    }

    /*------------------------------------------------hash相关操作------------------------------------------------*/

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   缓存键值
     * @param field 项
     */
    public Object hGet(String key, String field) {
        return stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 缓存键值
     */
    public Map<Object, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key    缓存键值
     * @param fields 项
     */
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return stringRedisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * 向一张hash表中放入数据，如果key不存在将创建
     *
     * @param key   缓存键值
     * @param field 项
     * @param value value
     */
    public void hPut(String key, String field, String value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 设置Hash类型的值
     *
     * @param key  缓存键值
     * @param maps maps
     */
    public void hPutAll(String key, Map<String, String> maps) {
        stringRedisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * description: 设置Hash类型的值并设置过期时间
     *
     * @param key  缓存键值  键
     * @param maps 对应多个键值
     * @param time 时间(秒)
     */
    public Boolean hPutAll(String key, Map<String, String> maps, Long time) {
        hPutAll(key, maps);
        return expire(key, time);
    }


    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key   缓存键值
     * @param field 项
     * @param value value
     */
    public Boolean hPutIfAbsent(String key, String field, String value) {
        return stringRedisTemplate.opsForHash().putIfAbsent(key, field, value);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key    缓存键值
     * @param fields 项
     */
    public Long hDelete(String key, Object... fields) {
        return stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key   缓存键值
     * @param field 项
     */
    public boolean hExists(String key, String field) {
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       缓存键值
     * @param field     项
     * @param increment 自增值
     */
    public Long hIncrBy(String key, Object field, long increment) {
        return stringRedisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key   缓存键值
     * @param field 项
     * @param delta 增量 增量
     */
    public Double hIncrByFloat(String key, Object field, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 缓存键值
     */
    public Set<Object> hKeys(String key) {
        return stringRedisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 缓存键值
     */
    public Long hSize(String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 缓存键值
     */
    public List<Object> hValues(String key) {
        return stringRedisTemplate.opsForHash().values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     缓存键值
     * @param options options
     */
    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForHash().scan(key, options);
    }

    /*------------------------------------------------list相关操作----------------------------------------------------*/

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   缓存键值
     * @param index 索引
     */
    public String lIndex(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   缓存键值
     * @param start start 开始位置, 0是开始位置
     * @param end   end   结束位置, -1返回所有
     */
    public List<String> lRange(String key, long start, long end) {
        return stringRedisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 存储在list头部
     *
     * @param key   缓存键值
     * @param value value
     */
    public Long lLeftPush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key   缓存键值
     * @param value value
     */
    public Long lLeftPushAll(String key, String... value) {
        return stringRedisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @param key   缓存键值
     * @param value value
     */
    public Long lLeftPushAll(String key, Collection<String> value) {
        return stringRedisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key   缓存键值
     * @param value value
     */
    public Long lLeftPushIfPresent(String key, String value) {
        return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     *
     * @param key   缓存键值
     * @param pivot pivot
     * @param value value
     */
    public Long lLeftPush(String key, String pivot, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * @param key   缓存键值
     * @param value value
     */
    public Long lRightPush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * @param key   缓存键值
     * @param value value
     */
    public Long lRightPushAll(String key, String... value) {
        return stringRedisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * @param key   缓存键值
     * @param value value
     */
    public Long lRightPushAll(String key, Collection<String> value) {
        return stringRedisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key   缓存键值
     * @param value value
     */
    public Long lRightPushIfPresent(String key, String value) {
        return stringRedisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     *
     * @param key   缓存键值
     * @param pivot pivot
     * @param value value
     */
    public Long lRightPush(String key, String pivot, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   缓存键值
     * @param index 索引 位置
     * @param value value
     */
    public void lSet(String key, long index, String value) {
        stringRedisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key 缓存键值
     *            删除的元素
     */
    public String lLeftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     缓存键值
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public String lLeftPop(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key 缓存键值
     *            删除的元素
     */
    public String lRightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     缓存键值
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public String lBRightPop(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      sourceKey
     * @param destinationKey destinationKey
     */
    public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      sourceKey
     * @param destinationKey destinationKey
     * @param timeout        timeout
     * @param unit           unit
     */
    public String lBRightPopAndLeftPush(String sourceKey, String destinationKey,
                                        long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key   缓存键值
     * @param index 索引 index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *              index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value value
     */
    public Long lRemove(String key, long index, String value) {
        return stringRedisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 裁剪list
     *
     * @param key   缓存键值
     * @param start start start
     * @param end   end   end
     */
    public void lTrim(String key, long start, long end) {
        stringRedisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 缓存键值
     */
    public Long lLen(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /*------------------------------------------------set相关操作------------------------------------------------*/

    /**
     * set添加元素
     *
     * @param key    缓存键值
     * @param values values
     */
    public Long sAdd(String key, String... values) {
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key    缓存键值
     * @param values values
     */
    public Long sRemove(String key, Object... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 缓存键值
     */
    public String sPop(String key) {
        return stringRedisTemplate.opsForSet().pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key     缓存键值
     * @param value   value
     * @param destKey 目的缓存键值
     */
    public Boolean sMove(String key, String value, String destKey) {
        return stringRedisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 缓存键值
     */
    public Long sSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key   缓存键值
     * @param value value
     */
    public Boolean sIsMember(String key, Object value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     */
    public Set<String> sIntersect(String key, String otherKey) {
        return stringRedisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     */
    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     * @param destKey  目的缓存键值
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     * @param destKey   目的缓存键值
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
        return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     */
    public Set<String> sUnion(String key, String otherKeys) {
        return stringRedisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     */
    public Set<String> sUnion(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     * @param destKey  目的缓存键值
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     * @param destKey   目的缓存键值
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys,
                               String destKey) {
        return stringRedisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     */
    public Set<String> sDifference(String key, String otherKey) {
        return stringRedisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     */
    public Set<String> sDifference(String key, Collection<String> otherKeys) {
        return stringRedisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     * @param destKey  目的缓存键值
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     * @param destKey   目的缓存键值
     */
    public Long sDifference(String key, Collection<String> otherKeys,
                            String destKey) {
        return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 缓存键值
     */
    public Set<String> setMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 缓存键值
     */
    public String sRandomMember(String key) {
        return stringRedisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     *
     * @param key   缓存键值
     * @param count count
     */
    public List<String> sRandomMembers(String key, long count) {
        return stringRedisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param key   缓存键值
     * @param count count
     */
    public Set<String> sDistinctRandomMembers(String key, long count) {
        return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * @param key     缓存键值
     * @param options options
     */
    public Cursor<String> sScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForSet().scan(key, options);
    }

    /*------------------------------------------------zSet相关操作------------------------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   缓存键值
     * @param value value
     * @param score score
     */
    public Boolean zAdd(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key    缓存键值
     * @param values values
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
        return stringRedisTemplate.opsForZSet().add(key, values);
    }

    /**
     * @param key    缓存键值
     * @param values values
     */
    public Long zRemove(String key, Object... values) {
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key   缓存键值
     * @param value value
     * @param delta 增量
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key   缓存键值
     * @param value value 0表示第一位
     */
    public Long zRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param key   缓存键值
     * @param value value
     */
    public Long zReverseRank(String key, Object value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   缓存键值
     * @param start start 开始位置
     * @param end   end   结束位置, -1查询所有
     */
    public Set<String> zRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key   缓存键值
     * @param start start
     * @param end   end
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start,
                                                                   long end) {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key 缓存键值
     * @param min min 最小值
     * @param max max 最大值
     */
    public Set<String> zRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key 缓存键值
     * @param min min 最小值
     * @param max max 最大值
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                          double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key   缓存键值
     * @param min   min
     * @param max   max
     * @param start start
     * @param end   end
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                          double min, double max, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
                start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key   缓存键值
     * @param start start
     * @param end   end
     */
    public Set<String> zReverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param key   缓存键值
     * @param start start
     * @param end   end
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key,
                                                                          long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start,
                end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key 缓存键值
     * @param min min
     * @param max max
     */
    public Set<String> zReverseRangeByScore(String key, double min,
                                            double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key 缓存键值
     * @param min min
     * @param max max
     */
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeByScoreWithScores(
            String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
                min, max);
    }

    /**
     * @param key   缓存键值
     * @param min   min
     * @param max   max
     * @param start start
     * @param end   end
     */
    public Set<String> zReverseRangeByScore(String key, double min,
                                            double max, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
                start, end);
    }

    /**
     * 根据score值获取集合元素数量
     *
     * @param key 缓存键值
     * @param min min
     * @param max max
     */
    public Long zCount(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取集合大小
     *
     * @param key 缓存键值
     */
    public Long zSize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取集合大小
     *
     * @param key 缓存键值
     */
    public Long zZCard(String key) {
        return stringRedisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取集合中value元素的score值
     *
     * @param key   缓存键值
     * @param value value
     */
    public Double zScore(String key, Object value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   缓存键值
     * @param start start
     * @param end   end
     */
    public Long zRemoveRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param key 缓存键值
     * @param min min min
     * @param max max max
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     * @param destKey  目的缓存键值
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     * @param destKey   目的缓存键值
     */
    public Long zUnionAndStore(String key, Collection<String> otherKeys,
                               String destKey) {
        return stringRedisTemplate.opsForZSet()
                .unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 交集
     *
     * @param key      缓存键值
     * @param otherKey 缓存键值
     * @param destKey  目的缓存键值
     */
    public Long zIntersectAndStore(String key, String otherKey,
                                   String destKey) {
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * 交集
     *
     * @param key       缓存键值
     * @param otherKeys 缓存键值
     * @param destKey   目的缓存键值
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
        return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * @param key     缓存键值
     * @param options options
     */
    public Cursor<ZSetOperations.TypedTuple<String>> zScan(String key, ScanOptions options) {
        return stringRedisTemplate.opsForZSet().scan(key, options);
    }

}