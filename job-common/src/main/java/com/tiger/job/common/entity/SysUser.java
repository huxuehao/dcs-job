package com.tiger.job.common.entity;

import com.tiger.job.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName SysUser
 * @Description 用户信息实体类
 * @Author StudiousTiger
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
