package com.tiger.job.core.executor;

import com.tiger.job.common.entity.ScheduleTaskDto;

/**
 * @InterfaceName Executor
 * @Description 使用策略模式来执行定时任务
 * @Author huxuehao
 **/
public interface Executor {
    Boolean execute(ScheduleTaskDto task);
}
