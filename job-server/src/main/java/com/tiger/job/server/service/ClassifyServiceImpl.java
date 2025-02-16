package com.tiger.job.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.constant.DBConst;
import com.tiger.job.common.dto.ClassifyDto;
import com.tiger.job.common.entity.Classify;
import com.tiger.job.common.mp.support.MP;
import com.tiger.job.common.util.Func;
import com.tiger.job.common.util.tree.TreeUtil;
import com.tiger.job.server.mapper.ClassifyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：任务分类
 *
 * @author huxuehao
 **/
@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> implements ClassifyService {
    @Override
    @Transactional
    public boolean deleteAllById(List<Long> ids) {
        QueryWrapper<Classify> qw = new QueryWrapper<>();
        qw.in("parent_id", ids);
        qw.eq("del_flag", 0);
        List<Classify> orgList = list(qw);

        if (Func.isEmpty(orgList)) {
            return removeBatchByIds(ids);
        }
        // 有子级的ID集合
        List<Long> hasChildIds = orgList.stream().map(Classify::getParentId).collect(Collectors.toList());
        // 没有子级的ID集合
        List<Long> noneChildIds = ids.stream().filter(id -> !hasChildIds.contains(id)).collect(Collectors.toList());
        removeBatchByIds(noneChildIds);

        return deleteAllById(hasChildIds);
    }

    @Override
    public List<ClassifyDto> tree(Classify classify) {
        List<ClassifyDto> list = baseMapper.listV2(DBConst.SYS_CLASSIFY, MP.getQueryWrapper(classify));
        return TreeUtil.convertTree(list);
    }
}
