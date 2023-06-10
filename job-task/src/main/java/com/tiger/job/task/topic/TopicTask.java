package com.tiger.job.task.topic;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName 测试定时任务
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class TopicTask {
    @TaskPath(name = "定时任务9", path = "/topic/execute/do-task1")
    private void topic1() {
        System.out.println("The topic1 task is executing now....");
    }
}
