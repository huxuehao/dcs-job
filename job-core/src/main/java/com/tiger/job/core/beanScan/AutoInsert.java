package com.tiger.job.core.beanScan;

import com.tiger.job.common.annotation.TaskPath;
import com.tiger.job.common.constant.ScanProperties;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import com.tiger.job.common.enums.JobType;
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
        List<ScheduledConfigEntity> taskList = genTaskDto(taskPaths);
        taskMapper.deleteRecordsNotIn(taskList);
        taskMapper.addRecordsMoreOf(taskList);
    }

    /**
     * 生成task实体
     */
    private List<ScheduledConfigEntity> genTaskDto(List<TaskPath> taskPaths) {
        List<ScheduledConfigEntity> taskList = new ArrayList<>();
        for (TaskPath taskPath : taskPaths) {
            ScheduledConfigEntity dto = new ScheduledConfigEntity();
            dto.setId(MeUtil.nextId());
            dto.setPath(taskPath.path());
            dto.setEnable(taskPath.enable());
            dto.setOpenLog(taskPath.openLog());
            dto.setTaskDescribe(taskPath.describe());
            dto.setTaskType(taskPath.type());
            dto.setType(String.valueOf(JobType.ANNOTATION));
            dto.setCreateUser(MeUtil.ADMINISTRATOR);
            dto.setCreateTime(MeUtil.currentDatetime());
            dto.setUpdateUser(MeUtil.ADMINISTRATOR);
            dto.setUpdateTime(MeUtil.currentDatetime());
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
