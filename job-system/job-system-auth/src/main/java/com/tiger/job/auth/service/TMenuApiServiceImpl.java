package com.tiger.job.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.auth.mapper.TMenuApiMapper;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.dto.MenuApiDto;
import com.tiger.job.common.entity.MenuApi;
import com.tiger.job.common.util.tree.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
@Service
public class TMenuApiServiceImpl extends ServiceImpl<TMenuApiMapper, MenuApi> implements TMenuApiService {
    @Override
    public List<MenuApiDto> tree() {
        List<MenuApiDto> list = baseMapper.listV2(DBConst.T_MENU, DBConst.T_MENU_API);
        return TreeUtil.convertTree(list);
    }
}
