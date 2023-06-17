package com.tiger.job.task.type.test3;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName A
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class A {
    @TaskPath(name = "定时任务19", path = "/type/test3/a/test1")
    private void test1() {
        System.out.println("The task is executing now....");
    }
}
