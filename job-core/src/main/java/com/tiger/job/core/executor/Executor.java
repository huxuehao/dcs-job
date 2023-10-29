package com.tiger.job.core.executor;

import com.tiger.job.common.entity.ScheduleTaskDto;

/**
 * 描述：使用策略模式来执行定时任务
 *
 * @author huxuehao
 **/
public interface Executor {
    Boolean execute(ScheduleTaskDto task);
}
