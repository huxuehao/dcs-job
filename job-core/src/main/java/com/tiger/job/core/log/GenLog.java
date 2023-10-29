package com.tiger.job.core.log;

import com.tiger.job.common.entity.ScheduleTaskDto;

/**
 * 日志接口
 *
 * @author huxuehao
 **/
public interface GenLog {
    void gen(ScheduleTaskDto task, String message);
}
