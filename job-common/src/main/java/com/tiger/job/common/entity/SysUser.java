package com.tiger.job.common.entity;

import com.tiger.job.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 描述：用户信息实体类
 *
 * @author huxuehao
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {
    private String id;
    private String userName;
    private String password;
    private Integer delFlag;
}
