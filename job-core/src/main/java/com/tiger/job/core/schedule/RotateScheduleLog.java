package com.tiger.job.core.schedule;

import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.service.LogRotate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @ClassName RotateScheduleLog
 * @Description 实现定时任务的轮转
 * @Author huxuehao
 **/
@Component
public class RotateScheduleLog {
    private final LogRotate logRotate;

    public RotateScheduleLog(LogRotate logRotate) {
        this.logRotate = logRotate;
    }

    /* 每天1点30分30秒尝试实现日志的轮转 */
    @Scheduled(cron = "30 30 1 * * ?")
    public void clearLog() {
        try {
            logRotate.doRotate(MeUtil.currentDatetime("yyyyMMddHHmmss"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
