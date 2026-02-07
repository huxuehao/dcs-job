package com.hxh.job.task.topic;

import com.hxh.job.common.annotation.SchedulerBean;
import com.hxh.job.common.annotation.TaskPath;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务（整合springboot）
 *
 * @author huxuehao
 **/
@Component
@SchedulerBean
public class TopicTask {

    private final ComponentBean c;

    public TopicTask(ComponentBean c) {
        this.c = c;
    }

    @TaskPath(name = "定时任务9", path = "/topic/execute/do-task1")
    private void topic1() {
        System.out.println("do something...");
        c.invoke();
    }
}
