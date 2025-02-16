package com.tiger.job.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.dto.MenuDto;
import com.tiger.job.common.entity.Menu;

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
