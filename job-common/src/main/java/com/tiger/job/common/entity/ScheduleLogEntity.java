package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.entity.base.BaseEntity;
import com.tiger.job.common.mp.annotation.QueryDefine;
import com.tiger.job.common.mp.support.QueryCondition;
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
@TableName(DBConst.SYS_SCHEDULED_LOG)
public class ScheduleLogEntity extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long taskId;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String taskName;

    @QueryDefine(condition = QueryCondition.EQ)
    private String executeStatus;

    private String content;

    @QueryDefine(condition = QueryCondition.BETWEEN)
    private String executeTime;
}
