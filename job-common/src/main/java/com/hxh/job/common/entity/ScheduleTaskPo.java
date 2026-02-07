package com.hxh.job.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：定时任务信息表PO
 *
 * @author huxuehao
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleTaskPo extends ScheduledConfigEntity {
    private String total;
}
