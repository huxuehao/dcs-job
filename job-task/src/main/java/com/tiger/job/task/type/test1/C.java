package com.tiger.job.task.type.test1;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName C
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class C {
    @TaskPath(name = "定时任务14", path = "/type/test1/c/test1")
    private void test1() {
        System.out.println("The task is executing now....");
    }
}
