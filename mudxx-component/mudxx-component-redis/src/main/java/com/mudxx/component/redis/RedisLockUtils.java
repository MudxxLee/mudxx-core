package com.mudxx.component.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description: redis分布式锁
 *
 * @author laiwen
 */
@Component
public class RedisLockUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisLockUtils.class);

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    /**
     * description: 加锁
     *
     * @param key   键
     * @param value 值：当前时间 + 超时时间
     * @return 如果加锁成功返回true，否则返回false
     * @date 2019-12-03 10:36:31
     */
    public Boolean lock(String key, String value) {
        //判断是否已经有线程加了锁，如果没有加锁，那么进行加锁，同时返回true，否则进行下一步
        if (redisTemplateUtils.setIfAbsent(key, value)) {
            //加锁成功，放行业务逻辑代码
            return true;
        }
        //利用超时时间解决死锁的问题
        //假如当前线程currentValue=A，这时同时来了两个线程，这两个线程的value都是B
        String currentValue = redisTemplateUtils.get(key).toString();
        //如果锁已经过期（currentValue小于当前时间）
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁，同时加新的锁
            //getAndSet方法一次只允许一个线程去执行，多线程执行该方法存在先后顺序，即体现了redis的单线程原理
            //也就是说多线程的情况下也只会有一个线程拿到锁
            String oldValue = redisTemplateUtils.getAndSet(key, value).toString();
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
     *
     * @param key   键
     * @param value 值
     * @date 2019-12-03 10:36:39
     */
    public void unlock(String key, String value) {
        try {
            //获取当前锁
            String currentValue = redisTemplateUtils.get(key).toString();
            //如果入参就是当前锁
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                //删除当前锁，即进行解锁
                redisTemplateUtils.delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常，{}", e.getMessage(), e);
        }
    }

}
