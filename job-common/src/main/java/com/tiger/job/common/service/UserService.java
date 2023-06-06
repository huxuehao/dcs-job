package com.tiger.job.common.service;


import com.tiger.job.common.entity.LoginBody;
import com.tiger.job.common.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description 用户操作
 * @Author StudiousTiger
 **/
public interface UserService {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    List<SysUser> selectUserByUserName(String userName);

    /**
     * 登录验证
     *
     * @param body 登录体
     * @return 是否成功
     */
    Object validateLogin(LoginBody body);

    /**
     * 退出登录
     *
     * @param id
     * @return
     */
    void logout(String id);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(String userId);

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean registerUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 修改用户密码
     *
     * @param  map 用户信息
     * @return
     */
    int updatePassword(Map<String, String> map);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(String userId);
}
