package com.tiger.job.core.init;

import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.core.beanscan.SchedulerScan;
import com.tiger.job.common.constant.ChannelConstant;
import com.tiger.job.common.constant.ClusterProperties;
import com.tiger.job.core.executor.TaskExecutor;
import com.tiger.job.core.queue.TaskQueue;
import com.tiger.job.core.worker.TaskWorker;
import com.tiger.job.server.service.ScheduleTaskService;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @ClassName TaskInit
 * @Description 初始化定时任务
 * scheduledFutureMap：作为ScheduledFuture的注册表,用于我们来操作其开启关闭，key为定时任务id。
 * operationMap: 存储的定时任务操作触发器，参数是ScheduleTaskDto。
 * threadPoolTaskScheduler：定时任务线程池，线程池的大小使用的是Runtime.getRuntime().availableProcessors()。
 * scheduleTaskService：操作schedule_task的Service。
 * initSchedule()：从数据表中寻找有效的定时任务，并执行定时任务。
 * initOperationMap()：初始化定时任务操作触发器，最终初始化好的定时任务操作会被填充到operationMap中，
 * 其目的是为了动态的控制定时任务的新增、开启、关闭。使用Consumer作为定时任务预处理载体。
 * @Author huxuehao
 **/
@SpringBootConfiguration
public class TaskInit {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Qualifier("scheduledFutureMap")
    @Autowired
    Map<String, ScheduledFuture> scheduledFutureMap;
    @Qualifier("triggerMap")
    @Autowired
    Map<String, Consumer<ScheduleTaskDto>> triggerMap;
    @Qualifier("threadPoolTaskScheduler")
    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Qualifier("schedulerScanMethodMap")
    @Autowired
    Map<String, Map<Object, Method>> schedulerScanMethodMap;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    TaskExecutor taskExecutor;
    @Autowired
    TaskQueue taskQueue;
    @Autowired
    ClusterProperties clusterProperties;
    @Autowired
    SchedulerScan schedulerScan;

    @Resource
    private RedissonClient locker;

    public TaskInit() {
    }

    /**
     * 描述：初始化
     */
    @PostConstruct
    public void init() {
        this.initSchedulerScan();
        this.initTask();
        this.initTrigger();
    }

    /**
     * 扫描定时任务
     */
    private void initSchedulerScan(){
        schedulerScanMethodMap.putAll(schedulerScan.schedulerScanMethod());
    }

    /**
     * 描述：初始化定时任务
     */
    private void initTask() {
        List<ScheduleTaskDto> taskList = scheduleTaskService.selectAll();
        List<ScheduleTaskDto> enableTask = taskList.stream().filter(item -> "1".equals(item.getEnable())).collect(Collectors.toList());
        log.info("定时任务：当前定时任务为[{}]", clusterProperties.isOpen() ? "集群模式" : "单例模式");
        enableTask.forEach(item -> {
            TaskWorker worker = new TaskWorker(item, taskExecutor, clusterProperties);
            CronTrigger cronTrigger = item.toCronTrigger();
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(worker, cronTrigger);
            scheduledFutureMap.put(item.getId(), schedule);
            log.info("定时任务：[{}]初始化完成", item.getName());
        });
    }

    /**
     * 描述：初始化定时任务操作触发器
     * 我们关注的核心方法是：
     *    threadPoolTaskScheduler.schedule(工作内容, 触发器) //此方法用于开启一个定时任务
     *    scheduledFuture.cancel(true) //此方法用于取消一个定时任务
     */
    public void initTrigger() {
        // 定时任务：开启操作
        Consumer<ScheduleTaskDto> openSchedule = item -> {
            String scheduleId = item.getId();
            if (scheduledFutureMap.containsKey(scheduleId)) { /* 当定时任务已经存在与scheduledFutureMap中*/
                scheduledFutureMap.compute(scheduleId, (k, v) -> { /* 重新计算scheduledFutureMap中key为scheduledId的value的值 */
                    Optional.ofNullable(v).ifPresent(v0 -> v0.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则将其先停跳 */
                    TaskWorker worker = new TaskWorker(item, taskExecutor, clusterProperties); /* 开启一个新的定时 */
                    CronTrigger cronTrigger = item.toCronTrigger();
                    return threadPoolTaskScheduler.schedule(worker, cronTrigger);
                });
            } else { /* 当定时任务不存在scheduledFutureMap中，则新建定时任务，并添加到map中 */
                TaskWorker worker = new TaskWorker(item, taskExecutor, clusterProperties);
                CronTrigger cronTrigger = item.toCronTrigger();
                ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(worker, cronTrigger);
                scheduledFutureMap.put(scheduleId, schedule);
            }
            /* 强制解锁 */
            locker.getLock(taskQueue.getQueueLockName(scheduleId)).forceUnlock();
        };
        triggerMap.put(ChannelConstant.OPEN, openSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.OPEN);

        // 定时任务：关闭操作
        Consumer<ScheduleTaskDto> closeSchedule = item -> {
            String scheduleId = item.getId();
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(scheduleId); // 从scheduledFutureMap中获取scheduledId对应的定时任务
            Optional.ofNullable(scheduledFuture).ifPresent(schedule -> schedule.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则停止定时 */
            taskQueue.delete(taskQueue.getQueueName(item.getId())); /* 定时队列删除 */
        };
        triggerMap.put(ChannelConstant.CLOSE, closeSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.CLOSE);

        // 定时任务：删除操作
        Consumer<ScheduleTaskDto> deleteSchedule = item -> {
            String scheduleId = item.getId();
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(scheduleId); // 从scheduledFutureMap中获取scheduledId对应的定时任务
            Optional.ofNullable(scheduledFuture).ifPresent(schedule -> schedule.cancel(true)); /* 先判空,如果对象（ScheduledFuture）存在,则停止定时 */
            scheduledFutureMap.remove(scheduleId); /* 从注册表中移除 */
            taskQueue.delete(taskQueue.getQueueName(item.getId())); /* 定时队列删除 */
        };
        triggerMap.put(ChannelConstant.DELETE, deleteSchedule);
        log.info("定时任务：操作注册表：[{}]初始化完成", ChannelConstant.DELETE);
    }
}
