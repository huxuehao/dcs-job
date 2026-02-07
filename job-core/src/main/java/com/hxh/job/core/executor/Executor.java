package com.hxh.job.core.executor;

import com.hxh.job.common.entity.ScheduledConfigEntity;

/**
 * 描述：使用策略模式来执行定时任务
 *
 * @author huxuehao
 **/
public interface Executor {
    Boolean execute(ScheduledConfigEntity task);
}
