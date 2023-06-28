package com.tiger.job.core.log;

import com.tiger.job.common.entity.ScheduleTaskDto;

/**
 * @InterfaceName GenLog
 * @Description TODO
 * @Author huxuehao
 **/
public interface GenLog {
    void gen(ScheduleTaskDto task, String message);
}
