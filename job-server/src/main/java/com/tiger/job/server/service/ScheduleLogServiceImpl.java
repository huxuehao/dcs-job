package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.entity.ScheduleLogEntity;
import com.tiger.job.server.mapper.ScheduleLogMapper;
import com.tiger.job.server.service.ScheduleLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志实现类
 *
 * @author huxuehao
 **/
@Service
public class ScheduleLogServiceImpl extends ServiceImpl<ScheduleLogMapper, ScheduleLogEntity> implements ScheduleLogService {
    private final ScheduleLogMapper scheduleLogMapper;

    public ScheduleLogServiceImpl(ScheduleLogMapper scheduleLogMapper) {
        this.scheduleLogMapper = scheduleLogMapper;
    }

    @Override
    public List<ScheduleLogEntity> getPage() {
        return scheduleLogMapper.getPage(DBConst.SYS_SCHEDULED_LOG);
    }

    @Override
    public int add(ScheduleLogEntity scheduleLog) {
        return scheduleLogMapper.add(DBConst.SYS_SCHEDULED_LOG, scheduleLog);
    }

    @Override
    /* 根据定时任务获取最新的错误日志*/
    public ScheduleLogEntity latestLogByTask(String taskId) {
        return scheduleLogMapper.latestLogByTask(DBConst.SYS_SCHEDULED_LOG, taskId);
    }

    @Override
    public ScheduleLogEntity lastLog(String taskId, String executeTime) {
        return scheduleLogMapper.lastLog(DBConst.SYS_SCHEDULED_LOG, taskId, executeTime);
    }

    @Override
    public ScheduleLogEntity nextLog(String taskId, String executeTime) {
        return scheduleLogMapper.nextLog(DBConst.SYS_SCHEDULED_LOG, taskId, executeTime);
    }

}
