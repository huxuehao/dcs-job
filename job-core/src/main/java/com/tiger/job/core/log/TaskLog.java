package com.tiger.job.core.log;

import com.tiger.job.common.constant.LogProperties;
import com.tiger.job.common.entity.ScheduleTaskDto;
import org.springframework.stereotype.Component;

/**
 * 日志
 *
 * @author huxuehao
 **/
@Component
public class TaskLog {
    private final LogProperties logProperties;
    private final AdapterGenLog adapterGenLog;

    public TaskLog(LogProperties logProperties, AdapterGenLog adapterGenLog) {
        this.logProperties = logProperties;
        this.adapterGenLog = adapterGenLog;
    }

    /**
     * 日志执行
     * @param task 定时任务DTO
     * @param message 消息
     */
    public void invoke(ScheduleTaskDto task, String message) {
        if ("0".equals(task.getOpenLog()) || (!logProperties.isFailOpen() && !logProperties.isSuccessOpen())) {
            return;
        }
        adapterGenLog.matchGenLog(message).gen(task, message);
    }
}
