package com.tiger.job.server.service;

import com.tiger.job.common.entity.ScheduleLogDto;

import java.util.List;

/**
 * @InterfaceName ScheduleLogService
 * @Description TODO
 * @Author huxuehao
 **/
public interface ScheduleLogService {
    /* 分页 */
    List<ScheduleLogDto> getPage();
    /* 添加 */
    int add(ScheduleLogDto scheduleLog);
    /* 根据定时任务获取最新的错误日志*/
    ScheduleLogDto latestLogByTask(String taskId);
    /* 上一条日志 */
    ScheduleLogDto lastLog(String taskId, String executeTime);
    /* 下一条日志 */
    ScheduleLogDto nextLog(String taskId, String executeTime);

}
