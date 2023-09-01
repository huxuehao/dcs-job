package com.tiger.job.core.unlock;

import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.core.queue.TaskQueue;
import com.tiger.job.server.service.ScheduleTaskService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：
 **/
@Service
public class Unlock{

    @Resource
    private RedissonClient locker;

    private final TaskQueue taskQueue;
    private final ScheduleTaskService scheduleTaskService;

    public Unlock(TaskQueue taskQueue, ScheduleTaskService scheduleTaskService) {
        this.taskQueue = taskQueue;
        this.scheduleTaskService = scheduleTaskService;
    }

    /**
     * 解锁定时任务执行锁
     */
    public void unlockTask(List<String> taskIds) {
        Optional.ofNullable(taskIds).ifPresent(v0 -> {
            v0.forEach(v1 -> {
                String queueName = taskQueue.getQueueName(v1);
                RLock queueLock = locker.getLock(taskQueue.getQueueLockName(queueName));
                if (queueLock.isLocked()) {
                    queueLock.forceUnlock();
                }
                RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG + v1);
                if (lock.isLocked()) {
                    lock.forceUnlock();
                }
            });
        });
    }

    /**
     * 解锁全部的定时任务锁
     */
    public void unlockTaskAll() {
        List<ScheduleTaskDto> list = scheduleTaskService.list();
        Optional.ofNullable(list)
                .ifPresent(v0 -> this.unlockTask(v0.stream().map(ScheduleTaskDto::getId).collect(Collectors.toList())));
    }

    /**
     * 解锁日志轮转定时任务锁
     */
    public void unLockRotate() {
        RLock lock = locker.getLock(JobConstant.ROTATE_LOCK_KEY);
        if (lock.isLocked()) {
            lock.forceUnlock();
        }
    }
}
