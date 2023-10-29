package com.tiger.job.server.mapper;

import com.tiger.job.common.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Map;

/**
 * mapper
 *
 * @author huxuehao
 **/
@Repository
public interface UserMapper {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    LinkedList<SysUser> selectUserByUserName(@Param("userName") String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(@Param("id") String id);

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
     * @param map 用户信息
     * @return 结果
     */
    int updatePassword(@Param("map") Map<String, String> map);

    /**
     * 通过用户ID删除用户
     *
     * @param id 用户ID
     * @return 结果
     */
    int deleteUserById(@Param("id") String id);
}
