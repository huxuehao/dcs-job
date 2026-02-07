package com.hxh.job.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.MenuApiDto;
import com.hxh.job.common.entity.MenuApi;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
public interface TMenuApiService extends IService<MenuApi> {
    List<MenuApiDto> tree();
}
