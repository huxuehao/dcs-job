package com.hxh.job.params.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxh.job.common.entity.Params;

/**
 * 描述：系统参数
 *
 * @author huxuehao
 **/
public interface TParamsService extends IService<Params> {
    String fetchValueByKey(String key);

    boolean saveV2(Params params);
    boolean updateByIdV2(Params params);
}
