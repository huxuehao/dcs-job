package com.tiger.job.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.auth.mapper.TMenuButtonMapper;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.dto.MenuButtonDto;
import com.tiger.job.common.entity.MenuButton;
import com.tiger.job.common.util.tree.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
@Service
public class TMenuButtonServiceImpl extends ServiceImpl<TMenuButtonMapper, MenuButton> implements TMenuButtonService {
    @Override
    public List<MenuButtonDto> tree() {
        List<MenuButtonDto> list = baseMapper.listV2(DBConst.T_MENU, DBConst.T_MENU_BUTTON);
        return TreeUtil.convertTree(list);
    }
}
