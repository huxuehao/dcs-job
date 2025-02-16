package com.tiger.job.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.dto.UserDto;
import com.tiger.job.common.entity.LoginBody;
import com.tiger.job.common.entity.User;

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
