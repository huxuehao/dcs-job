package com.tiger.job.core.executor.impl;

import com.tiger.job.common.annotation.Retry;
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
 * @ClassName SingleExecutor
 * @Description 单例执行
 * @Author huxuehao
 **/
@Component
public class SingleExecutor implements Executor {

    @Autowired
    Job job;

    @Resource
    private RedissonClient locker;

    @Retry
    @Override
    public Boolean execute(ScheduleTaskDto task) {
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
