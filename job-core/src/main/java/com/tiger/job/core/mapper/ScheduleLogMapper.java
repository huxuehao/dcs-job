package com.tiger.job.core.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.entity.ScheduleLogDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @InterfaceName ScheduleLogMapper
 * @Description TODO
 * @Author huxuehao
 **/
@Repository
public interface ScheduleLogMapper extends BaseMapper<ScheduleLogDto> {
    /* 分页 */
    List<ScheduleLogDto> getPage();
    /* 添加 */
    int add(@Param("log") ScheduleLogDto log);
    /* 清除日志 */
    int clearLog(@Param("endTime") String endTime);
    /* 根据定时任务获取最新的错误日志*/
    ScheduleLogDto latestLogByTask(@Param("taskId") String taskId);
    /* 上一条日志 */
    ScheduleLogDto lastLog(String taskId, String executeTime);
    /* 下一条日志 */
    ScheduleLogDto nextLog(String taskId, String executeTime);

}
