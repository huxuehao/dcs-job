package com.hxh.job.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.dto.OrganizationDto;
import com.hxh.job.common.entity.Organization;

import java.util.List;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
public interface TOrgService extends IService<Organization> {

    List<OrganizationDto> tree(Organization org);

    boolean deleteAllById(List<Long> ids);
}
