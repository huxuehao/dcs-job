package com.tiger.job.task.scheduled;

import com.tiger.job.common.ScheduledTaskTemplate;
import com.tiger.job.common.entity.ScheduledConfigEntity;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public class Demo implements ScheduledTaskTemplate {
    @Override
    public void doTask(ScheduledConfigEntity config) {
        System.out.println("demo done");
    }
}
