package com.tiger.job.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.dto.MenuButtonDto;
import com.tiger.job.common.entity.MenuButton;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
public interface TMenuButtonService extends IService<MenuButton> {
    List<MenuButtonDto> tree();
}
