package com.tiger.job.core.executor.impl;

import com.tiger.job.common.annotation.Retry;
import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.core.doJob.JobInvoke;
import com.tiger.job.core.executor.Executor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述：单例执行器
 *
 * @author huxuehao
 **/
@Component
public class SingleExecutor implements Executor {
    @Resource
    private RedissonClient locker;
    private final JobInvoke jobInvoke;

    public SingleExecutor(JobInvoke jobInvoke) {
        this.jobInvoke = jobInvoke;
    }

    @Retry
    @Override
    public Boolean execute(ScheduledConfigEntity task) {
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
