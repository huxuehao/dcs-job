package com.tiger.job.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.entity.ScheduleLogDto;
import com.tiger.job.server.mapper.ScheduleLogMapper;
import com.tiger.job.server.service.ScheduleLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ScheduleLogServiceImpl
 * @Description TODO
 * @Author huxuehao
 **/
@Service
public class ScheduleLogServiceImpl extends ServiceImpl<ScheduleLogMapper, ScheduleLogDto> implements ScheduleLogService {
    private final ScheduleLogMapper scheduleLogMapper;

    public ScheduleLogServiceImpl(ScheduleLogMapper scheduleLogMapper) {
        this.scheduleLogMapper = scheduleLogMapper;
    }

    @Override
    public List<ScheduleLogDto> getPage() {
        return scheduleLogMapper.getPage();
    }

    @Override
    public int add(ScheduleLogDto scheduleLog) {
        return scheduleLogMapper.add(scheduleLog);
    }

    @Override
    public int clearLog(String endTime) {
        return scheduleLogMapper.clearLog(endTime);
    }

    @Override
    /* 根据定时任务获取最新的错误日志*/
    public ScheduleLogDto latestLogByTask(String taskId) {
        return scheduleLogMapper.latestLogByTask(taskId);
    }

    @Override
    public ScheduleLogDto lastLog(String taskId, String executeTime) {
        return scheduleLogMapper.lastLog(taskId, executeTime);
    }

    @Override
    public ScheduleLogDto nextLog(String taskId, String executeTime) {
        return scheduleLogMapper.nextLog(taskId, executeTime);
    }

}
