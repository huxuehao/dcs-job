package com.tiger.job.common;

import com.tiger.job.common.entity.ScheduledConfigEntity;

/**
 * 描述：定时任务执行模板接口
 *
 * @author huxuehao
 **/
public interface ScheduledTaskTemplate {
    void doTask(ScheduledConfigEntity config);
}
