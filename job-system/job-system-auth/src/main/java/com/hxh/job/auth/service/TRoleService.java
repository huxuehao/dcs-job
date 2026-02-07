package com.hxh.job.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.RoleDto;
import com.hxh.job.common.entity.Role;
import com.hxh.job.common.vo.AuthConfig;
import com.hxh.job.common.vo.UserRoleConfig;

import java.util.List;

/**
 * 描述：角色
 *
 * @author huxuehao
 **/
public interface TRoleService extends IService<Role> {
    List<RoleDto> tree(Role role);

    boolean deleteAllById(List<Long> ids);

    /**
     * 保存权限配置
     * @param config 权限配置
     */
    boolean saveAuthConfig(AuthConfig config);

    /**
     * 保存权限配置
     * @param roleId 角色ID
     */
    AuthConfig getAuthConfig(Long roleId);

    /**
     * 保存用户角色
     * @param config 配置
     */
    boolean saveUserRole(UserRoleConfig config);


    /**
     * 保存获取用户角色
     * @param userId 用户ID
     */
    List<String> getUserRole(Long userId);

    /**
     * 保存获取用户按钮权限
     */
    List<String> getButtonPermissions();

    /**
     * 保存获取用户角色
     * @param roleId 角色ID
     */
    List<String> getUserByRoleId(Long roleId);
}
