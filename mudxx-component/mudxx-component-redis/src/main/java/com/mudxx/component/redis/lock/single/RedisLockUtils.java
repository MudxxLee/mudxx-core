package com.mudxx.component.redis.lock.single;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * description: redis分布式锁
 *
 * @author laiwen
 */
public class RedisLockUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisLockUtils.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisLockUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * description: 加锁
     * <p>
     * 存在问题:
     * 1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
     * 2. 当锁过期的时候，如果多个客户端同时执行getAndSet()方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖
     * (即虽然是被线程C锁成功了,如果存在其他线程在线程C后面执行过getAndSet()方法,会导致线程C存在redis的value被改变)。
     * 3. 锁不具备拥有者标识，即任何客户端都可以解锁。
     *
     * @param key   键
     * @param value 值：当前时间 + 超时时间
     * @return 如果加锁成功返回true，否则返回false
     * @date 2019-12-03 10:36:31
     */
    public Boolean lock(String key, String value) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        //判断是否已经有线程加了锁，如果没有加锁，那么进行加锁，同时返回true，否则进行下一步
        if (opsForValue.setIfAbsent(key, value)) {
            //加锁成功，放行业务逻辑代码
            return true;
        }
        //利用超时时间解决死锁的问题
        //假如当前线程currentValue=A，这时同时来了两个线程，这两个线程的value都是B
        String currentValue = opsForValue.get(key).toString();
        //如果锁已经过期（currentValue小于当前时间）
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁，同时加新的锁
            //getAndSet方法一次只允许一个线程去执行，多线程执行该方法存在先后顺序，即体现了redis的单线程原理
            //也就是说多线程的情况下也只会有一个线程拿到锁
            String oldValue = opsForValue.getAndSet(key, value).toString();
            //第一个线程进来的时候oldValue=currentValue(A=A)，第二个线程进来的时候oldValue!=currentValue(B!=A)
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                //加锁成功，放行业务逻辑代码
                return true;
            }
        }
        //加锁失败，不放行业务逻辑代码
        return false;
    }

    /**
     * description: 解锁
     * 存在问题:
     * 如果调用delete()方法的时候，这把锁已经不属于当前客户端的时候会解除他人加的锁。
     * 那么是否真的有这种场景？答案是肯定的，比如客户端A加锁，一段时间之后客户端A解锁，在执行delete()之前，锁突然过期了，此时客户端B尝试加锁成功，然后客户端A再执行delete()方法，则将客户端B的锁给解除了。
     *
     * @param key   键
     * @param value 值
     * @date 2019-12-03 10:36:39
     */
    public void unlock(String key, String value) {
        try {
            ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
            //获取当前锁
            String currentValue = opsForValue.get(key).toString();
            //如果入参就是当前锁
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                //删除当前锁，即进行解锁
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常，{}", e.getMessage(), e);
        }
    }

}
