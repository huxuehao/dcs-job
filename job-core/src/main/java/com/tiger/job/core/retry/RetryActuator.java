package com.tiger.job.core.retry;

import com.tiger.job.common.constant.RetryProperties;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.executor.AdapterExecutor;
import com.tiger.job.core.queue.TaskRetryQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

/**
 * 描述：重试执行器
 *
 * @author huxuehao
 **/
@Component
public class RetryActuator {
    private final RetryProperties retryProperties;
    private final TaskRetryQueue taskRetryQueue;
    private final AdapterExecutor adapterExecutor;
    private final Map<String, ScheduleTaskDto> scheduleTaskConfigMap;

    public RetryActuator(RetryProperties retryProperties, TaskRetryQueue taskRetryQueue, AdapterExecutor adapterExecutor, @Qualifier("scheduleTaskConfigMap") Map<String, ScheduleTaskDto> scheduleTaskConfigMap) {
        this.retryProperties = retryProperties;
        this.taskRetryQueue = taskRetryQueue;
        this.adapterExecutor = adapterExecutor;
        this.scheduleTaskConfigMap = scheduleTaskConfigMap;
    }

    public void run() {
        if (!retryProperties.isOpen()) {
            return;
        }
        ThreadPoolTaskExecutor executor = MeUtil.createThreadPool(1,1,0,null);
        executor.execute(this::tryDoRetry);
    }
    private void tryDoRetry() {
        // 获取重试次数上限
        int count = retryProperties.getCount();
        // 创建一个线程池
        int cpuNumber = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = MeUtil.createThreadPool(cpuNumber,cpuNumber*2,cpuNumber*2,"retryTask-");
        while (true) {
            // 获取本次需要执行的重试任务
            Set<String> retryTaskTags = taskRetryQueue.getNow();
            if (retryTaskTags != null && !retryTaskTags.isEmpty()) {
                for (String taskTag : retryTaskTags) {
                    // 获取任务ID
                    String taskId = taskRetryQueue.parseTaskId(taskTag);
                    // 解析当前重试任务
                    int retriedTimes = taskRetryQueue.parseRetriedTimes(taskTag);
                    // 条件成立：
                    // 1.当前的定时任务存活
                    // 2.判断当前待执行重试任务的执行次数是否到达上限
                    if (scheduleTaskConfigMap.containsKey(taskId) && retriedTimes <= count) {
                        try {
                            executor.execute(() -> {
                                // 通过适配器拿到当前执行模式下的执行器，并完成执行
                                Boolean res = adapterExecutor.matchExecutor().execute(scheduleTaskConfigMap.get(taskId));
                                if (res) {
                                    taskRetryQueue.remove(taskTag);
                                }
                            });
                        } catch (RejectedExecutionException e) {
                            MeUtil.sleep(1000L);
                        } catch (Throwable e){
                            e.printStackTrace();
                            MeUtil.sleep(1000L);
                        }
                    } else {
                        taskRetryQueue.remove(taskTag);
                    }
                }
            }
            MeUtil.sleep(3000L);
        }
    }
}
