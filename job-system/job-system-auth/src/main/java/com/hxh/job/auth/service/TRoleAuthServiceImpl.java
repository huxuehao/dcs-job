package com.hxh.job.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxh.job.auth.mapper.TRoleAuthMapper;
import com.hxh.job.common.entity.RoleAuth;
import org.springframework.stereotype.Service;

/**
 * 描述：角色权限
 *
 * @author huxuehao
 **/
@Service
public class TRoleAuthServiceImpl extends ServiceImpl<TRoleAuthMapper, RoleAuth> implements TRoleAuthService {
}
