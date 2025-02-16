package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.entity.base.BaseEntity;
import com.tiger.job.common.mp.annotation.QueryDefine;
import com.tiger.job.common.mp.support.QueryCondition;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Objects;

/**
 * 描述：定时任务信息表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.SYS_SCHEDULED)
public class ScheduledConfigEntity extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long id;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long taskClassify;

    private String taskDescribe;

    private String config;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String cron;

    @QueryDefine(condition = QueryCondition.LIKE)
    private String path;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer enable;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer openLog;

    @QueryDefine(condition = QueryCondition.EQ)
    private String type;

    private String createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    private String updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScheduledConfigEntity dto = (ScheduledConfigEntity) o;
        return Objects.equals(id, dto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    // 提供转换为CronTrigger的工具方法
    public CronTrigger toCronTrigger() {
        return new CronTrigger(this.cron);
    }
}
