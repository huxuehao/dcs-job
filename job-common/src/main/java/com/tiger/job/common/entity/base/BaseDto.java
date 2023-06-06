package com.tiger.job.common.entity.base;

import lombok.Data;

/**
 * @ClassName BaseDto
 * @Description 基础dto
 * @Author huxuehao
 **/
@Data
public class BaseDto extends BaseEntity {
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
}
