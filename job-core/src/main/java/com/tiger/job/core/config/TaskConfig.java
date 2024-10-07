package com.tiger.job.core.config;

import com.tiger.job.common.constant.TaskPoolProperties;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

/**
 * 描述：定时任务配置
 *
 * @author huxuehao
 **/
@Configuration
@AutoConfigureAfter({TaskPoolProperties.class})
public class TaskConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 类注册表
     * 描述：存储了当前工程中用户通过@SchedulerBean和@TaskPath标记的定时任务类和定时任务方法
     */
    @Bean(name = "schedulerScanMethodMap")
    public Map<String, Map<Object, Method>> schedulerScanMethodMap() {
        return new HashMap<>();
    }

    /**
     * 节点唯一标识符
     * 描述：当前项目的唯一标识符，用于分布式定时任务使用。
     * 使用主机地址和进程IP组合作为唯一标识。
     */
    @Bean(name = "uniqueIdentifier")
    public String uniqueIdentifier() {
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        String hostAddress = UUID.randomUUID().toString().replaceAll("-","");
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostAddress + ":" + pid;
    }

    /**
     * 定时任务注册表
     * 描述：我们没开启一个定时任务，就会产生一个ScheduledFuture实体，当我们需要动态的操作定时任务（上述的实体）时，
     * 我们需要调用scheduledFuture.cancel(true)或scheduledFuture.schedule(worker, cronTrigger)，这就意味着
     * 我们需要将每一个ScheduledFuture实体存储起来，当需要动态的控制定时任务时，我们去改Map中取出对应的实体进行上述的
     * cancel或schedule操作即可。所以我们需要在spring容器中维护一个ScheduledFuture的注册表注册表。
     */
    @Bean(name = "scheduledFutureMap")
    public Map<String, ScheduledFuture<?>> scheduledFutureMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 存放活跃的ScheduleTaskConfig
     */
    @Bean(name = "scheduleTaskConfigMap")
    public Map<String, ScheduledConfigEntity> scheduleTaskConfigMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 触发器注册表
     * 描述：对于定时任务的动态操作（添加、开启、停止、删除）我们需要将上述操作封装成触发器，想要使用的时候
     * 我们只需要取出已经初始化好的代码逻辑，取出对应的代码逻辑，传入参数，执行即可。上述描述是一种预处理的思想。
     * <p>
     * 在这里我们使用java8的Consumer（消费型函数式接口）进行时间，并将实现了不同代码逻辑的Consumer存储到Map中，
     * 这样我们一方面可以实现动态扩展，另一方面省去了多 if-else 的操作。
     */
    @Bean(name = "triggerMap")
    public Map<String, Consumer<ScheduledConfigEntity>> triggerMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 一个用于开启定时任务的线程池；我们关注的核心方法是：threadPoolTaskScheduler.schedule(工作内容, 触发器)
     */
    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(TaskPoolProperties taskPoolProperties) {
        int taskThreadPool;
        int processors = Runtime.getRuntime().availableProcessors(); // CPU核数
        if (taskPoolProperties.getProcessorTimes() < 1) {
            taskThreadPool = processors + taskPoolProperties.getFloatNumber();
        } else {
            taskThreadPool = processors * taskPoolProperties.getProcessorTimes() + taskPoolProperties.getFloatNumber();
        }
        log.info("定时任务线程池: CPU核数 = {}", processors);
        log.info("定时任务线程池: CPU核数倍数: tiger.scheduled-tasks.pool.processor-times = {}", taskPoolProperties.getProcessorTimes());
        log.info("定时任务线程池: 线程池上下浮动数: tiger.scheduled-tasks.pool.float-number = {}", taskPoolProperties.getFloatNumber());
        log.info("定时任务线程池: 线程名称前缀: tiger.scheduled-tasks.pool.thread-name-prefix = {}", taskPoolProperties.getThreadNamePrefix());
        log.info("定时任务线程池: 线程等待终止时间: tiger.scheduled-tasks.pool.await-termination-seconds = {}", taskPoolProperties.getAwaitTerminationSeconds());
        log.info("定时任务线程池: 调度器shutdown后，是否等待当前调度执行完成: tiger.scheduled-tasks.pool.complete-on-shutdown = {}", taskPoolProperties.isCompleteOnShutdown());
        log.info("定时任务线程池: 定时任务线程池最终大小 = {}", taskThreadPool);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 线程池的大小
        threadPoolTaskScheduler.setPoolSize(taskThreadPool);
        // 线程名称
        threadPoolTaskScheduler.setThreadNamePrefix(taskPoolProperties.getThreadNamePrefix());
        // 调度器shutdown后，等待当前调度执行完成
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(taskPoolProperties.isCompleteOnShutdown());
        // 等待终止时间
        threadPoolTaskScheduler.setAwaitTerminationSeconds(taskPoolProperties.getAwaitTerminationSeconds());
        return threadPoolTaskScheduler;
    }
}
