package com.tiger.job.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.dto.ClassifyDto;
import com.tiger.job.common.entity.Classify;

import java.util.List;

/**
 * 描述：任务分类
 *
 * @author huxuehao
 **/
public interface ClassifyService extends IService<Classify> {
    boolean deleteAllById(List<Long> ids);

    /**
     * 获取树形列表
     */
    List<ClassifyDto> tree(Classify classify);
}
