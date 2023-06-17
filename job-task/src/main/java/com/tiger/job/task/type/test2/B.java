package com.tiger.job.task.type.test2;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName B
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class B {
    @TaskPath(name = "定时任务17", path = "/type/test2/b/test1")
    private void test1() {
        System.out.println("The task is executing now....");
    }

    @TaskPath(name = "定时任务18", path = "/type/test2/b/test2")
    private void test2() {
        System.out.println("The task is executing now....");
    }
}
