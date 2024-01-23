package com.mudxx.common.utils.id;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 网上的教程一般存在两个问题：
 * 1. 机器ID（5位）和数据中心ID（5位）配置没有解决，分布式部署的时候会使用相同的配置，任然有ID重复的风险。
 * 2. 使用的时候需要实例化对象，没有形成开箱即用的工具类。
 * <p>
 * 针对上面两个问题进行解决，解决方案是，workId使用服务器hostName生成，
 * dataCenterId使用IP生成，这样可以最大限度防止10位机器码重复，但是由于两个ID都不能超过32，
 * 只能取余数，还是难免产生重复，但是实际使用中，hostName和IP的配置一般连续或相近，
 * 只要不是刚好相隔32位，就不会有问题，况且，hostName和IP同时相隔32的情况更加是几乎不可能
 * 的事，平时做的分布式部署，一般也不会超过10台容器。使用上面的方法可以零配置使用雪花算法，
 * 雪花算法10位机器码的设定理论上可以有1024个节点，生产上使用docker配置一般是一次编译，
 * 然后分布式部署到不同容器，不会有不同的配置，这里不知道其他公司是如何解决的，即使有方法
 * 使用一套配置，然后运行时根据不同容器读取不同的配置，但是给每个容器编配ID，1024个
 * （大部分情况下没有这么多），似乎也不太可能，此问题留待日后解决后再行补充。
 *
 * @author laiwen
 * @date 2020/11/18 16:53
 * 全局分布式ID配置（雪花算法）交给spring管理
 */
public class SnowflakeIdConfig {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdConfig.class);

    @Value("${snowflake.id.workId:noWorkId}")
    private String workId;

    @Value("${snowflake.id.dataCenterId:noDataCenterId}")
    private String dataCenterId;

    public SnowflakeIdConfig() {
    }

    public SnowflakeIdUtil snowflakeId() {
        // 这里还是采用的是上面的工具类，静态加载的配置可以删掉，也可以不删除，因为这里已经重新new对象了，不是同一个对象
        return new SnowflakeIdUtil(getWorkId(workId), getDataCenterId(dataCenterId));
    }

    /**
     * workId配置
     *
     * @param workId workId
     * @return workId
     */
    private long getWorkId(String workId) {
        logger.info("SnowflakeIdConfig workId : {}", workId);
        if ("noWorkId".equalsIgnoreCase(workId)) {
            return getWorkId();
        }
        return num2Model(string2Integer(workId));
    }

    /**
     * dataCenterId配置
     *
     * @param dataCenterId 数据中心ID
     * @return dataCenterId
     */
    private long getDataCenterId(String dataCenterId) {
        logger.info("SnowflakeIdConfig dataCenterId : {}", dataCenterId);
        if ("noDataCenterId".equalsIgnoreCase(dataCenterId)) {
            return getDataCenterId();
        }
        return num2Model(string2Integer(dataCenterId));
    }

    /**
     * 获取数据中心ID（机器ID）
     *
     * @return 采用本地机器名称取32的模（降低分布式部署工作ID相同）
     */
    private long getDataCenterId() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            List<Integer> tenNums = string2Integer(hostName);
            if (CollectionUtils.isEmpty(tenNums)) {
                return localRandom.nextInt(0, 31);
            }
            return num2Model(tenNums);
        } catch (UnknownHostException e) {
            return localRandom.nextInt(0, 31);
        }
    }


    /**
     * 获取工作工厂ID
     *
     * @return 采用本地IP地址取32的模（降低分布式部署工作ID相同）
     */
    private long getWorkId() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            List<Integer> tenNums = string2Integer(ip);
            if (CollectionUtils.isEmpty(tenNums)) {
                // IP为空，则采用随机数指定工作ID
                return localRandom.nextInt(0, 31);
            }
            return num2Model(tenNums);
        } catch (UnknownHostException e) {
            // ip 获取失败，则采用随机数指定工作ID
            return localRandom.nextInt(0, 31);
        }
    }

    /**
     * 字符串转成10进制的数字集合
     *
     * @param str 字符串
     * @return 10进制的数字集合（这里的是将字符转成对应的ASICC码）
     */
    private List<Integer> string2Integer(String str) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }
        List<Integer> integers = Lists.newArrayListWithCapacity(str.length());
        for (int i = 0; i < str.length(); i++) {
            integers.add(Integer.valueOf(Integer.toString(str.charAt(i), 10)));
        }
        return integers;
    }

    /**
     * 求和取模32
     *
     * @param nums 数字集合
     * @return long类型
     */
    private long num2Model(List<Integer> nums) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        if (CollectionUtils.isEmpty(nums)) {
            return localRandom.nextInt(0, 31);
        }
        int sums = 0;
        for (int i = 0; i < nums.size(); i++) {
            sums += nums.get(i);
        }
        return (long) (sums % 32);
    }

}