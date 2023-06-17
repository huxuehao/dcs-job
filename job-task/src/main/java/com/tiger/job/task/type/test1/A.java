package com.tiger.job.task.type.test1;

import com.tiger.job.common.annotation.SchedulerBean;
import com.tiger.job.common.annotation.TaskPath;

/**
 * @ClassName A
 * @Description TODO
 * @Author huxuehao
 **/
@SchedulerBean
public class A {
    @TaskPath(name = "定时任务10", path = "/type/test1/a/test1")
    private void test1() {
        System.out.println("The task is executing now....");
    }
    @TaskPath(name = "定时任务11", path = "/type/test1/a/test2")
    private void test2() {
        System.out.println("The task is executing now....");
    }
    @TaskPath(name = "定时任务12", path = "/type/test1/a/test3")
    private void test3() {
        System.out.println("The task is executing now....");
    }
}
