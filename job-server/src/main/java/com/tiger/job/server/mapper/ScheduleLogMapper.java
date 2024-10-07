package com.tiger.job.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.entity.ScheduleLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mapper
 *
 * @author huxuehao
 **/
@Mapper
public interface ScheduleLogMapper extends BaseMapper<ScheduleLogEntity> {
    /* 分页 */
    List<ScheduleLogEntity> getPage();
    /* 添加 */
    int add(@Param("log") ScheduleLogEntity log);
    /* 清除日志 */
    int clearLog(@Param("endTime") String endTime);
    /* 根据定时任务获取最新的错误日志*/
    ScheduleLogEntity latestLogByTask(@Param("taskId") String taskId);
    /* 上一条日志 */
    ScheduleLogEntity lastLog(String taskId, String executeTime);
    /* 下一条日志 */
    ScheduleLogEntity nextLog(String taskId, String executeTime);

}
