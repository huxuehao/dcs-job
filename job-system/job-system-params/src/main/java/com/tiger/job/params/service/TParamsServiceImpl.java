package com.tiger.job.params.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiger.job.common.entity.Params;
import com.tiger.job.common.util.Func;
import com.tiger.job.params.core.ParamsAdapter;
import com.tiger.job.params.mapper.TParamsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
@Service
public class TParamsServiceImpl extends ServiceImpl<TParamsMapper, Params> implements TParamsService {
    private final ParamsAdapter paramsAdapter;

    public TParamsServiceImpl(ParamsAdapter paramsAdapter) {
        this.paramsAdapter = paramsAdapter;
    }

    @Override
    public String fetchValueByKey(String key) {
        return paramsAdapter.getValue(key);
        //QueryWrapper<TParams> qw = new QueryWrapper<>();
        //qw.eq("param_key", key);
        //qw.eq("del_flag", 0);
        //try {
        //    return getOne(qw).getParamValue();
        //} catch (Exception e) {
        //    throw new RuntimeException("不存在唯一Key:" + key, e);
        //}
    }

    @Override
    public boolean saveV2(Params params) {
        if(Func.isEmpty(params.getParamValue())) {
            throw new RuntimeException("value值不可为空");
        }
        QueryWrapper<Params> qw = new QueryWrapper<>();
        qw.eq("param_key", params.getParamKey());
        qw.eq("del_flag", 0);
        List<Params> list = list(qw);
        if (list == null || list.isEmpty()) {
            return paramsAdapter.saveParams(params) > 0;
            //return save(params);
        } else {
            throw new RuntimeException("Key已存在");
        }
    }


    @Override
    public boolean updateByIdV2(Params params) {
        if(Func.isEmpty(params.getParamValue())) {
            throw new RuntimeException("value值不可为空");
        }
        QueryWrapper<Params> qw = new QueryWrapper<>();
        qw.eq("param_key", params.getParamKey());
        qw.eq("del_flag", 0);
        qw.ne("id", params.getId());
        List<Params> list = list(qw);
        if (list == null || list.isEmpty()) {
            return paramsAdapter.updateParams(params) > 0;
            //return updateById(params);
        } else {
            throw new RuntimeException("Key已存在");
        }
    }
}
