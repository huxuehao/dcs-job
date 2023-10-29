package com.tiger.job.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.entity.ScheduleTaskPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mapper
 *
 * @author huxuehao
 **/
@Repository
public interface ScheduleTaskMapper extends BaseMapper<ScheduleTaskDto> {
    /* 添加定时任务信息 */
    int add(@Param("task") ScheduleTaskDto task);
    /* 更新定时任务信息 */
    int update(@Param("task") ScheduleTaskDto task);

    List<ScheduleTaskDto> selectAll();

    List<ScheduleTaskDto> selectByIds(@Param("ids") List<String> ids);
    /* 获取总数 */
    int getTotals(@Param("taskName") String taskName,
                  @Param("taskType") String taskType,
                  @Param("taskStatus") String taskStatus);
    List<ScheduleTaskPo> getPage(@Param("current") Integer current,
                                 @Param("size") Integer size,
                                 @Param("taskName") String taskName,
                                 @Param("taskType") String taskType,
                                 @Param("taskStatus") String taskStatus);
    /* 批量开启定时任务信息 */
    int enableByIds(@Param("tasks") List<ScheduleTaskDto> tasks);
    /* 批量关闭定时任务信息 */
    int disableByIds(@Param("tasks") List<ScheduleTaskDto> tasks);
    /* 批量删除定时任务信息 */
    int deleteByIds(@Param("ids") List<String> ids);
    /* 根据定时任务id获取最新的定时任务信息*/
    ScheduleTaskPo refreshResult(@Param("taskId") String taskId);
    /* 删除不存在与tasks中的记录 */
    int deleteRecordsNotIn(@Param("tasks") List<ScheduleTaskDto> tasks);
    /* 添加tasks中多的记录 */
    int addRecordsMoreOf(@Param("tasks") List<ScheduleTaskDto> tasks);

}
