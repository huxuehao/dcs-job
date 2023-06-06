package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tiger.job.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ScheduleLogDto
 * @Description TODO
 * @Author huxuehao
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_scheduled_log")
public class ScheduleLogDto extends BaseEntity {
    private String id;
    private String taskId;
    private String taskName;
    private String executeStatus;
    private String content;
    private String executeTime;
}
