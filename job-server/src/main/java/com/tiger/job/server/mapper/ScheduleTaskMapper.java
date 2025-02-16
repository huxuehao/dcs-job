package com.tiger.job.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.entity.ScheduleTaskPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mapper
 *
 * @author huxuehao
 **/
@Mapper
public interface ScheduleTaskMapper extends BaseMapper<ScheduledConfigEntity> {
    /* 批量开启定时任务信息 */
    int enableByIds(@Param("dbName") String dnName, @Param("tasks") List<ScheduledConfigEntity> tasks);
    /* 批量关闭定时任务信息 */
    int disableByIds(@Param("dbName") String dnName, @Param("tasks") List<ScheduledConfigEntity> tasks);
    /* 根据定时任务id获取最新的定时任务信息*/
    ScheduleTaskPo refreshResult(@Param("dbName") String dnName, @Param("classifyDbName") String classifyDbName, @Param("taskId") Long taskId);
    /* 删除不存在与tasks中的记录 */
    int deleteRecordsNotIn(@Param("dbName") String dnName, @Param("tasks") List<ScheduledConfigEntity> tasks);
    /* 添加tasks中多的记录 */
    int addRecordsMoreOf(@Param("dbName") String dnName, @Param("tasks") List<ScheduledConfigEntity> tasks);

}
