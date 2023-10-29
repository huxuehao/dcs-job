package com.tiger.job.core.schedule;

import com.tiger.job.common.constant.JobConstant;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.service.LogRotate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 实现定时任务的轮转
 *
 * @author huxuehao
 **/
@Component
public class RotateScheduleLog {

    @Resource
    private RedissonClient locker;

    private final LogRotate logRotate;

    public RotateScheduleLog(LogRotate logRotate) {
        this.logRotate = logRotate;
    }

    /* 每天1点30分30秒尝试实现日志的轮转 */
    @Scheduled(cron = "30 30 1 * * ?")
    public void clearLog() {
        RLock lock = locker.getLock(JobConstant.ROTATE_LOCK_KEY);
        try {
            if (lock.tryLock(0, 3600000L, TimeUnit.MILLISECONDS)) {
                logRotate.doRotate(MeUtil.currentDatetime("yyyyMMdd"));
            }
        } catch (Exception ignored) {}
    }
}
