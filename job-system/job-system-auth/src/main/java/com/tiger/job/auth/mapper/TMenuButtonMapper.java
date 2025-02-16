package com.tiger.job.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiger.job.common.dto.MenuButtonDto;
import com.tiger.job.common.entity.MenuButton;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
@Mapper
public interface TMenuButtonMapper extends BaseMapper<MenuButton> {
    List<MenuButtonDto> listV2(
            @Param("menuDbName")String menuDbName,
            @Param("menuButtonDbName")String menuButtonDbName
    );
}
