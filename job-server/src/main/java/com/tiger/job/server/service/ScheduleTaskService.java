package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.entity.ScheduleTaskPo;

import java.util.List;

/**
 * 调度服务接口
 *
 * @author huxuehao
 **/
public interface ScheduleTaskService extends IService<ScheduledConfigEntity> {
    /* 获取列表*/
    List<ScheduledConfigEntity> selectAll();
    /* 新增 */
    int add(ScheduledConfigEntity scheduleTask);
    /* 更新 */
    int update(ScheduledConfigEntity scheduleTask);
    /* 总数 */
    int getTotals(String taskName, String taskType, String taskStatus);
    /* 分页 */
    List<ScheduleTaskPo> getPage(Integer current, Integer size, String taskName, String taskType, String taskStatus);
    /* 启用 */
    int enableByIds(List<String> ids);

    /* 禁用 */
    int disableByIds(List<String> ids);

    /* 删除 */
    int deleteByIds(List<String> ids);

    ScheduleTaskPo refreshResult(String tasId);
}
