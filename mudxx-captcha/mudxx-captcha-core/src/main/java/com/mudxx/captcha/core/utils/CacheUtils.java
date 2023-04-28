package com.mudxx.captcha.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author laiwen
 */
public final class CacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 缓存最大个数
     */
    private static Integer CACHE_MAX_NUMBER = 1000;
    /**
     * 线程池
     */
    private static ScheduledExecutorService scheduledExecutor;

    /**
     * 初始化
     * @param cacheMaxNumber 缓存最大个数
     * @param second 定时任务 秒执行清除过期缓存
     */
    public static void init(int cacheMaxNumber, long second) {
        CACHE_MAX_NUMBER = cacheMaxNumber;
        if (second > 0L) {
            scheduledExecutor = new ScheduledThreadPoolExecutor(1, r ->
                    new Thread(r,"thd-captcha-cache-clean"), new ThreadPoolExecutor.CallerRunsPolicy()
            );
            /*
             * scheduleAtFixedRate是以上一次任务的开始时间为间隔的
             * 当任务执行时间大于设置的间隔时间时，下一次任务开始的时间便为上一次任务的结束时间
             */
//            scheduledExecutor.scheduleAtFixedRate(
//                    CacheUtils::refresh,
//                    10,
//                    second,
//                    TimeUnit.SECONDS);
            /*
             * scheduleWithFixedDelay是以上一次任务的结束时间为间隔的
             */
            scheduledExecutor.scheduleWithFixedDelay(
                    CacheUtils::refresh,
                    10,
                    second,
                    TimeUnit.SECONDS
            );
        }
    }

    /**
     * 缓存刷新,清除过期数据
     */
    public static void refresh(){
        try {
            logger.debug("local缓存刷新,清除过期数据");
            for (String key : CACHE_MAP.keySet()) {
                exists(key);
            }
        } catch (Exception e) {
            logger.error("local缓存刷新异常", e);
        }
    }

    public static void set(String key, String value, long expiresInSeconds){
        //设置阈值，达到即clear缓存
        if (CACHE_MAP.size() > CACHE_MAX_NUMBER * 2) {
            logger.info("CACHE_MAP达到阈值，clear map");
            clear();
        }
        CACHE_MAP.put(key, value);
        if(expiresInSeconds >0) {
            //缓存失效时间
            CACHE_MAP.put(key + "_HoldTime", System.currentTimeMillis() + expiresInSeconds * 1000);
		}
    }

    public static void delete(String key){
        CACHE_MAP.remove(key);
        CACHE_MAP.remove(key + "_HoldTime");
    }

    public static boolean exists(String key){
        Long cacheHoldTime = (Long) CACHE_MAP.get(key + "_HoldTime");
        if (cacheHoldTime == null || cacheHoldTime == 0L) {
            return false;
        }
        if (cacheHoldTime < System.currentTimeMillis()) {
            delete(key);
            return false;
        }
        return true;
    }


    public static String get(String key){
        if (exists(key)) {
            return (String)CACHE_MAP.get(key);
        }
        return null;
    }

    /**
     * 删除所有缓存
     */
    public static void clear() {
        logger.debug("have clean all key !");
        CACHE_MAP.clear();
    }
}
