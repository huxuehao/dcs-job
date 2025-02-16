package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.entity.SysGlueVersion;
import com.tiger.job.server.mapper.GlueVersionMapper;
import org.springframework.stereotype.Service;

/**
 * 描述：glue版本控制
 *
 * @author huxuehao
 **/
@Service
public class GlueVersionServiceImpl extends ServiceImpl<GlueVersionMapper, SysGlueVersion> implements GlueVersionService {
}
