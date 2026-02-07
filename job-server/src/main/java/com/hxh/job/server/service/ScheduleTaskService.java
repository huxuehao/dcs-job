package com.hxh.job.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.entity.ScheduleTaskPo;

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
    boolean add(ScheduledConfigEntity scheduleTask);
    /* 更新 */
    boolean update(ScheduledConfigEntity scheduleTask);
    /* 启用 */
    int enableByIds(List<Long> ids);

    /* 禁用 */
    int disableByIds(List<Long> ids);

    /* 删除 */
    boolean deleteByIds(List<Long> ids);

    ScheduleTaskPo refreshResult(Long tasId);

    /**
     * 开启调度
     * @param ids 任务ID集合
     */
    void openSchedule(List<Long> ids);

    /**
     * 关闭调度
     * @param ids 任务ID集合
     */
    void closeSchedule(List<Long> ids);
}
