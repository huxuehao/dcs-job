package com.tiger.job.core.executor.impl;

import com.tiger.job.common.annotation.Retry;
import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.core.doJob.Job;
import com.tiger.job.core.executor.Executor;
import com.tiger.job.core.queue.TaskQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ClusterExecutor
 * @Description TODO
 * @Author huxuehao
 **/
@Component
public class ClusterExecutor implements Executor {

    @Autowired
    Job job;

    @Autowired
    TaskQueue taskQueue;

    @Autowired
    @Qualifier("uniqueIdentifier")
    String uniqueIdentifier;

    @Resource
    private RedissonClient locker;

    @Override
    public Boolean execute(ScheduleTaskDto task) {
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
                    return this.doExecute(task);
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
                        return doExecute(task);
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

    /**
     * 单例模式下的定时任务执行
     * @param task 定时任务实体
     * @return 执行结果
     */
    @Retry
    private boolean doExecute(ScheduleTaskDto task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) { /* 获取锁 */
            try {
                return job.invoke(task); /* 执行 */
            } finally {
                lock.unlock(); /* 解锁 */
            }
        } else {
            return true;
        }
    }
}
