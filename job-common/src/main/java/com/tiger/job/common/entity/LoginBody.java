package com.tiger.job.common.entity;

import com.tiger.job.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName LoginBody
 * @Description 登录类
 * @Author StudiousTiger
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginBody extends BaseEntity {
    private String userName;
    private String password;
}
