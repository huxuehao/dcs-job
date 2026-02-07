package com.hxh.job.org.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hxh.job.common.dto.OrganizationDto;
import com.hxh.job.common.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
@Mapper
public interface TOrgMapper extends BaseMapper<Organization> {
    List<OrganizationDto> listV2(@Param("dbName")String dbName, @Param(Constants.WRAPPER) Wrapper<Organization> queryWrapper);
}
