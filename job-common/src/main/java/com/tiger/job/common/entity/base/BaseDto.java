package com.tiger.job.common.entity.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述：基础dto
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class BaseDto extends BaseEntity {
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
}
