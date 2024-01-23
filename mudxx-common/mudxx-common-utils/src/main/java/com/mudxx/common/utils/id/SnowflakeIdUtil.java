package com.mudxx.common.utils.id;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * @author laiwen
 * @date 2020/11/18 10:14
 * 雪花算法分布式唯一ID生成工具
 */
public class SnowflakeIdUtil {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdUtil.class);

    /**
     * 开始时间截 (2000-01-01，一旦确定不可更改，否则时间被回调，或者改变，可能会造成id重复或冲突)
     */
    private final long startTimeStamp = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0)
            .atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();

    /**
     * 机器ID所占位数
     */
    private final long workIdBits = 5L;

    /**
     * 数据标志Id所占位数
     */
    private final long dataCenterIdBits = 5L;

    /**
     * 序列号ID所占位数
     */
    private final long sequenceBits = 12L;

    /**
     * 支持的机器最大ID，结果是31，这里受机器设置的ID所占位数大小变化
     */
    private final long maxSupportWorkId = -1L ^ (-1L << workIdBits);

    /**
     * 支持的最大数据标识ID，结果是31，这里受数据标识ID所占位数大小变化
     */
    private final long maxSupportDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 生成序列的掩码，这里是4095，受序列号位数影响，(0b111111111111=0xfff=4095)2^{12}-1 = 4095 即可以用0、1、2、3、4094 这 4095个数字
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作厂房ID向左移12位
     */
    private final long workIdLeftShift = sequenceBits;

    /**
     * 数据标识ID向左移17位（12+5）
     */
    private final long dataCenterIdLeftShift = sequenceBits + dataCenterIdBits;

    /**
     * 时间戳向左移22位（12+5+5）
     */
    private final long timeStampLeftShift = sequenceBits + workIdBits + dataCenterIdBits;

    /**
     * 工作厂房ID（0-31）
     */
    private long workId;

    /**
     * 数据中心ID(0-31)
     */
    private long dataCenterId;

    /**
     * 毫秒内序列ID（0-4094）
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimeStamp = -1L;

    /**
     * 全局ID生成器
     */
    private static SnowflakeIdUtil snowflakeId;

    public SnowflakeIdUtil() {
    }

    static {
        // 静态加载服务所在系统的ID配置
        logger.info("SnowflakeIdUtil workId : {}, dataCenterId : {}", getWorkId(), getDataCenterId());
        snowflakeId = new SnowflakeIdUtil(getWorkId(), getDataCenterId());
    }

    /**
     * 获取全局唯一ID
     *
     * @return
     */
    public static Long generateId() {
        return snowflakeId.nextId();
    }

    /**
     * 初始化构造函数
     *
     * @param workId       指定的工作厂房Id
     * @param dataCenterId 指定的数据中心ID(机器ID)
     */
    public SnowflakeIdUtil(long workId, long dataCenterId) {
        if (workId > maxSupportWorkId || workId < 0) {
            throw new IllegalArgumentException(MessageFormat.format("machineId can't greater than {0} or less than 0", maxSupportWorkId));
        }
        if (dataCenterId > maxSupportDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(MessageFormat.format("dataCenterId can't greater than {0} or less than 0", maxSupportDataCenterId));
        }
        this.workId = workId;
        this.dataCenterId = dataCenterId;
    }


    public synchronized long nextId() {
        long timeStamp = getCurrencyTime();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timeStamp < lastTimeStamp) {
            throw new RuntimeException(MessageFormat.format("Clock moved backwards.  Refusing to generate id for {0} milliseconds", (lastTimeStamp - timeStamp)));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimeStamp == timeStamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 阻塞到下一毫秒，获取新的时间戳
                timeStamp = blockNextMills(lastTimeStamp);
            }
            // 时间戳改变，毫秒内序列重置
        } else {
            sequence = 0L;
        }

        lastTimeStamp = timeStamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timeStamp - startTimeStamp) << timeStampLeftShift) |
                (dataCenterId << dataCenterIdLeftShift) |
                (workId << workIdLeftShift) |
                sequence;


    }


    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimeStamp 上次生成的时间戳ID
     * @return 当前时间戳
     */
    private static long blockNextMills(long lastTimeStamp) {
        long timeStamp = getCurrencyTime();
        while (timeStamp <= lastTimeStamp) {
            timeStamp = getCurrencyTime();
        }

        return timeStamp;
    }

    /**
     * 获取当前系统时间
     *
     * @return 毫秒
     */
    private static long getCurrencyTime() {
        return System.currentTimeMillis();
    }

    /**
     * IP地址取模
     *
     * @return IP地址取模
     */
    private static long ipModule() {
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // 获取IP失败，使用随机数兜底
            return RandomUtils.nextInt(0, 31);
        }
        // 机器位占用5位，所以取模32，最大限度避免重复
        return ipv4ToLong(localHost.getHostAddress()) % 32;
    }

    /**
     * IP地址转long类型
     *
     * @param strIP IP地址
     * @return long类型
     */
    private static long ipv4ToLong(String strIP) {
        Validator.validateIpv4(strIP, "Invalid IPv4 address!");
        long[] ip = Convert.convert(long[].class, StrUtil.split(strIP, '.'));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 获取数据中心ID（机器ID）
     *
     * @return 采用本地机器名称取32的模（降低分布式部署工作ID相同）
     */
    private static long getDataCenterId() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            List<Integer> tenNums = string2Integer(hostName);
            if (CollectionUtils.isEmpty(tenNums)) {
                return localRandom.nextInt(0, 31);
            }
            int sums = 0;
            for (Integer tenNum : tenNums) {
                sums += tenNum;
            }
            return sums % 32;
        } catch (UnknownHostException e) {
            return localRandom.nextInt(0, 31);
        }
    }


    /**
     * 获取工作工厂ID
     *
     * @return 采用本地IP地址取32的模（降低分布式部署工作ID相同）
     */
    private static long getWorkId() {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            List<Integer> tenNums = string2Integer(ip);
            if (CollectionUtils.isEmpty(tenNums)) {
                // IP为空，则采用随机数指定工作ID
                return localRandom.nextInt(0, 31);
            }
            int sums = 0;
            for (Integer tenNum : tenNums) {
                sums += tenNum;
            }
            return sums % 32;
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
    private static List<Integer> string2Integer(String str) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }
        List<Integer> integers = Lists.newArrayListWithCapacity(str.length());
        for (int i = 0; i < str.length(); i++) {
            integers.add(Integer.valueOf(Integer.toString(str.charAt(i), 10)));
        }
        return integers;
    }


    public static void main(String[] args) throws UnknownHostException {
        SnowflakeIdUtil idUtil = new SnowflakeIdUtil(0, 0);
        // 并发测试
        ExecutorService threadPool = newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(() -> {
                System.out.println(idUtil.nextId());
            });
//            threadPool.shutdown();
        }

        // 200W条数据，耗时在13ms左右，平均150条/ms
//        System.out.println("++++++++++++++++++++++++++=");
//        BufferedWriter bufferedWriter = null;
//        try {
//            //设置文件编码，解决文件乱码问题
//            //将字节流转换为字符流，实际上使用了一种设计模式——适配器模式(生成本地项目根目录)
//            bufferedWriter = new BufferedWriter(new FileWriter("./idSnow.txt"));
//            System.out.println(System.currentTimeMillis());
//            long startTime = System.nanoTime();
//            for (int i = 0; i < 2000000; i++) {
//                long id = snowflakeId.nextId();
//                bufferedWriter.write(String.valueOf(id));
//                bufferedWriter.newLine();//按行读取，写入一个分行符，否则所有内容都在一行显示
//                System.out.println(id);
//            }
//            System.out.println((System.nanoTime() - startTime) / 1000000 + "ms");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bufferedWriter != null) {
//                    bufferedWriter.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

}
