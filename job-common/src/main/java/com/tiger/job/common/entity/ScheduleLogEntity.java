package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tiger.job.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 描述：日志类
 *
 * @author huxuehao
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_scheduled_log")
public class ScheduleLogEntity extends BaseEntity {
    private String id;
    private String taskId;
    private String taskName;
    private String executeStatus;
    private String content;
    private String executeTime;
}
