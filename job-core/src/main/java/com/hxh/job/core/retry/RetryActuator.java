package com.hxh.job.core.retry;

import com.hxh.job.common.constant.RetryProperties;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.util.MeUtil;
import com.hxh.job.core.executor.AdapterExecutor;
import com.hxh.job.core.queue.TaskRetryQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：重试执行器
 *
 * @author huxuehao
 **/
@Component
public class RetryActuator {
    // 可重入锁，保证线程安全
    private static final Lock lock = new ReentrantLock();
    // retry 线程
    private static volatile Thread thread;
    // 标记线程是否被中断
    // 不使用 thread.isInterrupted() 代替 isInterrupted 是因为 volatile 的灵活性更高。
    // 如果你是用 isInterrupted() 尝试打断线程，但是在线程中使用了 sleep()，那么整个打断逻辑不清晰,
    // 需要手动维护 interrupt 的状态，即在 sleep() 的 catch 中需要手动再次调用 interrupt()
    private static volatile boolean isInterrupted = true;
    private final RetryProperties retryProperties;
    private final TaskRetryQueue taskRetryQueue;
    private final AdapterExecutor adapterExecutor;
    private final Map<String, ScheduledConfigEntity> scheduleTaskConfigMap;

    public RetryActuator(RetryProperties retryProperties, TaskRetryQueue taskRetryQueue, AdapterExecutor adapterExecutor, @Qualifier("scheduleTaskConfigMap") Map<String, ScheduledConfigEntity> scheduleTaskConfigMap) {
        this.retryProperties = retryProperties;
        this.taskRetryQueue = taskRetryQueue;
        this.adapterExecutor = adapterExecutor;
        this.scheduleTaskConfigMap = scheduleTaskConfigMap;
    }

    /**
     * 尝试关闭重试执行器
     */
    public void tryStop() {
        lock.lock();
        try {
            // 成立条件：线程存在且没有被中断
            if (thread != null && !isInterrupted) {
                Set<String> all = taskRetryQueue.getAll();
                if (all == null || all.isEmpty()) {
                    isInterrupted = true;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试启动重试执行器
     */
    public void tryStart() {
        lock.lock();
        try {
            // 成立条件：线程不存在或者已经被中断了
            if (thread == null || isInterrupted) {
                isInterrupted = false;
                thread = new Thread(this::tryDoRetry);
                thread.start();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 重试
     */
    private void tryDoRetry() {
        // 创建一个线程池
        long startTime = 0L;
        int cpuNumber = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = MeUtil.createThreadPool(cpuNumber,cpuNumber*2,cpuNumber*2,"retryTask-");
        try {
            while (!isInterrupted) {
                long endTime = System.currentTimeMillis();
                // 获取本次需要执行的重试任务
                Set<String> retryTaskTags = taskRetryQueue.get(startTime, endTime);
                startTime = endTime -1;
                if (retryTaskTags != null && !retryTaskTags.isEmpty()) {
                    // 遍历执行重试
                    retryTaskTags.forEach(taskTag -> handleTaskRetry(taskTag, executor));
                }
                MeUtil.sleep(1000L);
            }
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 重试处理器
     * @param taskTag  task 标记
     * @param executor 线程执行器
     */
    private void handleTaskRetry(String taskTag, ThreadPoolTaskExecutor executor) {
        // 获取任务ID
        String taskId = taskRetryQueue.parseTaskId(taskTag);
        // 解析当前重试任务
        int retriedTimes = taskRetryQueue.parseRetriedTimes(taskTag);

        // 条件成立：
        // 1.当前的定时任务存活
        // 2.判断当前待执行重试任务的执行次数是否到达上限
        if (scheduleTaskConfigMap.containsKey(taskId) && retriedTimes <= retryProperties.getCount()) {
            try {
                executor.execute(() -> {
                    // 通过适配器拿到当前执行模式下的执行器，并完成执行
                    Boolean result  = adapterExecutor.matchExecutor().execute(scheduleTaskConfigMap.get(taskId));
                    if (Boolean.TRUE.equals(result)) {
                        taskRetryQueue.remove(taskTag);
                        tryStop();
                    }
                });
            } catch (RejectedExecutionException e) {
                MeUtil.sleep(1000L);
            } catch (Throwable e) {
                e.printStackTrace();
                MeUtil.sleep(1000L);
            }
        } else {
            taskRetryQueue.remove(taskTag);
            tryStop();
        }
    }

    /**
     * 将失败的任务推送到重试队列
     * @param scheduledConfig 任务配置
     */
    public void pushToRetryQueue(Object scheduledConfig) {
        String taskId = String.valueOf(((ScheduledConfigEntity)scheduledConfig).getId());
        long nextTime = System.currentTimeMillis() + (retryProperties.getSleep() * 1000L);
        String taskTag = taskRetryQueue.getTagById(taskId);

        // 成立条件：在队列中没有找到
        if (taskTag == null) {
            // 向队列中添加，并指定下次执行时间
            taskRetryQueue.add(taskRetryQueue.genTaskTag(taskId, 1), nextTime);
            // 尝试启动
            this.tryStart();
        } else {
            int retriedTimes = taskRetryQueue.parseRetriedTimes(taskTag);
            // 成立条件：队列中找到了，但是执行次数已经达到上限
            if (retriedTimes > retryProperties.getCount()) {
                // 移除队列中的任务
                taskRetryQueue.remove(taskTag);
                // 尝试关闭重试
                tryStop();
            }
            // 成立条件：队列中找到了，但是执行次数未达到上限
            else {
                // 向队列中添加，并指定下次执行时间
                taskRetryQueue.add(taskRetryQueue.genTaskTag(taskId, retriedTimes + 1), nextTime);
                // 移除队列中的任务！因为每次的TaskTag都不同，一定要移除再添加
                taskRetryQueue.remove(taskTag);
                // 尝试启动重试
                tryStart();
            }
        }
    }
}
