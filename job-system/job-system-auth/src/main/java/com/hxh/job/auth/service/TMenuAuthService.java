package com.hxh.job.auth.service;

import com.hxh.job.common.dto.MenuDto;

import java.util.List;

/**
 * 描述：菜单权限
 *
 * @author huxuehao
 **/
public interface TMenuAuthService {
    List<MenuDto> permissionTree();
}
