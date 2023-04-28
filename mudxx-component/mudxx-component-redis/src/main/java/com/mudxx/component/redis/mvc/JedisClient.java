package com.mudxx.component.redis.mvc;

import java.util.List;

/**
 * @author laiwen
 * 为了适配JedisPool（单机版）和JedisCluster（集群版）而定义的接口
 */
public interface JedisClient {

    /**
     * 获取key对应的value（String类型）
     *
     * @param key 待获取的key
     * @return 返回key对应的value
     */
    String get(String key);

    /**
     * 给key设置value（String类型）
     *
     * @param key   待设置的key
     * @param value 待设置的值
     * @return 返回设置状态，如果为OK表示设置成功
     */
    String set(String key, String value);

    /**
     * 获取key对应的value（Hash类型）
     *
     * @param key   待获取的key
     * @param field 待获取的field
     * @return 返回field对应的value
     */
    String hget(String key, String field);

    /**
     * 给key设置value（Hash类型）
     *
     * @param key   待设置的key
     * @param field 待设置的field
     * @param value 待设置的值
     * @return 返回设置状态，如果为1表示设置成功
     */
    Long hset(String key, String field, String value);

    /**
     * 使key的值自增1
     *
     * @param key 待自增的key
     * @return 返回自增后的值
     */
    Long incr(String key);

    /**
     * 使key的值自减1
     *
     * @param key 待自减的key
     * @return 返回自减后的值
     */
    Long decr(String key);

    /**
     * 给key设置有效期
     *
     * @param key     待设置的key
     * @param seconds 有效期，单位是秒
     * @return 返回设置状态，如果为1表示设置成功
     */
    Long expire(String key, Integer seconds);

    /**
     * 查看key的有效期
     *
     * @param key 待查看的key
     * @return 返回key的有效期，如果为-1表示永久有效，如果为-2表示已失效，如果为其他表示还未失效
     */
    Long ttl(String key);

    /**
     * 删除指定的key（String类型）（缓存同步）
     *
     * @param key 待删除的key
     * @return 返回删除状态，如果为1表示删除成功
     */
    Long del(String key);

    /**
     * 删除指定的key的指定field（单个）（Hash类型）（缓存同步）
     *
     * @param key   待删除的key
     * @param field 待删除的field
     * @return 返回删除状态，如果为1表示删除成功
     */
    Long hdel(String key, String field);

    /**
     * 删除指定的key的指定fields（多个）（Hash类型）（缓存同步）
     *
     * @param fields 待删除的fields
     * @param key    待删除的key
     * @return 返回删除状态，如果为1表示删除成功
     */
    Long hdel(String[] fields, String key);

    /**
     * 删除指定的key的指定fields（单个或者多个）（Hash类型）（缓存同步）
     *
     * @param key    待删除的key
     * @param fields 待删除的fields
     * @return 返回删除状态，如果为1表示删除成功
     */
    Long hdel(String key, String... fields);

    /**
     * 只在键key不存在的情况下，将键key的值设置为value，若键key已经存在，则不做任何动作
     *
     * @param key   待设置的key
     * @param value 待设置的值
     * @return 设置成功返回1，设置失败返回0
     */
    Long setnx(String key, String value);

    /**
     * 将给定key的值设为value，并返回key的旧值(old value)
     *
     * @param key   键
     * @param value 值
     * @return 返回key之前的值（旧值）
     */
    String getSet(String key, String value);

    /**
     * 提醒：列表允许重复元素
     * 将一个或多个值插入到列表头部
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：
     * 比如说，对空列表mylist执行命令lpush mylist a b c ，列表的值将是 c b a，
     * 这等同于原子性地执行lpush mylist a 、 lpush mylist b 和 lpush mylist c三个命令。
     * 如果key不存在，一个空列表会被创建并执行lpush操作。
     * 当key存在但不是列表类型时，返回一个错误。
     *
     * @param key   键
     * @param value 值：可变字符串数组
     * @return 返回执行lpush命令后，列表的长度。
     */
    Long lpush(String key, String... value);

    /**
     * 提醒：列表允许重复元素
     * 将值value插入到列表key的表头，当且仅当 key存在并且是一个列表。
     * 和lpush key value [value …]命令相反，当 key不存在时， lpushx命令什么也不做。
     *
     * @param key   键
     * @param value 值：可变字符串数组
     * @return 返回lpushx命令执行之后，列表的长度。
     */
    Long lpushx(String key, String... value);

    /**
     * 提醒：列表允许重复元素
     * 在列表中添加一个或多个值，将一个或多个值 value 插入到列表 key的表尾(最右边)。
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：
     * 比如对一个空列表 mylist 执行 rpush mylist a b c ，得出的结果列表为 a b c ，
     * 等同于执行命令 rpush mylist a 、 rpush mylist b 、 rpush mylist c 。
     * 如果 key 不存在，一个空列表会被创建并执行 rpush 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key   键
     * @param value 值：可变字符串数组
     * @return 返回执行rpush操作后，列表的长度。
     */
    Long rpush(String key, String... value);

    /**
     * 提醒：列表允许重复元素
     * 将值value插入到列表 key的表尾，当且仅当 key存在并且是一个列表。
     * 和rpush key value [value …]命令相反，当 key不存在时， rpushx命令什么也不做。
     *
     * @param key   键
     * @param value 值：可变字符串数组
     * @return 返回rpushx命令执行之后，列表的长度。
     */
    Long rpushx(String key, String... value);

    /**
     * 获取列表key的长度
     * 如果 key不存在，则 key被解释为一个空列表，返回 0 .
     * 如果 key不是列表类型，返回一个错误。
     *
     * @param key 键
     * @return 返回列表 key的长度。
     */
    Long llen(String key);

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 超出范围的下标值不会引起错误。
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     *
     * @param key   键
     * @param start 起始下标（索引）（偏移量）
     * @param stop  截止下标（索引）（偏移量）
     * @return 返回一个列表，包含指定区间内的元素。
     */
    List<String> lrange(String key, long start, long stop);

    /**
     * 根据参数 count 的值，移除列表中与参数value相等的元素
     * count 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     *
     * @param key   键
     * @param count 数目
     * @param value 值
     * @return 返回被移除元素的数量。 因为不存在的key被视作空表(empty list)，所以当 key不存在时， lrem命令总是返回 0 。
     */
    Long lrem(String key, long count, String value);

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key   键
     * @param start 起始下标（索引）（偏移量）
     * @param stop  截止下标（索引）（偏移量）
     * @return 命令执行成功时，返回OK
     */
    String ltrim(String key, long start, long stop);

    /**
     * 移除并获取列表的第一个元素
     *
     * @param key 键
     * @return 返回值为移除的元素，当列表key不存在时，返回null
     */
    String lpop(String key);

    /**
     * 移除并获取列表的最后一个元素
     *
     * @param key 键
     * @return 返回值为移除的元素，当列表key不存在时，返回null
     */
    String rpop(String key);

}
