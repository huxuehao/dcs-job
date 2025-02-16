package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.entity.ScheduleLogEntity;

import java.util.List;

/**
 * 调度日志接口
 *
 * @author huxuehao
 **/
public interface ScheduleLogService extends IService<ScheduleLogEntity> {
    /* 分页 */
    List<ScheduleLogEntity> getPage();
    /* 添加 */
    int add(ScheduleLogEntity scheduleLog);
    /* 根据定时任务获取最新的错误日志*/
    ScheduleLogEntity latestLogByTask(String taskId);
    /* 上一条日志 */
    ScheduleLogEntity lastLog(String taskId, String executeTime);
    /* 下一条日志 */
    ScheduleLogEntity nextLog(String taskId, String executeTime);

}
