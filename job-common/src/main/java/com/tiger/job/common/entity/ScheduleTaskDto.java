package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tiger.job.common.entity.base.BaseDto;
import lombok.Data;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Objects;

/**
 * @ClassName ScheduleTask
 * @Description 定时任务信息表
 * @Author huxuehao
 **/
@Data
@TableName("sys_scheduled")
public class ScheduleTaskDto extends BaseDto {
    private String id;
    private String name;
    private String taskType;
    private String taskDescribe;
    private String cron;
    private String path;
    private String enable;
    private String openLog;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScheduleTaskDto dto = (ScheduleTaskDto) o;
        return Objects.equals(path, dto.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), path);
    }

    // 提供转换为CronTrigger的工具方法
    public CronTrigger toCronTrigger() {
        return new CronTrigger(this.cron);
    }
}
