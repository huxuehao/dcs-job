package com.hxh.job.core.executor.impl;

import com.hxh.job.common.annotation.Retry;
import com.hxh.job.common.constant.JobConstant;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.core.doJob.JobInvoke;
import com.hxh.job.core.executor.Executor;
import com.hxh.job.core.queue.TaskQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
/**
 * 描述：集群执行器
 *
 * @author huxuehao
 **/
@Component
@EnableAspectJAutoProxy(exposeProxy=true)
public class ClusterExecutor implements Executor {
    private final JobInvoke jobInvoke;
    private final TaskQueue taskQueue;
    private final String uniqueIdentifier;
    private final ClusterExecutor self;

    @Resource
    private RedissonClient locker;

    public ClusterExecutor(JobInvoke jobInvoke, TaskQueue taskQueue, @Qualifier("uniqueIdentifier") String uniqueIdentifier, @Lazy ClusterExecutor self) {
        this.jobInvoke = jobInvoke;
        this.taskQueue = taskQueue;
        this.uniqueIdentifier = uniqueIdentifier;
        this.self = self;
    }

    @Override
    public Boolean execute(ScheduledConfigEntity task) {
        /* 获取当前定时任务所属的队列 */
        String queueName = taskQueue.getQueueName(String.valueOf(task.getId()));
        /* 获取 queueName 对应的分布式锁 */
        RLock lock = locker.getLock(taskQueue.getQueueLockName(queueName));
        /* 查看队列队首元素 */
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
                    /*
                     * doExecute属于内部方法，如果直接使用this.doExecute(xx),那么是使用实例对象调用的，而不是代理对象，导致AOP失效。
                     * 所以我们通过@Lazy将当前对象注册到当前对象，从而使得AOP生效
                     * doExecute方法被代理对象调用，从而使得AOP生效
                     */
                    return self.doExecute(task);
                } else {
                    return this.pushQueue(queueName);
                }
            } catch (Exception e) {
                return false;
            }
        }
        /*
         * 若队首元素与当前节点的唯一标识符匹配，侧上锁、执行定时、出队、（不在队列时）入队；
         * 否则，当前节点（不在队列时）入队
         * */
        if (firstItem.startsWith(uniqueIdentifier)) {
            try {
                if (lock.tryLock(0,this.expirationTime(task.getCron()), TimeUnit.MILLISECONDS)) {
                    try {
                        return self.doExecute(task);
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
    public boolean doExecute(ScheduledConfigEntity task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) { /* 获取锁 */
            try {
                return jobInvoke.execute(task); /* 执行 */
            } finally {
                lock.unlock(); /* 解锁 */
            }
        } else {
            return true;
        }
    }
}
