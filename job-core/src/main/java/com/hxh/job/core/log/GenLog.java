package com.hxh.job.core.log;

import com.hxh.job.common.entity.ScheduledConfigEntity;

/**
 * 日志接口
 *
 * @author huxuehao
 **/
public interface GenLog {
    void gen(ScheduledConfigEntity task, String message);
}
