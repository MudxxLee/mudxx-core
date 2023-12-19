package com.mudxx.common.thread.pool.executor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author laiwen
 */
public class ThreadPoolExecutorFactory {

    /**
     * 初始化线程池
     * 任务拒绝策略：当线程池线程已经达到最大线程数，并且线程全部都在工作，队列也已经排队排满，此时便会触发拒绝策略。
     * 使用默认工厂：Executor.defaultThreadFactory()
     * 采用默认拒绝策略：ThreadPoolExecutor.AbortPolicy()
     *
     * @param corePoolSize        任务线程池核心个数-只要线程池不关闭，就不会被销毁
     * @param maximumPoolSize     任务线程池最大个数-（核心线程+非核心线程)，非核心线程没有执行任务的话是要被清理的
     * @param keepAliveTimeSecond 非核心线程存活时间(秒)
     * @param blockingQueueSize   阻塞队列数-当核心线程占满时（指所有的核心线程都在工作），这时候如果有新的任务来到线程池的话，就会放到任务队列中进行排队。
     */
    public static ThreadPoolExecutor initThreadPoolExecutor(int corePoolSize,
                                                            int maximumPoolSize,
                                                            long keepAliveTimeSecond,
                                                            int blockingQueueSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTimeSecond,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(blockingQueueSize),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 初始化线程池
     * 任务拒绝策略：当线程池线程已经达到最大线程数，并且线程全部都在工作，队列也已经排队排满，此时便会触发拒绝策略。
     * 使用默认工厂：Executor.defaultThreadFactory()
     * 采用默认拒绝策略：ThreadPoolExecutor.AbortPolicy()
     *
     * @param corePoolSize        任务线程池核心个数-只要线程池不关闭，就不会被销毁
     * @param maximumPoolSize     任务线程池最大个数-（核心线程+非核心线程)，非核心线程没有执行任务的话是要被清理的
     * @param keepAliveTimeSecond 非核心线程存活时间(秒)
     * @param blockingQueueSize   阻塞队列数-当核心线程占满时（指所有的核心线程都在工作），这时候如果有新的任务来到线程池的话，就会放到任务队列中进行排队。
     * @param threadFactory       自定义线程工厂
     */
    public static ThreadPoolExecutor initThreadPoolExecutor(int corePoolSize,
                                                            int maximumPoolSize,
                                                            long keepAliveTimeSecond,
                                                            int blockingQueueSize,
                                                            ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTimeSecond,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(blockingQueueSize),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

}

