package com.tiger.job.core.beanScan;

import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.constant.ScanProperties;
import com.tiger.job.common.entity.ScheduleTaskDto;
import com.tiger.job.common.util.MeUtil;
import com.tiger.job.server.mapper.ScheduleTaskMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描结果自动导入库
 *
 * @author huxuehao
 **/
@Component
public class AutoInsert {
    private final ScanProperties scanProperties;
    private final ScheduleTaskMapper taskMapper;

    public AutoInsert(ScanProperties scanProperties, ScheduleTaskMapper taskMapper) {
        this.scanProperties = scanProperties;
        this.taskMapper = taskMapper;
    }

    public void invoke(List<TaskPath> taskPaths) {
        if (scanProperties.getAutoInsert()) {
            autoInsert(taskPaths);
        }
    }

    private void autoInsert(List<TaskPath> taskPaths) {
        List<ScheduleTaskDto> taskList = genTaskDto(taskPaths);
        taskMapper.deleteRecordsNotIn(taskList);
        taskMapper.addRecordsMoreOf(taskList);
    }

    /**
     * 生成task实体
     */
    private List<ScheduleTaskDto> genTaskDto(List<TaskPath> taskPaths) {
        List<ScheduleTaskDto> taskList = new ArrayList<>();
        for (TaskPath taskPath : taskPaths) {
            ScheduleTaskDto dto = new ScheduleTaskDto();
            dto.setId(MeUtil.nextId());
            dto.setPath(taskPath.path());
            dto.setEnable(taskPath.enable());
            dto.setOpenLog(taskPath.openLog());
            dto.setTaskDescribe(taskPath.describe());
            dto.setTaskType(taskPath.type());
            dto.setCreateUser(MeUtil.ADMINISTRATOR);
            dto.setCreateTime(MeUtil.currentDatetime());
            if ("-1".equals(taskPath.cron())) {
                dto.setCron(scanProperties.getDefaultCron());
            } else {
                dto.setCron(taskPath.cron());
            }
            if ("".equals(taskPath.name())) {
                dto.setName(taskPath.path());
            } else {
                dto.setName(taskPath.name());
            }
            taskList.add(dto);
        }
        return taskList;
    }
}
