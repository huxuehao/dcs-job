package com.tiger.job.core.executor.impl;

import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.core.doJob.Job;
import com.tiger.job.core.executor.Executor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName ManualExecutor
 * @Description 手动执行
 * @Author huxuehao
 **/
@Component
public class ManualExecutor implements Executor {

    @Autowired
    Job job;

    @Resource
    private RedissonClient locker;

    @Override
    public Boolean execute(ScheduleTaskDto task) {
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + task.getId());
        if (lock.tryLock()) {
            try {
                return job.invoke(task);
            } finally {
                lock.unlock();
            }
        } else {
            return null;
        }
    }
}
