package com.tiger.job.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.dto.MenuDto;
import com.tiger.job.common.entity.Menu;
import com.tiger.job.common.entity.Role;
import com.tiger.job.common.entity.RoleAuth;
import com.tiger.job.common.mp.support.MP;
import com.tiger.job.common.util.AuthUtil;
import com.tiger.job.common.util.tree.TreeUtil;
import com.tiger.job.menu.mapper.TMenuMapper;
import com.tiger.job.menu.service.TMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：菜单权限
 *
 * @author huxuehao
 **/
@Service
public class TMenuAuthServiceImpl implements TMenuAuthService {
    private final TRoleService roleService;
    private final TMenuService menuService;
    private final TMenuMapper menuMapper;
    private final TRoleAuthService roleAuthService;

    public TMenuAuthServiceImpl(TRoleService roleService, TMenuService menuService, TMenuMapper menuMapper, TRoleAuthService roleAuthService) {
        this.roleService = roleService;
        this.menuService = menuService;
        this.menuMapper = menuMapper;
        this.roleAuthService = roleAuthService;
    }

    @Override
    public List<MenuDto> permissionTree() {
        List<String> userRole = roleService.getUserRole(AuthUtil.getUserId());
        // 获取有效的用户角色
        QueryWrapper<Role> roleQw = new QueryWrapper<>();
        roleQw.in("id", userRole);
        roleQw.eq("valid", 1);
        roleQw.eq("del_flag", 0);
        List<Role> roleList = roleService.list(roleQw);
        userRole = roleList.stream().map(Role::getId).map(Objects::toString).collect(Collectors.toList());

        if (userRole.isEmpty()) {
            return new ArrayList<>();
        }

        // 处理超级管理员角色
        if (userRole.contains("1111111111111111111")) {
            return menuService.tree(new Menu());
        }

        // 获取角色配置的菜单权限
        QueryWrapper<RoleAuth> authQw = new QueryWrapper<>();
        authQw.in("role_id", userRole);
        authQw.eq("auth_type", 1);
        List<RoleAuth> auths = roleAuthService.list(authQw);
        if (auths == null || auths.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取有效的菜单
        QueryWrapper<Menu> qw = new QueryWrapper<>();
        qw.in("id", auths.stream().map(RoleAuth::getAuthId).collect(Collectors.toList()));
        qw.eq("valid", 1);
        qw.eq("del_flag", 0);
        List<Menu> validMenu = menuService.list(qw);
        List<Long> validMenuIds = validMenu.stream().map(Menu::getId).collect(Collectors.toList());

        // 获取TMenuDto列表
        List<MenuDto> list = menuMapper.listV2(DBConst.T_MENU, MP.getQueryWrapper(new Menu()));
        // 过滤有效菜单
        List<MenuDto> permissionMenuList = list.stream().filter(item -> validMenuIds.contains(item.id)).collect(Collectors.toList());

        return TreeUtil.convertTree(permissionMenuList);
    }

}
