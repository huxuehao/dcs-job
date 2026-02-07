package com.hxh.job.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.MenuDto;
import com.hxh.job.common.entity.Menu;

import java.util.List;

/**
 * 描述：菜单
 *
 * @author huxuehao
 **/
public interface TMenuService extends IService<Menu> {
    List<MenuDto> tree(Menu menu);
    boolean deleteAllById(List<Long> ids);

}
