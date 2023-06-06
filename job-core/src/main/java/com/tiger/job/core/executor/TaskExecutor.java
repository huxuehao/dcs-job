package com.tiger.job.core.executor;

import com.tiger.job.common.entity.ScheduleTaskDto;

/**
 * @InterfaceName TaskExecutor
 * @Description 任务执行器
 * @Author huxuehao
 **/
public interface TaskExecutor {

    /**
     * 执行一次任务
     * @param task
     * @return
     */
    int singleExecute(ScheduleTaskDto task);

    /**
     * 执行任务
     * @param task
     * @return
     */
    boolean execute(ScheduleTaskDto task);

    /**
     * 分布式执行
     * @param task
     * @return
     */
    boolean clusterExecute(ScheduleTaskDto task);
}
