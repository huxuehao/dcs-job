package com.tiger.job.api.lock;

import com.tiger.job.common.annotation.LoginAuth;
import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.response.Response;
import com.tiger.job.core.queue.TaskQueue;
import com.tiger.job.server.service.ScheduleTaskService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UnlockController
 * @Description TODO 解锁分布式锁
 * @Author huxuehao
 **/
@RestController
@RequestMapping(value = "/unlock")
public class UnlockController {
    @Resource
    private RedissonClient locker;
    private final TaskQueue taskQueue;
    private final ScheduleTaskService scheduleTaskService;

    public UnlockController(TaskQueue taskQueue, ScheduleTaskService scheduleTaskService) {
        this.taskQueue = taskQueue;
        this.scheduleTaskService = scheduleTaskService;
    }

    @LoginAuth
    @GetMapping("/task/lock/by-id")
    @Description("根据定时任务id解锁，定时任务锁")
    public Response unlockByTaskId(@RequestParam("id") String id) {
        String queueName = taskQueue.getQueueName(id);
        RLock queueLock = locker.getLock(taskQueue.getQueueLockName(queueName));
        if (queueLock.isLocked()) {
            queueLock.forceUnlock();
        }
        RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG  + id);
        if (lock.isLocked()) {
            lock.forceUnlock();
        }
        return Response.success("解锁成功");
    }

    @LoginAuth
    @GetMapping("/task/lock/all")
    @Description("解锁全部的定时任务锁")
    public Response unlockTaskAll() {
        List<ScheduleTaskDto> list = scheduleTaskService.selectAll();
        for (ScheduleTaskDto dto : list) {
            String queueName = taskQueue.getQueueName(dto.getId());
            RLock queueLock = locker.getLock(taskQueue.getQueueLockName(queueName));
            if (queueLock.isLocked()) {
                queueLock.forceUnlock();
            }
            RLock lock = locker.getLock(JobConstant.LOCK_PREFIX + JobConstant.LINK_TAG  + dto.getId());
            if (lock.isLocked()) {
                lock.forceUnlock();
            }
        }
        return Response.success("解锁成功");
    }
}
