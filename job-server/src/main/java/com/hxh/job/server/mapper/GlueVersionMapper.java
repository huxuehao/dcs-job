package com.hxh.job.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxh.job.common.entity.SysGlueVersion;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述：glue版本控制
 *
 * @author huxuehao
 **/
@Mapper
public interface GlueVersionMapper extends BaseMapper<SysGlueVersion> {
}
