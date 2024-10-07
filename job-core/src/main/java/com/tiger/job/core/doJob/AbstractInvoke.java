package com.tiger.job.core.doJob;

import com.tiger.job.common.entity.ScheduledConfigEntity;

/**
 * 描述：抽象执行器类
 *
 * @author huxuehao
 **/
public abstract class AbstractInvoke {
    public abstract String invoke(ScheduledConfigEntity task);
}
