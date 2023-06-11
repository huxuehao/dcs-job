package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.entity.ScheduleTaskPo;

import java.util.List;

/**
 * @InterfaceName ScheduleTaskService
 * @Description TODO
 * @Author huxuehao
 **/
public interface ScheduleTaskService extends IService<ScheduleTaskDto> {
    /* 获取列表*/
    List<ScheduleTaskDto> selectAll();
    /* 新增 */
    int add(ScheduleTaskDto scheduleTask);
    /* 更新 */
    int update(ScheduleTaskDto scheduleTask);
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
