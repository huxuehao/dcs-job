package com.tiger.job.task.type.test1;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName B
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class B {
    @TaskPath(name = "定时任务13", path = "/type/test1/b/test1")
    private void test1() {
        System.out.println("The task is executing now....");
    }
}
