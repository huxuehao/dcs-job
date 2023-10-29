package com.tiger.job.core.log.impl;

import com.tiger.job.common.entity.ScheduleLogDto;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.core.log.GenLog;
import com.tiger.job.server.service.ScheduleLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 生成成功日志
 *
 * @author huxuehao
 **/
@Component
public class GenSuccessLog implements GenLog {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ScheduleLogService scheduleLogService;

    public GenSuccessLog(ScheduleLogService scheduleLogService) {
        this.scheduleLogService = scheduleLogService;
    }

    @Override
    public void gen(ScheduleTaskDto task, String message) {
        genSuccessLog(task, message);
    }

    private void genSuccessLog(ScheduleTaskDto task, String message){
        try {
            scheduleLogService.add(initScheduleLogEntity(task, message));
        } catch (Exception e) {
            log.error("[success]日志持久化错误");
        }
    }

    /* 初始化日志信息 */
    private ScheduleLogDto initScheduleLogEntity(ScheduleTaskDto task, String message) {
        return new ScheduleLogDto(
                MeUtil.nextId(),
                task.getId(),
                task.getName(),
                "success",
                message,
                MeUtil.currentDatetime()
        );
    }
}
