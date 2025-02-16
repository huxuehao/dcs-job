package com.tiger.job.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.mp.annotation.QueryDefine;
import com.tiger.job.common.mp.support.QueryCondition;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：角色权限表
 *
 * @author huxuehao
 **/
@Data
@TableName(DBConst.T_ROLE_AUTH)
public class RoleAuth implements Serializable {
    static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long roleId;

    @JsonSerialize(using = ToStringSerializer.class)
    @QueryDefine(condition = QueryCondition.EQ)
    private Long authId;

    @QueryDefine(condition = QueryCondition.EQ)
    private Integer authType; // 权限类型（1菜单，2按钮，3接口）
}
