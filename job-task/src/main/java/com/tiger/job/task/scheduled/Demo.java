package com.tiger.job.task.scheduled;

import com.tiger.job.common.JobTemplate;
import com.tiger.job.common.entity.ScheduledConfigEntity;
import org.springframework.stereotype.Component;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Component
public class Demo implements JobTemplate {
    @Override
    public void doTask(ScheduledConfigEntity scheduledConfig) {
        System.out.println("demo done");
    }
}
