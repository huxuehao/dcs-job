package com.hxh.job.core.beanScan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxh.job.common.annotation.TaskPath;
import com.hxh.job.common.constant.DBConst;
import com.hxh.job.common.constant.JobConstant;
import com.hxh.job.common.constant.ScanProperties;
import com.hxh.job.common.entity.Classify;
import com.hxh.job.common.entity.ScheduledConfigEntity;
import com.hxh.job.common.enums.JobType;
import com.hxh.job.common.util.MeUtil;
import com.hxh.job.server.mapper.ScheduleTaskMapper;
import com.hxh.job.server.service.ClassifyService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 扫描结果自动导入库
 *
 * @author huxuehao
 **/
@Component
public class AutoInsert {
    @Resource
    private RedissonClient locker;
    private final ScanProperties scanProperties;
    private final ScheduleTaskMapper taskMapper;
    private final ClassifyService classifyService;

    public AutoInsert(ScanProperties scanProperties, ScheduleTaskMapper taskMapper, ClassifyService classifyService) {
        this.scanProperties = scanProperties;
        this.taskMapper = taskMapper;
        this.classifyService = classifyService;
    }

    public void invoke(List<TaskPath> taskPaths) {
        if (scanProperties.getAutoInsert()) {
            autoInsert(taskPaths);
        }
    }

    private void autoInsert(List<TaskPath> taskPaths) {
        if (taskPaths == null || taskPaths.isEmpty()) {
            return;
        }
        List<ScheduledConfigEntity> taskList = genTaskDto(taskPaths);
        RLock lock = locker.getLock(JobConstant.AUTO_INSERT_LOCK);
        if (lock.tryLock()) {
            try {
                // 删除不存在的实例
                taskMapper.deleteRecordsNotIn(DBConst.SYS_SCHEDULED, taskList);

                // 排除已经存在的实例
                QueryWrapper<ScheduledConfigEntity> qw = new QueryWrapper<>();
                qw.in("path", taskPaths.stream().map(TaskPath::path).collect(Collectors.toList()));
                List<ScheduledConfigEntity> configEntityList = taskMapper.selectList(qw);
                if (!configEntityList.isEmpty()) {
                    List<String> existPaths = configEntityList.stream().map(ScheduledConfigEntity::getPath).collect(Collectors.toList());
                    taskList = taskList.stream().filter(item -> !existPaths.contains(item.getPath())).collect(Collectors.toList());
                }

                // 插入新的实例
                if (!taskList.isEmpty()) {
                    taskMapper.addRecordsMoreOf(DBConst.SYS_SCHEDULED, taskList);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 生成task实体
     */
    private List<ScheduledConfigEntity> genTaskDto(List<TaskPath> taskPaths) {
        List<Classify> list = classifyService.list();
        Map<String, Long> classifyMap = list.stream().collect(Collectors.toMap(Classify::getCode, Classify::getId));

        List<ScheduledConfigEntity> taskList = new ArrayList<>();
        for (TaskPath taskPath : taskPaths) {
            ScheduledConfigEntity dto = new ScheduledConfigEntity();
            dto.setId(MeUtil.nextLongId());
            dto.setPath(taskPath.path());
            dto.setEnable(taskPath.enable());
            dto.setOpenLog(taskPath.openLog());
            dto.setTaskDescribe(taskPath.describe());
            dto.setTaskClassify(classifyMap.get(taskPath.type()));
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
