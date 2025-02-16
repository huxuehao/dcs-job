package com.tiger.job.auth.service;

import com.tiger.job.common.dto.MenuDto;

import java.util.List;

/**
 * 描述：菜单权限
 *
 * @author huxuehao
 **/
public interface TMenuAuthService {
    List<MenuDto> permissionTree();
}
