package com.tiger.job.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.auth.mapper.TRoleAuthMapper;
import com.tiger.job.common.entity.RoleAuth;
import org.springframework.stereotype.Service;

/**
 * 描述：角色权限
 *
 * @author huxuehao
 **/
@Service
public class TRoleAuthServiceImpl extends ServiceImpl<TRoleAuthMapper, RoleAuth> implements TRoleAuthService {
}
