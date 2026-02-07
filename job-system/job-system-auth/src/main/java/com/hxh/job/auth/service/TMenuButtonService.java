package com.hxh.job.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.MenuButtonDto;
import com.hxh.job.common.entity.MenuButton;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
public interface TMenuButtonService extends IService<MenuButton> {
    List<MenuButtonDto> tree();
}
