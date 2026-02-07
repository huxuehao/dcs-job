package com.hxh.job.common.constant;

/**
 * 描述：定时任务常量类
 *
 * @author huxuehao
 **/
public class JobConstant {
    /* 统一连接符 */
    public static final String LINK_TAG = "::";
    /* 执行定时任务时的任务锁的前缀 */
    public static final String LOCK_PREFIX = "tiger-job-task-lock";
    public static final String AUTO_INSERT_LOCK = "tiger-job-auto-insert-lock";
    /* 心跳检查前缀 */
    public static final String HEARTBEAT_PREFIX = "task-heart-beat";
    /* 分布式队列前缀 */
    public static final String CLUSTER_QUEUE_PREFIX = "tiger-job-queue";
    /* 重试队列前缀 */
    public static final String CLUSTER_RETRY_QUEUE_PREFIX = "tiger-job-retry-queue";
    /* 分布式队列锁前缀 */
    public static final String CLUSTER_QUEUE_LOCK_PREFIX = "dcs-lock";
    /* 日志轮转事务锁 */
    public static final String ROTATE_LOCK_KEY = "lock:rotate-schedule-log";

}
