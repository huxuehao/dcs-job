package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.mp.annotation.QueryDefine;
import com.tiger.job.common.mp.support.QueryCondition;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 描述：日志类
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.SYS_GLUE_VERSION)
public class SysGlueVersion implements Serializable {
    private static final long serialVersionUID = -1;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long taskId;

    private String config;

    private String remark;

    @QueryDefine(condition = QueryCondition.EQ)
    private String type;

    @QueryDefine(condition = QueryCondition.EQ)
    private String createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @QueryDefine(condition = QueryCondition.EQ)
    private String createTime;
}
