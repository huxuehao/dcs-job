package com.tiger.job.core.executor.impl;

import com.tiger.job.common.annotation.Retry;
import com.tiger.job.common.entity.ScheduleLogDto;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.exception.member.NotMatchPathException;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.constant.JobConstant;
import com.tiger.job.core.constant.LogProperties;
import com.tiger.job.core.constant.RetryProperties;
import com.tiger.job.core.executor.TaskExecutor;
import com.tiger.job.core.queue.TaskQueue;
import com.tiger.job.core.service.ScheduleLogService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TaskExecutorImpl
 * @Description 定时任务执行器，最终定时任务会通过触发execute方法被执行
 * @Author huxuehao
 **/
@Service
public class TaskExecutorImpl implements TaskExecutor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    ScheduleLogService scheduleLogService;

    @Autowired
    LogProperties logProperties;

    @Autowired
    RetryProperties retryProperties;

    @Autowired
    TaskQueue taskQueue;

    @Autowired
    @Qualifier("uniqueIdentifier")
    String uniqueIdentifier;

    @Resource
    private RedissonClient locker;

    @Autowired
    @Qualifier("schedulerScanMethodMap")
    Map<String, Map<Object, Method>> schedulerScanMethodMap;

    /**
     * 触发执行，即用户手动执行，不需retry
     * @param task 定时任务实体
     * @return 执行结果
     */
    @Override
    public int singleExecute(ScheduleTaskDto task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) { /* 获取锁 */
            try {
                return this.doJob(task) ? 1 : 0; /* 1表示执行成功，0表示执行失败 */
            } finally {
                lock.unlock();
            }
        } else {
            return -1; /* -1表示任务正在执行 */
        }
    }

    /**
     * 单例模式下的定时任务执行
     * @param task 定时任务实体
     * @return 执行结果
     */
    @Retry
    @Override
    public boolean execute(ScheduleTaskDto task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) { /* 获取锁 */
            try {
                return this.doJob(task); /* 执行 */
            } finally {
                lock.unlock(); /* 解锁 */
            }
        } else {
            return true;
        }
    }

    /**
     * 集群模式下的分布式执行
     * 为了实现多节点 轮询+抢占 的分布式定时任务，本次的解决方案为： redis分布式锁 + redis队列。
     * @param task 定时任务实体
     * @return 执行结果
     */
    @Override
    public boolean clusterExecute(ScheduleTaskDto task) {
        /* 获取当前定时任务所属的队列 */
        String queueName = taskQueue.getQueueName(task.getId());
        /* 获取 queueName 对应的分布式锁 */
        RLock lock = locker.getLock(taskQueue.getQueueLockName(queueName));
        /* 产看队列队首元素 */
        String firstItem = taskQueue.peek(queueName);
        if (firstItem == null) {
            /*
            * 当元素为空时，意味着队列为空，那么所有的节点都有执行的机会。
            * 所有节点去抢占分布式锁：
            *     抢到时，设置过期时间，当前节点如队列，执行定时任务
            *     未抢到时，当前节点（不在队列时）入队
            *
            * */
            try {
                if (lock.tryLock(0,this.expirationTime(task.getCron()), TimeUnit.MILLISECONDS)) {
                    taskQueue.push(queueName, getQueueItem());
                    return this.execute(task);
                } else {
                    return this.pushQueue(queueName);
                }
            } catch (Exception e) {
                return false;
            }
        }
        /*
        * 若队首元素与当前节点的唯一表示父匹配，侧上锁、执行定时、出队、（不在队列时）入队；
        * 否则，当前节点（不在队列时）入队
        * */
        if (firstItem.startsWith(uniqueIdentifier)) {
            try {
                if (lock.tryLock(0,this.expirationTime(task.getCron()), TimeUnit.MILLISECONDS)) {
                    try {
                        return execute(task);
                    } finally {
                        taskQueue.pop(queueName);
                        this.pushQueue(queueName);
                    }
                } else {
                    return this.pushQueue(queueName);
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return this.pushQueue(queueName);
        }
    }

    /**
     * 警告：若修改当前函数逻辑，请同步修改 HeartbeatSchedule 中的 queueItemMoreHeartbeat() 逻辑。
     */
    private String getQueueItem() {
        return uniqueIdentifier;
    }

    /* 没有执行定时任务时 */
    private boolean pushQueue(String queueName) {
        if (!taskQueue.exist(queueName, uniqueIdentifier)) {
            taskQueue.push(queueName, getQueueItem());
        }
        return true;
    }

    /* 获取分布式锁的过期时间 */
    private long expirationTime(String cron) {
        CronExpression parse = CronExpression.parse(cron);
        LocalDateTime nextExecuteTime = parse.next(LocalDateTime.now()); /* 根据cron获取下次执行的时间 */
        LocalDateTime nowTime = LocalDateTime.now(); /* 获取当前时间 */
        Duration duration = Duration.between(nowTime, nextExecuteTime); /* 获取上面两者的时间差 */
        /* 提前3秒过期, 过期时间若大于60分钟，则设置60分钟 */
        return duration.toMillis() - 3000L > 1000*60*60 ? 1000*60*60 : duration.toMillis() - 3000L;
    }

    /* 用于定时任务执行*/
    private boolean doJob(ScheduleTaskDto task) {
        String message = null;
        try {
            /*执行定时任务*/
            if (schedulerScanMethodMap.containsKey(task.getPath())) {
                Map<Object, Method> objectMethodMap = schedulerScanMethodMap.get(task.getPath());
                /* objectMethodMap 中的Key是已经实例化过的对象，value是定时任务的方法载体 */
                for (Map.Entry<Object, Method> entry : objectMethodMap.entrySet()) {
                    /* 执行定时任务*/
                    entry.getValue().invoke(entry.getKey());
                    break;
                }
            } else {
                throw new NotMatchPathException("path[" + task.getPath() + "]未匹配到对应的定时任务");
            }
            log.info("定时任务[{}]执行成功", task.getName());
        } catch (Exception e) {
            log.error("定时任务[{}]执行失败", task.getName());
            /* 将异常栈信息存入字符串 */
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            message = sw.toString();
        } finally {
            /* 生成日志 */
            this.genLog(task, message);
        }
        return message == null;
    }

    /* 定时任务日志采集 */
    private void genLog(ScheduleTaskDto task, String message){
        /* 判断定时任务执行状态。true为执行成功，false为执行失败 */
        String status = message == null ? "success" : "fail";
        /* 错误日志和成功日志皆未开启时 */
        if (!logProperties.isFailOpen() && !logProperties.isSuccessOpen()) {
            return;
        }
        /* 成功日志未开启时 */
        if ("success".equals(status) && !logProperties.isSuccessOpen()) {
            return;
        }
        /* 失败日志未开启时 */
        if ("fail".equals(status) && !logProperties.isFailOpen()) {
            return;
        }
        /* 日志未开启时 */
        if ("0".equals(task.getOpenLog())) {
            return;
        }
        try {
            /* 初始化日志信息 */
            ScheduleLogDto scheduleLog = new ScheduleLogDto(
                    MeUtil.nextId(),
                    task.getId(),
                    task.getName(),
                    status,
                    message,
                    MeUtil.currentDatetime()
            );
            /* 生成日志 */
            scheduleLogService.add(scheduleLog);
        } catch (Exception e) {
            log.error("[error]日志持久化错误");
        }
    }
}
