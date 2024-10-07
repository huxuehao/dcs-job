package com.tiger.job.core.executor.impl;

import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.core.doJob.JobInvoke;
import com.tiger.job.core.executor.Executor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述：手动执行器
 *
 * @author huxuehao
 **/
@Component
public class ManualExecutor implements Executor {
    @Resource
    private RedissonClient locker;
    private final JobInvoke jobInvoke;

    public ManualExecutor(JobInvoke jobInvoke) {
        this.jobInvoke = jobInvoke;
    }

    @Override
    public Boolean execute(ScheduledConfigEntity task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) {
            try {
                return jobInvoke.execute(task);
            } finally {
                lock.unlock();
            }
        } else {
            return null;
        }
    }
}
