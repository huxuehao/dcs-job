package com.tiger.job.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tiger.job.common.dto.ClassifyDto;
import com.tiger.job.common.entity.Classify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：任务分类
 *
 * @author huxuehao
 **/
@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {
    List<ClassifyDto> listV2(@Param("dbName")String dbName, @Param(Constants.WRAPPER) Wrapper<Classify> queryWrapper);
}
