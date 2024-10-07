package com.tiger.job.core.log;

import com.tiger.job.common.entity.ScheduledConfigEntity;

/**
 * 日志接口
 *
 * @author huxuehao
 **/
public interface GenLog {
    void gen(ScheduledConfigEntity task, String message);
}
