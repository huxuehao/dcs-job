package com.tiger.job.core.init;

import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.core.beanScan.SchedulerScan;
import com.tiger.job.common.constant.ChannelConstant;
import com.tiger.job.core.executor.AdapterExecutor;
import com.tiger.job.core.queue.TaskQueue;
import com.tiger.job.core.unlock.Unlock;
import com.tiger.job.core.worker.TaskWorker;
import com.tiger.job.server.service.ScheduleTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 描述：初始化定时任务
 * SmartInitializingSingleton：保证在Spring容器中所有单例Bean初始化完成后，‌触发相应的初始化逻辑。
 * scheduledFutureMap：作为ScheduledFuture的注册表,用于我们来操作其开启关闭，key为定时任务id。
 * scheduleTaskConfigMap：作为调度任务配置的容器,主要用于重试时获取定时配置，key为定时任务id。
 * operationMap: 存储的定时任务操作触发器，参数是ScheduleTaskDto。
 * threadPoolTaskScheduler：定时任务线程池，线程池的大小使用的是Runtime.getRuntime().availableProcessors()。
 * scheduleTaskService：操作schedule_task的Service。
 * initSchedule()：从数据表中寻找有效的定时任务，并执行定时任务。
 * initOperationMap()：初始化定时任务操作触发器，最终初始化好的定时任务操作会被填充到operationMap中，
 * 其目的是为了动态的控制定时任务的新增、开启、关闭。使用Consumer作为定时任务预处理载体。
 * @author huxuehao
 **/
@Component
public class TaskInitialization implements SmartInitializingSingleton {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap;
    private final Map<String, ScheduledConfigEntity> scheduleTaskConfigMap;
    private final Map<String, Consumer<ScheduledConfigEntity>> triggerMap;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final Map<String, Map<Object, Method>> schedulerScanMethodMap;
    private final ScheduleTaskService scheduleTaskService;
    private final AdapterExecutor adapterExecutor;
    private final TaskQueue taskQueue;
    private final SchedulerScan schedulerScan;
    private final Unlock unlock;

    public TaskInitialization(@Qualifier("scheduledFutureMap") Map<String, ScheduledFuture<?>> scheduledFutureMap, @Qualifier("scheduleTaskConfigMap") Map<String, ScheduledConfigEntity> scheduleTaskConfigMap, @Qualifier("triggerMap") Map<String, Consumer<ScheduledConfigEntity>> triggerMap, @Qualifier("threadPoolTaskScheduler") ThreadPoolTaskScheduler threadPoolTaskScheduler, @Qualifier("schedulerScanMethodMap") Map<String, Map<Object, Method>> schedulerScanMethodMap, ScheduleTaskService scheduleTaskService, TaskQueue taskQueue, AdapterExecutor adapterExecutor, SchedulerScan schedulerScan, Unlock unlock) {
        this.scheduledFutureMap = scheduledFutureMap;
        this.scheduleTaskConfigMap = scheduleTaskConfigMap;
        this.triggerMap = triggerMap;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.schedulerScanMethodMap = schedulerScanMethodMap;
        this.scheduleTaskService = scheduleTaskService;
        this.taskQueue = taskQueue;
        this.adapterExecutor = adapterExecutor;
        this.schedulerScan = schedulerScan;
        this.unlock = unlock;
    }

    /**
     * 描述：初始化
     */
    @Override
    public void afterSingletonsInstantiated() {
        try {
            this.initSchedulerScan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.initTrigger();
        this.initTask();
    }

    /**
     * 扫描定时任务
     */
    private void initSchedulerScan() throws IOException, ClassNotFoundException {
        schedulerScanMethodMap.putAll(schedulerScan.schedulerScanMethod());
    }

    /**
     * 描述：初始化定时任务操作触发器
     * 我们关注的核心方法是：
     *    threadPoolTaskScheduler.schedule(工作内容, 触发器) //此方法用于开启一个定时任务
     *    scheduledFuture.cancel(true) //此方法用于取消一个定时任务
     */
    public void initTrigger() {
        this.openTrigger();
        this.closeTrigger();
        this.deleteTrigger();
    }

    /* 定时任务：开启操作 */
    private void openTrigger() {
        Consumer<ScheduledConfigEntity> openSchedule = item -> {
            String scheduleId = item.getId();
            if (scheduledFutureMap.containsKey(scheduleId)) { /* 当定时任务已经存在与scheduledFutureMap中*/
                scheduledFutureMap.compute(scheduleId, (k, v) -> { /* 重新计算scheduledFutureMap中key为scheduledId的value的值 */
                    Optional.ofNullable(v).ifPresent(v0 -> v0.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则将其先停跳 */
                    TaskWorker worker = new TaskWorker(item, adapterExecutor.matchExecutor()); /* 开启一个新的定时 */
                    CronTrigger cronTrigger = item.toCronTrigger();
                    return threadPoolTaskScheduler.schedule(worker, cronTrigger);
                });
            } else { /* 当定时任务不存在scheduledFutureMap中，则新建定时任务，并添加到map中 */
                TaskWorker worker = new TaskWorker(item, adapterExecutor.matchExecutor());
                CronTrigger cronTrigger = item.toCronTrigger();
                ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(worker, cronTrigger);
                scheduledFutureMap.put(scheduleId, schedule);
            }
            scheduleTaskConfigMap.put(item.getId(), item);
            /* 强制解锁 */
            unlock.unlockTask(Collections.singletonList(scheduleId));
        };
        triggerMap.put(ChannelConstant.OPEN, openSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.OPEN);
    }

    /* 定时任务：关闭操作 */
    private void closeTrigger(){
        Consumer<ScheduledConfigEntity> closeSchedule = item -> {
            String scheduleId = item.getId();
            ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(scheduleId); // 从scheduledFutureMap中获取scheduledId对应的定时任务
            Optional.ofNullable(scheduledFuture).ifPresent(schedule -> schedule.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则停止定时 */
            scheduleTaskConfigMap.remove(item.getId());
            taskQueue.delete(taskQueue.getQueueName(item.getId())); /* 定时队列删除 */
        };
        triggerMap.put(ChannelConstant.CLOSE, closeSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.CLOSE);
    }

    /* 定时任务：删除操作 */
    private void deleteTrigger(){
        Consumer<ScheduledConfigEntity> deleteSchedule = item -> {
            String scheduleId = item.getId();
            ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(scheduleId); // 从scheduledFutureMap中获取scheduledId对应的定时任务
            Optional.ofNullable(scheduledFuture).ifPresent(schedule -> schedule.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则停止定时 */
            scheduledFutureMap.remove(scheduleId); /* 从注册表中移除 */
            scheduleTaskConfigMap.remove(scheduleId);
            taskQueue.delete(taskQueue.getQueueName(item.getId())); /* 定时队列删除 */
        };
        triggerMap.put(ChannelConstant.DELETE, deleteSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.DELETE);
    }


    /**
     * 描述：初始化定时任务
     */
    private void initTask() {
        List<ScheduledConfigEntity> taskList = scheduleTaskService.selectAll();
        List<ScheduledConfigEntity> enableTask = taskList.stream().filter(item -> "1".equals(item.getEnable())).collect(Collectors.toList());
        enableTask.forEach(item -> {
            triggerMap.get(ChannelConstant.OPEN).accept(item);
            log.info("定时任务：[{}]初始化完成", item.getName());
        });
    }
}
