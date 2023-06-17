package com.tiger.job.common.entity;

import com.tiger.job.common.entity.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ScheduleTaskPo
 * @Description 定时任务信息表PO
 * @Author huxuehao
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleTaskPo extends BaseDto {
    private String id;
    private String name;
    private String taskType;
    private String taskDescribe;
    private String cron;
    private String path;
    private String enable;
    private String openLog;
    private String total;
}
