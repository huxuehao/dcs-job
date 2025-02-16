package com.tiger.job.params.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiger.job.common.entity.Params;

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
