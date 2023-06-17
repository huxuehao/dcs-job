package com.tiger.job.task.topic;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;
import org.springframework.stereotype.Component;

/**
 * @ClassName 测试定时任务
 * @Description TODO
 * @Author huxuehao
 **/
@Component
@SchedulerBean
public class TopicTask {

    private final ComponentC c;

    public TopicTask(ComponentC c) {
        this.c = c;
    }

    @TaskPath(name = "定时任务9", path = "/topic/execute/do-task1")
    private void topic1() {
        c.invoke();
    }
}
