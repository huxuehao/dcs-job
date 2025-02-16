package com.tiger.job.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
@Mapper
public interface TUserMapper extends BaseMapper<User> {
}
