package com.hxh.job.core.doJob;

import com.hxh.job.common.entity.ScheduledConfigEntity;

/**
 * 描述：抽象执行器类
 *
 * @author huxuehao
 **/
public abstract class AbstractInvoker {
    public abstract String invoke(ScheduledConfigEntity task);
}
