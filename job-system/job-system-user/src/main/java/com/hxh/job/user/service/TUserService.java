package com.hxh.job.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.UserDto;
import com.hxh.job.common.entity.LoginBody;
import com.hxh.job.common.entity.User;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
public interface TUserService extends IService<User> {
    UserDto validateLogin(LoginBody body);
    void logout(String id);
    UserDto refreshToken(String refreshToken);
}
