package com.mudxx.common.utils.id;

/**
 * @author laiw
 * @date 2024/1/18 14:18
 */
public class SnowflakeIdGenerator2 {
    private static final long START_TIMESTAMP = 1609430400000L; // 2021-01-01 00:00:00 UTC+8

    private static final int WORKER_BITS = 5;
    private static final int DATACENTER_BITS = 5;
    private static final int SEQUENCE_BITS = 10;

    private static final long MAX_WORKERS = ~(-1 << WORKER_BITS);
    private static final long MAX_DATACENTERS = ~(-1 << DATACENTER_BITS);

    private static final long WORKER_MASK = (~(MAX_WORKERS << SEQUENCE_BITS)) & (~(-1 << WORKER_BITS));
    private static final long DATACENTER_MASK = (~(MAX_DATACENTERS << SEQUENCE_BITS)) & (~(-1 << DATACENTER_BITS));

    private static final long TIMESTAMP_LEFTSHIFT = SEQUENCE_BITS + WORKER_BITS + DATACENTER_BITS;

    private static final long SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator2(long workerId, long datacenterId) {
        if (workerId < 0 || workerId > MAX_WORKERS) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKERS);
        }

        if (datacenterId < 0 || datacenterId > MAX_DATACENTERS) {
            throw new IllegalArgumentException("Datacenter ID must be between 0 and " + MAX_DATACENTERS);
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - timestamp) + " milliseconds");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;

            if (sequence == 0) {
                timestamp = getNextMillisecond(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFTSHIFT) |
                (datacenterId << DATACENTER_BITS) |
                (workerId << WORKER_BITS) |
                sequence;
    }

    private long getNextMillisecond(final long lastTimestamp) {
        long timestamp = System.currentTimeMillis();

        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }

        return timestamp;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        SnowflakeIdGenerator2 idWorker = new SnowflakeIdGenerator2(5, 5);
        for (int i = 0; i < 50; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
        }
    }
}
