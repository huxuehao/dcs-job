package com.hxh.job.core.log.impl;

import com.hxh.job.common.entity.ScheduleLogEntity;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.util.MeUtil;
import com.hxh.job.core.log.GenLog;
import com.hxh.job.server.service.ScheduleLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 生成错误日志
 *
 * @author huxuehao
 **/
@Component
public class GenFailLog implements GenLog {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ScheduleLogService scheduleLogService;

    public GenFailLog(ScheduleLogService scheduleLogService) {
        this.scheduleLogService = scheduleLogService;
    }

    @Override
    public void gen(ScheduledConfigEntity task, String message) {
        genFailLog(task, message);
    }

    private void genFailLog(ScheduledConfigEntity task, String message){
        try {
            scheduleLogService.add(initScheduleLogEntity(task, message));
        } catch (Exception e) {
            log.error("[error]日志持久化错误");
        }
    }

    /* 初始化日志信息 */
    private ScheduleLogEntity initScheduleLogEntity(ScheduledConfigEntity task, String message) {
        return new ScheduleLogEntity(
                MeUtil.nextLongId(),
                task.getId(),
                task.getName(),
                "fail",
                message,
                MeUtil.currentDatetime()
        );
    }
}
