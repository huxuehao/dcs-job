package com.tiger.job.core.schedule;

import com.tiger.job.core.constant.LogProperties;
import com.tiger.job.core.service.ScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * @ClassName ClearLogSchedule
 * @Description 默认定时任务，定期清除日志
 * @Author huxuehao
 **/
@Component
public class ClearLogSchedule {
    @Autowired
    ScheduleLogService scheduleLogService;

    @Autowired
    LogProperties logProperties;

    /* 每天1点30分30秒清除一次日志 */
    @Scheduled(cron = "30 30 1 * * ?")
    public void clearLog() {
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis() - 86400000 * logProperties.getSaveDays());
        scheduleLogService.clearLog(endTime);
    }
}
